package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TaskFilerRdf4j;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.bugReport.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.*;

public class WorkflowRepository {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowRepository.class);
    private final TaskFilerRdf4j taskFilerRdf4j = new TaskFilerRdf4j();
    public void createBugReport(BugReport bugReport) throws TaskFilerException {
        if (null == bugReport.getId() || bugReport.getId().getIri().isEmpty()) bugReport.setId(TTIriRef.iri(generateId()));
        taskFilerRdf4j.fileBugReport(bugReport);
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
        sparqlJoiner.add("OPTIONAL {?s ?osOther ?osOtherData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?browser ?browserData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?browserOther ?browserOtherData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?severity ?severityData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?status ?statusData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?error ?errorData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?description ?descriptionData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?reproduceSteps ?reproduceStepsData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?expectedResult ?expectedResultData ;}");
        sparqlJoiner.add("OPTIONAL {?s ?actualResult ?actualResultData ;}");
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

    public void deleteTask(String taskId) throws TaskFilerException {
        taskFilerRdf4j.deleteTask(taskId);
    }

    public void replaceBugReport(BugReport bugReport) throws TaskFilerException {
        taskFilerRdf4j.replaceBugReport(bugReport);
    }

    public WorkflowResponse getTasksByCreatedBy(WorkflowRequest request) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
        sparqlJoiner.add("SELECT ?s ?createdByData ?typeData ?assignedToData ?stateData ?dateCreatedData WHERE {");
        sparqlJoiner.add("?s ?createdBy ?createdByData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?type ?typeData .");
        sparqlJoiner.add("}");
        if(null != request.getSize()) sparqlJoiner.add("LIMIT " + request.getSize());
        if(null != request.getPage() && null != request.getSize()) sparqlJoiner.add("OFFSET " + request.getSize() * (request.getPage() == 0 ? 0 : request.getPage()-1));
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
        response.setPage(null == request.getPage() ? 1 : request.getPage());
        response.setCount(countTaskByCreatedBy(request));
        return response;
    }

    public Integer countTaskByCreatedBy(WorkflowRequest request) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        stringJoiner.add("SELECT (COUNT(DISTINCT ?s) AS ?count) WHERE { ?s ?createdBy ?createdByData }");
        String sparql = stringJoiner.toString();
        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            qry.setBinding("createdBy", iri(WORKFLOW.CREATED_BY));
            qry.setBinding("createdByData",literal(request.getUserId()));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return Integer.parseInt(bs.getValue("count").stringValue());
                }
            }
        }
        return null;
    }

    public WorkflowResponse getTasksByAssignedTo(WorkflowRequest request) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
        sparqlJoiner.add("SELECT ?s ?createdByData ?typeData ?assignedToData ?stateData ?dateCreatedData WHERE {");
        sparqlJoiner.add("?s ?createdBy ?createdByData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?type ?typeData .");
        sparqlJoiner.add("}");
        if(null != request.getSize()) sparqlJoiner.add("LIMIT " + request.getSize());
        if(null != request.getPage() && null != request.getSize()) sparqlJoiner.add("OFFSET " + request.getSize() * (request.getPage() == 0 ? 0 : request.getPage()-1));
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
        response.setPage(null == request.getPage() ? 1 : request.getPage());
        response.setCount(countTaskByAssignedTo(request));
        return response;
    }

    public Integer countTaskByAssignedTo(WorkflowRequest request) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        stringJoiner.add("SELECT (COUNT(DISTINCT ?s) AS ?count) WHERE { ?s ?assignedTo ?assignedToData }");
        String sparql = stringJoiner.toString();
        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            qry.setBinding("assignedTo", iri(WORKFLOW.ASSIGNED_TO));
            qry.setBinding("assignedToData",literal(request.getUserId()));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return Integer.parseInt(bs.getValue("count").stringValue());
                }
            }
        }
        return null;
    }

    public WorkflowResponse getUnassignedTasks(WorkflowRequest request) {
        StringJoiner sparqlJoiner = new StringJoiner(System.lineSeparator());
        sparqlJoiner.add("SELECT ?s ?createdByData ?typeData ?assignedToData ?stateData ?dateCreatedData WHERE {");
        sparqlJoiner.add("?s ?createdBy ?createdByData ;");
        sparqlJoiner.add("?dateCreated ?dateCreatedData ;");
        sparqlJoiner.add("?assignedTo ?assignedToData ;");
        sparqlJoiner.add("?state ?stateData ;");
        sparqlJoiner.add("?type ?typeData .");
        sparqlJoiner.add("}");
        if(null != request.getSize()) sparqlJoiner.add("LIMIT " + request.getSize());
        if(null != request.getPage() && null != request.getSize()) sparqlJoiner.add("OFFSET " + request.getSize() * (request.getPage() == 0 ? 0 : request.getPage()-1));
        String sparql = sparqlJoiner.toString();
        WorkflowResponse response = new WorkflowResponse();

        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            setTaskBindings(qry);
            qry.setBinding("assignedToData",literal("UNASSIGNED"));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    Task task = new Task();
                    mapTaskFromBindingSet(task,bs);
                    response.addTask(task);
                }
            }
        }
        response.setPage(null == request.getPage() ? 1 : request.getPage());
        response.setCount(countTaskByAssignedTo(request));
        return response;
    }

    public void createRoleRequest(RoleRequest roleRequest) throws TaskFilerException {
        if (null == roleRequest.getId() || roleRequest.getId().getIri().isEmpty()) roleRequest.setId(TTIriRef.iri(generateId()));
        taskFilerRdf4j.fileRoleRequest(roleRequest);
    }

    public String generateId() {
        String sparql = "SELECT DISTINCT ?s WHERE {?s ?p ?o .} ORDER BY DESC (?s) LIMIT 1";
        try (RepositoryConnection conn = ConnectionManager.getWorkflowConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            qry.setBinding("p",iri(WORKFLOW.CREATED_BY));
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
        qry.setBinding("type",iri(RDF.TYPE));
        qry.setBinding("state",iri(WORKFLOW.STATE));
        qry.setBinding("assignedTo",iri(WORKFLOW.ASSIGNED_TO));
        qry.setBinding("dateCreated",iri(WORKFLOW.DATE_CREATED));
    }

    private void setBugReportBindings(TupleQuery qry) {
        setTaskBindings(qry);
        qry.setBinding("product",iri(WORKFLOW.RELATED_PRODUCT));
        qry.setBinding("version",iri(WORKFLOW.RELATED_VERSION));
        qry.setBinding("module",iri(WORKFLOW.RELATED_MODULE));
        qry.setBinding("os",iri(WORKFLOW.OPERATING_SYSTEM));
        qry.setBinding("osOther",iri(WORKFLOW.OPERATING_SYSTEM_OTHER));
        qry.setBinding("browser",iri(WORKFLOW.BROWSER));
        qry.setBinding("browserOther",iri(WORKFLOW.BROWSER_OTHER));
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
        if (null != bs.getValue("productData")) bugReport.setProduct(bs.getValue("productData").stringValue());
        if (null != bs.getValue("moduleData")) bugReport.setModule(TaskModule.valueOf(bs.getValue("moduleData").stringValue()));
        if (null != bs.getValue("versionData")) bugReport.setVersion(bs.getValue("versionData").stringValue());
        if (null != bs.getValue("osData")) bugReport.setOs(OperatingSystem.valueOf(bs.getValue("osData").stringValue()));
        if (null != bs.getValue("osOtherData")) bugReport.setOsOther(bs.getValue("osOtherData").stringValue());
        if (null != bs.getValue("browserData")) bugReport.setBrowser(Browser.valueOf(bs.getValue("browserData").stringValue()));
        if (null != bs.getValue("browserOtherData"))bugReport.setBrowserOther(bs.getValue("browserOtherData").stringValue());
        if (null != bs.getValue("severityData")) bugReport.setSeverity(Severity.valueOf(bs.getValue("severityData").stringValue()));
        if (null != bs.getValue("statusData")) bugReport.setStatus(Status.valueOf(bs.getValue("statusData").stringValue()));
        if (null != bs.getValue("errorData"))bugReport.setError(bs.getValue("errorData").stringValue());
        if (null != bs.getValue("descriptionData")) bugReport.setDescription(bs.getValue("descriptionData").stringValue());
        if (null != bs.getValue("reproduceStepsData")) bugReport.setReproduceSteps(bs.getValue("reproduceStepsData").stringValue());
        if (null != bs.getValue("expectedResultData")) bugReport.setExpectedResult(bs.getValue("expectedResultData").stringValue());
        if (null != bs.getValue("actualResultData")) bugReport.setActualResult(bs.getValue("actualResultData").stringValue());
    }

    private void mapTaskFromBindingSet(Task task, BindingSet bs) {
        task.setId(TTIriRef.iri(bs.getValue("s").stringValue()));
        task.setType(TaskType.valueOf(bs.getValue("typeData").stringValue()));
        task.setCreatedBy(bs.getValue("createdByData").stringValue());
        task.setAssignedTo(bs.getValue("assignedToData").stringValue());
        task.setState(TaskState.valueOf(bs.getValue("stateData").stringValue()));
        task.setDateCreated(LocalDate.parse(bs.getValue("dateCreatedData").stringValue()));
    }
}
