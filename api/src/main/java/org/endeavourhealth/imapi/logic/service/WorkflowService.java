package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.model.workflow.WorkflowRequest;
import org.endeavourhealth.imapi.model.workflow.WorkflowResponse;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowService {

    private final WorkflowRepository workflowRepository = new WorkflowRepository();

    public BugReport createBugReport(BugReport bugReport) {
        return workflowRepository.createBugReport(bugReport);
    }

    public BugReport getBugReport(String id) {
        return workflowRepository.getBugReport(id);
    }

    public WorkflowResponse getWorkflowsByCreatedBy(WorkflowRequest request) {
        return workflowRepository.getWorkflowsByCreatedBy(request);
    }

    public WorkflowResponse getWorkflowsByAssignedTo(WorkflowRequest request) {
        return workflowRepository.getWorkflowsByAssignedTo(request);
    }
}
