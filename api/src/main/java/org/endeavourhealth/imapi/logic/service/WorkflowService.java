package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowService {

    private final WorkflowRepository workflowRepository = new WorkflowRepository();
    private final FilerService filerService = new FilerService();

    public WorkflowService() {}

    public void createBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
        bugReport.setId(generateId());
        workflowRepository.createBugReport(bugReport);
    }

    public BugReport getBugReport(String id) throws UserNotFoundException {
        return workflowRepository.getBugReport(id);
    }

    public WorkflowResponse getTasksByCreatedBy(WorkflowRequest request) throws UserNotFoundException {
        return workflowRepository.getTasksByCreatedBy(request);
    }

    public WorkflowResponse getTasksByAssignedTo(WorkflowRequest request) throws UserNotFoundException {
        return workflowRepository.getTasksByAssignedTo(request);
    }

    public WorkflowResponse getUnassignedTasks(WorkflowRequest request) throws UserNotFoundException {
        return workflowRepository.getUnassignedTasks(request);
    }

    public void deleteTask(String id) throws TaskFilerException {
        workflowRepository.deleteTask(id);
    }
    public TTIriRef generateId() {
        return TTIriRef.iri(workflowRepository.generateId());
    }

    public void createRoleRequest(RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException {
        roleRequest.setId(generateId());
        workflowRepository.createRoleRequest(roleRequest);
    }
}
