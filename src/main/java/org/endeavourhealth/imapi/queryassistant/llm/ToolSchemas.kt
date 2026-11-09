package org.endeavourhealth.imapi.queryassistant.llm

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Rewrites a tool's JSON Schema into a self-contained form for smaller models and for servers whose
 * constrained decoding does not resolve references (some llama.cpp / Ollama / vLLM builds):
 *
 * - every `$ref` is inlined and `$defs` / `definitions` removed;
 * - "simple" definitions (no union and no recursion anywhere below them: enums, bounds, leaf filters)
 *   are inlined at every use;
 * - every other definition is inlined in full once, at its first use in document order, with a `title`.
 *   Later uses, including recursive ones, become a short open object described as
 *   "same structure as '<title>' above". Fully unrolling instead multiplies the size (≈13× for the
 *   CohortSpec schema), which hurts small models more than references do;
 * - `oneOf` becomes `anyOf` (alternatives here are disjoint by their `kind`) and `const` a one-value `enum`.
 *
 * The open placeholders mean the schema guides rather than enforces those positions; the tool still
 * parses and validates its input in full and reports errors back to the model.
 */
object ToolSchemas {

  private val nodes = JsonNodeFactory.instance

  fun flatten(schema: JsonNode): JsonNode {
    val defs = schema.get("\$defs") ?: schema.get("definitions") ?: nodes.objectNode()
    return Flattener(defs).expand(schema, null)
  }

  private class Flattener(private val defs: JsonNode) {
    private val expanded = mutableSetOf<String>()
    private val simple: Set<String> = defs.fieldNames().asSequence().filter { isSimple(it, emptySet()) }.toSet()

    fun expand(node: JsonNode, title: String?): JsonNode {
      if (node.isArray) return nodes.arrayNode().addAll(node.map { expand(it, null) })
      if (!node.isObject) return node
      node.get("\$ref")?.let { return expandRef(node as ObjectNode, name(it.asText())) }

      val out = nodes.objectNode()
      title?.let { out.put("title", it) }
      for ((key, value) in node.properties()) {
        when (key) {
          "\$defs", "definitions" -> {}
          "const" -> out.putArray("enum").add(value)
          "oneOf", "anyOf" -> out.set<JsonNode>("anyOf", expand(value, null))
          "properties" -> {
            val properties = out.putObject("properties")
            value.properties().forEach { (name, schema) -> properties.set<JsonNode>(name, expand(schema, null)) }
          }
          "items", "additionalProperties", "allOf", "not" -> out.set<JsonNode>(key, expand(value, null))
          else -> out.set<JsonNode>(key, value.deepCopy())
        }
      }
      return out
    }

    private fun expandRef(node: ObjectNode, name: String): JsonNode {
      val target = defs.get(name) ?: throw IllegalArgumentException("unresolved reference to '$name'")
      val note = node.get("description")?.asText()
      val result = when {
        name in simple -> expand(target, null) as ObjectNode
        expanded.add(name) -> expand(target, name) as ObjectNode
        else -> nodes.objectNode().put("type", "object")
          .put("description", "Same structure as '$name' above.")
      }
      // keywords written next to the $ref (usually a description) take precedence
      node.properties().filter { it.key != "\$ref" && it.key != "description" }.forEach { (k, v) -> result.set<JsonNode>(k, v) }
      if (note != null) result.put("description", listOfNotNull(note, result.get("description")?.asText()).joinToString(" "))
      return result
    }

    private fun isSimple(name: String, path: Set<String>): Boolean {
      if (name in path) return false
      val def = defs.get(name) ?: throw IllegalArgumentException("unresolved reference to '$name'")
      return isSimpleNode(def, path + name)
    }

    private fun isSimpleNode(node: JsonNode, path: Set<String>): Boolean = when {
      node.isArray -> node.all { isSimpleNode(it, path) }
      !node.isObject -> true
      node.has("oneOf") || node.has("anyOf") -> false
      node.has("\$ref") -> isSimple(name(node["\$ref"].asText()), path)
      else -> node.properties().all { (k, v) -> k == "enum" || k == "const" || isSimpleNode(v, path) }
    }

    private fun name(ref: String) = ref.removePrefix("#/\$defs/").removePrefix("#/definitions/")
  }
}
