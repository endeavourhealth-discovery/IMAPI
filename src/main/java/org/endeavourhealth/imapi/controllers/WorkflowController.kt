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
import org.endeavourhealth.imapi.model.security.Permission
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.workflow.*
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
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
  private val log = LoggerFactory.getLogger(WorkflowController::class.java)

  @Operation(summary = "Create Bug Report", description = "Endpoint to create a new bug report.")
  @PostMapping(value = ["/createBugReport"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createBugReport(request: HttpServletRequest, @RequestBody bugReport: BugReport) {
    MetricsHelper.recordTime("API.Workflow.CreateBugReport.POST").use { t ->
      log.debug("createBugReport")
      val user = securityService.getUser(request)
      if (null == bugReport.createdBy) bugReport.createdBy = user.id
      workflowService.createBugReport(bugReport)
    }
  }

  @Operation(summary = "Get Bug Report", description = "Fetch a bug report using its unique ID.")
  @GetMapping(value = ["/getBugReport"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, UserAuthorisationException::class)
  fun getBugReport(@RequestParam(name = "id") id: String, request: HttpServletRequest): BugReport {
    MetricsHelper.recordTime("API.Workflow.bugReport.GET").use { t ->
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
  fun updateBugReport(request: HttpServletRequest, @RequestBody bugReport: BugReport) {
    MetricsHelper.recordTime("API.Workflow.updateBugReport.POST").use { t ->
      log.debug("updateBugReport")
      securityService.requiresPermission(
        Permission(
          Resource.BUG_REPORT,
          mutableListOf(UserRole.DEVELOPER),
          mutableListOf()
        ), request
      )
      workflowService.updateBugReport(bugReport, request)
    }
  }

  @Operation(
    summary = "Get Tasks by Creator",
    description = "Retrieve tasks created by the currently authenticated user."
  )
  @GetMapping(value = ["/getTasksByCreatedBy"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getTasksByCreatedBy(
    request: HttpServletRequest,
    @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
    @RequestParam(name = "size", required = false, defaultValue = "25") size: Int
  ): WorkflowResponse {
    MetricsHelper.recordTime("API.Workflow.tasksByCreator.GET").use { t ->
      log.debug("getWorkflowsByCreatedBy")
      securityService.requiresPermission(
        Permission(
          Resource.TASK,
          mutableListOf(UserRole.DEVELOPER, UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      val userId = securityService.getUser(request).id
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
    request: HttpServletRequest,
    @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
    @RequestParam(name = "size", required = false, defaultValue = "25") size: Int
  ): WorkflowResponse {
    MetricsHelper.recordTime("API.Workflow.tasksByAssignedTo.GET").use { t ->
      log.debug("getWorkflowsByAssignedTo")
      securityService.requiresPermission(
        Permission(
          Resource.TASK,
          mutableListOf(UserRole.DEVELOPER, UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      val userId = securityService.getUser(request).id
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
    request: HttpServletRequest,
    @RequestParam(name = "page", required = false, defaultValue = "1") page: Int,
    @RequestParam(name = "size", required = false, defaultValue = "25") size: Int
  ): WorkflowResponse {
    MetricsHelper.recordTime("API.Workflow.unassignedTasks.GET").use { t ->
      log.debug("getUnassignedTasks")
      securityService.requiresPermission(
        Permission(
          Resource.TASK,
          mutableListOf(UserRole.DEVELOPER, UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      val userId = securityService.getUser(request).id
      val wfRequest = WorkflowRequest(userId)
      if (page != 0) wfRequest.setPage(page)
      if (size != 0) wfRequest.setSize(size)
      return workflowService.getUnassignedTasks(wfRequest)
    }
  }

  @Operation(summary = "Get a Task", description = "Fetch a task using its unique ID.")
  @GetMapping(value = ["/getTask"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  fun getTask(request: HttpServletRequest, @RequestParam(name = "id") id: String): Task? {
    MetricsHelper.recordTime("API.Workfflow.task.GET").use { t ->
      log.debug("getTask")
      securityService.requiresPermission(
        Permission(
          Resource.TASK,
          mutableListOf(UserRole.DEVELOPER, UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      return workflowService.getTask(id)
    }
  }

  @Operation(summary = "Delete a Task", description = "Delete a task by its unique ID.")
  @DeleteMapping(value = ["/deleteTask"])
  @Throws(TaskFilerException::class)
  fun deleteTask(request: HttpServletRequest, @RequestParam(name = "id") id: String?) {
    MetricsHelper.recordTime("API.Workflow.task.DELETE").use { t ->
      log.debug("deleteTask")
      securityService.requiresPermission(
        Permission(
          Resource.TASK,
          mutableListOf(UserRole.DEVELOPER, UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      workflowService.deleteTask(id)
    }
  }

  @Operation(summary = "Create Role Request", description = "Submit a role request created by the user.")
  @PostMapping(value = ["/createRoleRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createRoleRequest(request: HttpServletRequest, @RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.createRoleRequest.POST").use { t ->
      val user = securityService.getUser(request)
      if (null == roleRequest.createdBy) roleRequest.createdBy = user.id
      workflowService.createRoleRequest(roleRequest)
    }
  }

  @Operation(summary = "Get Role Request", description = "Retrieve a role request using its unique ID.")
  @GetMapping(value = ["/roleRequest"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, UserAuthorisationException::class)
  fun getRoleRequest(@RequestParam(name = "id") id: String, request: HttpServletRequest): RoleRequest? {
    MetricsHelper.recordTime("API.Workflow.roleRequest.GET").use { t ->
      log.debug("getRoleRequest")
      securityService.requiresPermission(
        Permission(
          Resource.ROLE_REQUEST,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      return workflowService.getRoleRequest(id)
    }
  }

  @Operation(summary = "Update role request", description = "Update a role request workflow task")
  @PostMapping(value = ["/updateRoleRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateRoleRequest(request: HttpServletRequest, @RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.updateRoleRequest.POST").use { t ->
      log.debug("updateRoleRequest")
      securityService.requiresPermission(
        Permission(
          Resource.ROLE_REQUEST,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      workflowService.updateRoleRequest(roleRequest, request)
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
  fun approveRoleRequest(request: HttpServletRequest, @RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.approveRoleRequest.POST").use { t ->
      log.debug("approveRoleRequest")
      securityService.requiresPermission(
        Permission(
          Resource.ROLE_REQUEST,
          mutableListOf(UserRole.APPROVER),
          mutableListOf()
        ), request
      )
      workflowService.approveRoleRequest(request, roleRequest)
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
  fun rejectRoleRequest(request: HttpServletRequest, @RequestBody roleRequest: RoleRequest) {
    MetricsHelper.recordTime("API.Workflow.rejectRoleRequest.POST").use { t ->
      log.debug("rejectRoleRequest")
      securityService.requiresPermission(
        Permission(
          Resource.ROLE_REQUEST,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      workflowService.rejectRoleRequest(request, roleRequest)
    }
  }

  @Operation(summary = "Create Namespace Request", description = "Submit a namespace request created by the user.")
  @PostMapping(value = ["/createNamespaceRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createGraphRequest(request: HttpServletRequest, @RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.createNamespaceRequest.POST").use { t ->
      val user = securityService.getUser(request)
      if (null == namespaceRequest.createdBy) namespaceRequest.createdBy = user.id
      workflowService.createNamespaceRequest(namespaceRequest)
    }
  }

  @Operation(summary = "Get Namespace Request", description = "Retrieve a namespace request using its unique ID.")
  @GetMapping(value = ["/namespaceRequest"], produces = ["application/json"])
  @Throws(UserNotFoundException::class, UserAuthorisationException::class, JsonProcessingException::class)
  fun getNamespaceRequest(@RequestParam(name = "id") id: String, request: HttpServletRequest): NamespaceRequest? {
    MetricsHelper.recordTime("API.Workflow.namespaceRequest.GET").use { t ->
      log.debug("getNamespaceRequest")
      securityService.requiresPermission(
        Permission(
          Resource.NAMESPACE_REQUEST,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      return workflowService.getNamespaceRequest(id)
    }
  }

  @Operation(summary = "Update namespace request", description = "Update a graph request workflow task")
  @PostMapping(value = ["/updateNamespaceRequest"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateGraphRequest(request: HttpServletRequest, @RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.updateNamespaceRequest.POST").use { t ->
      log.debug("updateNamespaceRequest")
      securityService.requiresPermission(
        Permission(
          Resource.NAMESPACE_REQUEST,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      workflowService.updateNamespaceRequest(namespaceRequest, request)
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
  fun approveNamespaceRequest(request: HttpServletRequest, @RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.approveNamespaceRequest.POST").use { t ->
      log.debug("approveNamespaceRequest")
      securityService.requiresPermission(
        Permission(
          Resource.NAMESPACE_REQUEST,
          mutableListOf(UserRole.APPROVER),
          mutableListOf()
        ), request
      )
      workflowService.approveNamespaceRequest(request, namespaceRequest)
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
  fun rejectGraphRequest(request: HttpServletRequest, @RequestBody namespaceRequest: NamespaceRequest) {
    MetricsHelper.recordTime("API.Workflow.rejectNamespaceRequest.POST").use { t ->
      log.debug("rejectGraphRequest")
      securityService.requiresPermission(
        Permission(
          Resource.NAMESPACE_REQUEST,
          mutableListOf(UserRole.APPROVER),
          mutableListOf()
        ), request
      )
      workflowService.rejectNamespaceRequest(request, namespaceRequest)
    }
  }

  @Operation(summary = "Create Entity Approval", description = "Submit an approval request for an entity.")
  @PostMapping(value = ["/createEntityApproval"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun createEntityApproval(request: HttpServletRequest, @RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.createEntityApproval.POST").use { t ->
      log.debug("createEntityApproval")
      securityService.requiresPermission(
        Permission(
          Resource.ENTITY_APPROVAL,
          mutableListOf(UserRole.EDITOR, UserRole.CREATOR),
          mutableListOf()
        ), request
      )
      val user = securityService.getUser(request)
      if (null == entityApproval.createdBy) entityApproval.createdBy = user.id
      workflowService.createEntityApproval(entityApproval)
    }
  }

  @Operation(summary = "Get entity approval", description = "Get an approval request for an entity by id")
  @GetMapping(value = ["/entityApproval"])
  @Throws(UserNotFoundException::class)
  fun getEntityApproval(request: HttpServletRequest, @RequestParam(name = "id") id: String): EntityApproval? {
    MetricsHelper.recordTime("API.Workflow.entityApproval.GET").use { t ->
      log.debug("getEntityApproval")
      securityService.requiresPermission(
        Permission(
          Resource.ENTITY_APPROVAL,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      return workflowService.getEntityApproval(id)
    }
  }

  @Operation(summary = "Update entity approval", description = "Update an approval request for an entity")
  @PostMapping(value = ["/updateEntityApproval"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateEntityApproval(request: HttpServletRequest, @RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.updateEntityApproval.POST").use { t ->
      log.debug("updateEntityApproval")
      securityService.requiresPermission(
        Permission(
          Resource.ENTITY_APPROVAL,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      workflowService.updateEntityApproval(entityApproval, request)
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
    MetricsHelper.recordTime("API.Workflow.approveEntityApproval.POST").use { t ->
      log.debug("approveEntityApproval")
      securityService.requiresPermission(
        Permission(
          Resource.ENTITY_APPROVAL,
          mutableListOf(UserRole.APPROVER),
          mutableListOf()
        ), request
      )
      workflowService.approveEntityApproval(request, entityApproval)
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
  fun rejectEntityApproval(request: HttpServletRequest, @RequestBody entityApproval: EntityApproval) {
    MetricsHelper.recordTime("API.Workflow.rejectEntityApproval.POST").use { t ->
      log.debug("rejectEntityApproval")
      securityService.requiresPermission(
        Permission(
          Resource.ENTITY_APPROVAL,
          mutableListOf(UserRole.APPROVER),
          mutableListOf()
        ), request
      )
      workflowService.rejectEntityApproval(request, entityApproval)
    }
  }

  @Operation(summary = "Update Task", description = "Update details of an existing task.")
  @PostMapping(value = ["/updateTask"])
  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateTask(request: HttpServletRequest, @RequestBody task: Task) {
    MetricsHelper.recordTime("API.Workflow.updateTask.POST").use { t ->
      log.debug("updateTask")
      securityService.requiresPermission(
        Permission(
          Resource.TASK,
          mutableListOf(UserRole.TASK_MANAGER),
          mutableListOf()
        ), request
      )
      val user = securityService.getUser(request)
      workflowService.updateTask(task, user.id)
    }
  }
}
