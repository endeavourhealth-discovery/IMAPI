package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.WorkflowService;
import org.endeavourhealth.imapi.model.requests.WorkflowRequest;
import org.endeavourhealth.imapi.model.responses.WorkflowResponse;
import org.endeavourhealth.imapi.model.workflow.*;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

@RestController
@RequestMapping("api/workflow")
@CrossOrigin(origins = "*")
@Tag(name = "WorkflowController")
@RequestScope
@Slf4j
public class WorkflowController {

  private final WorkflowService workflowService = new WorkflowService();
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @Operation(summary = "Create Bug Report", description = "Endpoint to create a new bug report.")
  @PostMapping(value = "/createBugReport")
  public void createBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.CreateBugReport.POST")) {
      log.debug("createBugReport");
      String id = requestObjectService.getRequestAgentId(request);
      if (null == bugReport.getCreatedBy()) bugReport.setCreatedBy(id);
      workflowService.createBugReport(bugReport);
    }
  }

  @Operation(summary = "Get Bug Report", description = "Fetch a bug report using its unique ID.")
  @GetMapping(value = "/getBugReport", produces = "application/json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public BugReport getBugReport(@RequestParam(name = "id") String id) throws UserNotFoundException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.bugReport.GET")) {
      log.debug("getBugReport");
      return workflowService.getBugReport(id);
    }
  }

  @Operation(summary = "Update bug report")
  @PostMapping(value = "/updateBugReport")
  public void updateBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateBugReport.POST")) {
      log.debug("updateBugReport");
      workflowService.updateBugReport(bugReport, request);
    }
  }

  @Operation(summary = "Get Tasks by Creator", description = "Retrieve tasks created by the currently authenticated user.")
  @GetMapping(value = "/getTasksByCreatedBy", produces = "application/json")
  public WorkflowResponse getTasksByCreatedBy(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") int size) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.tasksByCreator.GET")) {
      log.debug("getWorkflowsByCreatedBy");
      WorkflowRequest wfRequest = new WorkflowRequest(request);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getTasksByCreatedBy(wfRequest);
    }
  }

  @Operation(summary = "Get Tasks by Assignee", description = "Retrieve tasks assigned to the currently authenticated user.")
  @GetMapping(value = "/getTasksByAssignedTo", produces = "application/json")
  public WorkflowResponse getTasksByAssignedTo(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.tasksByAssignedTo.GET")) {
      log.debug("getWorkflowsByAssignedTo");
      WorkflowRequest wfRequest = new WorkflowRequest(request);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getTasksByAssignedTo(wfRequest);
    }
  }

  @Operation(summary = "Get Unassigned Tasks", description = "Retrieve tasks that are not assigned to any user.")
  @GetMapping(value = "/getUnassignedTasks", produces = "application/json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public WorkflowResponse getUnassignedTasks(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.unassignedTasks.GET")) {
      log.debug("getUnassignedTasks");
      WorkflowRequest wfRequest = new WorkflowRequest(request);
      if (page != 0) wfRequest.setPage(page);
      if (size != 0) wfRequest.setSize(size);
      return workflowService.getUnassignedTasks(wfRequest);
    }
  }

  @Operation(summary = "Get a Task", description = "Fetch a task using its unique ID.")
  @GetMapping(value = "/getTask", produces = "application/json")
  public Task getTask(@RequestParam(name = "id") String id) throws UserNotFoundException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workfflow.task.GET")) {
      log.debug("getTask");
      return workflowService.getTask(id);
    }
  }

  @Operation(summary = "Delete a Task", description = "Delete a task by its unique ID.")
  @DeleteMapping(value = "/deleteTask")
  public void deleteTask(@RequestParam(name = "id") String id) throws TaskFilerException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.task.DELETE")) {
      workflowService.deleteTask(id);
    }
  }

  @Operation(summary = "Create Role Request", description = "Submit a role request created by the user.")
  @PostMapping(value = "/createRoleRequest")
  public void createRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createRoleRequest.POST")) {
      String id = requestObjectService.getRequestAgentId(request);
      if (null == roleRequest.getCreatedBy()) roleRequest.setCreatedBy(id);
      workflowService.createRoleRequest(roleRequest);
    }
  }

  @Operation(summary = "Get Role Request", description = "Retrieve a role request using its unique ID.")
  @GetMapping(value = "/roleRequest", produces = "application/json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public RoleRequest getRoleRequest(@RequestParam(name = "id") String id) throws UserNotFoundException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.roleRequest.GET")) {
      log.debug("getRoleRequest");
      return workflowService.getRoleRequest(id);
    }
  }

  @Operation(summary = "Update role request", description = "Update a role request workflow task")
  @PostMapping(value = "/updateRoleRequest")
  public void updateRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateRoleRequest.POST")) {
      log.debug("updateRoleRequest");
      workflowService.updateRoleRequest(roleRequest, request);
    }
  }

  @Operation(summary = "Approve role request")
  @PostMapping(value = "/approveRoleRequest")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void approveRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveRoleRequest.POST")) {
      log.debug("approveRoleRequest");
      workflowService.approveRoleRequest(request, roleRequest);
    }
  }

  @Operation(summary = "Reject role request")
  @PostMapping(value = "/rejectRoleRequest")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void rejectRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectRoleRequest.POST")) {
      log.debug("rejectRoleRequest");
      workflowService.rejectRoleRequest(request, roleRequest);
    }
  }

  @Operation(summary = "Create Graph Request", description = "Submit a graph request created by the user.")
  @PostMapping(value = "/createGraphRequest")
  public void createGraphRequest(HttpServletRequest request, @RequestBody GraphRequest graphRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createGraphRequest.POST")) {
      String id = requestObjectService.getRequestAgentId(request);
      if (null == graphRequest.getCreatedBy()) graphRequest.setCreatedBy(id);
      workflowService.createGraphRequest(graphRequest);
    }
  }

  @Operation(summary = "Get Graph Request", description = "Retrieve a graph request using its unique ID.")
  @GetMapping(value = "/graphRequest", produces = "application/json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public GraphRequest getGraphRequest(@RequestParam(name = "id") String id) throws UserNotFoundException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.graphRequest.GET")) {
      log.debug("getGraphRequest");
      return workflowService.getGraphRequest(id);
    }
  }

  @Operation(summary = "Update graph request", description = "Update a graph request workflow task")
  @PostMapping(value = "/updateGraphRequest")
  public void updateGraphRequest(HttpServletRequest request, @RequestBody GraphRequest graphRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateGraphRequest.POST")) {
      log.debug("updateGraphRequest");
      workflowService.updateGraphRequest(graphRequest, request);
    }
  }

  @Operation(summary = "Approve graph request")
  @PostMapping(value = "/approveGraphRequest")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void approveGraphRequest(HttpServletRequest request, @RequestBody GraphRequest graphRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveGraphRequest.POST")) {
      log.debug("approveGraphRequest");
      workflowService.approveGraphRequest(request, graphRequest);
    }
  }

  @Operation(summary = "Reject graph request")
  @PostMapping(value = "/rejectGraphRequest")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void rejectGraphRequest(HttpServletRequest request, @RequestBody GraphRequest graphRequest) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectGraphRequest.POST")) {
      log.debug("rejectGraphRequest");
      workflowService.rejectGraphRequest(request, graphRequest);
    }
  }

  @Operation(summary = "Create Entity Approval", description = "Submit an approval request for an entity.")
  @PostMapping(value = "/createEntityApproval")
  public void createEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws IOException, UserNotFoundException, TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.createEntityApproval.POST")) {
      log.debug("createEntityApproval");
      String id = requestObjectService.getRequestAgentId(request);
      if (null == entityApproval.getCreatedBy()) entityApproval.setCreatedBy(id);
      workflowService.createEntityApproval(entityApproval);
    }
  }

  @Operation(summary = "Get entity approval", description = "Get an approval request for an entity by id")
  @GetMapping(value = "/entityApproval")
  public EntityApproval getEntityApproval(@RequestParam(name = "id") String id) throws UserNotFoundException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.entityApproval.GET")) {
      log.debug("getEntityApproval");
      return workflowService.getEntityApproval(id);
    }
  }

  @Operation(summary = "Update entity approval", description = "Update an approval request for an entity")
  @PostMapping(value = "/updateEntityApproval")
  public void updateEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws IOException, UserNotFoundException, TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateEntityApproval.POST")) {
      log.debug("updateEntityApproval");
      workflowService.updateEntityApproval(entityApproval, request);
    }
  }

  @Operation(summary = "Approve entity approval")
  @PostMapping(value = "/approveEntityApproval")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void approveEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.approveEntityApproval.POST")) {
      log.debug("approveEntityApproval");
      workflowService.approveEntityApproval(request, entityApproval);
    }
  }

  @Operation(summary = "Reject entity approval")
  @PostMapping(value = "/rejectEntityApproval")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void rejectEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws IOException, TaskFilerException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.rejectEntityApproval.POST")) {
      log.debug("rejectEntityApproval");
      workflowService.rejectEntityApproval(request, entityApproval);
    }
  }

  @Operation(summary = "Update Task", description = "Update details of an existing task.")
  @PostMapping(value = "/updateTask")
  public void updateTask(HttpServletRequest request, @RequestBody Task task) throws IOException, UserNotFoundException, TaskFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Workflow.updateTask.POST")) {
      log.debug("updateTask");
      String id = requestObjectService.getRequestAgentId(request);
      workflowService.updateTask(task, id);
    }
  }
}
