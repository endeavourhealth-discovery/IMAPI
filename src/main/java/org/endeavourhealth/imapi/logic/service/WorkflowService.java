package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.extensions.TTIriRefExtensionsKt;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.interfacemanager.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WorkflowService {
  private final WorkflowRepository workflowRepository = new WorkflowRepository();
  private final SecurityService securityService = new SecurityService();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void createBugReport(BugReport bugReportExtended) throws TaskFilerException, UserNotFoundException {
    bugReportExtended.setId(generateId());
    workflowRepository.createBugReport(bugReportExtended);
  }

  public BugReport getBugReport(String id) throws UserNotFoundException {
    return workflowRepository.getBugReport(id);
  }

  public void updateBugReport(BugReport bugReport, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(bugReport.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update bug report");
    BugReport originalBugReport = getBugReport(bugReport.getId().getIri());
    if (!originalBugReport.getProduct().equals(bugReport.getProduct()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.RELATED_PRODUCT, originalBugReport.getProduct(), bugReport.getProduct(), user.getId());
    if (!originalBugReport.getModule().equals(bugReport.getModule()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.RELATED_MODULE, originalBugReport.getModule().toString(), bugReport.getModule().toString(), user.getId());
    if (!originalBugReport.getOs().equals(bugReport.getOs()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.OPERATING_SYSTEM, originalBugReport.getOs().toString(), bugReport.getOs().toString(), user.getId());
    if (!Objects.equals(originalBugReport.getOsOther(), bugReport.getOsOther()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.OPERATING_SYSTEM_OTHER, originalBugReport.getOsOther(), bugReport.getOsOther(), user.getId());
    if (!originalBugReport.getBrowser().equals(bugReport.getBrowser()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.BROWSER, originalBugReport.getBrowser().toString(), bugReport.getBrowser().toString(), user.getId());
    if (!Objects.equals(originalBugReport.getBrowserOther(), bugReport.getBrowserOther()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.BROWSER_OTHER, originalBugReport.getBrowserOther(), bugReport.getBrowserOther(), user.getId());
    if (!originalBugReport.getDescription().equals(bugReport.getDescription()))
      workflowRepository.update(bugReport.getId().getIri(), RdfsVocab.COMMENT, originalBugReport.getDescription(), bugReport.getDescription(), user.getId());
    if (!originalBugReport.getReproduceSteps().equals(bugReport.getReproduceSteps()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.REPRODUCE_STEPS, originalBugReport.getReproduceSteps(), bugReport.getReproduceSteps(), user.getId());
    if (!originalBugReport.getExpectedResult().equals(bugReport.getExpectedResult()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.EXPECTED_RESULT, originalBugReport.getExpectedResult(), bugReport.getExpectedResult(), user.getId());
    if (!originalBugReport.getActualResult().equals(bugReport.getActualResult()))
      workflowRepository.update(bugReport.getId().getIri(), WorkflowVocab.ACTUAL_RESULT, originalBugReport.getActualResult(), bugReport.getActualResult(), user.getId());
    updateTask(bugReport, user.getId());
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
    return TTIriRefExtensionsKt.iri(new TTIriRef(), workflowRepository.generateId());
  }

  public void createRoleRequest(RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException {
    roleRequest.setId(generateId());
    workflowRepository.createRoleRequest(roleRequest);
  }

  public RoleRequest getRoleRequest(String id) throws UserNotFoundException {
    return workflowRepository.getRoleRequest(id);
  }

  public void updateRoleRequest(RoleRequest roleRequest, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(roleRequest.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update role request");
    RoleRequest originalRoleRequest = getRoleRequest(roleRequest.getId().getIri());
    if (!originalRoleRequest.getRole().equals(roleRequest.getRole()))
      workflowRepository.update(roleRequest.getId().getIri(), WorkflowVocab.REQUESTED_ROLE, originalRoleRequest.getRole().toString(), roleRequest.getRole().toString(), user.getId());
    updateTask(roleRequest, user.getId());
  }

  public void approveRoleRequest(HttpServletRequest request, RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    // TODO
    // new AWSCognitoClient().adminAddUserToGroup(roleRequest.getCreatedBy(), roleRequest.getRole());
    workflowRepository.update(roleRequest.getId().getIri(), WorkflowVocab.STATE, roleRequest.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(roleRequest.getId().getIri(), WorkflowVocab.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectRoleRequest(HttpServletRequest request, RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    workflowRepository.update(roleRequest.getId().getIri(), WorkflowVocab.STATE, roleRequest.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void createNamespaceRequest(NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException {
    namespaceRequest.setId(generateId());
    workflowRepository.createNamespaceRequest(namespaceRequest);
  }

  public NamespaceRequest getNamespaceRequest(String id) throws UserNotFoundException, JsonProcessingException {
    return workflowRepository.getNamespaceRequest(id);
  }

  public void updateNamespaceRequest(NamespaceRequest namespaceRequest, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(namespaceRequest.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update namespace request");
    NamespaceRequest originalNamespaceRequest = getNamespaceRequest(namespaceRequest.getId().getIri());
    if (!originalNamespaceRequest.getNamespacePermission().getIri().equals(namespaceRequest.getNamespacePermission().getIri()) || !originalNamespaceRequest.getNamespacePermission().isRead() == namespaceRequest.getNamespacePermission().isRead() || !originalNamespaceRequest.getNamespacePermission().isWrite() == namespaceRequest.getNamespacePermission().isWrite())
      workflowRepository.update(namespaceRequest.getId().getIri(), WorkflowVocab.REQUESTED_NAMESPACE, objectMapper.writeValueAsString(originalNamespaceRequest.getNamespacePermission()), objectMapper.writeValueAsString(namespaceRequest.getNamespacePermission()), user.getId());
    updateTask(namespaceRequest, user.getId());
  }

  public void approveNamespaceRequest(HttpServletRequest request, NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    List<NamespacePermission> namespaces = user.getNamespaces();
    if (!namespaces.contains(namespaceRequest.getNamespacePermission())) {
      namespaces.add(namespaceRequest.getNamespacePermission());
      securityService.updateUserNamespaces(user.getId(), namespaces, request);
    }
    workflowRepository.update(namespaceRequest.getId().getIri(), WorkflowVocab.STATE, namespaceRequest.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(namespaceRequest.getId().getIri(), WorkflowVocab.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectNamespaceRequest(HttpServletRequest request, NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    workflowRepository.update(namespaceRequest.getId().getIri(), WorkflowVocab.STATE, namespaceRequest.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void createEntityApproval(EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException {
    entityApproval.setId(generateId());
    workflowRepository.createEntityApproval(entityApproval);
  }

  public EntityApproval getEntityApproval(String id) throws UserNotFoundException {
    return workflowRepository.getEntityApproval(id);
  }

  public void updateEntityApproval(EntityApproval entityApproval, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    if (!user.getUsername().equals(entityApproval.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update entity approval");
    EntityApproval originalEntityApproval = getEntityApproval(entityApproval.getId().getIri());
    if (!originalEntityApproval.getApprovalType().equals(entityApproval.getApprovalType()))
      workflowRepository.update(entityApproval.getId().getIri(), WorkflowVocab.APPROVAL_TYPE, originalEntityApproval.getApprovalType().toString(), entityApproval.getApprovalType().toString(), user.getId());
    updateTask(entityApproval, user.getId());
  }

  public void approveEntityApproval(HttpServletRequest request, EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = securityService.getUser(request);
    //TODO entity draft replace active
    workflowRepository.update(entityApproval.getId().getIri(), WorkflowVocab.STATE, entityApproval.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(entityApproval.getId().getIri(), WorkflowVocab.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectEntityApproval(HttpServletRequest request, EntityApproval entityApproval) throws TaskFilerException, JsonProcessingException, UserNotFoundException {
    User user = securityService.getUser(request);
    workflowRepository.update(entityApproval.getId().getIri(), WorkflowVocab.STATE, entityApproval.getState().toString(), TaskState.REJECTED.toString(), user.getId());
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
