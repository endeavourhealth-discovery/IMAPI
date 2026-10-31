package org.endeavourhealth.imapi.dataaccess

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.rdf4j.model.Literal
import org.eclipse.rdf4j.model.util.Values
import org.eclipse.rdf4j.query.BindingSet
import org.eclipse.rdf4j.query.TupleQuery
import org.endeavourhealth.imapi.dataaccess.databases.WorkflowDB
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.filer.TaskFilerException
import org.endeavourhealth.imapi.filer.rdf4j.TaskFilerRdf4j
import org.endeavourhealth.imapi.model.requests.WorkflowRequest
import org.endeavourhealth.imapi.model.responses.WorkflowResponse
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.workflow.*
import org.endeavourhealth.imapi.model.workflow.bugReport.*
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory
import org.endeavourhealth.imapi.model.workflow.task.TaskState
import org.endeavourhealth.imapi.model.workflow.task.TaskType
import org.endeavourhealth.imapi.vocabulary.*
import java.time.LocalDateTime
import java.util.*

class WorkflowRepository {
  private val taskFilerRdf4j = TaskFilerRdf4j()
  private val objectMapper = ObjectMapper()

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createBugReport(bugReport: BugReport) {
    if (null == bugReport.id || bugReport.id!!.iri.isEmpty()) bugReport.id = TTIriRef.iri(generateId())
    taskFilerRdf4j.fileBugReport(bugReport)
  }

  @Throws(UserNotFoundException::class)
  fun getBugReport(id: String): BugReport? {
    val sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?productData ?moduleData ?versionData ?osData ?browserData ?severityData ?statusData ?errorData ?descriptionData ?reproduceStepsData ?expectedResultData ?actualResultData ?dateCreatedData ?stateData ?hostUrlData
      WHERE {
        ?s ?type ?typeData ;
        ?createdBy ?createdByData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?dateCreated ?dateCreatedData ;
        ?hostUrl ?hostUrlData .
        OPTIONAL {?s ?product ?productData ;}
        OPTIONAL {?s ?module ?moduleData ;}
        OPTIONAL {?s ?version ?versionData ;}
        OPTIONAL {?s ?os ?osData ;}
        OPTIONAL {?s ?osOther ?osOtherData ;}
        OPTIONAL {?s ?browser ?browserData ;}
        OPTIONAL {?s ?browserOther ?browserOtherData ;}
        OPTIONAL {?s ?severity ?severityData ;}
        OPTIONAL {?s ?status ?statusData ;}
        OPTIONAL {?s ?error ?errorData ;}
        OPTIONAL {?s ?description ?descriptionData ;}
        OPTIONAL {?s ?reproduceSteps ?reproduceStepsData ;}
        OPTIONAL {?s ?expectedResult ?expectedResultData ;}
        OPTIONAL {?s ?actualResult ?actualResultData ;}
      }
      
      """.trimIndent()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setBugReportBindings(qry)
      qry.setBinding("s", Values.iri(id))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bugReport = BugReport()
          val bs = rs.next()
          mapBugReportFromBindingSet(bugReport, bs)
          return bugReport
        }
      }
    }
    return null
  }

  @Throws(UserNotFoundException::class)
  fun getHistory(id: String): MutableList<TaskHistory> {
    val sparql = """
      SELECT ?predicateData ?originalObjectData ?newObjectData ?changeDateData ?modifiedByData
      WHERE {
        ?s ?history ?historyId .
        ?historyId ?predicate ?predicateData ;
        ?changeDate ?changeDateData ;
        ?modifiedBy ?modifiedByData .
        Optional { ?historyId ?originalObject ?originalObjectData . }
        Optional { ?historyId ?newObject ?newObjectData . }
      }
      
      """.trimIndent()
    val results: MutableList<TaskHistory> = mutableListOf()
    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setBugReportBindings(qry)
      qry.setBinding("s", Values.iri(id))
      qry.setBinding("history", WORKFLOW.HISTORY.asDbIri())
      qry.setBinding("predicate", WORKFLOW.HISTORY_PREDICATE.asDbIri())
      qry.setBinding("originalObject", WORKFLOW.HISTORY_ORIGINAL_OBJECT.asDbIri())
      qry.setBinding("newObject", WORKFLOW.HISTORY_NEW_OBJECT.asDbIri())
      qry.setBinding("changeDate", WORKFLOW.HISTORY_CHANGE_DATE.asDbIri())
      qry.setBinding("modifiedBy", WORKFLOW.MODIFIED_BY.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val taskHistory = TaskHistory()
          val bs = rs.next()
          taskHistory.predicate = bs.getValue("predicateData").stringValue()
          if (bs.getValue("predicateData")
              .stringValue() == WORKFLOW.ASSIGNED_TO.toString() && bs.getValue("originalObjectData")
              .stringValue() != "UNASSIGNED"
          ) {
            /*
try {
   taskHistory.setOriginalObject(casdoorUserService.getUser(bs.getValue("originalObjectData").stringValue()).name);
} catch (IOException e) {
  throw new UserNotFoundException(bs.getValue("originalObjectData").stringValue());
}
*/
          } else if (null != bs.getValue("originalObjectData")) taskHistory.originalObject =
            bs.getValue("originalObjectData").stringValue()
          if (bs.getValue("predicateData")
              .stringValue() == WORKFLOW.ASSIGNED_TO.toString() && bs.getValue("newObjectData")
              .stringValue() != "UNASSIGNED"
          ) {
            /*
try {
   taskHistory.setNewObject(casdoorUserService.getUser(bs.getValue("newObjectData").stringValue()).name);
} catch (IOException e) {
  throw new UserNotFoundException(bs.getValue("newObjectData").stringValue());
}
*/
          } else if (null != bs.getValue("newObjectData")) taskHistory.newObject =
            bs.getValue("newObjectData").stringValue()
          taskHistory.changeDate = LocalDateTime.parse(bs.getValue("changeDateData").stringValue())
          /*
try {
   taskHistory.setModifiedBy(casdoorUserService.getUser(bs.getValue("modifiedByData").stringValue()).name);
} catch (IOException e) {
  throw new UserNotFoundException(bs.getValue("modifiedByData").stringValue());
}
*/
          results.add(taskHistory)
        }
      }
    }
    return results
  }

  @Throws(TaskFilerException::class)
  fun deleteTask(taskId: String?) {
    taskFilerRdf4j.deleteTask(taskId)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun update(subject: String?, predicate: VocabEnum?, originalObject: String?, newObject: String?, userId: String?) {
    taskFilerRdf4j.updateTask(subject, predicate, originalObject, newObject, userId)
  }

  private fun getTaskSparqlFromRequest(request: WorkflowRequest): String {
    val sparql = this.taskSparql
    val sj = StringJoiner(System.lineSeparator())
    sj.add(sparql)
    sj.add("LIMIT " + request.size)
    sj.add("OFFSET " + request.size * (if (request.page == 0) 0 else request.page - 1))
    return sj.toString()
  }

  private val taskSparql: String
    get() = """
      SELECT ?s ?createdByData ?typeData ?assignedToData ?stateData ?dateCreatedData ?hostUrlData
      WHERE {
        ?s ?createdBy ?createdByData ;
        ?dateCreated ?dateCreatedData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?type ?typeData ;
        ?hostUrl ?hostUrlData .
      }
      
      """.trimIndent()

  @Throws(UserNotFoundException::class)
  fun getTasksByCreatedBy(request: WorkflowRequest): WorkflowResponse {
    val sparql = getTaskSparqlFromRequest(request)
    val response = WorkflowResponse()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setTaskBindings(qry)
      qry.setBinding("createdByData", Values.literal(request.userId))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val task = Task()
          mapTaskFromBindingSet(task, bs)
          response.addTask(task)
        }
      }
    }
    response.setPage(request.page)
    response.setCount(countTaskByCreatedBy(request)!!)
    return response
  }

  fun countTaskByCreatedBy(request: WorkflowRequest): Int? {
    val sparql = """
      SELECT (COUNT(DISTINCT ?s) AS ?count)
      WHERE {
        ?s ?createdBy ?createdByData
      }
      
      """.trimIndent()
    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("createdBy", WORKFLOW.CREATED_BY.asDbIri())
      qry.setBinding("createdByData", Values.literal(request.userId))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return bs.getValue("count").stringValue().toInt()
        }
      }
    }
    return null
  }

  @Throws(UserNotFoundException::class)
  fun getTasksByAssignedTo(request: WorkflowRequest): WorkflowResponse {
    val sparql = getTaskSparqlFromRequest(request)
    val response = WorkflowResponse()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setTaskBindings(qry)
      qry.setBinding("assignedToData", Values.literal(request.userId))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val task = Task()
          mapTaskFromBindingSet(task, bs)
          response.addTask(task)
        }
      }
    }
    response.setPage(request.page)
    response.setCount(countTaskByAssignedTo(request)!!)
    return response
  }

  fun countTaskByAssignedTo(request: WorkflowRequest): Int? {
    val sparql = """
        SELECT (COUNT(DISTINCT ?s) AS ?count)
        WHERE {
          ?s ?assignedTo ?assignedToData
        }
      
      """.trimIndent()
    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("assignedTo", WORKFLOW.ASSIGNED_TO.asDbIri())
      qry.setBinding("assignedToData", Values.literal(request.userId))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return bs.getValue("count").stringValue().toInt()
        }
      }
    }
    return null
  }

  @Throws(UserNotFoundException::class)
  fun getUnassignedTasks(request: WorkflowRequest): WorkflowResponse {
    val sparql = getTaskSparqlFromRequest(request)
    val response = WorkflowResponse()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setTaskBindings(qry)
      qry.setBinding("assignedToData", Values.literal("UNASSIGNED"))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          val task = Task()
          mapTaskFromBindingSet(task, bs)
          response.addTask(task)
        }
      }
    }
    response.setPage(request.page)
    response.setCount(countTaskByAssignedTo(request)!!)
    return response
  }

  @Throws(UserNotFoundException::class)
  fun getTask(id: String): Task? {
    val sparql = this.taskSparql

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setTaskBindings(qry)
      qry.setBinding("s", Values.iri(id))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          val task = Task()
          mapTaskFromBindingSet(task, bs)
          return task
        }
      }
    }
    return null
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createRoleRequest(roleRequest: RoleRequest) {
    if (null == roleRequest.id || roleRequest.id!!.getIri().isEmpty()) roleRequest.id = TTIriRef.iri(generateId())
    taskFilerRdf4j.fileRoleRequest(roleRequest)
  }

  @Throws(UserNotFoundException::class)
  fun getRoleRequest(id: String): RoleRequest? {
    val sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?dateCreatedData ?stateData ?hostUrlData ?roleData
      WHERE {
        ?s ?type ?typeData ;
        ?createdBy ?createdByData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?dateCreated ?dateCreatedData ;
        ?hostUrl ?hostUrlData ;
        ?role ?roleData .
      }
      
      """.trimIndent()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setRoleRequestBindings(qry)
      qry.setBinding("s", Values.iri(id))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val roleRequest = RoleRequest()
          val bs = rs.next()
          mapRoleRequestFromBindingSet(roleRequest, bs)
          return roleRequest
        }
      }
    }
    return null
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createNamespaceRequest(namespaceRequest: NamespaceRequest) {
    if (null == namespaceRequest.id || namespaceRequest.id!!.getIri().isEmpty()) namespaceRequest.id =
      TTIriRef.iri(generateId())
    taskFilerRdf4j.fileNamespaceRequest(namespaceRequest)
  }

  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getNamespaceRequest(id: String): NamespaceRequest? {
    val sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?dateCreatedData ?stateData ?hostUrlData ?namespaceData
      WHERE {
        ?s ?type ?typeData ;
        ?createdBy ?createdByData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?dateCreated ?dateCreatedData ;
        ?hostUrl ?hostUrlData ;
        ?namespace ?namespaceData .
      }
      
      """.trimIndent()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setRoleRequestBindings(qry)
      qry.setBinding("s", Values.iri(id))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val namespaceRequest = NamespaceRequest()
          val bs = rs.next()
          mapNamespaceRequestFromBindingSet(namespaceRequest, bs)
          return namespaceRequest
        }
      }
    }
    return null
  }

  @Throws(UserNotFoundException::class)
  fun getEntityApproval(id: String): EntityApproval? {
    val sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?dateCreatedData ?stateData ?hostUrlData ?roleData
      WHERE {
        ?s ?type ?typeData ;
        ?createdBy ?createdByData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?dateCreated ?dateCreatedData ;
        ?hostUrl ?hostUrlData ;
        ?role ?roleData .
      }
      
      """.trimIndent()

    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      setEntityApprovalBindings(qry)
      qry.setBinding("s", Values.iri(id))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val entityApproval = EntityApproval()
          val bs = rs.next()
          mapEntityApprovalFromBindingSet(entityApproval, bs)
          return entityApproval
        }
      }
    }
    return null
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createEntityApproval(entityApproval: EntityApproval) {
    if (null == entityApproval.id || entityApproval.id!!.getIri().isEmpty()) entityApproval.id =
      TTIriRef.iri(generateId())
    taskFilerRdf4j.fileEntityApproval(entityApproval)
  }

  fun generateId(): String {
    val sparql = """
      SELECT DISTINCT ?s
      WHERE {?s ?p ?o .}
      ORDER BY DESC (?s)
      LIMIT 1
      
      """.trimIndent()
    WorkflowDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("p", WORKFLOW.CREATED_BY.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          val latestIri = bs.getValue("s").stringValue()
          val code = latestIri.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt() + 1
          return latestIri.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "#" + code
        }
      }
    }
    return NAMESPACE.WORKFLOW.toString() + "10000000"
  }

  private fun setTaskBindings(qry: TupleQuery) {
    qry.setBinding("createdBy", WORKFLOW.CREATED_BY.asDbIri())
    qry.setBinding("type", RDF.TYPE.asDbIri())
    qry.setBinding("state", WORKFLOW.STATE.asDbIri())
    qry.setBinding("assignedTo", WORKFLOW.ASSIGNED_TO.asDbIri())
    qry.setBinding("dateCreated", WORKFLOW.DATE_CREATED.asDbIri())
    qry.setBinding("hostUrl", WORKFLOW.HOST_URL.asDbIri())
  }

  private fun setBugReportBindings(qry: TupleQuery) {
    setTaskBindings(qry)
    qry.setBinding("product", WORKFLOW.RELATED_PRODUCT.asDbIri())
    qry.setBinding("version", WORKFLOW.RELATED_VERSION.asDbIri())
    qry.setBinding("module", WORKFLOW.RELATED_MODULE.asDbIri())
    qry.setBinding("os", WORKFLOW.OPERATING_SYSTEM.asDbIri())
    qry.setBinding("osOther", WORKFLOW.OPERATING_SYSTEM_OTHER.asDbIri())
    qry.setBinding("browser", WORKFLOW.BROWSER.asDbIri())
    qry.setBinding("browserOther", WORKFLOW.BROWSER_OTHER.asDbIri())
    qry.setBinding("severity", WORKFLOW.SEVERITY.asDbIri())
    qry.setBinding("status", IM.HAS_STATUS.asDbIri())
    qry.setBinding("error", WORKFLOW.ERROR.asDbIri())
    qry.setBinding("description", RDFS.COMMENT.asDbIri())
    qry.setBinding("reproduceSteps", WORKFLOW.REPRODUCE_STEPS.asDbIri())
    qry.setBinding("expectedResult", WORKFLOW.EXPECTED_RESULT.asDbIri())
    qry.setBinding("actualResult", WORKFLOW.ACTUAL_RESULT.asDbIri())
  }

  private fun setRoleRequestBindings(qry: TupleQuery) {
    setTaskBindings(qry)
    qry.setBinding("role", WORKFLOW.REQUESTED_ROLE.asDbIri())
  }

  private fun setEntityApprovalBindings(qry: TupleQuery) {
    setTaskBindings(qry)
    qry.setBinding("approvalType", WORKFLOW.APPROVAL_TYPE.asDbIri())
  }

  @Throws(UserNotFoundException::class)
  private fun mapBugReportFromBindingSet(bugReport: BugReport, bs: BindingSet) {
    mapTaskFromBindingSet(bugReport, bs)
    if (null != bs.getValue("productData")) bugReport.product = bs.getValue("productData").stringValue()
    if (null != bs.getValue("moduleData")) bugReport.module =
      TaskModule.valueOf(bs.getValue("moduleData").stringValue())
    if (null != bs.getValue("versionData")) bugReport.version = bs.getValue("versionData").stringValue()
    if (null != bs.getValue("osData")) bugReport.os = OperatingSystem.valueOf(bs.getValue("osData").stringValue())
    if (null != bs.getValue("osOtherData")) bugReport.osOther = bs.getValue("osOtherData").stringValue()
    if (null != bs.getValue("browserData")) bugReport.browser =
      Browser.valueOf(bs.getValue("browserData").stringValue())
    if (null != bs.getValue("browserOtherData")) bugReport.browserOther = bs.getValue("browserOtherData").stringValue()
    if (null != bs.getValue("severityData")) bugReport.severity =
      Severity.valueOf(bs.getValue("severityData").stringValue())
    if (null != bs.getValue("statusData")) bugReport.status = Status.valueOf(bs.getValue("statusData").stringValue())
    if (null != bs.getValue("errorData")) bugReport.error = bs.getValue("errorData").stringValue()
    if (null != bs.getValue("descriptionData")) bugReport.description = bs.getValue("descriptionData").stringValue()
    if (null != bs.getValue("reproduceStepsData")) bugReport.reproduceSteps =
      bs.getValue("reproduceStepsData").stringValue()
    if (null != bs.getValue("expectedResultData")) bugReport.expectedResult =
      bs.getValue("expectedResultData").stringValue()
    if (null != bs.getValue("actualResultData")) bugReport.actualResult = bs.getValue("actualResultData").stringValue()
  }

  @Throws(UserNotFoundException::class)
  private fun mapTaskFromBindingSet(task: Task, bs: BindingSet) {
    task.id = TTIriRef.iri(bs.getValue("s").stringValue())
    task.type = TaskType.valueOf(bs.getValue("typeData").stringValue())
    /*
try {
  task.setCreatedBy(casdoorUserService.getUser(bs.getValue("createdByData").stringValue()).name);
} catch (IOException e) {
  throw new UserNotFoundException(bs.getValue("createdByData").stringValue());
}
*/
    if (bs.getValue("assignedToData").stringValue() != "UNASSIGNED") {
      /*
try {
  task.setAssignedTo(casdoorUserService.getUser(bs.getValue("assignedToData").stringValue()).name);
} catch (IOException e) {
  throw new UserNotFoundException(bs.getValue("assignedToData").stringValue());
}
*/
    } else task.assignedTo = bs.getValue("assignedToData").stringValue()
    task.state = TaskState.valueOf(bs.getValue("stateData").stringValue())
    task.dateCreated = LocalDateTime.parse(bs.getValue("dateCreatedData").stringValue())
    task.history = getHistory(task.id!!.getIri())
    task.hostUrl = bs.getValue("hostUrlData").stringValue()
  }

  @Throws(UserNotFoundException::class)
  private fun mapRoleRequestFromBindingSet(roleRequest: RoleRequest, bs: BindingSet) {
    mapTaskFromBindingSet(roleRequest, bs)
    if (null != bs.getValue("roleData")) roleRequest.role = UserRole.valueOf(bs.getValue("roleData").stringValue())
  }

  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  private fun mapNamespaceRequestFromBindingSet(namespaceRequest: NamespaceRequest, bs: BindingSet) {
    mapTaskFromBindingSet(namespaceRequest, bs)
    val value = bs.getValue("namespaceData")
    if (value is Literal) {
      val namespacePermission =
        objectMapper.readValue<NamespacePermission?>(value.getLabel(), NamespacePermission::class.java)
      namespaceRequest.namespacePermission = namespacePermission
    }
  }

  @Throws(UserNotFoundException::class)
  private fun mapEntityApprovalFromBindingSet(entityApproval: EntityApproval, bs: BindingSet) {
    mapTaskFromBindingSet(entityApproval, bs)
    if (null != bs.getValue("approvalTypeData")) entityApproval.approvalType =
      ApprovalType.valueOf(bs.getValue("approvalTypeData").stringValue())
  }
}
