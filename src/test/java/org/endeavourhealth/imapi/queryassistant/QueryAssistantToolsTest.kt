package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.endeavourhealth.imapi.model.imq.Operator
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.RuleAction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryAssistantToolsTest {

  private val json = jacksonObjectMapper()
  private val sct = "http://snomed.info/sct#"
  private val astCod = "${sct}999012891000230104"
  private val astTrt = "${sct}12463601000001108"
  private val asthmaRegister = "http://endhealth.info/qof#36e70531-462a-427a-8ee2-eeb584018cc9"

  private val astRegQuery: Query = QuerySpecDecompiler.defaultImqMapper().readValue(
    javaClass.getResource("/queryassistant/ast_reg.imq.json")!!.readText(), Query::class.java
  )

  private val lookups = FakeLookups(
    valueSets = listOf(
      LookupHit(astCod, "AST_COD", types = listOf("http://endhealth.info/im#ConceptSet")),
      LookupHit(astTrt, "ASTTRT_COD"),
      LookupHit("${sct}999010051000230100", "ASTRES_COD"),
    ),
    cohorts = listOf(LookupHit(asthmaRegister, "AST_REG - Patients on the asthma register")),
    propertiesByType = mapOf(
      "http://endhealth.info/im#Patient" to listOf(PropertyInfo("http://endhealth.info/im#gender", "gender", null)),
    ),
    definitions = mapOf(asthmaRegister to astRegQuery),
  )
  private val tools = QueryAssistantTools(lookups)
  private fun workspace() = AssistantWorkspace(QueryAssistantPrompt.coreIris)

  private fun run(name: String, input: String, ws: AssistantWorkspace) = tools.execute(name, json.readTree(input), ws)
  private fun body(outcome: ToolOutcome): JsonNode = json.readTree(outcome.content)

  /** Registered for GMS, then an AST_COD record in the last 12 months. */
  private val asthmaSpec = """
    {"spec": {"rules": [
      {"match": {"kind": "cohort", "iri": "im:Q_RegisteredGMS"}, "ifTrue": "NEXT", "ifFalse": "REJECT"},
      {"match": {"kind": "records", "entity": "im:ClinicalEntry", "filter": {"kind": "group", "operator": "ALL", "items": [
        {"kind": "concept", "concepts": [{"iri": "sct:999012891000230104", "match": "SET_MEMBERS"}]},
        {"kind": "value", "property": "im:effectiveDate", "operator": ">", "value": "-12", "units": "MONTHS",
         "relativeTo": {"parameter": "${'$'}searchDate"}}
      ]}}, "ifTrue": "SELECT", "ifFalse": "REJECT"}
    ]}}
  """.trimIndent()

  // ------------------------------------------------------------------ lookups

  @Test
  fun searchCompactsIrisAndRecordsThemAsSeen() {
    val ws = workspace()
    val outcome = run(QueryAssistantTools.SEARCH_CONCEPTS, """{"term": "AST_COD"}""", ws)

    assertFalse(outcome.isError)
    val hit = body(outcome)[0]
    assertEquals("sct:999012891000230104", hit["iri"].asText())
    assertEquals("im:ConceptSet", hit["types"][0].asText())
    assertTrue(astCod in ws.seenIris)
    assertEquals(listOf("concepts:AST_COD:VALUE_SET"), lookups.searches)
  }

  @Test
  fun searchValidatesInput() {
    assertTrue(run(QueryAssistantTools.SEARCH_CONCEPTS, """{}""", workspace()).isError)
    assertTrue(run(QueryAssistantTools.SEARCH_CONCEPTS, """{"term": "x", "kind": "BOGUS"}""", workspace()).isError)
    assertTrue(run("no_such_tool", """{}""", workspace()).isError)
  }

  @Test
  fun getPropertiesRecordsPropertyIris() {
    val ws = workspace()
    val outcome = run(QueryAssistantTools.GET_PROPERTIES, """{"type": "im:Patient"}""", ws)
    assertEquals("im:gender", body(outcome)[0]["iri"].asText())
    assertTrue("http://endhealth.info/im#gender" in ws.seenIris)
  }

  // ------------------------------------------------------------------ set_draft

  @Test
  fun setDraftRejectsIrisThatWereNotLookedUp() {
    val ws = workspace()
    val outcome = run(QueryAssistantTools.SET_DRAFT, asthmaSpec, ws)

    assertTrue(outcome.isError)
    val errors = body(outcome)["errors"].map { it.asText() }
    assertEquals(1, errors.size, errors.toString())
    assertTrue(errors[0].startsWith("$.rules[1].match.filter.items[0].concepts[0].iri: sct:999012891000230104 was not returned"), errors[0])
    assertNull(ws.draft)
  }

  @Test
  fun setDraftAcceptsLookedUpIrisAndFillsNames() {
    val ws = workspace()
    run(QueryAssistantTools.SEARCH_CONCEPTS, """{"term": "AST_COD"}""", ws)
    val outcome = run(QueryAssistantTools.SET_DRAFT, asthmaSpec, ws)

    assertFalse(outcome.isError, outcome.content)
    assertEquals("valid", body(outcome)["status"].asText())
    val draft = ws.draft!!
    val concept = ((draft.spec.rules!![1].match as Records).filter as FilterGroup).items[0] as ConceptFilter
    assertEquals("AST_COD", concept.concepts[0].name)
    assertTrue(draft.readback.contains("AST_COD (set)"), draft.readback)
    assertTrue(draft.readback.contains("12 months before the search date"), draft.readback)
    assertEquals(astCod, json.valueToTree<JsonNode>(draft.query).at("/rule/1/where/and/0/is/0/iri").asText())
  }

  @Test
  fun setDraftRejectsIrisThatDoNotExist() {
    val ghost = "${sct}0000000"
    val ws = AssistantWorkspace(QueryAssistantPrompt.coreIris + ghost)
    val outcome = run(QueryAssistantTools.SET_DRAFT, asthmaSpec.replace("999012891000230104", "0000000"), ws)

    val errors = body(outcome)["errors"].map { it.asText() }
    assertTrue(errors.single().endsWith("sct:0000000 does not exist in the Information Model"), errors.toString())
  }

  @Test
  fun setDraftReportsJsonProblemsWithTheirLocation() {
    val outcome = run(QueryAssistantTools.SET_DRAFT, asthmaSpec.replace("\"ifFalse\": \"REJECT\"}", "\"ifFalse\": \"MAYBE\"}"), workspace())
    val errors = body(outcome)["errors"].map { it.asText() }
    assertTrue(errors.single().startsWith("$.spec.rules[0].ifFalse") || errors.single().startsWith("$.rules[0].ifFalse"), errors.toString())
  }

  @Test
  fun setDraftReportsCompilerErrorsAndKeepsPreviousDraft() {
    val ws = workspace()
    run(QueryAssistantTools.SEARCH_CONCEPTS, """{"term": "AST_COD"}""", ws)
    run(QueryAssistantTools.SET_DRAFT, asthmaSpec, ws)
    val previous = ws.draft

    val broken = """{"spec": {"allOf": [{"kind": "records", "entity": "im:ClinicalEntry",
      "then": {"filter": {"kind": "value", "property": "im:value", "operator": "<", "value": "140"}}}]}}"""
    val outcome = run(QueryAssistantTools.SET_DRAFT, broken, ws)

    assertTrue(outcome.isError)
    assertTrue(body(outcome)["errors"].any { it.asText().contains("'pick' is required") }, outcome.content)
    assertSame(previous, ws.draft)
  }

  // ------------------------------------------------------------------ get_cohort_definition

  @Test
  fun cohortDefinitionCanBeUsedAsATemplate() {
    val ws = workspace()
    val outcome = run(QueryAssistantTools.GET_COHORT_DEFINITION, """{"iri": "qof:36e70531-462a-427a-8ee2-eeb584018cc9"}""", ws)
    assertFalse(outcome.isError, outcome.content)
    val result = body(outcome)
    assertTrue(result["readback"].asText().contains("in the cohort \"Registered and GP for GMS services"))

    // the returned spec's IRIs are now grounded, so it can be saved as-is (e.g. after edits)
    val save = run(QueryAssistantTools.SET_DRAFT, json.writeValueAsString(mapOf("spec" to result["spec"])), ws)
    assertFalse(save.isError, save.content)
  }

  @Test
  fun unsupportedDefinitionSuggestsReusingTheCohort() {
    val dataset = Query().setTypeOf("http://endhealth.info/im#Patient").addColumnGroup(Query())
    val tools = QueryAssistantTools(FakeLookups(definitions = mapOf(asthmaRegister to dataset)))
    val outcome = tools.execute(
      QueryAssistantTools.GET_COHORT_DEFINITION, json.readTree("""{"iri": "$asthmaRegister"}"""), workspace()
    )
    assertFalse(outcome.isError)
    assertTrue(outcome.content.contains("{\"kind\":\"cohort\",\"iri\":\"qof:36e70531-462a-427a-8ee2-eeb584018cc9\"}"), outcome.content)
  }

  // ------------------------------------------------------------------ schema stays in sync with the Kotlin model

  @Test
  fun draftSchemaReferencesResolve() {
    val schema = tools.specs.single { it.name == QueryAssistantTools.SET_DRAFT }.inputSchema
    val defs = schema["\$defs"]
    assertNotNull(defs)
    val refs = mutableListOf<String>()
    fun collect(n: JsonNode) {
      if (n.isObject) n.fields().forEach { (k, v) -> if (k == "\$ref") refs += v.asText() else collect(v) }
      if (n.isArray) n.forEach(::collect)
    }
    collect(schema)
    assertTrue(refs.isNotEmpty())
    refs.forEach { ref ->
      assertTrue(ref.startsWith("#/\$defs/") && defs.has(ref.removePrefix("#/\$defs/")), "unresolved $ref")
    }
  }

  @Test
  fun draftSchemaEnumsMatchKotlinModel() {
    val defs = tools.specs.single { it.name == QueryAssistantTools.SET_DRAFT }.inputSchema["\$defs"]
    fun enumAt(pointer: String) = defs.at(pointer).map { it.asText() }.toSet()

    assertEquals(Operator.entries.map { it.value }.toSet(), enumAt("/operator/enum"))
    assertEquals(TimeUnit.entries.map { it.name }.toSet(), enumAt("/units/enum"))
    assertEquals(RuleAction.entries.map { it.name }.toSet(), enumAt("/ruleAction/enum"))
    assertEquals(BoolOp.entries.map { it.name }.toSet(), enumAt("/boolOp/enum"))
    assertEquals(PeriodQualifier.entries.map { it.name }.toSet(), enumAt("/valueFilter/properties/qualifier/enum"))
    assertEquals(ConceptMatch.entries.map { it.name }.toSet(), enumAt("/conceptFilter/properties/concepts/items/properties/match/enum"))
  }

  @Test
  fun promptExampleIsAValidSpec() {
    val example = Regex("```json\\n(.*?)\\n```", RegexOption.DOT_MATCHES_ALL).find(QueryAssistantPrompt.system)!!.groupValues[1]
    val spec = json.readValue(example, CohortSpec::class.java)
    QuerySpecCompiler().compile(spec)
    // every IRI in the example that is not core vocabulary is something the model must look up
    val nonCore = spec.referencedIris().map { it.iri }.filter { it !in QueryAssistantPrompt.coreIris }.toSet()
    assertEquals(setOf(astCod, astTrt, "${sct}999010051000230100"), nonCore)
  }
}
