package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TaskFilerRdf4j;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;
import org.springframework.stereotype.Component;

@Component
public class WorkflowService {

  private final WorkflowRepository workflowRepository = new WorkflowRepository();
  private final TaskFilerRdf4j taskFilerRdf4j = new TaskFilerRdf4j();

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

  public Task getTask(String id) throws UserNotFoundException {
    return workflowRepository.getTask(id);
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

  public RoleRequest getRoleRequest(String id) throws UserNotFoundException {
    return workflowRepository.getRoleRequest(id);
  }

  public void createEntityApproval(EntityApproval entityApproval) throws UserNotFoundException, TaskFilerException {
    entityApproval.setId(generateId());
    workflowRepository.createEntityApproval(entityApproval);
  }

  public void updateTask(Task task, String userId) throws UserNotFoundException, TaskFilerException {
    Task originalTask = getTask(task.getId().getIri());
    if (!task.getType().equals(originalTask.getType()))
      workflowRepository.update(task.getId().getIri(), RDF.TYPE, originalTask.getType().toString(), task.getType().toString(), userId);
    if (!task.getState().equals(originalTask.getState()))
      workflowRepository.update(task.getId().getIri(), WORKFLOW.STATE, originalTask.getState().toString(), task.getState().toString(), userId);
    if (!task.getAssignedTo().equals(originalTask.getAssignedTo()))
      workflowRepository.update(task.getId().getIri(), WORKFLOW.ASSIGNED_TO, originalTask.getAssignedTo(), task.getAssignedTo(), userId);
  }
}
