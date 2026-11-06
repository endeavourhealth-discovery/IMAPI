package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.tripletree.TTIriRef

class UnsupportedQueryException(val path: String, val reason: String) :
  RuntimeException("$path: $reason")

/**
 * Converts an IMQ cohort [Query] into a [CohortSpec].
 *
 * The result is verified by recompiling it and comparing the IMQ JSON with the original,
 * so a returned spec is always lossless. Anything that cannot be represented raises
 * [UnsupportedQueryException] with the location and reason.
 */
class QuerySpecDecompiler(
  private val compiler: QuerySpecCompiler = QuerySpecCompiler(),
  private val mapper: ObjectMapper = defaultImqMapper(),
) {

  fun decompile(query: Query): CohortSpec {
    val spec = toSpec(query)
    val recompiled = try {
      compiler.compile(spec)
    } catch (e: QuerySpecException) {
      throw UnsupportedQueryException("$", "decompiled spec does not compile: ${e.errors.joinToString("; ")}")
    }
    val diff = firstDifference(mapper.valueToTree(query), mapper.valueToTree(recompiled))
    if (diff != null) throw UnsupportedQueryException(diff.path, "lossy conversion: ${diff.description}")
    return spec
  }

  private fun toSpec(q: Query): CohortSpec {
    if (q.columnGroup != null) unsupported("$.columnGroup", "dataset queries (columnGroup) are not supported")
    val typeOf = q.typeOf?.iri ?: unsupported("$.typeOf", "a base type is required")
    if (q.rule != null && q.and != null) unsupported("$", "both 'rule' and 'and' at top level")
    return CohortSpec(
      name = q.name,
      description = q.description,
      iri = q.iri?.let(::c),
      baseType = c(typeOf),
      rules = q.rule?.mapIndexed { i, r ->
        val action = { a: RuleAction?, field: String -> a ?: unsupported("$.rule[$i].$field", "missing") }
        RuleSpec(criterion(r, "$.rule[$i]"), action(r.ifTrue, "ifTrue"), action(r.ifFalse, "ifFalse"))
      },
      allOf = q.and?.mapIndexed { i, c -> criterion(c, "$.and[$i]") },
    )
  }

  // ------------------------------------------------------------------ criteria

  private fun criterion(q: Query, path: String): Criterion {
    rejectUnsupportedClauses(q, path)
    val isNode = q.`is`
    return when {
      isNode != null -> {
        if (!isNode.isCohort) unsupported("$path.is", "only cohort references are supported")
        CohortRef(c(isNode.iri ?: unsupported("$path.is.iri", "missing")), isNode.name)
      }

      q.and != null || q.or != null -> {
        if (q.and != null && q.or != null) unsupported(path, "both 'and' and 'or' on one clause")
        val (op, items, key) =
          if (q.and != null) Triple(BoolOp.ALL, q.and, "and") else Triple(BoolOp.ANY, q.or, "or")
        Group(
          operator = op,
          items = items.mapIndexed { i, c -> criterion(c, "$path.$key[$i]") },
          filter = q.where?.let { filter(it, "$path.where") },
          pick = q.orderBy?.let { pick(it, "$path.orderBy") },
          alias = q.`as`,
          then = q.then?.let { then(it, q.`as`, "$path.then") },
          exclude = q.notExists(),
        )
      }

      q.typeOf != null -> Records(
        entity = c(q.typeOf.iri ?: unsupported("$path.typeOf.iri", "missing")),
        filter = q.where?.let { filter(it, "$path.where") },
        pick = q.orderBy?.let { pick(it, "$path.orderBy") },
        alias = q.`as`,
        exposes = q.`return`?.mapIndexed { i, r -> exposed(r, "$path.return[$i]") },
        then = q.then?.let { then(it, q.`as`, "$path.then") },
        exclude = q.notExists(),
      )

      else -> unsupported(path, "clause has no cohort reference, type or boolean group")
    }
  }

  private fun rejectUnsupportedClauses(q: Query, path: String) {
    if (q.path != null) unsupported("$path.path", "path traversal is not supported")
    if (q.having != null) unsupported("$path.having", "aggregate 'having' is not supported")
    if (q.function != null) unsupported("$path.function", "functions are not supported")
    if (q.union != null) unsupported("$path.union", "union is not supported")
    if (q.each != null) unsupported("$path.each", "each is not supported")
    q.`return`?.forEachIndexed { i, r ->
      if (r.case != null || r.function != null) {
        unsupported("$path.return[$i]", "computed returns (case/function, e.g. risk scores) are not supported")
      }
    }
    if (q.`return` != null && q.typeOf == null) unsupported("$path.return", "return is only supported on records")
  }

  private fun exposed(r: Return, path: String): Exposed {
    if (r.case != null || r.function != null) unsupported(path, "computed returns (case/function) are not supported")
    return Exposed(
      property = c(r.iri ?: unsupported("$path.iri", "missing")),
      alias = r.`as` ?: unsupported("$path.as", "missing"),
      name = r.name,
      nodeRef = r.nodeRef,
    )
  }

  private fun then(t: Query, parentAlias: String?, path: String): ThenTest {
    if (t.from == null || t.from != parentAlias) unsupported("$path.from", "must refer to the parent alias '$parentAlias'")
    return ThenTest(filter(t.where ?: unsupported("$path.where", "missing"), "$path.where"), t.`as`)
  }

  private fun pick(o: OrderLimit, path: String): Pick {
    val property = o.property?.singleOrNull() ?: unsupported("$path.property", "exactly one order property is supported")
    return Pick(
      direction = property.direction ?: unsupported("$path.property[0].direction", "missing"),
      count = o.limit,
      orderBy = c(property.iri ?: unsupported("$path.property[0].iri", "missing")),
      partitionBy = o.partition?.map { c(it.iri) } ?: unsupported("$path.partition", "missing"),
    )
  }

  // ------------------------------------------------------------------ filters

  private fun filter(w: Where, path: String): Filter = when {
    w.and != null || w.or != null -> {
      if (w.and != null && w.or != null) unsupported(path, "both 'and' and 'or' on one condition")
      val (op, items, key) =
        if (w.and != null) Triple(BoolOp.ALL, w.and, "and") else Triple(BoolOp.ANY, w.or, "or")
      FilterGroup(op, items.mapIndexed { i, c -> filter(c, "$path.$key[$i]") })
    }

    w.`is` != null -> ConceptFilter(
      property = c(w.iri ?: unsupported("$path.iri", "concept condition needs a property")),
      concepts = w.`is`.mapIndexed { i, n -> conceptRef(n, "$path.is[$i]") },
      negate = w.isNot,
      label = w.valueLabel,
      shortLabel = w.shortLabel,
    )

    w.range != null -> RangeFilter(
      property = c(property(w, path)),
      from = bound(w.range.from ?: unsupported("$path.range.from", "missing"), "$path.range.from"),
      to = bound(w.range.to ?: unsupported("$path.range.to", "missing"), "$path.range.to"),
      relativeTo = w.compare?.let { relativeTo(w, it, "$path.compare") },
    )

    w.operator != null -> ValueFilter(
      property = c(property(w, path)),
      operator = w.operator,
      value = w.value,
      units = w.units?.let { units(it, "$path.units") },
      qualifier = w.qualifier?.let { qualifier(it, "$path.qualifier") },
      valueTerm = w.valueTerm,
      relativeTo = w.compare?.let { relativeTo(w, it, "$path.compare") },
    )

    else -> unsupported(path, "condition has no concept list, range, operator or boolean group")
  }

  /** The property is on the where itself, or only in compare.left when comparing to another result set. */
  private fun property(w: Where, path: String): String =
    w.iri ?: w.compare?.left?.iri ?: unsupported("$path.iri", "condition needs a property")

  private fun relativeTo(w: Where, c: Compare, path: String): RelativeTo {
    val property = property(w, path)
    if (c.left?.iri != property) unsupported("$path.left", "must refer to the condition's own property")
    val right = c.right ?: unsupported("$path.right", "missing")
    return when {
      right.parameter != null -> RelativeTo(parameter = right.parameter)
      right.nodeRef != null -> RelativeTo(alias = right.nodeRef, property = right.iri?.takeIf { it != property }?.let(::c))
      else -> unsupported("$path.right", "must be a parameter or a reference to another result set")
    }
  }

  private fun conceptRef(n: Node, path: String): ConceptRef {
    if (n.isDescendantsOf || n.isAncestorsOf || n.isChildOf || n.isChildOrSelfOf) {
      unsupported(path, "only self, descendantsOrSelfOf and memberOf entailments are supported")
    }
    return ConceptRef(
      iri = c(n.iri ?: unsupported("$path.iri", "missing")),
      name = n.name,
      match = ConceptMatch.of(n.isMemberOf, n.isDescendantsOrSelfOf),
      exclude = n.isExclude,
    )
  }

  private fun bound(v: Value, path: String) = Bound(
    operator = v.operator ?: unsupported("$path.operator", "missing"),
    value = v.value,
    units = v.units?.let { units(it, "$path.units") },
  )

  private fun units(u: TTIriRef, path: String): TimeUnit =
    TimeUnit.entries.firstOrNull { it.iri == u.iri } ?: unsupported(path, "unknown units ${u.iri}")

  private fun qualifier(q: TTIriRef, path: String): PeriodQualifier =
    PeriodQualifier.entries.firstOrNull { it.iri == q.iri } ?: unsupported(path, "unknown qualifier ${q.iri}")

  private fun c(iri: String) = IriPrefixes.compact(iri)

  private fun unsupported(path: String, reason: String): Nothing = throw UnsupportedQueryException(path, reason)

  companion object {
    fun defaultImqMapper(): ObjectMapper =
      ObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
  }
}

// ---------------------------------------------------------------------- JSON diff

data class JsonDifference(val path: String, val description: String)

/** First structural difference between two JSON trees (object key order is ignored). */
fun firstDifference(expected: JsonNode?, actual: JsonNode?, path: String = "$"): JsonDifference? {
  if (expected == actual) return null
  if (expected == null || actual == null || expected.nodeType != actual.nodeType) {
    return JsonDifference(path, "expected ${expected.preview()} but was ${actual.preview()}")
  }
  if (expected.isObject) {
    for (key in (expected.fieldNames().asSequence() + actual.fieldNames().asSequence()).toSortedSet()) {
      firstDifference(expected.get(key), actual.get(key), "$path.$key")?.let { return it }
    }
  } else if (expected.isArray) {
    if (expected.size() != actual.size()) {
      return JsonDifference(path, "expected ${expected.size()} items but was ${actual.size()}")
    }
    for (i in 0 until expected.size()) {
      firstDifference(expected.get(i), actual.get(i), "$path[$i]")?.let { return it }
    }
  }
  return JsonDifference(path, "expected ${expected.preview()} but was ${actual.preview()}")
}

private fun JsonNode?.preview(): String = this?.toString()?.take(120) ?: "<absent>"
