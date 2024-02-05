package org.endeavourhealth.imapi.filer.rdf4j;

import jakarta.mail.MessagingException;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.service.EmailService;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.RoleRequest;
import org.endeavourhealth.imapi.model.workflow.bugReport.Browser;
import org.endeavourhealth.imapi.model.workflow.bugReport.OperatingSystem;
import org.endeavourhealth.imapi.model.workflow.bugReport.Severity;
import org.endeavourhealth.imapi.model.workflow.bugReport.Status;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;

import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

public class TaskFilerRdf4j {
    private RepositoryConnection conn;
    private EmailService emailService = new EmailService(
        System.getenv("EMAILER_HOST"),
        Integer.parseInt(System.getenv("EMAILER_PORT")),
        System.getenv("EMAILER_USERNAME"),
        System.getenv("EMAILER_PASSWORD")
    );
    public TaskFilerRdf4j(RepositoryConnection conn) {
        this.conn = conn;
    }
    public TaskFilerRdf4j() {
        this(ConnectionManager.getWorkflowConnection());
    }

    public void fileBugReport(BugReport bugReport) throws TaskFilerException {
        try {
            ModelBuilder builder = new ModelBuilder();
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.CREATED_BY),literal(bugReport.getCreatedBy()));
            builder.add(iri(bugReport.getId().getIri()), iri(RDF.TYPE),literal(bugReport.getType()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.STATE),literal(bugReport.getState()));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.ASSIGNED_TO),literal(null == bugReport.getAssignedTo() ? "UNASSIGNED" : bugReport.getAssignedTo() ));
            builder.add(iri(bugReport.getId().getIri()), iri(WORKFLOW.DATE_CREATED),literal(bugReport.getDateCreated()));
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
            emailService.sendMail(emailSubject, emailContent);
        } catch (RepositoryException e) {
            throw new TaskFilerException("Failed to file task", e);
        } catch (MessagingException e) {
            throw new TaskFilerException("Failed to send email",e);
        }
    }

    public void fileRoleRequest(RoleRequest roleRequest) throws TaskFilerException {
        try {
            ModelBuilder builder = new ModelBuilder();
            builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.CREATED_BY),literal(roleRequest.getCreatedBy()));
            builder.add(iri(roleRequest.getId().getIri()), iri(RDF.TYPE),literal(roleRequest.getType()));
            builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.STATE),literal(roleRequest.getState()));
            builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.ASSIGNED_TO),literal(null == roleRequest.getAssignedTo() ? "UNASSIGNED" : roleRequest.getAssignedTo() ));
            builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.DATE_CREATED),literal(roleRequest.getDateCreated()));
            builder.add(iri(roleRequest.getId().getIri()), iri(WORKFLOW.REQUESTED_ROLE),literal(roleRequest.getRole()));
            conn.add(builder.build());
            String emailSubject = "New role request added: [" + roleRequest.getId().getIri() + "]";
            String emailContent = "Click <a href=\"https://im.endhealth.net/#/workflow/roleRequest/" + roleRequest.getId().getIri() + "\">here</a>";
            emailService.sendMail(emailSubject, emailContent);
        } catch (RepositoryException e) {
            throw new TaskFilerException("Failed to file task", e);
        } catch (MessagingException e) {
            throw new TaskFilerException("Failed to send email",e);
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

    public void replaceBugReport(BugReport bugReport) throws TaskFilerException {
        deleteTask(bugReport.getId().getIri());
        fileBugReport(bugReport);
    }
}
