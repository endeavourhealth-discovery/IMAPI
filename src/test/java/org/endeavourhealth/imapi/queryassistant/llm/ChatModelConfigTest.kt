package org.endeavourhealth.imapi.queryassistant.llm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Duration

class ChatModelConfigTest {

  @Test
  fun defaultsToAnthropic() {
    val config = ChatModelConfig.fromEnv(emptyMap())
    assertEquals(Protocol.ANTHROPIC, config.protocol)
    assertEquals("claude-opus-5-5", config.resolvedModel)
    assertTrue(config.create() is AnthropicChatModel)
  }

  @Test
  fun readsOpenAiCompatibleSettings() {
    val config = ChatModelConfig.fromEnv(
      mapOf(
        "QUERY_ASSISTANT_PROTOCOL" to "openai-compatible",
        "QUERY_ASSISTANT_BASE_URL" to "http://localhost:11434/v1",
        "QUERY_ASSISTANT_MODEL" to "qwen2.5:72b",
        "QUERY_ASSISTANT_TIMEOUT_SECONDS" to "600",
        "QUERY_ASSISTANT_EXTRA_HEADERS" to "api-key=abc; X-Team = imapi",
        "QUERY_ASSISTANT_MAX_TOKENS_FIELD" to "max_completion_tokens",
      )
    )
    assertEquals(Protocol.OPENAI_COMPATIBLE, config.protocol)
    assertEquals("http://localhost:11434/v1", config.baseUrl)
    assertEquals("qwen2.5:72b", config.resolvedModel)
    assertEquals(Duration.ofSeconds(600), config.timeout)
    assertEquals(mapOf("api-key" to "abc", "X-Team" to "imapi"), config.extraHeaders)
    assertEquals("max_completion_tokens", config.maxTokensField)
    assertTrue(config.create() is OpenAiCompatibleChatModel)
  }

  @Test
  fun openAiCompatibleRequiresAModel() {
    val config = ChatModelConfig(protocol = Protocol.OPENAI_COMPATIBLE, baseUrl = "http://localhost:8000/v1")
    assertThrows<IllegalStateException> { config.create() }
  }

  @Test
  fun rejectsUnknownProtocolAndMalformedHeaders() {
    assertThrows<IllegalArgumentException> { ChatModelConfig.fromEnv(mapOf("QUERY_ASSISTANT_PROTOCOL" to "carrier-pigeon")) }
    assertThrows<IllegalArgumentException> { ChatModelConfig.fromEnv(mapOf("QUERY_ASSISTANT_EXTRA_HEADERS" to "novalue")) }
  }

  @Test
  fun toStringHidesTheKey() {
    val text = ChatModelConfig(apiKey = "sk-very-secret").toString()
    assertFalse(text.contains("sk-very-secret"))
    assertTrue(text.contains("apiKey=<set>"))
  }
}
