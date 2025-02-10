package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.WorkflowService;
import org.endeavourhealth.imapi.model.workflow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/workflow")
@CrossOrigin(origins = "*")
@Tag(name = "WorkflowController")
@RequestScope
public class WorkflowController {

  private static final Logger LOG = LoggerFactory.getLogger(WorkflowController.class);
  private final WorkflowService workflowService = new WorkflowService();
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @Operation(summary = "Create Bug Report", description = "Endpoint to create a new bug report.")
  @PostMapping(value = "/createBugReport")
  public void createBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws JsonProcessingException, TaskFilerException, UserNotFoundException {
    LOG.debug("createBugReport");
    String id = requestObjectService.getRequestAgentId(request);
    if (null == bugReport.getCreatedBy()) bugReport.setCreatedBy(id);
    workflowService.createBugReport(bugReport);
  }

  @Operation(summary = "Get Bug Report", description = "Fetch a bug report using its unique ID.")
  @GetMapping(value = "/getBugReport", produces = "application/json")
  @PreAuthorize("hasAuthority('IMAdmin')")
  public BugReport getBugReport(@RequestParam(name = "id") String id) throws UserNotFoundException {
    LOG.debug("getBugReport");
    return workflowService.getBugReport(id);
  }

  @Operation(summary = "Get Tasks by Creator", description = "Retrieve tasks created by the currently authenticated user.")
  @GetMapping(value = "/getTasksByCreatedBy", produces = "application/json")
  public WorkflowResponse getTasksByCreatedBy(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") int size) throws JsonProcessingException, UserNotFoundException {
    LOG.debug("getWorkflowsByCreatedBy");
    WorkflowRequest wfRequest = new WorkflowRequest(request);
    if (page != 0) wfRequest.setPage(page);
    if (size != 0) wfRequest.setSize(size);
    return workflowService.getTasksByCreatedBy(wfRequest);
  }

  @Operation(summary = "Get Tasks by Assignee", description = "Retrieve tasks assigned to the currently authenticated user.")
  @GetMapping(value = "/getTasksByAssignedTo", produces = "application/json")
  public WorkflowResponse getTasksByAssignedTo(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws JsonProcessingException, UserNotFoundException {
    LOG.debug("getWorkflowsByAssignedTo");
    WorkflowRequest wfRequest = new WorkflowRequest(request);
    if (page != 0) wfRequest.setPage(page);
    if (size != 0) wfRequest.setSize(size);
    return workflowService.getTasksByAssignedTo(wfRequest);
  }

  @Operation(summary = "Get Unassigned Tasks", description = "Retrieve tasks that are not assigned to any user.")
  @GetMapping(value = "/getUnassignedTasks", produces = "application/json")
  @PreAuthorize("hasAuthority('IMAdmin')")
  public WorkflowResponse getUnassignedTasks(HttpServletRequest request, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "25") Integer size) throws JsonProcessingException, UserNotFoundException {
    LOG.debug("getUnassignedTasks");
    WorkflowRequest wfRequest = new WorkflowRequest(request);
    if (page != 0) wfRequest.setPage(page);
    if (size != 0) wfRequest.setSize(size);
    return workflowService.getUnassignedTasks(wfRequest);
  }

  @Operation(summary = "Get a Task", description = "Fetch a task using its unique ID.")
  @GetMapping(value = "/getTask", produces = "application/json")
  public Task getTask(@RequestParam(name = "id") String id) throws UserNotFoundException {
    LOG.debug("getTask");
    return workflowService.getTask(id);
  }

  @Operation(summary = "Delete a Task", description = "Delete a task by its unique ID.")
  @DeleteMapping(value = "/deleteTask")
  public void deleteTask(@RequestParam(name = "id") String id) throws TaskFilerException {
    workflowService.deleteTask(id);
  }

  @Operation(summary = "Create Role Request", description = "Submit a role request created by the user.")
  @PostMapping(value = "/createRoleRequest")
  public void createRoleRequest(HttpServletRequest request, @RequestBody RoleRequest roleRequest) throws JsonProcessingException, TaskFilerException, UserNotFoundException {
    String id = requestObjectService.getRequestAgentId(request);
    if (null == roleRequest.getCreatedBy()) roleRequest.setCreatedBy(id);
    workflowService.createRoleRequest(roleRequest);
  }

  @Operation(summary = "Get Role Request", description = "Retrieve a role request using its unique ID.")
  @GetMapping(value = "/getRoleRequest", produces = "application/json")
  @PreAuthorize("hasAuthority('IMAdmin')")
  public RoleRequest getRoleRequest(@RequestParam(name = "id") String id) throws UserNotFoundException {
    LOG.debug("getRoleRequest");
    return workflowService.getRoleRequest(id);
  }

  @Operation(summary = "Create Entity Approval", description = "Submit an approval request for an entity.")
  @PostMapping(value = "/createEntityApproval")
  public void createEntityApproval(HttpServletRequest request, @RequestBody EntityApproval entityApproval) throws JsonProcessingException, UserNotFoundException, TaskFilerException {
    LOG.debug("createEntityApproval");
    String id = requestObjectService.getRequestAgentId(request);
    if (null == entityApproval.getCreatedBy()) entityApproval.setCreatedBy(id);
    workflowService.createEntityApproval(entityApproval);
  }

  @Operation(summary = "Update Task", description = "Update details of an existing task.")
  @PostMapping(value = "/updateTask")
  public void updateTask(HttpServletRequest request, @RequestBody Task task) throws JsonProcessingException, UserNotFoundException, TaskFilerException {
    LOG.debug("updateTask");
    String id = requestObjectService.getRequestAgentId(request);
    workflowService.updateTask(task, id);
  }
}
