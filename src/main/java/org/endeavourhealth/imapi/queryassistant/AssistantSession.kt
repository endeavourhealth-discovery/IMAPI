package org.endeavourhealth.imapi.queryassistant

import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.queryassistant.llm.ChatTurn
import org.endeavourhealth.imapi.queryassistant.llm.ToolResult
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/** The current valid draft: the spec (with names filled in), its compiled IMQ, and the plain-English read-back. */
data class Draft(val spec: CohortSpec, val query: Query, val readback: String)

/** State the tools read and write during a conversation. */
class AssistantWorkspace(initiallyKnownIris: Set<String> = emptySet()) {
  /** Full IRIs the model has been shown by a lookup (or that are core vocabulary). */
  val seenIris: MutableSet<String> = initiallyKnownIris.toMutableSet()
  var draft: Draft? = null
}

/**
 * One chat with one user. [turns] is append-only: assistant turns keep the provider's native form and are
 * never edited, which keeps prompt caching valid and lets providers that need it (Claude) replay thinking.
 */
class AssistantSession(
  val id: String,
  val owner: String,
  val workspace: AssistantWorkspace,
  now: Instant,
) {
  val turns: MutableList<ChatTurn> = mutableListOf()
  /** Tool results owed to the model when a turn was cut off mid tool call; sent with the next user message. */
  val pendingToolResults: MutableList<ToolResult> = mutableListOf()
  @Volatile var lastUsed: Instant = now
}

class SessionNotFoundException(id: String) : RuntimeException("Assistant session '$id' not found or expired")

/** In-memory session store with idle expiry. Sessions are only visible to the user who created them. */
class AssistantSessionStore(
  private val idleTimeout: Duration = Duration.ofHours(2),
  private val maxSessions: Int = 1000,
  private val clock: Clock = Clock.systemUTC(),
) {
  private val sessions = ConcurrentHashMap<String, AssistantSession>()

  fun create(owner: String, initiallyKnownIris: Set<String>): AssistantSession {
    evictExpired()
    if (sessions.size >= maxSessions) evictOldest()
    val session = AssistantSession(UUID.randomUUID().toString(), owner, AssistantWorkspace(initiallyKnownIris), clock.instant())
    sessions[session.id] = session
    return session
  }

  fun get(id: String, owner: String): AssistantSession {
    val session = sessions[id]
    if (session == null || session.owner != owner || isExpired(session)) {
      if (session != null && isExpired(session)) sessions.remove(id)
      throw SessionNotFoundException(id)
    }
    session.lastUsed = clock.instant()
    return session
  }

  fun delete(id: String, owner: String) {
    sessions.computeIfPresent(id) { _, s -> if (s.owner == owner) null else s }
  }

  private fun isExpired(s: AssistantSession) = s.lastUsed.plus(idleTimeout).isBefore(clock.instant())

  private fun evictExpired() {
    sessions.values.filter(::isExpired).forEach { sessions.remove(it.id) }
  }

  private fun evictOldest() {
    sessions.values.minByOrNull { it.lastUsed }?.let { sessions.remove(it.id) }
  }
}
