package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.casbin.casdoor.service.UserService;
import org.endeavourhealth.imapi.dataaccess.UserRepository;
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.WORKFLOW;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class WorkflowService {
  private final WorkflowRepository workflowRepository = new WorkflowRepository();
  private final CasdoorService casdoorService = new CasdoorService();
  private final UserRepository userRepository = new UserRepository();
  @Resource
  private UserService casdoorUserService;

  public void createBugReport(BugReport bugReport) throws TaskFilerException, UserNotFoundException {
    bugReport.setId(generateId());
    workflowRepository.createBugReport(bugReport);
  }

  public BugReport getBugReport(String id) throws UserNotFoundException {
    return workflowRepository.getBugReport(id);
  }

  public void updateBugReport(BugReport bugReport, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    if (!user.getUsername().equals(bugReport.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update bug report");
    BugReport originalBugReport = getBugReport(bugReport.getId().getIri());
    if (!originalBugReport.getProduct().equals(bugReport.getProduct()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.RELATED_PRODUCT, originalBugReport.getProduct(), bugReport.getProduct(), user.getId());
    if (!originalBugReport.getModule().equals(bugReport.getModule()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.RELATED_MODULE, originalBugReport.getModule().toString(), bugReport.getModule().toString(), user.getId());
    if (!originalBugReport.getOs().equals(bugReport.getOs()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.OPERATING_SYSTEM, originalBugReport.getOs().toString(), bugReport.getOs().toString(), user.getId());
    if (!Objects.equals(originalBugReport.getOsOther(), bugReport.getOsOther()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.OPERATING_SYSTEM_OTHER, originalBugReport.getOsOther(), bugReport.getOsOther(), user.getId());
    if (!originalBugReport.getBrowser().equals(bugReport.getBrowser()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.BROWSER, originalBugReport.getBrowser().toString(), bugReport.getBrowser().toString(), user.getId());
    if (!Objects.equals(originalBugReport.getBrowserOther(), bugReport.getBrowserOther()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.BROWSER_OTHER, originalBugReport.getBrowserOther(), bugReport.getBrowserOther(), user.getId());
    if (!originalBugReport.getDescription().equals(bugReport.getDescription()))
      workflowRepository.update(bugReport.getId().getIri(), RDFS.COMMENT, originalBugReport.getDescription(), bugReport.getDescription(), user.getId());
    if (!originalBugReport.getReproduceSteps().equals(bugReport.getReproduceSteps()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.REPRODUCE_STEPS, originalBugReport.getReproduceSteps(), bugReport.getReproduceSteps(), user.getId());
    if (!originalBugReport.getExpectedResult().equals(bugReport.getExpectedResult()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.EXPECTED_RESULT, originalBugReport.getExpectedResult(), bugReport.getExpectedResult(), user.getId());
    if (!originalBugReport.getActualResult().equals(bugReport.getActualResult()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.ACTUAL_RESULT, originalBugReport.getActualResult(), bugReport.getActualResult(), user.getId());
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
    return TTIriRef.iri(workflowRepository.generateId());
  }

  public void createRoleRequest(RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException {
    roleRequest.setId(generateId());
    workflowRepository.createRoleRequest(roleRequest);
  }

  public RoleRequest getRoleRequest(String id) throws UserNotFoundException {
    return workflowRepository.getRoleRequest(id);
  }

  public void updateRoleRequest(RoleRequest roleRequest, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    if (!user.getUsername().equals(roleRequest.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update role request");
    RoleRequest originalRoleRequest = getRoleRequest(roleRequest.getId().getIri());
    if (!originalRoleRequest.getRole().equals(roleRequest.getRole()))
      workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.REQUESTED_ROLE, originalRoleRequest.getRole().toString(), roleRequest.getRole().toString(), user.getId());
    updateTask(roleRequest, user.getId());
  }

  public void approveRoleRequest(HttpServletRequest request, RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    // TODO
    // new AWSCognitoClient().adminAddUserToGroup(roleRequest.getCreatedBy(), roleRequest.getRole());
    workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.STATE, roleRequest.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectRoleRequest(HttpServletRequest request, RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.STATE, roleRequest.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void createGraphRequest(GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException {
    graphRequest.setId(generateId());
    workflowRepository.createGraphRequest(graphRequest);
  }

  public GraphRequest getGraphRequest(String id) throws UserNotFoundException {
    return workflowRepository.getGraphRequest(id);
  }

  public void updateGraphRequest(GraphRequest graphRequest, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    if (!user.getUsername().equals(graphRequest.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update graph request");
    GraphRequest originalGraphRequest = getGraphRequest(graphRequest.getId().getIri());
    if (!originalGraphRequest.getGraph().equals(graphRequest.getGraph()))
      workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.REQUESTED_GRAPH, originalGraphRequest.getGraph().toString(), graphRequest.getGraph().toString(), user.getId());
    updateTask(graphRequest, user.getId());
  }

  public void approveGraphRequest(HttpServletRequest request, GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    List<Graph> graphs = userRepository.getUserGraphs(user.getId());
    if (!graphs.contains(graphRequest.getGraph())) {
      graphs.add(graphRequest.getGraph());
      userRepository.updateUserGraphs(user.getId(), graphs);
    }
    workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.STATE, graphRequest.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectGraphRequest(HttpServletRequest request, GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.STATE, graphRequest.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void createEntityApproval(EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException {
    entityApproval.setId(generateId());
    workflowRepository.createEntityApproval(entityApproval);
  }

  public EntityApproval getEntityApproval(String id) throws UserNotFoundException {
    return workflowRepository.getEntityApproval(id);
  }

  public void updateEntityApproval(EntityApproval entityApproval, HttpServletRequest request) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    if (!user.getUsername().equals(entityApproval.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update entity approval");
    EntityApproval originalEntityApproval = getEntityApproval(entityApproval.getId().getIri());
    if (!originalEntityApproval.getApprovalType().equals(entityApproval.getApprovalType()))
      workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.APPROVAL_TYPE, originalEntityApproval.getApprovalType().toString(), entityApproval.getApprovalType().toString(), user.getId());
    updateTask(entityApproval, user.getId());
  }

  public void approveEntityApproval(HttpServletRequest request, EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    //TODO entity draft replace active
    workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.STATE, entityApproval.getState().toString(), TaskState.APPROVED.toString(), user.getId());
    workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.getId());
  }

  public void rejectEntityApproval(HttpServletRequest request, EntityApproval entityApproval) throws TaskFilerException, JsonProcessingException, UserNotFoundException {
    User user = casdoorService.getUser(request);
    workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.STATE, entityApproval.getState().toString(), TaskState.REJECTED.toString(), user.getId());
  }

  public void updateTask(Task task, String userId) throws TaskFilerException, UserNotFoundException {
    Task originalTask = getTask(task.getId().getIri());
    if (!task.getType().equals(originalTask.getType()))
      workflowRepository.update(task.getId().getIri(), RDF.TYPE, originalTask.getType().toString(), task.getType().toString(), userId);
    if (!task.getState().equals(originalTask.getState()))
      workflowRepository.update(task.getId().getIri(), WORKFLOW.STATE, originalTask.getState().toString(), task.getState().toString(), userId);
    if (!task.getAssignedTo().equals(originalTask.getAssignedTo()))
      workflowRepository.update(task.getId().getIri(), WORKFLOW.ASSIGNED_TO, originalTask.getAssignedTo(), task.getAssignedTo(), userId);
  }
}
