package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.WorkflowDB;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TaskFilerRdf4j;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

public class WorkflowRepository {
  private final TaskFilerRdf4j taskFilerRdf4j = new TaskFilerRdf4j();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void createBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    if (null == bugReport.getId() || bugReport.getId().getIri().isEmpty()) bugReport.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileBugReport(bugReport);
  }

  public BugReport getBugReport(String id) throws UserNotFoundException {
    String sparql = """
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
      """;

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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
    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      setBugReportBindings(qry);
      qry.setBinding("s", iri(id));
      qry.setBinding("history", EnumUtils.asDbIri(WORKFLOW.HISTORY));
      qry.setBinding("predicate", EnumUtils.asDbIri(WORKFLOW.HISTORY_PREDICATE));
      qry.setBinding("originalObject", EnumUtils.asDbIri(WORKFLOW.HISTORY_ORIGINAL_OBJECT));
      qry.setBinding("newObject", EnumUtils.asDbIri(WORKFLOW.HISTORY_NEW_OBJECT));
      qry.setBinding("changeDate", EnumUtils.asDbIri(WORKFLOW.HISTORY_CHANGE_DATE));
      qry.setBinding("modifiedBy", EnumUtils.asDbIri(WORKFLOW.MODIFIED_BY));

      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          TaskHistory taskHistory = new TaskHistory();
          BindingSet bs = rs.next();
          taskHistory.setPredicate(bs.getValue("predicateData").stringValue());
          if (bs.getValue("predicateData").stringValue().equals(WORKFLOW.ASSIGNED_TO.toString()) && !bs.getValue("originalObjectData").stringValue().equals("UNASSIGNED")) {
/*
            try {
               taskHistory.setOriginalObject(casdoorUserService.getUser(bs.getValue("originalObjectData").stringValue()).name);
            } catch (IOException e) {
              throw new UserNotFoundException(bs.getValue("originalObjectData").stringValue());
            }
*/
          } else if (null != bs.getValue("originalObjectData"))
            taskHistory.setOriginalObject(bs.getValue("originalObjectData").stringValue());
          if (bs.getValue("predicateData").stringValue().equals(WORKFLOW.ASSIGNED_TO.toString()) && !bs.getValue("newObjectData").stringValue().equals("UNASSIGNED")) {
/*
            try {
               taskHistory.setNewObject(casdoorUserService.getUser(bs.getValue("newObjectData").stringValue()).name);
            } catch (IOException e) {
              throw new UserNotFoundException(bs.getValue("newObjectData").stringValue());
            }
*/
          } else if (null != bs.getValue("newObjectData"))
            taskHistory.setNewObject(bs.getValue("newObjectData").stringValue());
          taskHistory.setChangeDate(LocalDateTime.parse(bs.getValue("changeDateData").stringValue()));
/*
          try {
             taskHistory.setModifiedBy(casdoorUserService.getUser(bs.getValue("modifiedByData").stringValue()).name);
          } catch (IOException e) {
            throw new UserNotFoundException(bs.getValue("modifiedByData").stringValue());
          }
*/
          results.add(taskHistory);
        }
      }
    }
    return results;
  }

  public void deleteTask(String taskId) throws TaskFilerException {
    taskFilerRdf4j.deleteTask(taskId);
  }

  public void update(String subject, Enum<?> predicate, String originalObject, String newObject, String userId) throws TaskFilerException, UserNotFoundException {
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

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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
      WHERE {
        ?s ?createdBy ?createdByData
      }
      """;
    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("createdBy", EnumUtils.asDbIri(WORKFLOW.CREATED_BY));
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

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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
        WHERE {
          ?s ?assignedTo ?assignedToData
        }
      """;
    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("assignedTo", EnumUtils.asDbIri(WORKFLOW.ASSIGNED_TO));
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

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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

  public void createNamespaceRequest(NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException {
    if (null == namespaceRequest.getId() || namespaceRequest.getId().getIri().isEmpty())
      namespaceRequest.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileNamespaceRequest(namespaceRequest);
  }

  public NamespaceRequest getNamespaceRequest(String id) throws UserNotFoundException, JsonProcessingException {
    String sparql = """
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
      """;

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      setRoleRequestBindings(qry);
      qry.setBinding("s", iri(id));

      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          NamespaceRequest namespaceRequest = new NamespaceRequest();
          BindingSet bs = rs.next();
          mapNamespaceRequestFromBindingSet(namespaceRequest, bs);
          return namespaceRequest;
        }
      }
    }
    return null;
  }

  public EntityApproval getEntityApproval(String id) throws UserNotFoundException {
    String sparql = """
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
      """;

    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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

  public void createEntityApproval(EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException {
    if (null == entityApproval.getId() || entityApproval.getId().getIri().isEmpty())
      entityApproval.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileEntityApproval(entityApproval);
  }

  public String generateId() {
    String sparql = """
      SELECT DISTINCT ?s
      WHERE {?s ?p ?o .}
      ORDER BY DESC (?s)
      LIMIT 1
      """;
    try (WorkflowDB conn = WorkflowDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("p", EnumUtils.asDbIri(WORKFLOW.CREATED_BY));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          String latestIri = bs.getValue("s").stringValue();
          int code = Integer.parseInt(latestIri.split("#")[1]) + 1;
          return latestIri.split("#")[0] + "#" + code;
        }
      }
    }
    return NAMESPACE.WORKFLOW + "10000000";
  }

  private void setTaskBindings(TupleQuery qry) {
    qry.setBinding("createdBy", EnumUtils.asDbIri(WORKFLOW.CREATED_BY));
    qry.setBinding("type", EnumUtils.asDbIri(RDF.TYPE));
    qry.setBinding("state", EnumUtils.asDbIri(WORKFLOW.STATE));
    qry.setBinding("assignedTo", EnumUtils.asDbIri(WORKFLOW.ASSIGNED_TO));
    qry.setBinding("dateCreated", EnumUtils.asDbIri(WORKFLOW.DATE_CREATED));
    qry.setBinding("hostUrl", EnumUtils.asDbIri(WORKFLOW.HOST_URL));
  }

  private void setBugReportBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("product", EnumUtils.asDbIri(WORKFLOW.RELATED_PRODUCT));
    qry.setBinding("version", EnumUtils.asDbIri(WORKFLOW.RELATED_VERSION));
    qry.setBinding("module", EnumUtils.asDbIri(WORKFLOW.RELATED_MODULE));
    qry.setBinding("os", EnumUtils.asDbIri(WORKFLOW.OPERATING_SYSTEM));
    qry.setBinding("osOther", EnumUtils.asDbIri(WORKFLOW.OPERATING_SYSTEM_OTHER));
    qry.setBinding("browser", EnumUtils.asDbIri(WORKFLOW.BROWSER));
    qry.setBinding("browserOther", EnumUtils.asDbIri(WORKFLOW.BROWSER_OTHER));
    qry.setBinding("severity", EnumUtils.asDbIri(WORKFLOW.SEVERITY));
    qry.setBinding("status", EnumUtils.asDbIri(IM.HAS_STATUS));
    qry.setBinding("error", EnumUtils.asDbIri(WORKFLOW.ERROR));
    qry.setBinding("description", EnumUtils.asDbIri(RDFS.COMMENT));
    qry.setBinding("reproduceSteps", EnumUtils.asDbIri(WORKFLOW.REPRODUCE_STEPS));
    qry.setBinding("expectedResult", EnumUtils.asDbIri(WORKFLOW.EXPECTED_RESULT));
    qry.setBinding("actualResult", EnumUtils.asDbIri(WORKFLOW.ACTUAL_RESULT));
  }

  private void setRoleRequestBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("role", EnumUtils.asDbIri(WORKFLOW.REQUESTED_ROLE));
  }

  private void setEntityApprovalBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("approvalType", EnumUtils.asDbIri(WORKFLOW.APPROVAL_TYPE));
  }

  private void mapBugReportFromBindingSet(BugReport bugReport, BindingSet bs) throws UserNotFoundException {
    mapTaskFromBindingSet(bugReport, bs);
    if (null != bs.getValue("productData")) bugReport.setProduct(bs.getValue("productData").stringValue());
    if (null != bs.getValue("moduleData"))
      bugReport.setModule(TaskModule.valueOf(bs.getValue("moduleData").stringValue()));
    if (null != bs.getValue("versionData")) bugReport.setVersion(bs.getValue("versionData").stringValue());
    if (null != bs.getValue("osData"))
      bugReport.setOs(OperatingSystem.Companion.decode(bs.getValue("osData").stringValue()));
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
    task.setId(TTIriRef.iri(bs.getValue("s").stringValue()));
    task.setType(TaskType.valueOf(bs.getValue("typeData").stringValue()));
/*
    try {
      task.setCreatedBy(casdoorUserService.getUser(bs.getValue("createdByData").stringValue()).name);
    } catch (IOException e) {
      throw new UserNotFoundException(bs.getValue("createdByData").stringValue());
    }
*/
    if (!bs.getValue("assignedToData").stringValue().equals("UNASSIGNED")) {
/*
      try {
        task.setAssignedTo(casdoorUserService.getUser(bs.getValue("assignedToData").stringValue()).name);
      } catch (IOException e) {
        throw new UserNotFoundException(bs.getValue("assignedToData").stringValue());
      }
*/
    } else task.setAssignedTo(bs.getValue("assignedToData").stringValue());
    task.setState(TaskState.valueOf(bs.getValue("stateData").stringValue()));
    task.setDateCreated(LocalDateTime.parse(bs.getValue("dateCreatedData").stringValue()));
    task.setHistory(getHistory(task.getId().getIri()));
    task.setHostUrl(bs.getValue("hostUrlData").stringValue());
  }

  private void mapRoleRequestFromBindingSet(RoleRequest roleRequest, BindingSet bs) throws UserNotFoundException {
    mapTaskFromBindingSet(roleRequest, bs);
    if (null != bs.getValue("roleData")) roleRequest.setRole(UserRole.valueOf(bs.getValue("roleData").stringValue()));
  }

  private void mapNamespaceRequestFromBindingSet(NamespaceRequest namespaceRequest, BindingSet bs) throws UserNotFoundException, JsonProcessingException {
    mapTaskFromBindingSet(namespaceRequest, bs);
    Value value = bs.getValue("namespaceData");
    if (value instanceof Literal literal) {
      NamespacePermission namespacePermission = objectMapper.readValue(literal.getLabel(), NamespacePermission.class);
      namespaceRequest.setNamespacePermission(namespacePermission);
    }
  }

  private void mapEntityApprovalFromBindingSet(EntityApproval entityApproval, BindingSet bs) throws UserNotFoundException {
    mapTaskFromBindingSet(entityApproval, bs);
    if (null != bs.getValue("approvalTypeData"))
      entityApproval.setApprovalType(ApprovalType.Companion.decode(bs.getValue("approvalTypeData").stringValue()));
  }
}
