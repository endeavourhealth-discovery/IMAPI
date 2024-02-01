package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.model.workflow.WorkflowRequest;
import org.endeavourhealth.imapi.model.workflow.WorkflowResponse;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowService {

    private final WorkflowRepository workflowRepository = new WorkflowRepository();
    private final FilerService filerService = new FilerService();

    public void createBugReport(BugReport bugReport) throws TaskFilerException {
        bugReport.setId(generateId());
        workflowRepository.createBugReport(bugReport);
    }

    public BugReport getBugReport(String id) {
        return workflowRepository.getBugReport(id);
    }

    public WorkflowResponse getTasksByCreatedBy(WorkflowRequest request) {
        return workflowRepository.getTasksByCreatedBy(request);
    }

    public WorkflowResponse getTasksByAssignedTo(WorkflowRequest request) {
        return workflowRepository.getTasksByAssignedTo(request);
    }

    public WorkflowResponse getUnassignedTasks(WorkflowRequest request) {
        return workflowRepository.getUnassignedTasks(request);
    }

    public void deleteTask(String id) throws TaskFilerException {
        workflowRepository.deleteTask(id);
    }
    public TTIriRef generateId() {
        return TTIriRef.iri(workflowRepository.generateId());
    }
}
