package org.endeavourhealth.imapi.filer.rdf4j;

import jakarta.mail.MessagingException;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.databases.WorkflowDB;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.EmailService;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.bugReport.Browser;
import org.endeavourhealth.imapi.model.workflow.bugReport.OperatingSystem;
import org.endeavourhealth.imapi.model.workflow.bugReport.Severity;
import org.endeavourhealth.imapi.model.workflow.bugReport.Status;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.vocabulary.*;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.*;

public class TaskFilerRdf4j {
  private final WorkflowDB conn;
  private EmailService emailService;

  public TaskFilerRdf4j() {
    conn = WorkflowDB.getConnection();
  }

  public void fileBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(bugReport);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, bugReport);
      ModelBuilder namedGraph = builder.namedGraph(Graph.IM.toString());
      namedGraph.add(iri(bugReport.getId().getIri()), WORKFLOW.RELATED_PRODUCT.asDbIri(), literal(bugReport.getProduct()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.RELATED_VERSION.asDbIri(), literal(bugReport.getVersion()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.RELATED_MODULE.asDbIri(), literal(bugReport.getModule()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.OPERATING_SYSTEM.asDbIri(), literal(bugReport.getOs()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.BROWSER.asDbIri(), literal(bugReport.getBrowser()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.SEVERITY.asDbIri(), literal(null == bugReport.getSeverity() ? Severity.UNASSIGNED : bugReport.getSeverity()))
        .add(iri(bugReport.getId().getIri()), IM.HAS_STATUS.asDbIri(), literal(null == bugReport.getStatus() ? Status.NEW : bugReport.getStatus()))
        .add(iri(bugReport.getId().getIri()), RDFS.COMMENT.asDbIri(), literal(bugReport.getDescription()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.REPRODUCE_STEPS.asDbIri(), literal(bugReport.getReproduceSteps()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.EXPECTED_RESULT.asDbIri(), literal(bugReport.getExpectedResult()))
        .add(iri(bugReport.getId().getIri()), WORKFLOW.ACTUAL_RESULT.asDbIri(), literal(bugReport.getActualResult()));


      if (bugReport.getOs().equals(OperatingSystem.OTHER))
        namedGraph.add(iri(bugReport.getId().getIri()), WORKFLOW.OPERATING_SYSTEM_OTHER.asDbIri(), literal(bugReport.getOsOther()));

      if (bugReport.getBrowser().equals(Browser.OTHER))
        namedGraph.add(iri(bugReport.getId().getIri()), WORKFLOW.BROWSER_OTHER.asDbIri(), literal(bugReport.getBrowserOther()));

      if (null != bugReport.getError())
        namedGraph.add(iri(bugReport.getId().getIri()), WORKFLOW.ERROR.asDbIri(), literal(bugReport.getError()));

      conn.add(builder.build());
      String emailSubject = "New bug report added: [" + bugReport.getId().getIri() + "]";
      String emailContent = "Click <a href=\"" + bugReport.getHostUrl() + "/#/workflow/bugReport/" + bugReport.getId().getIri() + "\">here</a>";
      getEmailService().sendMail(emailSubject, emailContent, "bugreport@endeavourhealth.net");
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
      builder.namedGraph(Graph.IM.toString())
        .add(iri(roleRequest.getId().getIri()), WORKFLOW.REQUESTED_ROLE.asDbIri(), literal(roleRequest.getRole()));
      conn.add(builder.build());
      String emailSubject = "New role request added: [" + roleRequest.getId().getIri() + "]";
      String emailContent = "Click <a href=\"" + roleRequest.getHostUrl() + "/#/workflow/roleRequest/" + roleRequest.getId().getIri() + "\">here</a>";
      getEmailService().sendMail(emailSubject, emailContent, "rolerequest@endeavourhealth.net");
    } catch (RepositoryException e) {
      throw new TaskFilerException("Failed to file task", e);
    } catch (MessagingException e) {
      throw new TaskFilerException("Failed to send email", e);
    }
  }

  public void fileGraphRequest(GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(graphRequest);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, graphRequest);
      builder.namedGraph(Graph.IM.toString())
        .add(iri(graphRequest.getId().getIri()), WORKFLOW.REQUESTED_GRAPH.asDbIri(), literal(graphRequest.getGraph()));
      conn.add(builder.build());
      String emailSubject = "New graph request added: [" + graphRequest.getId().getIri() + "]";
      String emailContent = "Click <a href=\"" + graphRequest.getHostUrl() + "/#/workflow/graphRequest/" + graphRequest.getId().getIri() + "\">here</a>";
      getEmailService().sendMail(emailSubject, emailContent, "graphrequest@endeavourhealth.net");
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
      builder.namedGraph(Graph.IM.toString())
        .add(iri(entityApproval.getId().getIri()), WORKFLOW.APPROVAL_TYPE.asDbIri(), literal(entityApproval.getApprovalType()));
      conn.add(builder.build());
      String emailSubject = "New role request added: [" + entityApproval.getId().getIri() + "]";
      String emailContent = "Click <a href=\"" + entityApproval.getHostUrl() + "/#/workflow/entityApproval/" + entityApproval.getId().getIri() + "\">here</a>";
      getEmailService().sendMail(emailSubject, emailContent, "entityapproval@endeavourhealth.net");
    } catch (RepositoryException e) {
      throw new TaskFilerException("Failed to file task", e);
    } catch (MessagingException e) {
      throw new TaskFilerException("Failed to send email", e);
    }
  }

  public void deleteTask(String taskId) throws TaskFilerException {
    try {
      String sparql = """
          DELETE { ?s ?p ?o }
          WHERE {
            ?s ?p ?o
          }
        """;
      Update update = conn.prepareInsertSparql(sparql);
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

  public void updateTask(String subject, VocabEnum predicate, String originalObject, String newObject, String userId) throws TaskFilerException, UserNotFoundException {
    if (null == originalObject && null == newObject) return;
    if (predicate.equals(WORKFLOW.ASSIGNED_TO) || predicate.equals(WORKFLOW.CREATED_BY)) {
      newObject = usernameToId(newObject);
      if (null != originalObject) originalObject = usernameToId(originalObject);
    }
    try {
      StringJoiner stringJoiner = getTaskUpdateSparql(originalObject, newObject);
      Update update = conn.prepareInsertSparql(stringJoiner.toString());
      update.setBinding("subject", iri(subject));
      update.setBinding("predicate", predicate.asDbIri());
      if (null != newObject) update.setBinding("newVal", literal(newObject));
      if (null != originalObject) update.setBinding("originalObject", literal(originalObject));
      update.execute();
      updateHistory(subject, predicate, originalObject, newObject, userId);
    } catch (UpdateExecutionException e) {
      throw new TaskFilerException("Failed to update task", e);
    }
  }

  private static StringJoiner getTaskUpdateSparql(String originalObject, String newObject) {
    StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
    stringJoiner.add("DELETE { ?subject ?predicate ?originalObject }");
    stringJoiner.add("INSERT { ?subject ?predicate ?newObject }");
    stringJoiner.add("WHERE { ?subject ?predicate ?o ");
    if (null != originalObject) stringJoiner.add("FILTER (?o = ?originalObject)");
    stringJoiner.add("BIND(?o AS ?originalObject)");
    if (null != newObject) stringJoiner.add("BIND(?newVal AS ?newObject)");
    stringJoiner.add("}");
    return stringJoiner;
  }

  private void updateHistory(String subject, VocabEnum predicate, String originalObject, String newObject, String userId) throws TaskFilerException {
    try {
      ModelBuilder builder = new ModelBuilder();
      BNode bn = bnode();
      ModelBuilder ng = builder.namedGraph(Graph.IM.toString());
      ng.add(iri(subject), WORKFLOW.HISTORY.asDbIri(), bn)
        .add(bn, WORKFLOW.HISTORY_PREDICATE.asDbIri(), predicate.asDbIri())
        .add(bn, WORKFLOW.HISTORY_CHANGE_DATE.asDbIri(), literal(LocalDateTime.now()))
        .add(bn, WORKFLOW.MODIFIED_BY.asDbIri(), literal(userId));

      if (null != originalObject)
        ng.add(bn, WORKFLOW.HISTORY_ORIGINAL_OBJECT.asDbIri(), literal(originalObject));

      if (null != newObject)
        ng.add(bn, WORKFLOW.HISTORY_NEW_OBJECT.asDbIri(), literal(newObject));

      conn.add(builder.build());
    } catch (Exception e) {
      throw new TaskFilerException("Update task history failed.", e);
    }
  }

  private void replaceUsernameWithId(Task task) throws UserNotFoundException {
    AWSCognitoClient awsCognitoClient = new AWSCognitoClient();
    String cognitoIdRegex = "[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}";
    if (null != task.getCreatedBy() && !task.getCreatedBy().matches(cognitoIdRegex))
      task.setCreatedBy(awsCognitoClient.adminGetId(task.getCreatedBy()));
    if (null != task.getAssignedTo() && !(task.getAssignedTo().matches(cognitoIdRegex) || task.getAssignedTo().equals("UNASSIGNED")))
      task.setAssignedTo(awsCognitoClient.adminGetId(task.getAssignedTo()));
  }

  private String usernameToId(String username) throws UserNotFoundException {
    if (username.equals("UNASSIGNED")) return username;
    String cognitoIdRegex = "[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}";
    if (!username.matches(cognitoIdRegex)) return new AWSCognitoClient().adminGetId(username);
    return username;
  }

  private void buildTask(ModelBuilder builder, Task task) {
    builder.namedGraph(Graph.IM.toString())
      .add(iri(task.getId().getIri()), WORKFLOW.CREATED_BY.asDbIri(), literal(task.getCreatedBy()))
      .add(iri(task.getId().getIri()), RDF.TYPE.asDbIri(), literal(task.getType()))
      .add(iri(task.getId().getIri()), WORKFLOW.STATE.asDbIri(), literal(null == task.getState() ? TaskState.TODO : task.getState()))
      .add(iri(task.getId().getIri()), WORKFLOW.ASSIGNED_TO.asDbIri(), literal(null == task.getAssignedTo() ? "UNASSIGNED" : task.getAssignedTo()))
      .add(iri(task.getId().getIri()), WORKFLOW.DATE_CREATED.asDbIri(), literal(null == task.getDateCreated() ? LocalDateTime.now() : task.getDateCreated()))
      .add(iri(task.getId().getIri()), WORKFLOW.HOST_URL.asDbIri(), literal(task.getHostUrl()));
  }

  private EmailService getEmailService() {
    if (emailService == null) {
      emailService = new EmailService(
        System.getenv("EMAILER_HOST"),
        Integer.parseInt(System.getenv("EMAILER_PORT")),
        System.getenv("EMAILER_USERNAME"),
        System.getenv("EMAILER_PASSWORD")
      );
    }
    return emailService;
  }
}
