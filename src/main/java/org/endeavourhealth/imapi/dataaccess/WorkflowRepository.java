package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.databases.WorkflowDB;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TaskFilerRdf4j;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.EntityApproval;
import org.endeavourhealth.imapi.model.workflow.RoleRequest;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.model.workflow.bugReport.*;
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;
import org.endeavourhealth.imapi.vocabulary.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

public class WorkflowRepository {
  private final TaskFilerRdf4j taskFilerRdf4j = new TaskFilerRdf4j();

  public void createBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    if (null == bugReport.getId() || bugReport.getId().getIri().isEmpty()) bugReport.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileBugReport(bugReport);
  }

  public BugReport getBugReport(String id) throws UserNotFoundException {
    String sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?productData ?moduleData ?versionData ?osData ?browserData ?severityData ?statusData ?errorData ?descriptionData ?reproduceStepsData ?expectedResultData ?actualResultData ?dateCreatedData ?stateData ?hostUrlData
      FROM ?g
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
      """;

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setBugReportBindings(qry);
      qry.setBinding("s", iri(id));

      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BugReport bugReport = new BugReport();
          BindingSet bs = rs.next();
          mapBugReportFromBindingSet(bugReport, bs);
          return bugReport;
        }
      }
    }
    return null;
  }

  public List<TaskHistory> getHistory(String id) throws UserNotFoundException {
    String sparql = """
      SELECT ?predicateData ?originalObjectData ?newObjectData ?changeDateData ?modifiedByData
      FROM ?g
      WHERE {
        ?s ?history ?historyId .
        ?historyId ?predicate ?predicateData ;
        ?changeDate ?changeDateData ;
        ?modifiedBy ?modifiedByData .
        Optional { ?historyId ?originalObject ?originalObjectData . }
        Optional { ?historyId ?newObject ?newObjectData . }
      }
      """;
    List<TaskHistory> results = new ArrayList<>();
    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setBugReportBindings(qry);
      qry.setBinding("s", iri(id));
      qry.setBinding("history", WORKFLOW.HISTORY.asDbIri());
      qry.setBinding("predicate", WORKFLOW.HISTORY_PREDICATE.asDbIri());
      qry.setBinding("originalObject", WORKFLOW.HISTORY_ORIGINAL_OBJECT.asDbIri());
      qry.setBinding("newObject", WORKFLOW.HISTORY_NEW_OBJECT.asDbIri());
      qry.setBinding("changeDate", WORKFLOW.HISTORY_CHANGE_DATE.asDbIri());
      qry.setBinding("modifiedBy", WORKFLOW.MODIFIED_BY.asDbIri());

      AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          TaskHistory taskHistory = new TaskHistory();
          BindingSet bs = rs.next();
          taskHistory.setPredicate(bs.getValue("predicateData").stringValue());
          if (bs.getValue("predicateData").stringValue().equals(WORKFLOW.ASSIGNED_TO) && !bs.getValue("originalObjectData").stringValue().equals("UNASSIGNED"))
            taskHistory.setOriginalObject(awsCognitoClient.adminGetUsername(bs.getValue("originalObjectData").stringValue()));
          else if (null != bs.getValue("originalObjectData"))
            taskHistory.setOriginalObject(bs.getValue("originalObjectData").stringValue());
          if (bs.getValue("predicateData").stringValue().equals(WORKFLOW.ASSIGNED_TO) && !bs.getValue("newObjectData").stringValue().equals("UNASSIGNED"))
            taskHistory.setNewObject(awsCognitoClient.adminGetUsername(bs.getValue("newObjectData").stringValue()));
          else if (null != bs.getValue("newObjectData"))
            taskHistory.setNewObject(bs.getValue("newObjectData").stringValue());
          taskHistory.setChangeDate(LocalDateTime.parse(bs.getValue("changeDateData").stringValue()));
          taskHistory.setModifiedBy(awsCognitoClient.adminGetUsername(bs.getValue("modifiedByData").stringValue()));
          results.add(taskHistory);
        }
      }
    }
    return results;
  }

  public void deleteTask(String taskId) throws TaskFilerException {
    taskFilerRdf4j.deleteTask(taskId);
  }

  public void update(String subject, VocabEnum predicate, String originalObject, String newObject, String userId) throws TaskFilerException, UserNotFoundException {
    taskFilerRdf4j.updateTask(subject, predicate, originalObject, newObject, userId);
  }

  private String getTaskSparqlFromRequest(WorkflowRequest request) {
    String sparql = getTaskSparql();
    StringJoiner sj = new StringJoiner(System.lineSeparator());
    sj.add(sparql);
    if (null != request.getSize()) sj.add("LIMIT " + request.getSize());
    if (null != request.getPage() && null != request.getSize())
      sj.add("OFFSET " + request.getSize() * (request.getPage() == 0 ? 0 : request.getPage() - 1));
    return sj.toString();
  }

  private String getTaskSparql() {
    return """
      SELECT ?s ?createdByData ?typeData ?assignedToData ?stateData ?dateCreatedData ?hostUrlData
      FROM ?g
      WHERE {
        ?s ?createdBy ?createdByData ;
        ?dateCreated ?dateCreatedData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?type ?typeData ;
        ?hostUrl ?hostUrlData .
      }
      """;
  }

  public WorkflowResponse getTasksByCreatedBy(WorkflowRequest request) throws UserNotFoundException {
    String sparql = getTaskSparqlFromRequest(request);
    WorkflowResponse response = new WorkflowResponse();

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setTaskBindings(qry);
      qry.setBinding("createdByData", literal(request.getUserId()));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Task task = new Task();
          mapTaskFromBindingSet(task, bs);
          response.addTask(task);
        }
      }
    }
    response.setPage(null == request.getPage() ? 1 : request.getPage());
    response.setCount(countTaskByCreatedBy(request));
    return response;
  }

  public Integer countTaskByCreatedBy(WorkflowRequest request) {
    String sparql = """
      SELECT (COUNT(DISTINCT ?s) AS ?count)
      FROM ?g
      WHERE {
        ?s ?createdBy ?createdByData
      }
      """;
    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      qry.setBinding("createdBy", WORKFLOW.CREATED_BY.asDbIri());
      qry.setBinding("createdByData", literal(request.getUserId()));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          return Integer.parseInt(bs.getValue("count").stringValue());
        }
      }
    }
    return null;
  }

  public WorkflowResponse getTasksByAssignedTo(WorkflowRequest request) throws UserNotFoundException {
    String sparql = getTaskSparqlFromRequest(request);
    WorkflowResponse response = new WorkflowResponse();

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setTaskBindings(qry);
      qry.setBinding("assignedToData", literal(request.getUserId()));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Task task = new Task();
          mapTaskFromBindingSet(task, bs);
          response.addTask(task);
        }
      }
    }
    response.setPage(null == request.getPage() ? 1 : request.getPage());
    response.setCount(countTaskByAssignedTo(request));
    return response;
  }

  public Integer countTaskByAssignedTo(WorkflowRequest request) {
    String sparql = """
        SELECT (COUNT(DISTINCT ?s) AS ?count)
        FROM ?g
        WHERE {
          ?s ?assignedTo ?assignedToData
        }
      """;
    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      qry.setBinding("assignedTo", WORKFLOW.ASSIGNED_TO.asDbIri());
      qry.setBinding("assignedToData", literal(request.getUserId()));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          return Integer.parseInt(bs.getValue("count").stringValue());
        }
      }
    }
    return null;
  }

  public WorkflowResponse getUnassignedTasks(WorkflowRequest request) throws UserNotFoundException {
    String sparql = getTaskSparqlFromRequest(request);
    WorkflowResponse response = new WorkflowResponse();

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setTaskBindings(qry);
      qry.setBinding("assignedToData", literal("UNASSIGNED"));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Task task = new Task();
          mapTaskFromBindingSet(task, bs);
          response.addTask(task);
        }
      }
    }
    response.setPage(null == request.getPage() ? 1 : request.getPage());
    response.setCount(countTaskByAssignedTo(request));
    return response;
  }

  public Task getTask(String id) throws UserNotFoundException {
    String sparql = getTaskSparql();

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setTaskBindings(qry);
      qry.setBinding("s", iri(id));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          Task task = new Task();
          mapTaskFromBindingSet(task, bs);
          return task;
        }
      }
    }
    return null;
  }

  public void createRoleRequest(RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException {
    if (null == roleRequest.getId() || roleRequest.getId().getIri().isEmpty())
      roleRequest.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileRoleRequest(roleRequest);
  }

  public RoleRequest getRoleRequest(String id) throws UserNotFoundException {
    String sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?dateCreatedData ?stateData ?hostUrlData ?roleData
      FROM ?g
      WHERE {
        ?s ?type ?typeData ;
        ?createdBy ?createdByData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?dateCreated ?dateCreatedData ;
        ?hostUrl ?hostUrlData ;
        ?role ?roleData .
      }
      """;

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setRoleRequestBindings(qry);
      qry.setBinding("s", iri(id));

      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          RoleRequest roleRequest = new RoleRequest();
          BindingSet bs = rs.next();
          mapRoleRequestFromBindingSet(roleRequest, bs);
          return roleRequest;
        }
      }
    }
    return null;
  }

  public EntityApproval getEntityApproval(String id) throws UserNotFoundException {
    String sparql = """
      SELECT ?s ?typeData ?createdByData ?assignedToData ?dateCreatedData ?stateData ?hostUrlData ?roleData
      FROM ?g
      WHERE {
        ?s ?type ?typeData ;
        ?createdBy ?createdByData ;
        ?assignedTo ?assignedToData ;
        ?state ?stateData ;
        ?dateCreated ?dateCreatedData ;
        ?hostUrl ?hostUrlData ;
        ?role ?roleData .
      }
      """;

    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      setEntityApprovalBindings(qry);
      qry.setBinding("s", iri(id));

      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          EntityApproval entityApproval = new EntityApproval();
          BindingSet bs = rs.next();
          mapEntityApprovalFromBindingSet(entityApproval, bs);
          return entityApproval;
        }
      }
    }
    return null;
  }

  public void createEntityApproval(EntityApproval entityApproval) throws UserNotFoundException, TaskFilerException {
    if (null == entityApproval.getId() || entityApproval.getId().getIri().isEmpty())
      entityApproval.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileEntityApproval(entityApproval);
  }

  public String generateId() {
    String sparql = """
      SELECT DISTINCT ?s
      FROM ?g
      WHERE {?s ?p ?o .}
      ORDER BY DESC (?s)
      LIMIT 1
      """;
    try (RepositoryConnection conn = WorkflowDB.getConnection()) {
      TupleQuery qry = WorkflowDB.prepareTupleSparql(conn, sparql);
      qry.setBinding("p", WORKFLOW.CREATED_BY.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          String latestIri = bs.getValue("s").stringValue();
          int code = Integer.parseInt(latestIri.split("#")[1]) + 1;
          return latestIri.split("#")[0] + "#" + code;
        }
      }
    }
    return WORKFLOW.NAMESPACE + "10000000";
  }

  private void setTaskBindings(TupleQuery qry) {
    qry.setBinding("createdBy", WORKFLOW.CREATED_BY.asDbIri());
    qry.setBinding("type", RDF.TYPE.asDbIri());
    qry.setBinding("state", WORKFLOW.STATE.asDbIri());
    qry.setBinding("assignedTo", WORKFLOW.ASSIGNED_TO.asDbIri());
    qry.setBinding("dateCreated", WORKFLOW.DATE_CREATED.asDbIri());
    qry.setBinding("hostUrl", WORKFLOW.HOST_URL.asDbIri());
  }

  private void setBugReportBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("product", WORKFLOW.RELATED_PRODUCT.asDbIri());
    qry.setBinding("version", WORKFLOW.RELATED_VERSION.asDbIri());
    qry.setBinding("module", WORKFLOW.RELATED_MODULE.asDbIri());
    qry.setBinding("os", WORKFLOW.OPERATING_SYSTEM.asDbIri());
    qry.setBinding("osOther", WORKFLOW.OPERATING_SYSTEM_OTHER.asDbIri());
    qry.setBinding("browser", WORKFLOW.BROWSER.asDbIri());
    qry.setBinding("browserOther", WORKFLOW.BROWSER_OTHER.asDbIri());
    qry.setBinding("severity", WORKFLOW.SEVERITY.asDbIri());
    qry.setBinding("status", IM.HAS_STATUS.asDbIri());
    qry.setBinding("error", WORKFLOW.ERROR.asDbIri());
    qry.setBinding("description", RDFS.COMMENT.asDbIri());
    qry.setBinding("reproduceSteps", WORKFLOW.REPRODUCE_STEPS.asDbIri());
    qry.setBinding("expectedResult", WORKFLOW.EXPECTED_RESULT.asDbIri());
    qry.setBinding("actualResult", WORKFLOW.ACTUAL_RESULT.asDbIri());
  }

  private void setRoleRequestBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("role", WORKFLOW.REQUESTED_ROLE.asDbIri());
  }

  private void setEntityApprovalBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("approvalType", WORKFLOW.APPROVAL_TYPE.asDbIri());
  }

  private void mapBugReportFromBindingSet(BugReport bugReport, BindingSet bs) throws UserNotFoundException {
    mapTaskFromBindingSet(bugReport, bs);
    if (null != bs.getValue("productData")) bugReport.setProduct(bs.getValue("productData").stringValue());
    if (null != bs.getValue("moduleData"))
      bugReport.setModule(TaskModule.valueOf(bs.getValue("moduleData").stringValue()));
    if (null != bs.getValue("versionData")) bugReport.setVersion(bs.getValue("versionData").stringValue());
    if (null != bs.getValue("osData")) bugReport.setOs(OperatingSystem.valueOf(bs.getValue("osData").stringValue()));
    if (null != bs.getValue("osOtherData")) bugReport.setOsOther(bs.getValue("osOtherData").stringValue());
    if (null != bs.getValue("browserData"))
      bugReport.setBrowser(Browser.valueOf(bs.getValue("browserData").stringValue()));
    if (null != bs.getValue("browserOtherData"))
      bugReport.setBrowserOther(bs.getValue("browserOtherData").stringValue());
    if (null != bs.getValue("severityData"))
      bugReport.setSeverity(Severity.valueOf(bs.getValue("severityData").stringValue()));
    if (null != bs.getValue("statusData")) bugReport.setStatus(Status.valueOf(bs.getValue("statusData").stringValue()));
    if (null != bs.getValue("errorData")) bugReport.setError(bs.getValue("errorData").stringValue());
    if (null != bs.getValue("descriptionData")) bugReport.setDescription(bs.getValue("descriptionData").stringValue());
    if (null != bs.getValue("reproduceStepsData"))
      bugReport.setReproduceSteps(bs.getValue("reproduceStepsData").stringValue());
    if (null != bs.getValue("expectedResultData"))
      bugReport.setExpectedResult(bs.getValue("expectedResultData").stringValue());
    if (null != bs.getValue("actualResultData"))
      bugReport.setActualResult(bs.getValue("actualResultData").stringValue());
  }

  private void mapTaskFromBindingSet(Task task, BindingSet bs) throws UserNotFoundException {
    AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

    task.setId(TTIriRef.iri(bs.getValue("s").stringValue()));
    task.setType(TaskType.valueOf(bs.getValue("typeData").stringValue()));
    task.setCreatedBy(awsCognitoClient.adminGetUsername(bs.getValue("createdByData").stringValue()));
    if (!bs.getValue("assignedToData").stringValue().equals("UNASSIGNED"))
      task.setAssignedTo(awsCognitoClient.adminGetUsername(bs.getValue("assignedToData").stringValue()));
    else task.setAssignedTo(bs.getValue("assignedToData").stringValue());
    task.setState(TaskState.valueOf(bs.getValue("stateData").stringValue()));
    task.setDateCreated(LocalDateTime.parse(bs.getValue("dateCreatedData").stringValue()));
    task.setHistory(getHistory(task.getId().getIri()));
    task.setHostUrl(bs.getValue("hostUrlData").stringValue());
  }

  private void mapRoleRequestFromBindingSet(RoleRequest roleRequest, BindingSet bs) throws UserNotFoundException {
    mapTaskFromBindingSet(roleRequest, bs);
    if (null != bs.getValue("roleData")) roleRequest.setRole(UserRole.valueOf(bs.getValue("roleData").stringValue()));
  }

  private void mapEntityApprovalFromBindingSet(EntityApproval entityApproval, BindingSet bs) throws UserNotFoundException {
    mapTaskFromBindingSet(entityApproval, bs);
    if (null != bs.getValue("approvalTypeData"))
      entityApproval.setApprovalType(ApprovalType.valueOf(bs.getValue("approvalTypeData").stringValue()));
  }
}
