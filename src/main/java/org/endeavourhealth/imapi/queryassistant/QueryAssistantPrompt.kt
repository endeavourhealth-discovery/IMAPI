package org.endeavourhealth.imapi.queryassistant

/** The fixed system prompt and the IRIs it names, which the model may use without a lookup. */
object QueryAssistantPrompt {

  val system: String by lazy {
    QueryAssistantPrompt::class.java.getResourceAsStream("/queryassistant/system-prompt.md")
      ?.use { it.readBytes().toString(Charsets.UTF_8) }
      ?: error("system-prompt.md missing from classpath")
  }

  /** Must match the "Core vocabulary" section of the prompt, plus the defaults the spec fills in itself. */
  val coreIris: Set<String> = listOf(
    "im:Patient", "im:ClinicalEntry", "im:MedicationRequest",
    "im:concept", "im:effectiveDate", "im:value", "im:age", "im:patient",
    "im:Q_RegisteredGMS",
  ).map(IriPrefixes::expand).toSet()
}
