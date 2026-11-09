package org.endeavourhealth.imapi.queryassistant.llm

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.queryassistant.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class ToolSchemasTest {

  private val json = jacksonObjectMapper()
  private val draftSchema = QueryAssistantTools(FakeLookups()).specs.single { it.name == QueryAssistantTools.SET_DRAFT }.inputSchema
  private val flat = ToolSchemas.flatten(draftSchema)

  private fun size(n: JsonNode) = json.writeValueAsString(n).length

  @Test
  fun flatDraftSchemaIsSelfContainedAndCompact() {
    val keywords = mutableSetOf<String>()
    fun walk(n: JsonNode) {
      if (n.isObject) n.properties().forEach { (k, v) -> keywords += k; if (k != "properties") walk(v) else v.forEach(::walk) }
      if (n.isArray) n.forEach(::walk)
    }
    walk(flat)
    assertTrue(keywords.none { it in setOf("\$ref", "\$defs", "definitions", "oneOf", "const") }, keywords.toString())
    assertEquals("object", flat["type"].asText())
    println("set_draft schema: ${size(draftSchema)} chars with \$defs, ${size(flat)} flattened")
    assertTrue(size(flat) < size(draftSchema) * 1.5, "flattening should not blow up the schema")
  }

  @Test
  fun compositeDefinitionsAreInlinedOnceThenReferredToByTitle() {
    val criterion = flat.at("/properties/spec/properties/rules/items/properties/match")
    assertEquals("criterion", criterion["title"].asText())
    assertEquals(listOf("cohort", "records", "group"), kinds(criterion))

    val records = criterion["anyOf"][1]
    val filter = records.at("/properties/filter")
    assertEquals("filter", filter["title"].asText())
    assertEquals(listOf("group", "concept", "value", "range"), kinds(filter))

    // the recursive and repeated uses point back to the first expansion
    assertPlaceholder("filter", filter["anyOf"][0].at("/properties/items/items"))
    assertPlaceholder("filter", records.at("/properties/then/properties/filter"))
    assertPlaceholder("criterion", criterion["anyOf"][2].at("/properties/items/items"))
    assertPlaceholder("criterion", flat.at("/properties/spec/properties/allOf/items"))
  }

  @Test
  fun simpleDefinitionsAreInlinedEverywhere() {
    val rule = flat.at("/properties/spec/properties/rules/items/properties")
    for (action in listOf("ifTrue", "ifFalse")) {
      assertEquals(listOf("NEXT", "SELECT", "REJECT"), rule[action]["enum"].map { it.asText() })
      assertTrue(rule[action]["description"].asText().startsWith("NEXT = go on"))
    }
    val value = flat.at("/properties/spec/properties/rules/items/properties/match/anyOf/1/properties/filter/anyOf/2/properties")
    assertEquals("=", value["operator"]["enum"][0].asText())
    assertEquals(listOf("parameter", "alias", "property"), value["relativeTo"]["properties"].fieldNames().asSequence().toList())
  }

  @Test
  fun descriptionNextToARefIsKept() {
    val schema = json.readTree(
      """{"type":"object","properties":{"a":{"${'$'}ref":"#/${'$'}defs/u","description":"Where."},"b":{"${'$'}ref":"#/${'$'}defs/u","description":"Again."}},
          "${'$'}defs":{"u":{"description":"A thing.","oneOf":[{"type":"string"},{"type":"integer"}]}}}"""
    )
    val result = ToolSchemas.flatten(schema)
    assertEquals("Where. A thing.", result.at("/properties/a/description").asText())
    assertEquals("Again. Same structure as 'u' above.", result.at("/properties/b/description").asText())
    assertThrows<IllegalArgumentException> { ToolSchemas.flatten(json.readTree("""{"${'$'}ref":"#/${'$'}defs/missing"}""")) }
  }

  @Test
  fun configDecidesWhenToFlatten() {
    val spec = ToolSpec("set_draft", "d", draftSchema)
    assertSame(draftSchema, ChatModelConfig().toolSchema(spec))
    assertEquals(flat, ChatModelConfig(Protocol.OPENAI_COMPATIBLE, model = "m").toolSchema(spec))
    assertEquals(flat, ChatModelConfig(flattenToolSchemas = true).toolSchema(spec))
    assertSame(draftSchema, ChatModelConfig(Protocol.OPENAI_COMPATIBLE, model = "m", flattenToolSchemas = false).toolSchema(spec))
    assertEquals(
      true,
      ChatModelConfig.fromEnv(mapOf("QUERY_ASSISTANT_FLATTEN_TOOL_SCHEMAS" to "true")).resolvedFlattenToolSchemas,
    )
  }

  /** Every real query, as the model would write it, must satisfy both the full and the flattened schema. */
  @Test
  fun corpusSpecsConformToBothSchemas() {
    val file = File(System.getProperty("querySpec.corpus") ?: "query-result.csv")
    assumeTrue(file.exists(), "corpus ${file.absolutePath} not found")
    val imq = QuerySpecDecompiler.defaultImqMapper()
    val decompiler = QuerySpecDecompiler(QuerySpecCompiler(), imq)
    val specJson = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)

    var checked = 0
    val failures = mutableListOf<String>()
    for ((iri, text) in CorpusCsv.read(file).drop(1)) {
      val query = imq.readValue(text, Query::class.java)
      if (query.columnGroup != null) continue
      val spec = try { decompiler.decompile(query) } catch (e: UnsupportedQueryException) { continue }
      val node = specJson.valueToTree<ObjectNode>(spec).apply { remove("iri") } // assigned by the server, not the model
      val input = json.createObjectNode().set<JsonNode>("spec", node)
      checked++
      validate(draftSchema, input, "$", draftSchema["\$defs"])?.let { failures += "full $iri $it" }
      validate(flat, input, "$", null)?.let { failures += "flat $iri $it" }
    }
    println("Schema check: $checked corpus specs, ${failures.size} failures")
    assertTrue(checked > 0)
    assertTrue(failures.isEmpty(), failures.take(5).joinToString("\n"))
  }

  private fun kinds(schema: JsonNode) = schema["anyOf"].map { it.at("/properties/kind/enum/0").asText() }

  private fun assertPlaceholder(title: String, node: JsonNode) {
    assertEquals("object", node["type"].asText(), node.toString())
    assertTrue(node["description"].asText().endsWith("Same structure as '$title' above."), node.toString())
    assertFalse(node.has("properties"))
  }

  /** Validator for the keywords these schemas use; returns the first violation or null. */
  private fun validate(schema: JsonNode, value: JsonNode, path: String, defs: JsonNode?): String? {
    schema["\$ref"]?.let { return validate(defs!![it.asText().removePrefix("#/\$defs/")], value, path, defs) }
    (schema["anyOf"] ?: schema["oneOf"])?.let { alternatives ->
      val errors = alternatives.map { validate(it, value, path, defs) }
      if (errors.none { it == null }) return errors.firstOrNull { !it!!.startsWith("$path.kind") } ?: errors.first()
    }
    schema["const"]?.let { if (it != value) return "$path: expected $it" }
    schema["enum"]?.let { e -> if (e.none { it == value }) return "$path: $value not in $e" }
    when (schema["type"]?.asText()) {
      "object" -> {
        if (!value.isObject) return "$path: not an object"
        schema["required"]?.forEach { if (!value.has(it.asText())) return "$path: missing ${it.asText()}" }
        val properties = schema["properties"]
        for ((k, v) in value.properties()) {
          val propertySchema = properties?.get(k)
            ?: if (schema["additionalProperties"]?.asBoolean(true) == false) return "$path.$k: not allowed" else continue
          validate(propertySchema, v, "$path.$k", defs)?.let { return it }
        }
      }
      "array" -> {
        if (!value.isArray) return "$path: not an array"
        value.forEachIndexed { i, v -> validate(schema["items"], v, "$path[$i]", defs)?.let { return it } }
      }
      "string" -> if (!value.isTextual) return "$path: not a string"
      "integer" -> if (!value.isIntegralNumber) return "$path: not an integer"
      "boolean" -> if (!value.isBoolean) return "$path: not a boolean"
    }
    return null
  }
}
