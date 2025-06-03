package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TaskFilerRdf4j;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.bugReport.*;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class WorkflowRepository {
  private final TaskFilerRdf4j taskFilerRdf4j = new TaskFilerRdf4j();
  private final AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

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

    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
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
    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      setBugReportBindings(qry);
      qry.setBinding("s", iri(id));
      qry.setBinding("history", iri(WORKFLOW.HISTORY));
      qry.setBinding("predicate", iri(WORKFLOW.HISTORY_PREDICATE));
      qry.setBinding("originalObject", iri(WORKFLOW.HISTORY_ORIGINAL_OBJECT));
      qry.setBinding("newObject", iri(WORKFLOW.HISTORY_NEW_OBJECT));
      qry.setBinding("changeDate", iri(WORKFLOW.HISTORY_CHANGE_DATE));
      qry.setBinding("modifiedBy", iri(WORKFLOW.MODIFIED_BY));
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

  public void update(String subject, String predicate, String originalObject, String newObject, String userId) throws TaskFilerException, UserNotFoundException {
    taskFilerRdf4j.updateTask(subject, predicate, originalObject, newObject, userId);
  }

  private String getTaskSparqlFromRequest(WorkflowRequest request) throws UserNotFoundException {
    StringJoiner sparqlJoiner = getTaskSparql();
    if (null != request.getSize()) sparqlJoiner.add("LIMIT " + request.getSize());
    if (null != request.getPage() && null != request.getSize())
      sparqlJoiner.add("OFFSET " + request.getSize() * (request.getPage() == 0 ? 0 : request.getPage() - 1));
    return sparqlJoiner.toString();
  }

  private StringJoiner getTaskSparql() {
    StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
    sparqlJoiner.add("SELECT ?s ?createdByData ?typeData ?assignedToData ?stateData ?dateCreatedData ?hostUrlData WHERE {");
    sparqlJoiner.add("?s ?createdBy ?createdByData ;");
    sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
    sparqlJoiner.add("?assignedTo ?assignedToData ;");
    sparqlJoiner.add("?state ?stateData ;");
    sparqlJoiner.add("?type ?typeData ;");
    sparqlJoiner.add("?hostUrl ?hostUrlData .");
    sparqlJoiner.add("}");
    return sparqlJoiner;
  }

  public WorkflowResponse getTasksByCreatedBy(WorkflowRequest request) throws UserNotFoundException {
    String sparql = getTaskSparqlFromRequest(request);
    WorkflowResponse response = new WorkflowResponse();

    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
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
    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      qry.setBinding("createdBy", iri(WORKFLOW.CREATED_BY));
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

    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
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
    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      qry.setBinding("assignedTo", iri(WORKFLOW.ASSIGNED_TO));
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

    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
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
    StringJoiner sparqlJoiner = getTaskSparql();
    String sparql = sparqlJoiner.toString();

    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
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
    StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
    sparqlJoiner.add("SELECT ?s ?typeData ?createdByData ?assignedToData ?productData ?moduleData ?versionData ?osData ?browserData ?severityData ?statusData ?errorData ?descriptionData ?reproduceStepsData ?expectedResultData ?dateCreatedData ?stateData ");
    sparqlJoiner.add("WHERE { ");
    sparqlJoiner.add("?s ?type ?typeData ;");
    sparqlJoiner.add("?createdBy ?createdByData ;");
    sparqlJoiner.add("?assignedTo ?assignedToData ;");
    sparqlJoiner.add("?state ?stateData ;");
    sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
    sparqlJoiner.add("?role ?roleData .");
    sparqlJoiner.add("}");
    String sparql = sparqlJoiner.toString();

    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      setBugReportBindings(qry);
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

  public void createEntityApproval(EntityApproval entityApproval) throws UserNotFoundException, TaskFilerException {
    if (null == entityApproval.getId() || entityApproval.getId().getIri().isEmpty())
      entityApproval.setId(TTIriRef.iri(generateId()));
    taskFilerRdf4j.fileEntityApproval(entityApproval);
  }

  public String generateId() {
    String sparql = "SELECT DISTINCT ?s WHERE {?s ?p ?o .} ORDER BY DESC (?s) LIMIT 1";
    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      qry.setBinding("p", iri(WORKFLOW.CREATED_BY));
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
    qry.setBinding("createdBy", iri(WORKFLOW.CREATED_BY));
    qry.setBinding("type", iri(RDF.TYPE));
    qry.setBinding("state", iri(WORKFLOW.STATE));
    qry.setBinding("assignedTo", iri(WORKFLOW.ASSIGNED_TO));
    qry.setBinding("dateCreated", iri(WORKFLOW.DATE_CREATED));
    qry.setBinding("hostUrl", iri(WORKFLOW.HOST_URL));
  }

  private void setBugReportBindings(TupleQuery qry) {
    setTaskBindings(qry);
    qry.setBinding("product", iri(WORKFLOW.RELATED_PRODUCT));
    qry.setBinding("version", iri(WORKFLOW.RELATED_VERSION));
    qry.setBinding("module", iri(WORKFLOW.RELATED_MODULE));
    qry.setBinding("os", iri(WORKFLOW.OPERATING_SYSTEM));
    qry.setBinding("osOther", iri(WORKFLOW.OPERATING_SYSTEM_OTHER));
    qry.setBinding("browser", iri(WORKFLOW.BROWSER));
    qry.setBinding("browserOther", iri(WORKFLOW.BROWSER_OTHER));
    qry.setBinding("severity", iri(WORKFLOW.SEVERITY));
    qry.setBinding("status", iri(IM.HAS_STATUS));
    qry.setBinding("error", iri(WORKFLOW.ERROR));
    qry.setBinding("description", iri(RDFS.COMMENT));
    qry.setBinding("reproduceSteps", iri(WORKFLOW.REPRODUCE_STEPS));
    qry.setBinding("expectedResult", iri(WORKFLOW.EXPECTED_RESULT));
    qry.setBinding("actualResult", iri(WORKFLOW.ACTUAL_RESULT));
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
}
