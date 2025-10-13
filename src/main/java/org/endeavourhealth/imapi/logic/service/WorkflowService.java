package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.UserService;
import org.endeavourhealth.imapi.dataaccess.UserRepository;
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
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

  public void updateBugReport(BugReport bugReport, HttpSession session) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    if (!user.name.equals(bugReport.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update bug report");
    BugReport originalBugReport = getBugReport(bugReport.getId().getIri());
    if (!originalBugReport.getProduct().equals(bugReport.getProduct()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.RELATED_PRODUCT, originalBugReport.getProduct(), bugReport.getProduct(), user.id);
    if (!originalBugReport.getModule().equals(bugReport.getModule()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.RELATED_MODULE, originalBugReport.getModule().toString(), bugReport.getModule().toString(), user.id);
    if (!originalBugReport.getOs().equals(bugReport.getOs()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.OPERATING_SYSTEM, originalBugReport.getOs().toString(), bugReport.getOs().toString(), user.id);
    if (!Objects.equals(originalBugReport.getOsOther(), bugReport.getOsOther()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.OPERATING_SYSTEM_OTHER, originalBugReport.getOsOther(), bugReport.getOsOther(), user.id);
    if (!originalBugReport.getBrowser().equals(bugReport.getBrowser()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.BROWSER, originalBugReport.getBrowser().toString(), bugReport.getBrowser().toString(), user.id);
    if (!Objects.equals(originalBugReport.getBrowserOther(), bugReport.getBrowserOther()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.BROWSER_OTHER, originalBugReport.getBrowserOther(), bugReport.getBrowserOther(), user.id);
    if (!originalBugReport.getDescription().equals(bugReport.getDescription()))
      workflowRepository.update(bugReport.getId().getIri(), RDFS.COMMENT, originalBugReport.getDescription(), bugReport.getDescription(), user.id);
    if (!originalBugReport.getReproduceSteps().equals(bugReport.getReproduceSteps()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.REPRODUCE_STEPS, originalBugReport.getReproduceSteps(), bugReport.getReproduceSteps(), user.id);
    if (!originalBugReport.getExpectedResult().equals(bugReport.getExpectedResult()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.EXPECTED_RESULT, originalBugReport.getExpectedResult(), bugReport.getExpectedResult(), user.id);
    if (!originalBugReport.getActualResult().equals(bugReport.getActualResult()))
      workflowRepository.update(bugReport.getId().getIri(), WORKFLOW.ACTUAL_RESULT, originalBugReport.getActualResult(), bugReport.getActualResult(), user.id);
    updateTask(bugReport, user.id);
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

  public void updateRoleRequest(RoleRequest roleRequest, HttpSession session) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    if (!user.name.equals(roleRequest.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update role request");
    RoleRequest originalRoleRequest = getRoleRequest(roleRequest.getId().getIri());
    if (!originalRoleRequest.getRole().equals(roleRequest.getRole()))
      workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.REQUESTED_ROLE, originalRoleRequest.getRole().toString(), roleRequest.getRole().toString(), user.id);
    updateTask(roleRequest, user.id);
  }

  public void approveRoleRequest(HttpSession session, RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    // TODO
    // new AWSCognitoClient().adminAddUserToGroup(roleRequest.getCreatedBy(), roleRequest.getRole());
    workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.STATE, roleRequest.getState().toString(), TaskState.APPROVED.toString(), user.id);
    workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.id);
  }

  public void rejectRoleRequest(HttpSession session, RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    workflowRepository.update(roleRequest.getId().getIri(), WORKFLOW.STATE, roleRequest.getState().toString(), TaskState.REJECTED.toString(), user.id);
  }

  public void createGraphRequest(GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException {
    graphRequest.setId(generateId());
    workflowRepository.createGraphRequest(graphRequest);
  }

  public GraphRequest getGraphRequest(String id) throws UserNotFoundException {
    return workflowRepository.getGraphRequest(id);
  }

  public void updateGraphRequest(GraphRequest graphRequest, HttpSession session) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    if (!user.name.equals(graphRequest.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update graph request");
    GraphRequest originalGraphRequest = getGraphRequest(graphRequest.getId().getIri());
    if (!originalGraphRequest.getGraph().equals(graphRequest.getGraph()))
      workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.REQUESTED_GRAPH, originalGraphRequest.getGraph().toString(), graphRequest.getGraph().toString(), user.id);
    updateTask(graphRequest, user.id);
  }

  public void approveGraphRequest(HttpSession session, GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    List<Graph> graphs = userRepository.getUserGraphs(user.id);
    if (!graphs.contains(graphRequest.getGraph())) {
      graphs.add(graphRequest.getGraph());
      userRepository.updateUserGraphs(user.id, graphs);
    }
    workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.STATE, graphRequest.getState().toString(), TaskState.APPROVED.toString(), user.id);
    workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.id);
  }

  public void rejectGraphRequest(HttpSession request, GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    workflowRepository.update(graphRequest.getId().getIri(), WORKFLOW.STATE, graphRequest.getState().toString(), TaskState.REJECTED.toString(), user.id);
  }

  public void createEntityApproval(EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException {
    entityApproval.setId(generateId());
    workflowRepository.createEntityApproval(entityApproval);
  }

  public EntityApproval getEntityApproval(String id) throws UserNotFoundException {
    return workflowRepository.getEntityApproval(id);
  }

  public void updateEntityApproval(EntityApproval entityApproval, HttpSession session) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    if (!user.name.equals(entityApproval.getCreatedBy()))
      throw new TaskFilerException("User does not have permission to update entity approval");
    EntityApproval originalEntityApproval = getEntityApproval(entityApproval.getId().getIri());
    if (!originalEntityApproval.getApprovalType().equals(entityApproval.getApprovalType()))
      workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.APPROVAL_TYPE, originalEntityApproval.getApprovalType().toString(), entityApproval.getApprovalType().toString(), user.id);
    updateTask(entityApproval, user.id);
  }

  public void approveEntityApproval(HttpSession session, EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    User user = casdoorService.getUser(session);
    //TODO entity draft replace active
    workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.STATE, entityApproval.getState().toString(), TaskState.APPROVED.toString(), user.id);
    workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.STATE, TaskState.APPROVED.toString(), TaskState.COMPLETE.toString(), user.id);
  }

  public void rejectEntityApproval(HttpSession session, EntityApproval entityApproval) throws TaskFilerException, JsonProcessingException, UserNotFoundException {
    User user = casdoorService.getUser(session);
    workflowRepository.update(entityApproval.getId().getIri(), WORKFLOW.STATE, entityApproval.getState().toString(), TaskState.REJECTED.toString(), user.id);
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
