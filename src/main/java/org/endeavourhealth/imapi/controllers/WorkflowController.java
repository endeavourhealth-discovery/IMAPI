package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.logic.service.WorkflowService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/workflow")
@CrossOrigin(origins = "*")
@Tag(name = "WorkflowController")
@RequestScope
@Slf4j
public class WorkflowController {

  private final WorkflowService workflowService = new WorkflowService();
  private final CasbinEnforcer casbinEnforcer = new CasbinEnforcer();
  private final CasdoorService casdoorService = new CasdoorService();

  @Operation(summary = "Create Bug Report", description = "Endpoint to create a new bug report.")
  @PostMapping(value = "/createBugReport")
  public void createBugReport(HttpSession session, @RequestBody BugReport bugReport) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.CreateBugReport.POST")) {
      log.debug("createBugReport");
      User user = casdoorService.getUser(session);
      if (null == bugReport.getCreatedBy()) bugReport.setCreatedBy(user.getId());
      workflowService.createBugReport(bugReport);
    }
  }

  @Operation(summary = "Get Bug Report", description = "Fetch a bug report using its unique ID.")
  @GetMapping(value = "/getBugReport", produces = "application/json")
  public BugReport getBugReport(@RequestParam(name = "id") String id, HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.bugReport.GET")) {
      log.debug("getBugReport");
      return workflowService.getBugReport(id);
    }
  }

  @Operation(summary = "Update bug report")
  @PostMapping(value = "/updateBugReport")
  @PreAuthorize("@guard.hasPermission('BUG_REPORT','WRITE')")
  public void updateBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateBugReport.POST")) {
      log.debug("updateBugReport");
      workflowService.updateBugReport(bugReport, request.getSession());
    }
  }

  @Operation(summary = "Get Tasks by Creator", description = "Retrieve tasks created by the currently authenticated user.")
  @GetMapping(value = "/getTasksByCreatedBy", produces = "application/json")
  @PreAuthorize("@guard.hasPermission('TASK','READ')")
  public WorkflowResponse getTasksByCreatedBy(HttpSession session, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") int size) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.tasksByCreator.GET")) {
      log.debug("getWorkflowsByCreatedBy");
      WorkflowRequest wfRequest = new WorkflowRequest(session);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getTasksByCreatedBy(wfRequest);
    }
  }

  @Operation(summary = "Get Tasks by Assignee", description = "Retrieve tasks assigned to the currently authenticated user.")
  @GetMapping(value = "/getTasksByAssignedTo", produces = "application/json")
  @PreAuthorize("@guard.hasPermission('TASK','READ')")
  public WorkflowResponse getTasksByAssignedTo(HttpSession session, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.tasksByAssignedTo.GET")) {
      log.debug("getWorkflowsByAssignedTo");
      WorkflowRequest wfRequest = new WorkflowRequest(session);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getTasksByAssignedTo(wfRequest);
    }
  }

  @Operation(summary = "Get Unassigned Tasks", description = "Retrieve tasks that are not assigned to any user.")
  @GetMapping(value = "/getUnassignedTasks", produces = "application/json")
  @PreAuthorize("@guard.hasPermission('TASK','READ')")
  public WorkflowResponse getUnassignedTasks(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.unassignedTasks.GET")) {
      log.debug("getUnassignedTasks");
      WorkflowRequest wfRequest = new WorkflowRequest(request.getSession());
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getUnassignedTasks(wfRequest);
    }
  }

  @Operation(summary = "Get a Task", description = "Fetch a task using its unique ID.")
  @GetMapping(value = "/getTask", produces = "application/json")
  @PreAuthorize("@guard.hasPermission('TASK','READ')")
  public Task getTask(@RequestParam(name = "id") String id) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workfflow.task.GET")) {
      log.debug("getTask");
      return workflowService.getTask(id);
    }
  }

  @Operation(summary = "Delete a Task", description = "Delete a task by its unique ID.")
  @DeleteMapping(value = "/deleteTask")
  @PreAuthorize("@guard.hasPermission('TASK','DELETE')")
  public void deleteTask(@RequestParam(name = "id") String id) throws TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.task.DELETE")) {
      workflowService.deleteTask(id);
    }
  }

  @Operation(summary = "Create Role Request", description = "Submit a role request created by the user.")
  @PostMapping(value = "/createRoleRequest")
  public void createRoleRequest(HttpSession session, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createRoleRequest.POST")) {
      User user = casdoorService.getUser(session);
      if (null == roleRequest.getCreatedBy()) roleRequest.setCreatedBy(user.getId());
      workflowService.createRoleRequest(roleRequest);
    }
  }

  @Operation(summary = "Get Role Request", description = "Retrieve a role request using its unique ID.")
  @GetMapping(value = "/roleRequest", produces = "application/json")
  @PreAuthorize("@guard.hasPermission('ROLE_REQUEST','READ')")
  public RoleRequest getRoleRequest(@RequestParam(name = "id") String id, HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.roleRequest.GET")) {
      log.debug("getRoleRequest");
      return workflowService.getRoleRequest(id);
    }
  }

  @Operation(summary = "Update role request", description = "Update a role request workflow task")
  @PostMapping(value = "/updateRoleRequest")
  @PreAuthorize("@guard.hasPermission('ROLE_REQUEST','WRITE')")
  public void updateRoleRequest(HttpSession session, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateRoleRequest.POST")) {
      log.debug("updateRoleRequest");
      workflowService.updateRoleRequest(roleRequest, session);
    }
  }

  @Operation(summary = "Approve role request")
  @PostMapping(value = "/approveRoleRequest")
  @PreAuthorize("@guard.hasPermission('ROLE_REQUEST','APPROVE')")
  public void approveRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveRoleRequest.POST")) {
      log.debug("approveRoleRequest");
      workflowService.approveRoleRequest(request.getSession(), roleRequest);
    }
  }

  @Operation(summary = "Reject role request")
  @PostMapping(value = "/rejectRoleRequest")
  @PreAuthorize("@guard.hasPermission('ROLE_REQUEST','APPROVE')")
  public void rejectRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectRoleRequest.POST")) {
      log.debug("rejectRoleRequest");
      workflowService.rejectRoleRequest(request.getSession(), roleRequest);
    }
  }

  @Operation(summary = "Create Graph Request", description = "Submit a graph request created by the user.")
  @PostMapping(value = "/createGraphRequest")
  public void createGraphRequest(HttpSession session, @RequestBody GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createGraphRequest.POST")) {
      User user = casdoorService.getUser(session);
      if (null == graphRequest.getCreatedBy()) graphRequest.setCreatedBy(user.getId());
      workflowService.createGraphRequest(graphRequest);
    }
  }

  @Operation(summary = "Get Graph Request", description = "Retrieve a graph request using its unique ID.")
  @GetMapping(value = "/graphRequest", produces = "application/json")
  @PreAuthorize("@guard.hasPermission('GRAPH_REQUEST','READ')")
  public GraphRequest getGraphRequest(@RequestParam(name = "id") String id, HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.graphRequest.GET")) {
      log.debug("getGraphRequest");
      return workflowService.getGraphRequest(id);
    }
  }

  @Operation(summary = "Update graph request", description = "Update a graph request workflow task")
  @PostMapping(value = "/updateGraphRequest")
  @PreAuthorize("@guard.hasPermission('GRAPH_REQUEST','WRITE')")
  public void updateGraphRequest(HttpSession session, @RequestBody GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateGraphRequest.POST")) {
      log.debug("updateGraphRequest");
      workflowService.updateGraphRequest(graphRequest, session);
    }
  }

  @Operation(summary = "Approve graph request")
  @PostMapping(value = "/approveGraphRequest")
  @PreAuthorize("@guard.hasPermission('GRAPH_REQUEST','APPROVE')")
  public void approveGraphRequest(HttpServletRequest request, @RequestBody GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveGraphRequest.POST")) {
      log.debug("approveGraphRequest");
      workflowService.approveGraphRequest(request.getSession(), graphRequest);
    }
  }

  @Operation(summary = "Reject graph request")
  @PostMapping(value = "/rejectGraphRequest")
  @PreAuthorize("@guard.hasPermission('GRAPH_REQUEST','APPROVE')")
  public void rejectGraphRequest(HttpServletRequest request, @RequestBody GraphRequest graphRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectGraphRequest.POST")) {
      log.debug("rejectGraphRequest");
      workflowService.rejectGraphRequest(request.getSession(), graphRequest);
    }
  }

  @Operation(summary = "Create Entity Approval", description = "Submit an approval request for an entity.")
  @PostMapping(value = "/createEntityApproval")
  @PreAuthorize("@guard.hasPermission('ENTITY_APPROVAL','WRITE')")
  public void createEntityApproval(HttpSession session, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createEntityApproval.POST")) {
      log.debug("createEntityApproval");
      User user = casdoorService.getUser(session);
      if (null == entityApproval.getCreatedBy()) entityApproval.setCreatedBy(user.getId());
      workflowService.createEntityApproval(entityApproval);
    }
  }

  @Operation(summary = "Get entity approval", description = "Get an approval request for an entity by id")
  @GetMapping(value = "/entityApproval")
  @PreAuthorize("@guard.hasPermission('ENTITY_APPROVAL','READ')")
  public EntityApproval getEntityApproval(@RequestParam(name = "id") String id) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.entityApproval.GET")) {
      log.debug("getEntityApproval");
      return workflowService.getEntityApproval(id);
    }
  }

  @Operation(summary = "Update entity approval", description = "Update an approval request for an entity")
  @PostMapping(value = "/updateEntityApproval")
  @PreAuthorize("@guard.hasPermission('ENTITY_APPROVAL','WRITE')")
  public void updateEntityApproval(HttpSession session, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateEntityApproval.POST")) {
      log.debug("updateEntityApproval");
      workflowService.updateEntityApproval(entityApproval, session);
    }
  }

  @Operation(summary = "Approve entity approval")
  @PostMapping(value = "/approveEntityApproval")
  @PreAuthorize("@guard.hasPermission('ENTITY_APPROVAL','APPROVE')")
  public void approveEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveEntityApproval.POST")) {
      log.debug("approveEntityApproval");
      workflowService.approveEntityApproval(request.getSession(), entityApproval);
    }
  }

  @Operation(summary = "Reject entity approval")
  @PostMapping(value = "/rejectEntityApproval")
  @PreAuthorize("@guard.hasPermission('ENTITY_APPROVAL','APPROVE')")
  public void rejectEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectEntityApproval.POST")) {
      log.debug("rejectEntityApproval");
      workflowService.rejectEntityApproval(request.getSession(), entityApproval);
    }
  }

  @Operation(summary = "Update Task", description = "Update details of an existing task.")
  @PostMapping(value = "/updateTask")
  @PreAuthorize("@guard.hasPermission('TASK','WRITE')")
  public void updateTask(HttpSession session, @RequestBody Task task) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateTask.POST")) {
      log.debug("updateTask");
      User user = casdoorService.getUser(session);
      workflowService.updateTask(task, user.getId());
    }
  }
}
