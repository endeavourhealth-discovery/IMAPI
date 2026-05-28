package org.endeavourhealth.imapi.filer.rdf4j;

import jakarta.mail.MessagingException;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.databases.WorkflowDB;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.EmailService;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.*;

@Component
public class TaskFilerRdf4j {
  private final WorkflowDB conn;

  private EmailService emailService;

  public TaskFilerRdf4j() {
    conn = WorkflowDB.getConnection();
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

  public void fileBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(bugReport);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, bugReport);
      ModelBuilder namedGraph = builder.namedGraph(GRAPH.IM.toString());
      namedGraph.add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.RELATED_PRODUCT), literal(bugReport.getProduct()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.RELATED_VERSION), literal(bugReport.getVersion()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.RELATED_MODULE), literal(bugReport.getModule()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.OPERATING_SYSTEM), literal(bugReport.getOs()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.BROWSER), literal(bugReport.getBrowser()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.SEVERITY), literal(null == bugReport.getSeverity() ? Severity.UNASSIGNED : bugReport.getSeverity()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(IM.HAS_STATUS), literal(null == bugReport.getStatus() ? Status.NEW : bugReport.getStatus()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(RDFS.COMMENT), literal(bugReport.getDescription()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.REPRODUCE_STEPS), literal(bugReport.getReproduceSteps()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.EXPECTED_RESULT), literal(bugReport.getExpectedResult()))
        .add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.ACTUAL_RESULT), literal(bugReport.getActualResult()));


      if (bugReport.getOs().equals(OperatingSystem.OTHER))
        namedGraph.add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.OPERATING_SYSTEM_OTHER), literal(bugReport.getOsOther()));

      if (bugReport.getBrowser().equals(Browser.OTHER))
        namedGraph.add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.BROWSER_OTHER), literal(bugReport.getBrowserOther()));

      if (null != bugReport.getError())
        namedGraph.add(iri(bugReport.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.ERROR), literal(bugReport.getError()));

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
      builder.namedGraph(GRAPH.IM.toString())
        .add(iri(roleRequest.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.REQUESTED_ROLE), literal(roleRequest.getRole()));
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

  public void fileNamespaceRequest(NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(namespaceRequest);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, namespaceRequest);
      builder.namedGraph(GRAPH.IM.toString())
        .add(iri(namespaceRequest.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.REQUESTED_NAMESPACE), literal(namespaceRequest.getNamespacePermission()));
      conn.add(builder.build());
      String emailSubject = "New namespace request added: [" + namespaceRequest.getId().getIri() + "]";
      String emailContent = "Click <a href=\"" + namespaceRequest.getHostUrl() + "/#/workflow/namespaceRequest/" + namespaceRequest.getId().getIri() + "\">here</a>";
      getEmailService().sendMail(emailSubject, emailContent, "namespacerequest@endeavourhealth.net");
    } catch (RepositoryException e) {
      throw new TaskFilerException("Failed to file task", e);
    } catch (MessagingException e) {
      throw new TaskFilerException("Failed to send email", e);
    }
  }

  public void fileEntityApproval(EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException {
    replaceUsernameWithId(entityApproval);
    try {
      ModelBuilder builder = new ModelBuilder();
      buildTask(builder, entityApproval);
      builder.namedGraph(GRAPH.IM.toString())
        .add(iri(entityApproval.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.APPROVAL_TYPE), literal(entityApproval.getApprovalType()));
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

  public void replaceBugReport(BugReport bugReport) throws TaskFilerException, IOException, UserNotFoundException {
    deleteTask(bugReport.getId().getIri());
    fileBugReport(bugReport);
  }

  public void updateTask(String subject, Enum<?> predicate, String originalObject, String newObject, String userId) throws TaskFilerException, UserNotFoundException {
    if (null == originalObject && null == newObject) return;
    if (predicate.equals(WORKFLOW.ASSIGNED_TO) || predicate.equals(WORKFLOW.CREATED_BY)) {
      newObject = usernameToId(newObject);
      if (null != originalObject) originalObject = usernameToId(originalObject);
    }
    try {
      StringJoiner stringJoiner = getTaskUpdateSparql(originalObject, newObject);
      Update update = conn.prepareInsertSparql(stringJoiner.toString());
      update.setBinding("subject", iri(subject));
      update.setBinding("predicate", EnumUtils.asDbIri(predicate));
      if (null != newObject) update.setBinding("newVal", literal(newObject));
      if (null != originalObject) update.setBinding("originalObject", literal(originalObject));
      update.execute();
      updateHistory(subject, predicate, originalObject, newObject, userId);
    } catch (UpdateExecutionException e) {
      throw new TaskFilerException("Failed to update task", e);
    }
  }

  private void updateHistory(String subject, Enum<?> predicate, String originalObject, String newObject, String userId) throws TaskFilerException {
    try {
      ModelBuilder builder = new ModelBuilder();
      BNode bn = bnode();
      ModelBuilder ng = builder.namedGraph(GRAPH.IM.toString());
      ng.add(iri(subject), EnumUtils.asDbIri(WORKFLOW.HISTORY), bn)
        .add(bn, EnumUtils.asDbIri(WORKFLOW.HISTORY_PREDICATE), EnumUtils.asDbIri(predicate))
        .add(bn, EnumUtils.asDbIri(WORKFLOW.HISTORY_CHANGE_DATE), literal(LocalDateTime.now()))
        .add(bn, EnumUtils.asDbIri(WORKFLOW.MODIFIED_BY), literal(userId));

      if (null != originalObject)
        ng.add(bn, EnumUtils.asDbIri(WORKFLOW.HISTORY_ORIGINAL_OBJECT), literal(originalObject));

      if (null != newObject)
        ng.add(bn, EnumUtils.asDbIri(WORKFLOW.HISTORY_NEW_OBJECT), literal(newObject));

      conn.add(builder.build());
    } catch (Exception e) {
      throw new TaskFilerException("Update task history failed.", e);
    }
  }

  private void replaceUsernameWithId(Task task) throws UserNotFoundException {
    String cognitoIdRegex = "[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}";
    if (null != task.getCreatedBy() && !task.getCreatedBy().matches(cognitoIdRegex)) {
/*
      try {
        task.setCreatedBy(casdoorUserService.getUser(task.getCreatedBy()).id);
      } catch (IOException e) {
        throw new UserNotFoundException(task.getCreatedBy());
      }
*/
    }
    if (null != task.getAssignedTo() && !(task.getAssignedTo().matches(cognitoIdRegex) || task.getAssignedTo().equals("UNASSIGNED"))) {
/*
      try {
        task.setAssignedTo(casdoorUserService.getUser(task.getAssignedTo()).id);
      } catch (IOException e) {
        throw new UserNotFoundException(task.getAssignedTo());
      }
*/
    }
  }

  private String usernameToId(String username) throws UserNotFoundException {
    if (username.equals("UNASSIGNED")) return username;
    String cognitoIdRegex = "[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}-[a-zA-Z0-9]{4,}";
    if (!username.matches(cognitoIdRegex)) {
/*
      try {
        return casdoorUserService.getUser(username).id;
      } catch (IOException e) {
        throw new UserNotFoundException(username);
      }
*/
    }
    return username;
  }

  private void buildTask(ModelBuilder builder, Task task) {
    builder.namedGraph(GRAPH.IM.toString())
      .add(iri(task.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.CREATED_BY), literal(task.getCreatedBy()))
      .add(iri(task.getId().getIri()), EnumUtils.asDbIri(RDF.TYPE), literal(task.getType()))
      .add(iri(task.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.STATE), literal(null == task.getState() ? TaskState.TODO : task.getState()))
      .add(iri(task.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.ASSIGNED_TO), literal(null == task.getAssignedTo() ? "UNASSIGNED" : task.getAssignedTo()))
      .add(iri(task.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.DATE_CREATED), literal(null == task.getDateCreated() ? LocalDateTime.now() : task.getDateCreated()))
      .add(iri(task.getId().getIri()), EnumUtils.asDbIri(WORKFLOW.HOST_URL), literal(task.getHostUrl()));
  }

  private EmailService getEmailService() {
    if (emailService == null) {
      emailService = new EmailService(
        System.getenv("EMAILER_PORTAL_HOST"),
        Integer.parseInt(System.getenv("EMAILER_PORTAL_PORT")),
        System.getenv("EMAILER_PORTAL_USERNAME"),
        System.getenv("EMAILER_PORTAL_PASSWORD")
      );
    }
    return emailService;
  }
}
