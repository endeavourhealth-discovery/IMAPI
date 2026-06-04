package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.interfacemanager.model.TaskState;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WorkflowService {
  private final WorkflowRepository workflowRepository = new WorkflowRepository();
  private final SecurityService securityService = new SecurityService();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void createBugReport(BugReportExtended bugReportExtended) throws TaskFilerException, UserNotFoundException {
    bugReportExtended.setId(generateId());
    workflowRepository.createBugReport(bugReportExtended);
  }

  public BugReportExtended getBugReport(String id) throws UserNotFoundException {
    return workflowRepository.getBugReport(id);
  }

  public void updateBugReport(BugReportExtended bugReportExtended, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(bugReportExtended.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update bug report");
    BugReportExtended originalBugReportExtended = getBugReport(bugReportExtended.getId().getIri());
    if (!originalBugReportExtended.getProduct().equals(bugReportExtended.getProduct()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.RELATED_PRODUCT, originalBugReportExtended.getProduct(), bugReportExtended.getProduct(), user.getId());
    if (!originalBugReportExtended.getModule().equals(bugReportExtended.getModule()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.RELATED_MODULE, originalBugReportExtended.getModule().toString(), bugReportExtended.getModule().toString(), user.getId());
    if (!originalBugReportExtended.getOs().equals(bugReportExtended.getOs()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.OPERATING_SYSTEM, originalBugReportExtended.getOs().toString(), bugReportExtended.getOs().toString(), user.getId());
    if (!Objects.equals(originalBugReportExtended.getOsOther(), bugReportExtended.getOsOther()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.OPERATING_SYSTEM_OTHER, originalBugReportExtended.getOsOther(), bugReportExtended.getOsOther(), user.getId());
    if (!originalBugReportExtended.getBrowser().equals(bugReportExtended.getBrowser()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.BROWSER, originalBugReportExtended.getBrowser().toString(), bugReportExtended.getBrowser().toString(), user.getId());
    if (!Objects.equals(originalBugReportExtended.getBrowserOther(), bugReportExtended.getBrowserOther()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.BROWSER_OTHER, originalBugReportExtended.getBrowserOther(), bugReportExtended.getBrowserOther(), user.getId());
    if (!originalBugReportExtended.getDescription().equals(bugReportExtended.getDescription()))
      workflowRepository.update(bugReportExtended.getId().getIri(), RdfsVocab.COMMENT, originalBugReportExtended.getDescription(), bugReportExtended.getDescription(), user.getId());
    if (!originalBugReportExtended.getReproduceSteps().equals(bugReportExtended.getReproduceSteps()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.REPRODUCE_STEPS, originalBugReportExtended.getReproduceSteps(), bugReportExtended.getReproduceSteps(), user.getId());
    if (!originalBugReportExtended.getExpectedResult().equals(bugReportExtended.getExpectedResult()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.EXPECTED_RESULT, originalBugReportExtended.getExpectedResult(), bugReportExtended.getExpectedResult(), user.getId());
    if (!originalBugReportExtended.getActualResult().equals(bugReportExtended.getActualResult()))
      workflowRepository.update(bugReportExtended.getId().getIri(), WorkflowVocab.ACTUAL_RESULT, originalBugReportExtended.getActualResult(), bugReportExtended.getActualResult(), user.getId());
    updateTask(bugReportExtended, user.getId());
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

  public TTIriRefExtended generateId() {
    return new TTIriRefExtended(workflowRepository.generateId());
  }

  public void createRoleRequest(RoleRequestExtended roleRequestExtended) throws TaskFilerException, UserNotFoundException {
    roleRequestExtended.setId(generateId());
    workflowRepository.createRoleRequest(roleRequestExtended);
  }

  public RoleRequestExtended getRoleRequest(String id) throws UserNotFoundException {
    return workflowRepository.getRoleRequest(id);
  }

  public void updateRoleRequest(RoleRequestExtended roleRequestExtended, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(roleRequestExtended.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update role request");
    RoleRequestExtended originalRoleRequestExtended = getRoleRequest(roleRequestExtended.getId().getIri());
    if (!originalRoleRequestExtended.getRole().equals(roleRequestExtended.getRole()))
      workflowRepository.update(roleRequestExtended.getId().getIri(), WorkflowVocab.REQUESTED_ROLE, originalRoleRequestExtended.getRole().toString(), roleRequestExtended.getRole().toString(), user.getId());
    updateTask(roleRequestExtended, user.getId());
  }

  public void approveRoleRequest(HttpServletRequest request, RoleRequestExtended roleRequestExtended) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    // TODO
    // new AWSCognitoClient().adminAddUserToGroup(roleRequest.getCreatedBy(), roleRequest.getRole());
    workflowRepository.update(roleRequestExtended.getId().getIri(), WorkflowVocab.STATE, roleRequestExtended.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(roleRequestExtended.getId().getIri(), WorkflowVocab.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectRoleRequest(HttpServletRequest request, RoleRequestExtended roleRequestExtended) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    workflowRepository.update(roleRequestExtended.getId().getIri(), WorkflowVocab.STATE, roleRequestExtended.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void createNamespaceRequest(NamespaceRequestExtended namespaceRequestExtended) throws TaskFilerException, UserNotFoundException {
    namespaceRequestExtended.setId(generateId());
    workflowRepository.createNamespaceRequest(namespaceRequestExtended);
  }

  public NamespaceRequestExtended getNamespaceRequest(String id) throws UserNotFoundException, JsonProcessingException {
    return workflowRepository.getNamespaceRequest(id);
  }

  public void updateNamespaceRequest(NamespaceRequestExtended namespaceRequestExtended, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(namespaceRequestExtended.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update namespace request");
    NamespaceRequestExtended originalNamespaceRequestExtended = getNamespaceRequest(namespaceRequestExtended.getId().getIri());
    if (!originalNamespaceRequestExtended.getNamespacePermission().getIri().equals(namespaceRequestExtended.getNamespacePermission().getIri()) || !originalNamespaceRequestExtended.getNamespacePermission().isRead() == namespaceRequestExtended.getNamespacePermission().isRead() || !originalNamespaceRequestExtended.getNamespacePermission().isWrite() == namespaceRequestExtended.getNamespacePermission().isWrite())
      workflowRepository.update(namespaceRequestExtended.getId().getIri(), WorkflowVocab.REQUESTED_NAMESPACE, objectMapper.writeValueAsString(originalNamespaceRequestExtended.getNamespacePermission()), objectMapper.writeValueAsString(namespaceRequestExtended.getNamespacePermission()), user.getId());
    updateTask(namespaceRequestExtended, user.getId());
  }

  public void approveNamespaceRequest(HttpServletRequest request, NamespaceRequestExtended namespaceRequestExtended) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    List<NamespacePermission> namespaces = user.getNamespaces();
    if (!namespaces.contains(namespaceRequestExtended.getNamespacePermission())) {
      namespaces.add(namespaceRequestExtended.getNamespacePermission());
      securityService.updateUserNamespaces(user.getId(), namespaces, request);
    }
    workflowRepository.update(namespaceRequestExtended.getId().getIri(), WorkflowVocab.STATE, namespaceRequestExtended.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(namespaceRequestExtended.getId().getIri(), WorkflowVocab.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectNamespaceRequest(HttpServletRequest request, NamespaceRequestExtended namespaceRequestExtended) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    workflowRepository.update(namespaceRequestExtended.getId().getIri(), WorkflowVocab.STATE, namespaceRequestExtended.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void createEntityApproval(EntityApprovalExtended entityApprovalExtended) throws TaskFilerException, UserNotFoundException {
    entityApprovalExtended.setId(generateId());
    workflowRepository.createEntityApproval(entityApprovalExtended);
  }

  public EntityApprovalExtended getEntityApproval(String id) throws UserNotFoundException {
    return workflowRepository.getEntityApproval(id);
  }

  public void updateEntityApproval(EntityApprovalExtended entityApprovalExtended, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(entityApprovalExtended.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update entity approval");
    EntityApprovalExtended originalEntityApprovalExtended = getEntityApproval(entityApprovalExtended.getId().getIri());
    if (!originalEntityApprovalExtended.getApprovalType().equals(entityApprovalExtended.getApprovalType()))
      workflowRepository.update(entityApprovalExtended.getId().getIri(), WorkflowVocab.APPROVAL_TYPE, originalEntityApprovalExtended.getApprovalType().toString(), entityApprovalExtended.getApprovalType().toString(), user.getId());
    updateTask(entityApprovalExtended, user.getId());
  }

  public void approveEntityApproval(HttpServletRequest request, EntityApprovalExtended entityApprovalExtended) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    //TODO entity draft replace active
    workflowRepository.update(entityApprovalExtended.getId().getIri(), WorkflowVocab.STATE, entityApprovalExtended.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(entityApprovalExtended.getId().getIri(), WorkflowVocab.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectEntityApproval(HttpServletRequest request, EntityApprovalExtended entityApprovalExtended) throws TaskFilerException, JsonProcessingException, UserNotFoundException {
    User user = securityService.getUser(request);
    workflowRepository.update(entityApprovalExtended.getId().getIri(), WorkflowVocab.STATE, entityApprovalExtended.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void updateTask(Task task, String userId) throws TaskFilerException, UserNotFoundException {
    Task originalTask = getTask(task.getId().getIri());
    if (!task.getType().equals(originalTask.getType()))
      workflowRepository.update(task.getId().getIri(), RdfVocab.
        TYPE, originalTask.getType().toString(), task.getType().toString(), userId);
    if (!task.getState().equals(originalTask.getState()))
      workflowRepository.update(task.getId().getIri(), WorkflowVocab.STATE, originalTask.getState().toString(), task.getState().toString(), userId);
    if (!task.getAssignedTo().equals(originalTask.getAssignedTo()))
      workflowRepository.update(task.getId().getIri(), WorkflowVocab.ASSIGNED_TO, originalTask.getAssignedTo(), task.getAssignedTo(), userId);
  }
}
