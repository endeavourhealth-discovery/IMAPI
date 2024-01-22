package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.bugReport.*;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.*;

public class WorkflowRepository {
    public BugReport createBugReport(BugReport bugReport) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator()).add("INSERT DATA {");
        sparqlJoiner.add("?s ?createdBy ?createdByData ;");
        sparqlJoiner.add("?type ?typeData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
        if (null != bugReport.getProduct()) sparqlJoiner.add("?product ?productData ;");
        if (null != bugReport.getVersion()) sparqlJoiner.add("?version ?versionData ;");
        if (null != bugReport.getModule()) sparqlJoiner.add("?module ?moduleData ;");
        if (null != bugReport.getOs()) sparqlJoiner.add("?os ?osData ;");
        if (null != bugReport.getBrowser()) sparqlJoiner.add("?browser ?browserData ;");
        if (null != bugReport.getSeverity()) sparqlJoiner.add("?severity ?severityData ;");
        if (null != bugReport.getStatus()) sparqlJoiner.add("?status ?statusData ;");
        if (null != bugReport.getError()) sparqlJoiner.add("?error ?errorData ;");
        if (null != bugReport.getDescription()) sparqlJoiner.add("?description ?descriptionData ;");
        if (null != bugReport.getReproduceSteps()) sparqlJoiner.add("?reproduceSteps ?reproduceStepsData ;");
        if (null != bugReport.getExpectedResult()) sparqlJoiner.add("?expectedResult ?expectedResultData ;");
        if (null != bugReport.getActualResult()) sparqlJoiner.add("?actualResult ?actualResultData ;");
        String sparql =   sparqlJoiner.add(". }").toString();

        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            Update qry = prepareUpdateSparql(conn, sparql);
            setBugReportBindings(qry,bugReport);
            qry.execute();
            return bugReport;
        }
    }

    public BugReport getBugReport(String id) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
        sparqlJoiner.add("SELECT ?s ?typeData ?createdByData ?assignedToData ?productData ?moduleData ?versionData ?osData ?browserData ?severityData ?statusData ?errorData ?descriptionData ?reproduceStepsData ?expectedResultData ?dateCreatedData ?stateData ");
        sparqlJoiner.add("WHERE { ");
        sparqlJoiner.add("?s ?type ?typeData ;");
        sparqlJoiner.add("?createdBy ?createdByData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData .");
        sparqlJoiner.add("OPTIONAL {?s ?product ?productData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?module ?moduleData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?version ?versionData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?os ?osData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?browser ?browserData ;");
        sparqlJoiner.add("OPTIONAL {?s ?severity ?severityData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?status ?statusData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?error ?errorData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?description ?descriptionData ;");
        sparqlJoiner.add("OPTIONAL {?s ?reproduceSteps ?reproduceStepsData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?expectedResult ?expectedResultData ;}");
        sparqlJoiner.add("OPTIONAL {?actualResult ?actualResultData ;}");
        sparqlJoiner.add("}");
        String sparql = sparqlJoiner.toString();

        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            setBugReportBindings(qry);
            qry.setBinding("s",iri(id));

            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BugReport bugReport = new BugReport();
                    BindingSet bs = rs.next();
                    mapBugReportFromBindingSet(bugReport,bs);
                    return bugReport;
                }
            }
        }
        return null;
    }

    public WorkflowResponse getWorkflowsByCreatedBy(WorkflowRequest request) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
        sparqlJoiner.add("SELECT ?s ?typeData ?assignedToData ?stateData ?dateCreatedData WHERE {");
        sparqlJoiner.add("?s ?createdBy ?createdByData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?type ?typeData .");
        sparqlJoiner.add("}");
        String sparql = sparqlJoiner.toString();
        WorkflowResponse response = new WorkflowResponse();

        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            setTaskBindings(qry);
            qry.setBinding("createdByData",literal(request.getUserId()));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    Task task = new Task();
                    mapTaskFromBindingSet(task,bs);
                    response.addTask(task);
                }
            }
        }
        return response;
    }

    public WorkflowResponse getWorkflowsByAssignedTo(WorkflowRequest request) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
        sparqlJoiner.add("SELECT ?s ?typeData ?assignedToData ?stateData ?dateCreatedData WHERE {");
        sparqlJoiner.add("?s ?createdBy ?createdByData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?type ?typeData .");
        sparqlJoiner.add("}");
        String sparql = sparqlJoiner.toString();
        WorkflowResponse response = new WorkflowResponse();

        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            setTaskBindings(qry);
            qry.setBinding("assignedToData",literal(request.getUserId()));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    Task task = new Task();
                    mapTaskFromBindingSet(task,bs);
                    response.addTask(task);
                }
            }
        }
        return response;
    }

    private void setBugReportBindings(Update qry, BugReport bugReport) {
        if (null != bugReport.getCreatedBy()) {
            qry.setBinding("createdBy", iri(WORKFLOW.CREATED_BY));
            qry.setBinding("createdByData",literal(bugReport.getId()));
        }
        if (null != bugReport.getType()) {
            qry.setBinding("type",iri(RDF.TYPE));
            qry.setBinding("typeData",literal(bugReport.getType()));
        }
        if (null != bugReport.getState()) {
            qry.setBinding("state",iri(WORKFLOW.STATE));
            qry.setBinding("stateData",literal(bugReport.getState()));
        }
        if (null != bugReport.getAssignedTo()) {
            qry.setBinding("assignedTo",iri(WORKFLOW.ASSIGNED_TO));
            qry.setBinding("assignedToData",literal(bugReport.getAssignedTo()));
        }
        if (null != bugReport.getDateCreated()) {
            qry.setBinding("dateCreated",iri(WORKFLOW.DATE_CREATED));
            qry.setBinding("dateCreatedData",literal(WORKFLOW.DATE_CREATED));
        }
        if (null != bugReport.getProduct()) {
            qry.setBinding("product",iri(WORKFLOW.RELATED_PRODUCT));
            qry.setBinding("productData",literal(bugReport.getProduct()));
        }
        if (null != bugReport.getVersion()) {
            qry.setBinding("version",iri(WORKFLOW.RELATED_VERSION));
            qry.setBinding("versionData",literal(bugReport.getVersion()));
        }
        if (null != bugReport.getModule()) {
            qry.setBinding("module",iri(WORKFLOW.RELATED_MODULE));
            qry.setBinding("moduleData",literal(bugReport.getModule()));
        }
        if (null != bugReport.getOs()) {
            qry.setBinding("os",iri(WORKFLOW.OPERATING_SYSTEM));
            qry.setBinding("osData",literal(bugReport.getOs()));
        }
        if (null != bugReport.getBrowser()) {
            qry.setBinding("browser",iri(WORKFLOW.BROWSER));
            qry.setBinding("browserData",literal(bugReport.getBrowser()));
        }
        if (null != bugReport.getSeverity()) {
            qry.setBinding("severity",iri(WORKFLOW.SEVERITY));
            qry.setBinding("severityData",literal(bugReport.getSeverity()));
        }
        if (null != bugReport.getStatus()) {
            qry.setBinding("status",iri(IM.HAS_STATUS));
            qry.setBinding("statusData",literal(bugReport.getStatus()));
        }
        if (null != bugReport.getError()) {
            qry.setBinding("error",iri(WORKFLOW.ERROR));
            qry.setBinding("errorData",literal(bugReport.getError()));
        }
        if (null != bugReport.getDescription()) {
            qry.setBinding("description",iri(RDFS.COMMENT));
            qry.setBinding("descriptionData",literal(bugReport.getDescription()));
        }
        if (null != bugReport.getReproduceSteps()) {
            qry.setBinding("reproduceSteps",iri(WORKFLOW.REPRODUCE_STEPS));
            qry.setBinding("reproduceStepsData",literal(bugReport.getReproduceSteps()));
        }
        if (null != bugReport.getExpectedResult()) {
            qry.setBinding("expectedResult",iri(WORKFLOW.EXPECTED_RESULT));
            qry.setBinding("expectedResultData",literal(bugReport.getExpectedResult()));
        }
        if (null != bugReport.getActualResult()) {
            qry.setBinding("actualResult",iri(WORKFLOW.ACTUAL_RESULT));
            qry.setBinding("actualResultData",literal(bugReport.getActualResult()));
        }
    }

    private void setBugReportBindings(TupleQuery qry) {
        qry.setBinding("createdBy", iri(WORKFLOW.CREATED_BY));
        qry.setBinding("type",iri(RDF.TYPE));
        qry.setBinding("state",iri(WORKFLOW.STATE));
        qry.setBinding("assignedTo",iri(WORKFLOW.ASSIGNED_TO));
        qry.setBinding("dateCreated",iri(WORKFLOW.DATE_CREATED));
    }

    private void setTaskBindings(TupleQuery qry) {
        qry.setBinding("createdBy", iri(WORKFLOW.CREATED_BY));
        qry.setBinding("type",iri(RDF.TYPE));
        qry.setBinding("state",iri(WORKFLOW.STATE));
        qry.setBinding("assignedTo",iri(WORKFLOW.ASSIGNED_TO));
        qry.setBinding("dateCreated",iri(WORKFLOW.DATE_CREATED));
        qry.setBinding("product",iri(WORKFLOW.RELATED_PRODUCT));
        qry.setBinding("version",iri(WORKFLOW.RELATED_VERSION));
        qry.setBinding("module",iri(WORKFLOW.RELATED_MODULE));
        qry.setBinding("os",iri(WORKFLOW.OPERATING_SYSTEM));
        qry.setBinding("browser",iri(WORKFLOW.BROWSER));
        qry.setBinding("severity",iri(WORKFLOW.SEVERITY));
        qry.setBinding("status",iri(IM.HAS_STATUS));
        qry.setBinding("error",iri(WORKFLOW.ERROR));
        qry.setBinding("description",iri(RDFS.COMMENT));
        qry.setBinding("reproduceSteps",iri(WORKFLOW.REPRODUCE_STEPS));
        qry.setBinding("expectedResult",iri(WORKFLOW.EXPECTED_RESULT));
        qry.setBinding("actualResult",iri(WORKFLOW.ACTUAL_RESULT));
    }

    private void mapBugReportFromBindingSet(BugReport bugReport, BindingSet bs) {
        bugReport.setId(TTIriRef.iri(bs.getValue("s").stringValue()));
        bugReport.setType(TaskType.valueOf(bs.getValue("typeData").stringValue()));
        bugReport.setCreatedBy(bs.getValue("createdByData").toString());
        bugReport.setAssignedTo(bs.getValue("assignedToData").stringValue());
        bugReport.setState(TaskState.valueOf(bs.getValue("stateData").stringValue()));
        bugReport.setDateCreated(LocalDate.parse(bs.getValue("dateCreatedData").stringValue()));
        bugReport.setProduct(bs.getValue("productData").stringValue());
        bugReport.setModule(TaskModule.valueOf(bs.getValue("moduleData").stringValue()));
        bugReport.setVersion(bs.getValue("versionData").stringValue());
        bugReport.setOs(OperatingSystem.valueOf(bs.getValue("osData").stringValue()));
        bugReport.setBrowser(Browser.valueOf(bs.getValue("browserData").stringValue()));
        bugReport.setSeverity(Severity.valueOf(bs.getValue("severityData").stringValue()));
        bugReport.setStatus(Status.valueOf(bs.getValue("statusData").stringValue()));
        bugReport.setError(bs.getValue("errorData").stringValue());
        bugReport.setDescription(bs.getValue("descriptionData").stringValue());
        bugReport.setReproduceSteps(bs.getValue("reproduceStepsData").stringValue());
        bugReport.setExpectedResult(bs.getValue("expectedResultData").stringValue());
        bugReport.setActualResult(bs.getValue("actualResultData").stringValue());
    }

    private void mapTaskFromBindingSet(Task task, BindingSet bs) {
        task.setId(TTIriRef.iri(bs.getValue("s").stringValue()));
        task.setType(TaskType.valueOf(bs.getValue("typeData").stringValue()));
        task.setCreatedBy(bs.getValue("createdByData").toString());
        task.setAssignedTo(bs.getValue("assignedToData").stringValue());
        task.setState(TaskState.valueOf(bs.getValue("stateData").stringValue()));
        task.setDateCreated(LocalDate.parse(bs.getValue("dateCreatedData").stringValue()));
    }
}
