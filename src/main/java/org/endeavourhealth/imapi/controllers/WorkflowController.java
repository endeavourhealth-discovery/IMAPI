package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.logic.service.WorkflowService;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.security.Permission;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("api/workflow")
@CrossOrigin(origins = "*")
@Tag(name = "WorkflowController")
@RequestScope
@Slf4j
public class WorkflowController {

  private final WorkflowService workflowService = new WorkflowService();
  private final SecurityService securityService = new SecurityService();

  @Operation(summary = "Create Bug Report", description = "Endpoint to create a new bug report.")
  @PostMapping(value = "/createBugReport")
  public void createBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.CreateBugReport.POST")) {
      log.debug("createBugReport");
      User user = securityService.getUser(request);
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
  public void updateBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateBugReport.POST")) {
      log.debug("updateBugReport");
      securityService.requiresPermission(new Permission(Resource.BUG_REPORT, List.of(UserRole.DEVELOPER), List.of()), request);
      workflowService.updateBugReport(bugReport, request);
    }
  }

  @Operation(summary = "Get Tasks by Creator", description = "Retrieve tasks created by the currently authenticated user.")
  @GetMapping(value = "/getTasksByCreatedBy", produces = "application/json")
  public WorkflowResponse getTasksByCreatedBy(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") int size) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.tasksByCreator.GET")) {
      log.debug("getWorkflowsByCreatedBy");
      securityService.requiresPermission(new Permission(Resource.TASK, List.of(UserRole.DEVELOPER, UserRole.TASK_MANAGER), List.of()), request);
      WorkflowRequest wfRequest = new WorkflowRequest(request);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getTasksByCreatedBy(wfRequest);
    }
  }

  @Operation(summary = "Get Tasks by Assignee", description = "Retrieve tasks assigned to the currently authenticated user.")
  @GetMapping(value = "/getTasksByAssignedTo", produces = "application/json")
  public WorkflowResponse getTasksByAssignedTo(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.tasksByAssignedTo.GET")) {
      log.debug("getWorkflowsByAssignedTo");
      securityService.requiresPermission(new Permission(Resource.TASK, List.of(UserRole.DEVELOPER, UserRole.TASK_MANAGER), List.of()), request);
      WorkflowRequest wfRequest = new WorkflowRequest(request);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getTasksByAssignedTo(wfRequest);
    }
  }

  @Operation(summary = "Get Unassigned Tasks", description = "Retrieve tasks that are not assigned to any user.")
  @GetMapping(value = "/getUnassignedTasks", produces = "application/json")
  public WorkflowResponse getUnassignedTasks(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.unassignedTasks.GET")) {
      log.debug("getUnassignedTasks");
      securityService.requiresPermission(new Permission(Resource.TASK, List.of(UserRole.DEVELOPER, UserRole.TASK_MANAGER), List.of()), request);
      WorkflowRequest wfRequest = new WorkflowRequest(request);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getUnassignedTasks(wfRequest);
    }
  }

  @Operation(summary = "Get a Task", description = "Fetch a task using its unique ID.")
  @GetMapping(value = "/getTask", produces = "application/json")
  public Task getTask(HttpServletRequest request, @RequestParam(name = "id") String id) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workfflow.task.GET")) {
      log.debug("getTask");
      securityService.requiresPermission(new Permission(Resource.TASK, List.of(UserRole.DEVELOPER, UserRole.TASK_MANAGER), List.of()), request);
      return workflowService.getTask(id);
    }
  }

  @Operation(summary = "Delete a Task", description = "Delete a task by its unique ID.")
  @DeleteMapping(value = "/deleteTask")
  public void deleteTask(HttpServletRequest request, @RequestParam(name = "id") String id) throws TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.task.DELETE")) {
      log.debug("deleteTask");
      securityService.requiresPermission(new Permission(Resource.TASK, List.of(UserRole.DEVELOPER, UserRole.TASK_MANAGER), List.of()), request);
      workflowService.deleteTask(id);
    }
  }

  @Operation(summary = "Create Role Request", description = "Submit a role request created by the user.")
  @PostMapping(value = "/createRoleRequest")
  public void createRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createRoleRequest.POST")) {
      User user = securityService.getUser(request);
      if (null == roleRequest.getCreatedBy()) roleRequest.setCreatedBy(user.getId());
      workflowService.createRoleRequest(roleRequest);
    }
  }

  @Operation(summary = "Get Role Request", description = "Retrieve a role request using its unique ID.")
  @GetMapping(value = "/roleRequest", produces = "application/json")
  public RoleRequest getRoleRequest(@RequestParam(name = "id") String id, HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.roleRequest.GET")) {
      log.debug("getRoleRequest");
      securityService.requiresPermission(new Permission(Resource.ROLE_REQUEST, List.of(UserRole.TASK_MANAGER), List.of()), request);
      return workflowService.getRoleRequest(id);
    }
  }

  @Operation(summary = "Update role request", description = "Update a role request workflow task")
  @PostMapping(value = "/updateRoleRequest")
  public void updateRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateRoleRequest.POST")) {
      log.debug("updateRoleRequest");
      securityService.requiresPermission(new Permission(Resource.ROLE_REQUEST, List.of(UserRole.TASK_MANAGER), List.of()), request);
      workflowService.updateRoleRequest(roleRequest, request);
    }
  }

  @Operation(summary = "Approve role request")
  @PostMapping(value = "/approveRoleRequest")
  public void approveRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveRoleRequest.POST")) {
      log.debug("approveRoleRequest");
      securityService.requiresPermission(new Permission(Resource.ROLE_REQUEST, List.of(UserRole.APPROVER), List.of()), request);
      workflowService.approveRoleRequest(request, roleRequest);
    }
  }

  @Operation(summary = "Reject role request")
  @PostMapping(value = "/rejectRoleRequest")
  public void rejectRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectRoleRequest.POST")) {
      log.debug("rejectRoleRequest");
      securityService.requiresPermission(new Permission(Resource.ROLE_REQUEST, List.of(UserRole.TASK_MANAGER), List.of()), request);
      workflowService.rejectRoleRequest(request, roleRequest);
    }
  }

  @Operation(summary = "Create Namespace Request", description = "Submit a namespace request created by the user.")
  @PostMapping(value = "/createNamespaceRequest")
  public void createGraphRequest(HttpServletRequest request, @RequestBody NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createNamespaceRequest.POST")) {
      User user = securityService.getUser(request);
      if (null == namespaceRequest.getCreatedBy()) namespaceRequest.setCreatedBy(user.getId());
      workflowService.createNamespaceRequest(namespaceRequest);
    }
  }

  @Operation(summary = "Get Namespace Request", description = "Retrieve a namespace request using its unique ID.")
  @GetMapping(value = "/namespaceRequest", produces = "application/json")
  public NamespaceRequest getNamespaceRequest(@RequestParam(name = "id") String id, HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.namespaceRequest.GET")) {
      log.debug("getNamespaceRequest");
      securityService.requiresPermission(new Permission(Resource.NAMESPACE_REQUEST, List.of(UserRole.TASK_MANAGER), List.of()), request);
      return workflowService.getNamespaceRequest(id);
    }
  }

  @Operation(summary = "Update namespace request", description = "Update a graph request workflow task")
  @PostMapping(value = "/updateNamespaceRequest")
  public void updateGraphRequest(HttpServletRequest request, @RequestBody NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateNamespaceRequest.POST")) {
      log.debug("updateNamespaceRequest");
      securityService.requiresPermission(new Permission(Resource.NAMESPACE_REQUEST, List.of(UserRole.TASK_MANAGER), List.of()), request);
      workflowService.updateNamespaceRequest(namespaceRequest, request);
    }
  }

  @Operation(summary = "Approve namespace request")
  @PostMapping(value = "/approveNamespaceRequest")
  public void approveNamespaceRequest(HttpServletRequest request, @RequestBody NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveNamespaceRequest.POST")) {
      log.debug("approveNamespaceRequest");
      securityService.requiresPermission(new Permission(Resource.NAMESPACE_REQUEST, List.of(UserRole.APPROVER), List.of()), request);
      workflowService.approveNamespaceRequest(request, namespaceRequest);
    }
  }

  @Operation(summary = "Reject namespace request")
  @PostMapping(value = "/rejectNamespaceRequest")
  public void rejectGraphRequest(HttpServletRequest request, @RequestBody NamespaceRequest namespaceRequest) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectNamespaceRequest.POST")) {
      log.debug("rejectGraphRequest");
      securityService.requiresPermission(new Permission(Resource.NAMESPACE_REQUEST, List.of(UserRole.APPROVER), List.of()), request);
      workflowService.rejectNamespaceRequest(request, namespaceRequest);
    }
  }

  @Operation(summary = "Create Entity Approval", description = "Submit an approval request for an entity.")
  @PostMapping(value = "/createEntityApproval")
  public void createEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createEntityApproval.POST")) {
      log.debug("createEntityApproval");
      securityService.requiresPermission(new Permission(Resource.ENTITY_APPROVAL, List.of(UserRole.EDITOR, UserRole.CREATOR), List.of()), request);
      User user = securityService.getUser(request);
      if (null == entityApproval.getCreatedBy()) entityApproval.setCreatedBy(user.getId());
      workflowService.createEntityApproval(entityApproval);
    }
  }

  @Operation(summary = "Get entity approval", description = "Get an approval request for an entity by id")
  @GetMapping(value = "/entityApproval")
  public EntityApproval getEntityApproval(HttpServletRequest request, @RequestParam(name = "id") String id) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.entityApproval.GET")) {
      log.debug("getEntityApproval");
      securityService.requiresPermission(new Permission(Resource.ENTITY_APPROVAL, List.of(UserRole.TASK_MANAGER), List.of()), request);
      return workflowService.getEntityApproval(id);
    }
  }

  @Operation(summary = "Update entity approval", description = "Update an approval request for an entity")
  @PostMapping(value = "/updateEntityApproval")
  public void updateEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateEntityApproval.POST")) {
      log.debug("updateEntityApproval");
      securityService.requiresPermission(new Permission(Resource.ENTITY_APPROVAL, List.of(UserRole.TASK_MANAGER), List.of()), request);
      workflowService.updateEntityApproval(entityApproval, request);
    }
  }

  @Operation(summary = "Approve entity approval")
  @PostMapping(value = "/approveEntityApproval")
  public void approveEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveEntityApproval.POST")) {
      log.debug("approveEntityApproval");
      securityService.requiresPermission(new Permission(Resource.ENTITY_APPROVAL, List.of(UserRole.APPROVER), List.of()), request);
      workflowService.approveEntityApproval(request, entityApproval);
    }
  }

  @Operation(summary = "Reject entity approval")
  @PostMapping(value = "/rejectEntityApproval")
  public void rejectEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws TaskFilerException, UserNotFoundException, JsonProcessingException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectEntityApproval.POST")) {
      log.debug("rejectEntityApproval");
      securityService.requiresPermission(new Permission(Resource.ENTITY_APPROVAL, List.of(UserRole.APPROVER), List.of()), request);
      workflowService.rejectEntityApproval(request, entityApproval);
    }
  }

  @Operation(summary = "Update Task", description = "Update details of an existing task.")
  @PostMapping(value = "/updateTask")
  public void updateTask(HttpServletRequest request, @RequestBody Task task) throws TaskFilerException, UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateTask.POST")) {
      log.debug("updateTask");
      securityService.requiresPermission(new Permission(Resource.TASK, List.of(UserRole.TASK_MANAGER), List.of()), request);
      User user = securityService.getUser(request);
      workflowService.updateTask(task, user.getId());
    }
  }
}
