package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.endeavourhealth.imapi.model.imq.Operator
import org.endeavourhealth.imapi.model.imq.Order
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.RuleAction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class QuerySpecCompilerTest {

  private val imqMapper = QuerySpecDecompiler.defaultImqMapper()
  private val specMapper = jacksonObjectMapper()
  private val compiler = QuerySpecCompiler()
  private val decompiler = QuerySpecDecompiler(compiler, imqMapper)

  private fun resource(name: String) =
    javaClass.getResource("/queryassistant/$name")?.readText() ?: error("missing fixture $name")

  private fun tree(query: Query): JsonNode = imqMapper.valueToTree(query)

  // ------------------------------------------------------------------ fixtures from the QOF / SMH corpus

  @ParameterizedTest
  @ValueSource(strings = ["ast_reg", "home_bp"])
  fun specFixtureCompilesToOriginalImq(name: String) {
    val expected = tree(imqMapper.readValue(resource("$name.imq.json"), Query::class.java))
    val spec = specMapper.readValue<CohortSpec>(resource("$name.spec.json"))

    assertNull(firstDifference(expected, tree(compiler.compile(spec))))
  }

  @ParameterizedTest
  @ValueSource(strings = ["ast_reg", "home_bp"])
  fun imqFixtureDecompilesToSpecFixture(name: String) {
    val query = imqMapper.readValue(resource("$name.imq.json"), Query::class.java)
    val expected = specMapper.readValue<CohortSpec>(resource("$name.spec.json"))

    assertEquals(expected, decompiler.decompile(query))
  }

  // ------------------------------------------------------------------ hand-written spec, as an LLM would produce it

  private val latestBpUnder140 = """
    {
      "name": "Hypertensive patients whose latest systolic BP is under 140",
      "rules": [
        { "match": { "kind": "cohort", "iri": "im:Q_RegisteredGMS" }, "ifTrue": "NEXT", "ifFalse": "REJECT" },
        {
          "match": {
            "kind": "records",
            "entity": "im:ClinicalEntry",
            "filter": { "kind": "group", "operator": "ALL", "items": [
              { "kind": "concept", "concepts": [ { "iri": "qof:systolic", "match": "SET_MEMBERS" } ] },
              { "kind": "value", "property": "im:effectiveDate", "operator": ">", "value": "-12", "units": "MONTHS",
                "relativeTo": { "parameter": "${'$'}searchDate" } }
            ] },
            "pick": { "direction": "descending" },
            "then": { "filter": { "kind": "value", "property": "im:value", "operator": "<", "value": "140" } }
          },
          "ifTrue": "SELECT", "ifFalse": "REJECT"
        }
      ]
    }
  """.trimIndent()

  @Test
  fun compilesHandWrittenSpecWithDefaultsAndGeneratedAlias() {
    val query = compiler.compile(specMapper.readValue<CohortSpec>(latestBpUnder140))
    val json = tree(query)

    assertEquals("http://endhealth.info/im#Patient", json.at("/typeOf/iri").asText())

    val registered = json.at("/rule/0")
    assertEquals("http://endhealth.info/im#Q_RegisteredGMS", registered.at("/is/iri").asText())
    assertTrue(registered.at("/is/cohort").asBoolean())

    val bp = json.at("/rule/1")
    assertEquals("SELECT", bp.at("/ifTrue").asText())
    assertEquals("http://endhealth.info/im#concept", bp.at("/where/and/0/iri").asText())
    assertEquals("http://endhealth.info/qof#systolic", bp.at("/where/and/0/is/0/iri").asText())
    assertTrue(bp.at("/where/and/0/is/0/memberOf").asBoolean())

    val window = bp.at("/where/and/1")
    assertEquals("http://endhealth.info/im#effectiveDate", window.at("/compare/left/iri").asText())
    assertEquals("\$searchDate", window.at("/compare/right/parameter").asText())
    assertEquals("http://endhealth.info/im#Months", window.at("/units/iri").asText())

    assertEquals("http://endhealth.info/im#patient", bp.at("/orderBy/partition/0/iri").asText())
    assertEquals("patient", bp.at("/orderBy/partition/0/name").asText())
    assertEquals("descending", bp.at("/orderBy/property/0/direction").asText())
    assertEquals(1, bp.at("/orderBy/limit").asInt())

    val alias = bp.at("/as").asText()
    assertEquals("match_1", alias)
    assertEquals(alias, bp.at("/then/from").asText())
    assertEquals("<", bp.at("/then/where/operator").asText())
  }

  @Test
  fun relativeToAliasOmitsWherePropertyAndBuildsNodeRef() {
    val spec = CohortSpec(
      allOf = listOf(
        Records(entity = "im:ClinicalEntry", pick = Pick(Order.descending), alias = "latestBp"),
        Records(
          entity = "im:ClinicalEntry",
          filter = ValueFilter("im:effectiveDate", Operator.eq, relativeTo = RelativeTo(alias = "latestBp")),
        ),
      )
    )
    val where = tree(compiler.compile(spec)).at("/and/1/where")

    assertTrue(where.path("iri").isMissingNode)
    assertEquals("http://endhealth.info/im#effectiveDate", where.at("/compare/left/iri").asText())
    assertEquals("latestBp", where.at("/compare/right/nodeRef").asText())
    assertEquals("effectiveDate", where.at("/compare/right/propertyRef").asText())
  }

  @Test
  fun generatedAliasAvoidsAliasesDefinedLater() {
    val spec = CohortSpec(
      allOf = listOf(
        Records("im:ClinicalEntry", pick = Pick(Order.descending), then = ThenTest(ValueFilter("im:value", Operator.gt, "0"))),
        Records("im:ClinicalEntry", alias = "match_1"),
      )
    )
    assertEquals("match_2", tree(compiler.compile(spec)).at("/and/0/as").asText())
  }

  // ------------------------------------------------------------------ validation

  @Test
  fun collectsAllValidationErrorsWithPaths() {
    val spec = CohortSpec(
      rules = listOf(
        RuleSpec(
          Records(
            entity = "im:ClinicalEntry",
            alias = "dup",
            filter = ValueFilter("im:effectiveDate", Operator.eq, relativeTo = RelativeTo(alias = "nowhere")),
            then = ThenTest(ValueFilter("im:value", Operator.gt, "0")),
          ),
          RuleAction.NEXT, RuleAction.REJECT,
        ),
        RuleSpec(Group(BoolOp.ANY, emptyList(), alias = "dup"), RuleAction.SELECT, RuleAction.REJECT),
      )
    )

    val errors = assertThrows(QuerySpecException::class.java) { compiler.compile(spec) }.errors

    assertTrue(errors.contains("$.rules[0].match.then: 'then' tests picked records, so 'pick' is required"), errors.toString())
    assertTrue(errors.contains("$.rules[1].match.items: must not be empty"), errors.toString())
    assertTrue(errors.contains("$.rules[1].match: alias 'dup' is already defined at $.rules[0].match"), errors.toString())
    assertTrue(errors.contains("$.rules[0].match.filter.relativeTo: alias 'nowhere' is not defined anywhere in the query"), errors.toString())
    assertEquals(4, errors.size, errors.toString())
  }

  @Test
  fun requiresExactlyOneOfRulesOrAllOf() {
    assertThrows(QuerySpecException::class.java) { compiler.compile(CohortSpec()) }
    assertThrows(QuerySpecException::class.java) {
      compiler.compile(
        CohortSpec(
          rules = listOf(RuleSpec(CohortRef("im:Q_RegisteredGMS"), RuleAction.SELECT, RuleAction.REJECT)),
          allOf = listOf(CohortRef("im:Q_RegisteredGMS")),
        )
      )
    }
  }

  @Test
  fun rejectsRelativeToWithBothParameterAndAlias() {
    val spec = CohortSpec(
      allOf = listOf(
        Records("im:Patient", filter = ValueFilter("im:age", Operator.gte, "5", relativeTo = RelativeTo("\$searchDate", "x")))
      )
    )
    val errors = assertThrows(QuerySpecException::class.java) { compiler.compile(spec) }.errors
    assertTrue(errors.any { it.startsWith("$.allOf[0].filter.relativeTo: set either 'parameter' or 'alias'") }, errors.toString())
  }

  // ------------------------------------------------------------------ decompiler limits

  @Test
  fun decompilerRejectsDatasetQueries() {
    val dataset = Query().setTypeOf("http://endhealth.info/im#Patient").addColumnGroup(Query())
    val e = assertThrows(UnsupportedQueryException::class.java) { decompiler.decompile(dataset) }
    assertEquals("$.columnGroup", e.path)
  }

  @Test
  fun iriPrefixesRoundTrip() {
    val full = "http://snomed.info/sct#999012891000230104"
    assertEquals("sct:999012891000230104", IriPrefixes.compact(full))
    assertEquals(full, IriPrefixes.expand("sct:999012891000230104"))
    assertEquals("http://example.org/x#y", IriPrefixes.compact("http://example.org/x#y"))
    assertEquals("unknown:y", IriPrefixes.expand("unknown:y"))
  }
}
