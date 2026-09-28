package org.endeavourhealth.imapi.logic.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.endeavourhealth.imapi.dataaccess.WorkflowRepository
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.filer.TaskFilerException
import org.endeavourhealth.imapi.model.requests.WorkflowRequest
import org.endeavourhealth.imapi.model.responses.WorkflowResponse
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.workflow.*
import org.endeavourhealth.imapi.model.workflow.task.TaskState
import org.endeavourhealth.imapi.vocabulary.RDF
import org.endeavourhealth.imapi.vocabulary.RDFS
import org.endeavourhealth.imapi.vocabulary.WORKFLOW
import org.springframework.stereotype.Component

@Component
class WorkflowService {
  private val workflowRepository = WorkflowRepository()
  private val securityService = SecurityService()
  private val objectMapper = ObjectMapper()

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createBugReport(bugReport: BugReport) {
    bugReport.id = generateId()
    workflowRepository.createBugReport(bugReport)
  }

  @Throws(UserNotFoundException::class)
  fun getBugReport(id: String): BugReport {
    return requireNotNull(workflowRepository.getBugReport(id))
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateBugReport(bugReport: BugReport, request: HttpServletRequest) {
    val user = securityService.getUser(request)
    if (user.username != bugReport.createdBy) throw TaskFilerException("User does not have permission to update bug report")
    val id = requireNotNull(bugReport.id) { "Id is required" }
    val originalBugReport = getBugReport(id.iri)
    if (originalBugReport.product != bugReport.product) workflowRepository.update(
      id.iri,
      WORKFLOW.RELATED_PRODUCT,
      originalBugReport.product,
      bugReport.product,
      user.id
    )
    if (originalBugReport.module != bugReport.module) workflowRepository.update(
      id.iri,
      WORKFLOW.RELATED_MODULE,
      originalBugReport.module.toString(),
      bugReport.module.toString(),
      user.id
    )
    if (originalBugReport.os != bugReport.os) workflowRepository.update(
      id.iri,
      WORKFLOW.OPERATING_SYSTEM,
      originalBugReport.os.toString(),
      bugReport.os.toString(),
      user.id
    )
    if (originalBugReport.osOther != bugReport.osOther) workflowRepository.update(
      id.iri,
      WORKFLOW.OPERATING_SYSTEM_OTHER,
      originalBugReport.osOther,
      bugReport.osOther,
      user.id
    )
    if (originalBugReport.browser != bugReport.browser) workflowRepository.update(
      id.iri,
      WORKFLOW.BROWSER,
      originalBugReport.browser.toString(),
      bugReport.browser.toString(),
      user.id
    )
    if (originalBugReport.browserOther != bugReport.browserOther) workflowRepository.update(
      id.iri,
      WORKFLOW.BROWSER_OTHER,
      originalBugReport.browserOther,
      bugReport.browserOther,
      user.id
    )
    if (originalBugReport.description != bugReport.description) workflowRepository.update(
      id.iri,
      RDFS.COMMENT,
      originalBugReport.description,
      bugReport.description,
      user.id
    )
    if (originalBugReport.reproduceSteps != bugReport.reproduceSteps) workflowRepository.update(
      id.iri,
      WORKFLOW.REPRODUCE_STEPS,
      originalBugReport.reproduceSteps,
      bugReport.reproduceSteps,
      user.id
    )
    if (originalBugReport.expectedResult != bugReport.expectedResult) workflowRepository.update(
      id.iri,
      WORKFLOW.EXPECTED_RESULT,
      originalBugReport.expectedResult,
      bugReport.expectedResult,
      user.id
    )
    if (originalBugReport.actualResult != bugReport.actualResult) workflowRepository.update(
      id.iri,
      WORKFLOW.ACTUAL_RESULT,
      originalBugReport.actualResult,
      bugReport.actualResult,
      user.id
    )
    updateTask(bugReport, user.id)
  }

  @Throws(UserNotFoundException::class)
  fun getTasksByCreatedBy(request: WorkflowRequest): WorkflowResponse {
    return workflowRepository.getTasksByCreatedBy(request)
  }

  @Throws(UserNotFoundException::class)
  fun getTasksByAssignedTo(request: WorkflowRequest): WorkflowResponse {
    return workflowRepository.getTasksByAssignedTo(request)
  }

  @Throws(UserNotFoundException::class)
  fun getUnassignedTasks(request: WorkflowRequest): WorkflowResponse {
    return workflowRepository.getUnassignedTasks(request)
  }

  @Throws(UserNotFoundException::class)
  fun getTask(id: String): Task? {
    return workflowRepository.getTask(id)
  }

  @Throws(TaskFilerException::class)
  fun deleteTask(id: String?) {
    workflowRepository.deleteTask(id)
  }

  fun generateId(): TTIriRef {
    return TTIriRef.iri(workflowRepository.generateId())
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createRoleRequest(roleRequest: RoleRequest) {
    roleRequest.id = generateId()
    workflowRepository.createRoleRequest(roleRequest)
  }

  @Throws(UserNotFoundException::class)
  fun getRoleRequest(id: String): RoleRequest? {
    return workflowRepository.getRoleRequest(id)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateRoleRequest(roleRequest: RoleRequest, request: HttpServletRequest) {
    val user = securityService.getUser(request)
    if (user.username != roleRequest.createdBy) throw TaskFilerException("User does not have permission to update role request")
    val id = requireNotNull(roleRequest.id) { "Id is required" }
    val originalRoleRequest = requireNotNull(getRoleRequest(id.iri))
    if (originalRoleRequest.role != roleRequest.role) workflowRepository.update(
      id.iri,
      WORKFLOW.REQUESTED_ROLE,
      originalRoleRequest.role.toString(),
      roleRequest.role.toString(),
      user.id
    )
    updateTask(roleRequest, user.id)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun approveRoleRequest(request: HttpServletRequest, roleRequest: RoleRequest) {
    val user = securityService.getUser(request)
    val id = requireNotNull(roleRequest.id) { "Id is required" }
    // TODO
    // new AWSCognitoClient().adminAddUserToGroup(roleRequest.getCreatedBy(), roleRequest.getRole());
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      roleRequest.state.toString(),
      TaskState.APPROVED.toString(),
      user.id
    )
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      TaskState.APPROVED.toString(),
      TaskState.COMPLETE.toString(),
      user.id
    )
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun rejectRoleRequest(request: HttpServletRequest, roleRequest: RoleRequest) {
    val user = securityService.getUser(request)
    val id = requireNotNull(roleRequest.id) { "Id is required" }
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      roleRequest.state.toString(),
      TaskState.REJECTED.toString(),
      user.id
    )
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createNamespaceRequest(namespaceRequest: NamespaceRequest) {
    namespaceRequest.id = generateId()
    workflowRepository.createNamespaceRequest(namespaceRequest)
  }

  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getNamespaceRequest(id: String): NamespaceRequest? {
    return workflowRepository.getNamespaceRequest(id)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateNamespaceRequest(namespaceRequest: NamespaceRequest, request: HttpServletRequest) {
    val user = securityService.getUser(request)
    if (user.username != namespaceRequest.createdBy) throw TaskFilerException("User does not have permission to update namespace request")
    val id = requireNotNull(namespaceRequest.id) { "Id is required" }
    val namespacePermission =
      requireNotNull(namespaceRequest.namespacePermission) { "Namespace permission is required" }
    val originalNamespaceRequest =
      requireNotNull(getNamespaceRequest(id.iri)) { "Failed to update namespace request" }
    val originalNamespacePermission = requireNotNull(originalNamespaceRequest.namespacePermission)
    if ((originalNamespacePermission.iri != namespacePermission.iri) || !originalNamespacePermission.read == namespacePermission.read || !originalNamespacePermission.write == namespacePermission.write) workflowRepository.update(
      id.iri,
      WORKFLOW.REQUESTED_NAMESPACE,
      objectMapper.writeValueAsString(originalNamespacePermission),
      objectMapper.writeValueAsString(namespacePermission),
      user.id
    )
    updateTask(namespaceRequest, user.id)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun approveNamespaceRequest(request: HttpServletRequest, namespaceRequest: NamespaceRequest) {
    val user = securityService.getUser(request)
    val namespaces: MutableList<NamespacePermission> = user.namespaces.toMutableList()
    val namespacePermission =
      requireNotNull(namespaceRequest.namespacePermission) { "namespace permissions is required" }
    val id = requireNotNull(namespaceRequest.id) { "Id is required" }
    if (!namespaces.contains(namespacePermission)) {
      namespaces.add(namespacePermission)
      securityService.updateUserNamespaces(user.id, namespaces, request)
    }
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      namespaceRequest.state.toString(),
      TaskState.APPROVED.toString(),
      user.id
    )
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      TaskState.APPROVED.toString(),
      TaskState.COMPLETE.toString(),
      user.id
    )
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun rejectNamespaceRequest(request: HttpServletRequest, namespaceRequest: NamespaceRequest) {
    val user = securityService.getUser(request)
    val id = requireNotNull(namespaceRequest.id) { "Id is required" }
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      namespaceRequest.state.toString(),
      TaskState.REJECTED.toString(),
      user.id
    )
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun createEntityApproval(entityApproval: EntityApproval) {
    entityApproval.id = generateId()
    workflowRepository.createEntityApproval(entityApproval)
  }

  @Throws(UserNotFoundException::class)
  fun getEntityApproval(id: String): EntityApproval? {
    return workflowRepository.getEntityApproval(id)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun updateEntityApproval(entityApproval: EntityApproval, request: HttpServletRequest) {
    val user = securityService.getUser(request)
    if (user.username != entityApproval.createdBy) throw TaskFilerException("User does not have permission to update entity approval")
    val id = requireNotNull(entityApproval.id) { "Id is required" }
    val originalEntityApproval = requireNotNull(getEntityApproval(id.iri)) { "Failed to update entity approval" }
    if (originalEntityApproval.approvalType != entityApproval.approvalType) workflowRepository.update(
      id.iri,
      WORKFLOW.APPROVAL_TYPE,
      originalEntityApproval.approvalType.toString(),
      entityApproval.approvalType.toString(),
      user.id
    )
    updateTask(entityApproval, user.id)
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class, JsonProcessingException::class)
  fun approveEntityApproval(request: HttpServletRequest, entityApproval: EntityApproval) {
    val user = securityService.getUser(request)
    val id = requireNotNull(entityApproval.id) { "Id is required" }
    //TODO entity draft replace active
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      entityApproval.state.toString(),
      TaskState.APPROVED.toString(),
      user.id
    )
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      TaskState.APPROVED.toString(),
      TaskState.COMPLETE.toString(),
      user.id
    )
  }

  @Throws(TaskFilerException::class, JsonProcessingException::class, UserNotFoundException::class)
  fun rejectEntityApproval(request: HttpServletRequest, entityApproval: EntityApproval) {
    val user = securityService.getUser(request)
    val id = requireNotNull(entityApproval.id) { "Id is required" }
    workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      entityApproval.state.toString(),
      TaskState.REJECTED.toString(),
      user.id
    )
  }

  @Throws(TaskFilerException::class, UserNotFoundException::class)
  fun updateTask(task: Task, userId: String?) {
    val id = requireNotNull(task.id) { "Id is required" }
    val originalTask = requireNotNull(getTask(id.iri)) { "Failed to update task" }
    if (task.type != originalTask.type) workflowRepository.update(
      id.iri,
      RDF.TYPE,
      originalTask.type.toString(),
      task.type.toString(),
      userId
    )
    if (task.state != originalTask.state) workflowRepository.update(
      id.iri,
      WORKFLOW.STATE,
      originalTask.state.toString(),
      task.state.toString(),
      userId
    )
    if (task.assignedTo != originalTask.assignedTo) workflowRepository.update(
      id.iri,
      WORKFLOW.ASSIGNED_TO,
      originalTask.assignedTo,
      task.assignedTo,
      userId
    )
  }
}
