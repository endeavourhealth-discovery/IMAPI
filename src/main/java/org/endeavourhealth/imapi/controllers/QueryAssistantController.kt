package org.endeavourhealth.imapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.security.Action
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.queryassistant.AssistantReply
import org.endeavourhealth.imapi.queryassistant.DraftView
import org.endeavourhealth.imapi.queryassistant.QueryAssistantService
import org.endeavourhealth.imapi.queryassistant.SessionNotFoundException
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.server.ResponseStatusException

data class AssistantMessageRequest(val sessionId: String? = null, val message: String = "")

@RestController
@RequestMapping("api/query/protected/assistant")
@CrossOrigin(origins = ["*"])
@Tag(name = "Query Assistant", description = "Chat assistant that drafts cohort queries from a description")
@RequestScope
class QueryAssistantController(private val assistant: QueryAssistantService) {
  private val securityService = SecurityService()
  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("/message")
  @Operation(
    summary = "Send a message to the query assistant",
    description = "Starts a conversation when sessionId is absent. Returns the reply and the current draft query, if any.",
  )
  fun message(@RequestBody request: AssistantMessageRequest): AssistantReply {
    MetricsHelper.recordTime("API.Query.Assistant.Message.POST").use {
      securityService.requiresPermission(Resource.QUERY, Action.CREATE)
      if (request.message.isBlank()) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "message must not be blank")
      val owner = securityService.user.id
      return withSession { assistant.chat(request.sessionId, owner, request.message) }
        .also { log.debug("assistant turn {} ended with {} after {} model calls", it.sessionId, it.stopReason, it.usage.modelCalls) }
    }
  }

  @GetMapping("/session/{sessionId}/draft")
  @Operation(summary = "Get the current draft query of an assistant conversation")
  fun draft(@PathVariable sessionId: String): DraftView? {
    securityService.requiresPermission(Resource.QUERY, Action.CREATE)
    return withSession { assistant.currentDraft(sessionId, securityService.user.id) }
  }

  @DeleteMapping("/session/{sessionId}")
  @Operation(summary = "End an assistant conversation")
  fun endSession(@PathVariable sessionId: String) {
    securityService.requiresPermission(Resource.QUERY, Action.CREATE)
    assistant.endSession(sessionId, securityService.user.id)
  }

  private fun <T> withSession(block: () -> T): T =
    try {
      block()
    } catch (e: SessionNotFoundException) {
      throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
    }
}
