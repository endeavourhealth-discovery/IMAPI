package org.endeavourhealth.imapi.filer.rdf4j;

import jakarta.mail.MessagingException;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.EmailService;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.EntityApproval;
import org.endeavourhealth.imapi.model.workflow.RoleRequest;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.model.workflow.bugReport.Browser;
import org.endeavourhealth.imapi.model.workflow.bugReport.OperatingSystem;
import org.endeavourhealth.imapi.model.workflow.bugReport.Severity;
import org.endeavourhealth.imapi.model.workflow.bugReport.Status;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.*;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class TaskFilerRdf4j {
  private final AWSCognitoClient awsCognitoClient = new AWSCognitoClient();
  private RepositoryConnection conn;
  private EmailService emailService = new EmailService(
    System.getenv("EMAILER_HOST"),
    Integer.parseInt(System.getenv("EMAILER_PORT")),
    System.getenv("EMAILER_USERNAME"),
    System.getenv("EMAILER_PASSWORD")
  );

  public TaskFilerRdf4j(RepositoryConnection conn, AWSCognitoClient awsCognitoClient) {
    this.conn = conn;
  }

  public TaskFilerRdf4j() {
    this(ConnectionManager.getWorkflowConnection(), new AWSCognitoClient());
  }

  public void fileBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(bugReport);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, bugReport);
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.RELATED_PRODUCT), literal(bugReport.getProduct()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.RELATED_VERSION), literal(bugReport.getVersion()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.RELATED_MODULE), literal(bugReport.getModule()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.OPERATING_SYSTEM), literal(bugReport.getOs()));
      if (bugReport.getOs().equals(OperatingSystem.OTHER))
        builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.OPERATING_SYSTEM_OTHER), literal(bugReport.getOsOther()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.BROWSER), literal(bugReport.getBrowser()));
      if (bugReport.getBrowser().equals(Browser.OTHER))
        builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.BROWSER_OTHER), literal(bugReport.getBrowserOther()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.SEVERITY), literal(null == bugReport.getSeverity() ? Severity.UNASSIGNED : bugReport.getSeverity()));
      builder.add(iri(bugReport.getId().getIri()), iri(IM.HAS_STATUS), literal(null == bugReport.getStatus() ? Status.NEW : bugReport.getStatus()));
      if (null != bugReport.getError())
        builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.ERROR), literal(bugReport.getError()));
      builder.add(iri(bugReport.getId().getIri()), iri(RDFS.COMMENT), literal(bugReport.getDescription()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.REPRODUCE_STEPS), literal(bugReport.getReproduceSteps()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.EXPECTED_RESULT), literal(bugReport.getExpectedResult()));
      builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.ACTUAL_RESULT), literal(bugReport.getActualResult()));
      conn.add(builder.build());
      String emailSubject = "New bug report added: [" + bugReport.getId().getIri() + "]";
      String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/bugReport/" + bugReport.getId().getIri() + "\">here</a>";
      emailService.sendMail(emailSubject, emailContent, "bugreport@endeavourhealth.net");
    } catch (RepositoryException e) {
      throw new TaskFilerException("Failed to file task", e);
    } catch (MessagingException e) {
      throw new TaskFilerException("Failed to send email", e);
    }
  }

  public void fileRoleRequest(RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(roleRequest);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, roleRequest);
      builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.REQUESTED_ROLE), literal(roleRequest.getRole()));
      conn.add(builder.build());
      String emailSubject = "New role request added: [" + roleRequest.getId().getIri() + "]";
      String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/roleRequest/" + roleRequest.getId().getIri() + "\">here</a>";
      emailService.sendMail(emailSubject, emailContent, "rolerequest@endeavourhealth.net");
    } catch (RepositoryException e) {
      throw new TaskFilerException("Failed to file task", e);
    } catch (MessagingException e) {
      throw new TaskFilerException("Failed to send email", e);
    }
  }

  public void fileEntityApproval(EntityApproval entityApproval) throws UserNotFoundException, TaskFilerException {
    replaceUsernameWithId(entityApproval);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, entityApproval);
      builder.add(iri(entityApproval.getId().getIri()), iri(WORKFLOW.APPROVAL_TYPE), literal(entityApproval.getApprovalType()));
      conn.add(builder.build());
      String emailSubject = "New role request added: [" + entityApproval.getId().getIri() + "]";
      String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/entityApproval/" + entityApproval.getId().getIri() + "\">here</a>";
      emailService.sendMail(emailSubject, emailContent, "entityapproval@endeavourhealth.net");
    } catch (RepositoryException e) {
      throw new TaskFilerException("Failed to file task", e);
    } catch (MessagingException e) {
      throw new TaskFilerException("Failed to send email", e);
    }
  }

  public void deleteTask(String taskId) throws TaskFilerException {
    try {
      StringJoiner sparqlBuilder = new StringJoiner(System.lineSeparator());
      sparqlBuilder.add("DELETE { ?s ?p ?o }");
      sparqlBuilder.add("WHERE { ?s ?p ?o }");
      Update update = conn.prepareUpdate(sparqlBuilder.toString());
      update.setBinding("s", iri(taskId));
      update.execute();
    } catch (UpdateExecutionException e) {
      throw new TaskFilerException("Failed to delete task", e);
    }
  }

  public void replaceBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    deleteTask(bugReport.getId().getIri());
    fileBugReport(bugReport);
  }

  public void updateTask(String subject, String predicate, String originalObject, String newObject, String userId) throws TaskFilerException, UserNotFoundException {
    if (null == originalObject && null == newObject) return;
    if (predicate.equals(WORKFLOW.ASSIGNED_TO) || predicate.equals(WORKFLOW.CREATED_BY)) {
      newObject = usernameToId(newObject);
      originalObject = usernameToId(originalObject);
    }
    try {
      StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
      stringJoiner.add("DELETE { ?subject ?predicate ?originalObject }");
      stringJoiner.add("INSERT { ?subject ?predicate ?newObject }");
      stringJoiner.add("WHERE { ?subject ?predicate ?o ");
      if (null != originalObject) stringJoiner.add("FILTER (?o = ?originalObject)");
      stringJoiner.add("BIND(?o AS ?originalObject)");
      if (null != newObject) stringJoiner.add("BIND(?newVal AS ?newObject)");
      stringJoiner.add("}");
      Update update = conn.prepareUpdate(stringJoiner.toString());
      update.setBinding("subject", iri(subject));
      update.setBinding("predicate", iri(predicate));
      if (null != newObject) update.setBinding("newVal", literal(newObject));
      if (null != originalObject) update.setBinding("originalObject", literal(originalObject));
      update.execute();
      updateHistory(subject, predicate, originalObject, newObject, userId);
    } catch (UpdateExecutionException e) {
      throw new TaskFilerException("Failed to update task", e);
    }
  }

  private void updateHistory(String subject, String predicate, String originalObject, String newObject, String userId) throws TaskFilerException {
    try {
      ModelBuilder builder = new ModelBuilder();
      BNode bn = bnode();
      builder.add(iri(subject), iri(WORKFLOW.HISTORY), bn);
      builder.add(bn, iri(WORKFLOW.HISTORY_PREDICATE), literal(predicate));
      if (null != originalObject) builder.add(bn, iri(WORKFLOW.HISTORY_ORIGINAL_OBJECT), literal(originalObject));
      if (null != newObject) builder.add(bn, iri(WORKFLOW.HISTORY_NEW_OBJECT), literal(newObject));
      builder.add(bn, iri(WORKFLOW.HISTORY_CHANGE_DATE), literal(LocalDateTime.now()));
      builder.add(bn, iri(WORKFLOW.MODIFIED_BY), literal(userId));
      conn.add(builder.build());
    } catch (Exception e) {
      throw new TaskFilerException("Update task history failed.", e);
    }
  }


  private String getCurrentObject(String subject, String predicate) {
    StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
    stringJoiner.add("SELECT ?o WHERE {");
    stringJoiner.add("?s ?p ?o }");
    String sparql = stringJoiner.toString();
    try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
      TupleQuery qry = prepareSparql(conn, sparql);
      qry.setBinding("s", iri(subject));
      qry.setBinding("p", iri(predicate));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          return bs.getValue("o").stringValue();
        }
      }
    }
    return null;
  }

  private void replaceUsernameWithId(Task task) throws UserNotFoundException {
    String cognitoIdRegex = "[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}";
    if (null != task.getCreatedBy() && !task.getCreatedBy().matches(cognitoIdRegex))
      task.setCreatedBy(awsCognitoClient.adminGetId(task.getCreatedBy()));
    if (null != task.getAssignedTo() && !(task.getAssignedTo().matches(cognitoIdRegex) || task.getAssignedTo().equals("UNASSIGNED")))
      task.setAssignedTo(awsCognitoClient.adminGetId(task.getAssignedTo()));
  }

  private String usernameToId(String username) throws UserNotFoundException {
    if (username.equals("UNASSIGNED")) return username;
    String cognitoIdRegex = "[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}";
    if (!username.matches(cognitoIdRegex)) return awsCognitoClient.adminGetId(username);
    return username;
  }

  private void buildTask(ModelBuilder builder, Task task) {
    builder.add(iri(task.getId().getIri()), iri(WORKFLOW.CREATED_BY), literal(task.getCreatedBy()));
    builder.add(iri(task.getId().getIri()), iri(RDF.TYPE), literal(task.getType()));
    builder.add(iri(task.getId().getIri()), iri(WORKFLOW.STATE), literal(null == task.getState() ? TaskState.TODO : task.getState()));
    builder.add(iri(task.getId().getIri()), iri(WORKFLOW.ASSIGNED_TO), literal(null == task.getAssignedTo() ? "UNASSIGNED" : task.getAssignedTo()));
    builder.add(iri(task.getId().getIri()), iri(WORKFLOW.DATE_CREATED), literal(null == task.getDateCreated() ? LocalDateTime.now() : task.getDateCreated()));
    builder.add(iri(task.getId().getIri()), iri(WORKFLOW.HOST_URL), literal(task.getHostUrl()));
  }
}
