package org.endeavourhealth.imapi.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.filer.TaskFilerException
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.logic.service.WorkflowService
import org.endeavourhealth.imapi.model.requests.WorkflowRequest
import org.endeavourhealth.imapi.model.responses.WorkflowResponse
import org.endeavourhealth.imapi.model.security.Action
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.workflow.*
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope

@RestController
@RequestMapping("api/workflow/private")
@CrossOrigin(origins = ["*"])
@Tag(name = "WorkflowController")
@RequestScope
@Slf4j
class WorkflowController {
  private val workflowService = WorkflowService()
  private val securityService = SecurityService()
  private val log = LoggerFactory.getLogger(javaClass)

  @Operation(summary = "Create Bug Report", description = "Endpoint to create a new bug report.")
  @PostMapping(value = ["/createBugReport"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createBugReport(request: HttpServletRequest?, @RequestBody bugReport: BugReport) {
    MetricsHelper.recordTime("API.Workflow.CreateBugReport.POST").use {
      log.debug("createBugReport")
      val user = securityService.user
      if (null == bugReport.createdBy) bugReport.createdBy = user.id
      workflowService.createBugReport(bugReport)
    }
  }

  @Operation(summary = "Get Bug Report", description = "Fetch a bug report using its unique ID.")
  @GetMapping(value = ["/getBugReport"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, UserAuthorisationException::class)
  fun getBugReport(@RequestParam(name = "id") id: String): BugReport {
    MetricsHelper.recordTime("API.Workflow.bugReport.GET").use {
      log.debug("getBugReport")
      return workflowService.getBugReport(id)
    }
  }

  @Operation(summary = "Update bug report")
  @PostMapping(value = ["/updateBugReport"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun updateBugReport(@RequestBody bugReport: BugReport) {
    MetricsHelper.recordTime("API.Workflow.updateBugReport.POST").use {
      log.debug("updateBugReport")
      securityService.requiresPermission(Resource.BUG_REPORT, Action.UPDATE)
      workflowService.updateBugReport(bugReport)
    }
  }

  @Operation(
    summary = "Get Tasks by Creator",
    description = "Retrieve tasks created by the currently authenticated user."
  )
  @GetMapping(value = ["/getTasksByCreatedBy"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getTasksByCreatedBy(
    @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
    @RequestParam(name = "size", required = false, defaultValue = "25") size: Int
  ): WorkflowResponse {
    MetricsHelper.recordTime("API.Workflow.tasksByCreator.GET").use {
      log.debug("getWorkflowsByCreatedBy")
      securityService.requiresPermission(Resource.TASK, Action.READ)
      val userId = securityService.user.id
      val wfRequest = WorkflowRequest(userId)
      if (page != 0) wfRequest.setPage(page)
      if (size != 0) wfRequest.setSize(size)
      return workflowService.getTasksByCreatedBy(wfRequest)
    }
  }

  @Operation(
    summary = "Get Tasks by Assignee",
    description = "Retrieve tasks assigned to the currently authenticated user."
  )
  @GetMapping(value = ["/getTasksByAssignedTo"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getTasksByAssignedTo(
    @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
    @RequestParam(name = "size", required = false, defaultValue = "25") size: Int
  ): WorkflowResponse {
    MetricsHelper.recordTime("API.Workflow.tasksByAssignedTo.GET").use {
      log.debug("getWorkflowsByAssignedTo")
      securityService.requiresPermission(Resource.TASK, Action.READ)
      val userId = securityService.user.id
      val wfRequest = WorkflowRequest(userId)
      if (page != 0) wfRequest.setPage(page)
      if (size != 0) wfRequest.setSize(size)
      return workflowService.getTasksByAssignedTo(wfRequest)
    }
  }

  @Operation(summary = "Get Unassigned Tasks", description = "Retrieve tasks that are not assigned to any user.")
  @GetMapping(value = ["/getUnassignedTasks"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, JsonProcessingException::class, UserAuthorisationException::class)
  fun getUnassignedTasks(
    @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
    @RequestParam(name = "size", required = false, defaultValue = "25") size: Int
  ): WorkflowResponse {
    MetricsHelper.recordTime("API.Workflow.unassignedTasks.GET").use {
      log.debug("getUnassignedTasks")
      securityService.requiresPermission(Resource.TASK, Action.READ)
      val userId = securityService.user.id
      val wfRequest = WorkflowRequest(userId)
      if (page != 0) wfRequest.setPage(page)
      if (size != 0) wfRequest.setSize(size)
      return workflowService.getUnassignedTasks(wfRequest)
    }
  }

  @Operation(summary = "Get a Task", description = "Fetch a task using its unique ID.")
  @GetMapping(value = ["/getTask"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  fun getTask(@RequestParam(name = "id") id: String): Task? {
    MetricsHelper.recordTime("API.Workfflow.task.GET").use {
      log.debug("getTask")
      securityService.requiresPermission(Resource.TASK, Action.READ)
      return workflowService.getTask(id)
    }
  }

  @Operation(summary = "Delete a Task", description = "Delete a task by its unique ID.")
  @DeleteMapping(value = ["/deleteTask"])
  @Throws(TaskFilerException::class)
  fun deleteTask(@RequestParam(name = "id") id: String) {
    MetricsHelper.recordTime("API.Workflow.task.DELETE").use {
      log.debug("deleteTask")
      securityService.requiresPermission(Resource.TASK, Action.DELETE)
      workflowService.deleteTask(id)
    }
  }

  @Operation(summary = "Create Role Request", description = "Submit a role request created by the user.")
  @PostMapping(value = ["/createRoleRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createRoleRequest(@RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.createRoleRequest.POST").use {
      log.debug("createRoleRequest")
      val user = securityService.user
      if (null == roleRequest.createdBy) roleRequest.createdBy = user.id
      workflowService.createRoleRequest(roleRequest)
    }
  }

  @Operation(summary = "Get Role Request", description = "Retrieve a role request using its unique ID.")
  @GetMapping(value = ["/roleRequest"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, UserAuthorisationException::class)
  fun getRoleRequest(@RequestParam(name = "id") id: String): RoleRequest? {
    MetricsHelper.recordTime("API.Workflow.roleRequest.GET").use {
      log.debug("getRoleRequest")
      securityService.requiresPermission(Resource.ROLE_REQUEST, Action.READ)
      return workflowService.getRoleRequest(id)
    }
  }

  @Operation(summary = "Update role request", description = "Update a role request workflow task")
  @PostMapping(value = ["/updateRoleRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateRoleRequest(@RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.updateRoleRequest.POST").use {
      log.debug("updateRoleRequest")
      securityService.requiresPermission(Resource.ROLE_REQUEST, Action.UPDATE)
      workflowService.updateRoleRequest(roleRequest)
    }
  }

  @Operation(summary = "Approve role request")
  @PostMapping(value = ["/approveRoleRequest"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun approveRoleRequest(@RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.approveRoleRequest.POST").use {
      log.debug("approveRoleRequest")
      securityService.requiresPermission(Resource.ROLE_REQUEST, Action.APPROVE)
      workflowService.approveRoleRequest(roleRequest)
    }
  }

  @Operation(summary = "Reject role request")
  @PostMapping(value = ["/rejectRoleRequest"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun rejectRoleRequest(@RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.rejectRoleRequest.POST").use {
      log.debug("rejectRoleRequest")
      securityService.requiresPermission(Resource.ROLE_REQUEST, Action.REJECT)
      workflowService.rejectRoleRequest(roleRequest)
    }
  }

  @Operation(summary = "Create Namespace Request", description = "Submit a namespace request created by the user.")
  @PostMapping(value = ["/createNamespaceRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createGraphRequest(@RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.createNamespaceRequest.POST").use {
      log.debug("createNamespaceRequest")
      val user = securityService.user
      if (null == namespaceRequest.createdBy) namespaceRequest.createdBy = user.id
      workflowService.createNamespaceRequest(namespaceRequest)
    }
  }

  @Operation(summary = "Get Namespace Request", description = "Retrieve a namespace request using its unique ID.")
  @GetMapping(value = ["/namespaceRequest"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, UserAuthorisationException::class, JsonProcessingException::class)
  fun getNamespaceRequest(@RequestParam(name = "id") id: String): NamespaceRequest? {
    MetricsHelper.recordTime("API.Workflow.namespaceRequest.GET").use {
      log.debug("getNamespaceRequest")
      securityService.requiresPermission(Resource.NAMESPACE_REQUEST, Action.READ)
      return workflowService.getNamespaceRequest(id)
    }
  }

  @Operation(summary = "Update namespace request", description = "Update a graph request workflow task")
  @PostMapping(value = ["/updateNamespaceRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateGraphRequest(@RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.updateNamespaceRequest.POST").use {
      log.debug("updateNamespaceRequest")
      securityService.requiresPermission(Resource.NAMESPACE_REQUEST, Action.UPDATE)
      workflowService.updateNamespaceRequest(namespaceRequest)
    }
  }

  @Operation(summary = "Approve namespace request")
  @PostMapping(value = ["/approveNamespaceRequest"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun approveNamespaceRequest(@RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.approveNamespaceRequest.POST").use {
      log.debug("approveNamespaceRequest")
      securityService.requiresPermission(Resource.NAMESPACE_REQUEST, Action.APPROVE)
      workflowService.approveNamespaceRequest(namespaceRequest)
    }
  }

  @Operation(summary = "Reject namespace request")
  @PostMapping(value = ["/rejectNamespaceRequest"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun rejectGraphRequest(@RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.rejectNamespaceRequest.POST").use {
      log.debug("rejectGraphRequest")
      securityService.requiresPermission(Resource.NAMESPACE_REQUEST, Action.REJECT)
      workflowService.rejectNamespaceRequest(namespaceRequest)
    }
  }

  @Operation(summary = "Create Entity Approval", description = "Submit an approval request for an entity.")
  @PostMapping(value = ["/createEntityApproval"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createEntityApproval(@RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.createEntityApproval.POST").use {
      log.debug("createEntityApproval")
      securityService.requiresPermission(Resource.ENTITY_APPROVAL, Action.CREATE)
      val user = securityService.user
      if (null == entityApproval.createdBy) entityApproval.createdBy = user.id
      workflowService.createEntityApproval(entityApproval)
    }
  }

  @Operation(summary = "Get entity approval", description = "Get an approval request for an entity by id")
  @GetMapping(value = ["/entityApproval"])
  @Throws(UserNotFoundException::class)
  fun getEntityApproval(@RequestParam(name = "id") id: String): EntityApproval? {
    MetricsHelper.recordTime("API.Workflow.entityApproval.GET").use {
      log.debug("getEntityApproval")
      securityService.requiresPermission(Resource.ENTITY_APPROVAL, Action.READ)
      return workflowService.getEntityApproval(id)
    }
  }

  @Operation(summary = "Update entity approval", description = "Update an approval request for an entity")
  @PostMapping(value = ["/updateEntityApproval"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateEntityApproval(@RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.updateEntityApproval.POST").use {
      log.debug("updateEntityApproval")
      securityService.requiresPermission(Resource.ENTITY_APPROVAL, Action.UPDATE)
      workflowService.updateEntityApproval(entityApproval)
    }
  }

  @Operation(summary = "Approve entity approval")
  @PostMapping(value = ["/approveEntityApproval"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun approveEntityApproval(request: HttpServletRequest, @RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.approveEntityApproval.POST").use {
      log.debug("approveEntityApproval")
      securityService.requiresPermission(Resource.ENTITY_APPROVAL, Action.APPROVE)
      workflowService.approveEntityApproval(entityApproval)
    }
  }

  @Operation(summary = "Reject entity approval")
  @PostMapping(value = ["/rejectEntityApproval"])
  @Throws(
    TaskFilerException::class,
    UserNotFoundException::class,
    JsonProcessingException::class,
    UserAuthorisationException::class
  )
  fun rejectEntityApproval(@RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.rejectEntityApproval.POST").use {
      log.debug("rejectEntityApproval")
      securityService.requiresPermission(Resource.ENTITY_APPROVAL, Action.REJECT)
      workflowService.rejectEntityApproval(entityApproval)
    }
  }

  @Operation(summary = "Update Task", description = "Update details of an existing task.")
  @PostMapping(value = ["/updateTask"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateTask(@RequestBody task: Task) {
    MetricsHelper.recordTime("API.Workflow.updateTask.POST").use {
      log.debug("updateTask")
      securityService.requiresPermission(Resource.TASK, Action.UPDATE)
      val user = securityService.user
      workflowService.updateTask(task, user.id)
    }
  }
}
