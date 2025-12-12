package org.endeavourhealth.imapi.utility

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.Response
import org.slf4j.LoggerFactory

class SetNameGenerator {
  private val log = LoggerFactory.getLogger(SetNameGenerator::class.java)
  private val om = ObjectMapper()

  data class ApiRequest(
    val model: String = "deepseek-r1-distill-qwen-14b",
    val input: String,
    val reasoning: Map<String, String> = mapOf("effort" to "medium")
  )

  fun generateSetName(members: Set<String>): String {
    ClientBuilder.newClient().use { client ->

      client.target("http://localhost:1234")
        .path("/v1/responses")
        .let { target ->
          val prompt = """
        |RESPOND WITH ONLY ONE TERM.
        |ONLY INCLUDE THE TERM ITSELF IN THE OUTPUT, NO ADDITIONAL TEXT. 
        |What is the best term to describe a medical value set containing the following members:
        |* ${members.joinToString("\n|* ")}""".trimMargin("|")

          log.info("Prompt:\n$prompt")
          val request = ApiRequest(input = prompt.replace("\n", "\\n"))
          val json = om.writeValueAsString(request)

          val response: Response = target.request().post(Entity.json(json))
          val responseRaw = response.readEntity(String::class.java)

          val term = om.readTree(responseRaw).extractMessageText

          return term
        }
    }
  }

  val JsonNode.extractMessageText: String
    get() = this["output"]
      .find { it.isMessageType }
      ?.messageText ?: ""

  val JsonNode.isMessageType: Boolean
    get() = this["type"].asText() == "message"

  val JsonNode.messageText: String?
    get() = this["content"]?.get(0)?.get("text")?.asText()?.trim()
}