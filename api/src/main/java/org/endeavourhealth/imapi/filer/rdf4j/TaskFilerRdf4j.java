package org.endeavourhealth.imapi.filer.rdf4j;

import jakarta.mail.MessagingException;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.service.EmailService;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.EntityApproval;
import org.endeavourhealth.imapi.model.workflow.RoleRequest;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.model.workflow.bugReport.Browser;
import org.endeavourhealth.imapi.model.workflow.bugReport.OperatingSystem;
import org.endeavourhealth.imapi.model.workflow.bugReport.Severity;
import org.endeavourhealth.imapi.model.workflow.bugReport.Status;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;
import org.joni.Regex;

import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.*;

public class TaskFilerRdf4j {
    private RepositoryConnection conn;
//    private EmailService emailService = new EmailService(
//        System.getenv("EMAILER_HOST"),
//        Integer.parseInt(System.getenv("EMAILER_PORT")),
//        System.getenv("EMAILER_USERNAME"),
//        System.getenv("EMAILER_PASSWORD")
//    );
    private AWSCognitoClient awsCognitoClient;
    public TaskFilerRdf4j(RepositoryConnection conn, AWSCognitoClient awsCognitoClient) {
        this.conn = conn;
    }
    public TaskFilerRdf4j() {
        this(ConnectionManager.getWorkflowConnection(),new AWSCognitoClient());
    }

    public void fileBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
        replaceUsernameWithId(bugReport);
        try {
            ModelBuilder builder = new ModelBuilder();
            buildTask(builder,bugReport);
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.RELATED_PRODUCT),literal(bugReport.getProduct()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.RELATED_VERSION),literal(bugReport.getVersion()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.RELATED_MODULE),literal(bugReport.getModule()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.OPERATING_SYSTEM),literal(bugReport.getOs()));
            if (bugReport.getOs().equals(OperatingSystem.OTHER)) builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.OPERATING_SYSTEM_OTHER),literal(bugReport.getOsOther()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.BROWSER),literal(bugReport.getBrowser()));
            if (bugReport.getBrowser().equals(Browser.OTHER)) builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.BROWSER_OTHER),literal(bugReport.getBrowserOther()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.SEVERITY),literal(null == bugReport.getSeverity() ? Severity.UNASSIGNED : bugReport.getSeverity()));
            builder.add(iri(bugReport.getId().getIri()), iri(IM.HAS_STATUS),literal(null == bugReport.getStatus() ? Status.NEW : bugReport.getStatus()));
            if (null != bugReport.getError()) builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.ERROR),literal(bugReport.getError()));
            builder.add(iri(bugReport.getId().getIri()), iri(RDFS.COMMENT),literal(bugReport.getDescription()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.REPRODUCE_STEPS),literal(bugReport.getReproduceSteps()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.EXPECTED_RESULT),literal(bugReport.getExpectedResult()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.ACTUAL_RESULT),literal(bugReport.getActualResult()));
            conn.add(builder.build());
            String emailSubject = "New bug report added: [" + bugReport.getId().getIri() + "]";
            String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/bugReport/" + bugReport.getId().getIri() + "\">here</a>";
//            emailService.sendMail(emailSubject, emailContent);
        } catch (RepositoryException e) {
            throw new TaskFilerException("Failed to file task", e);
//        } catch (MessagingException e) {
//            throw new TaskFilerException("Failed to send email",e);
        }
    }

    public void fileRoleRequest(RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException {
        replaceUsernameWithId(roleRequest);
        try {
            ModelBuilder builder = new ModelBuilder();
            buildTask(builder,roleRequest);
            builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.REQUESTED_ROLE),literal(roleRequest.getRole()));
            conn.add(builder.build());
            String emailSubject = "New role request added: [" + roleRequest.getId().getIri() + "]";
            String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/roleRequest/" + roleRequest.getId().getIri() + "\">here</a>";
//            emailService.sendMail(emailSubject, emailContent);
        } catch (RepositoryException e) {
            throw new TaskFilerException("Failed to file task", e);
//        } catch (MessagingException e) {
//            throw new TaskFilerException("Failed to send email",e);
        }
    }

    public void fileEntityApproval(EntityApproval entityApproval) throws UserNotFoundException, TaskFilerException {
        replaceUsernameWithId(entityApproval);
        try {
            ModelBuilder builder = new ModelBuilder();
            buildTask(builder,entityApproval);
            builder.add(iri(entityApproval.getId().getIri()), iri(WORKFLOW.APPROVAL_TYPE),literal(entityApproval.getApprovalType()));
            conn.add(builder.build());
            String emailSubject = "New role request added: [" + entityApproval.getId().getIri() + "]";
            String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/entityApproval/" + entityApproval.getId().getIri() + "\">here</a>";
//            emailService.sendMail(emailSubject, emailContent);
        } catch (RepositoryException e) {
            throw new TaskFilerException("Failed to file task", e);
//        } catch (MessagingException e) {
//            throw new TaskFilerException("Failed to send email",e);
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

    public void updateTask(String subject, String predicate, String object, Task task) throws TaskFilerException {
        try {
            String originalObject = getCurrentObject(subject, predicate);
            StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
            stringJoiner.add("DELETE { ?subject ?predicate ?oldObject }");
            stringJoiner.add("INSERT { ?subject ?predicate ?object }");
            stringJoiner.add("WHERE { ?subject ?predicate ?object }");
            Update update = conn.prepareUpdate(stringJoiner.toString());
            update.setBinding("subject", iri(subject));
            update.setBinding("predicate", iri(predicate));
            update.setBinding("object", literal(object));
            update.execute();
            updateHistory(subject, predicate, originalObject, object, task);
        } catch (UpdateExecutionException e) {
            throw new TaskFilerException("Failed to update task", e);
        }
    }

    private void updateHistory(String subject, String predicate, String originalObject, String newObject, Task task) throws TaskFilerException {
        try {
            task.addTaskHistory(new TaskHistory(subject, predicate, originalObject, newObject));
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
        String cognitoIdRegex = "\\w{4,}-\\w{4,}-\\w{4,}-\\w{4,}";
        if (!task.getCreatedBy().matches(cognitoIdRegex)) task.setCreatedBy(awsCognitoClient.adminGetId(task.getCreatedBy()));
        if (!(task.getAssignedTo().matches(cognitoIdRegex) || task.getAssignedTo().equals("UNASSIGNED"))) task.setAssignedTo(awsCognitoClient.adminGetId(task.getAssignedTo()));
    }

    private void buildTask(ModelBuilder builder, Task task) {
        builder.add(iri(task.getId().getIri()), iri(WORKFLOW.CREATED_BY),literal(task.getCreatedBy()));
        builder.add(iri(task.getId().getIri()), iri(RDF.TYPE),literal(task.getType()));
        builder.add(iri(task.getId().getIri()), iri(WORKFLOW.STATE),literal(task.getState()));
        builder.add(iri(task.getId().getIri()), iri(WORKFLOW.ASSIGNED_TO),literal(null == task.getAssignedTo() ? "UNASSIGNED" : task.getAssignedTo() ));
        builder.add(iri(task.getId().getIri()), iri(WORKFLOW.DATE_CREATED),literal(task.getDateCreated()));
    }
}
