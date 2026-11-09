package org.endeavourhealth.imapi.queryassistant

import org.endeavourhealth.imapi.model.imq.Order
import org.endeavourhealth.imapi.model.imq.RuleAction

/**
 * Renders a [CohortSpec] as a plain-English outline, so the user (and the model) can check
 * what the query means without reading JSON. Names are used where the spec carries them
 * (see [withNames]); otherwise the prefixed IRI is shown.
 */
object SpecReadback {

  fun render(spec: CohortSpec): String {
    val out = StringBuilder()
    spec.name?.let { out.appendLine(it) }
    out.appendLine("Population: ${label(spec.baseType)}")
    spec.rules?.forEachIndexed { i, rule ->
      out.appendLine()
      out.append("Rule ${i + 1}: ")
      criterion(rule.match, out, 1, inline = true)
      out.appendLine("  If true: ${action(rule.ifTrue)}. If false: ${action(rule.ifFalse)}.")
    }
    spec.allOf?.let { items ->
      out.appendLine()
      out.appendLine("Include patients who meet all of:")
      items.forEach { criterion(it, out, 1, inline = false) }
    }
    return out.toString().trimEnd()
  }

  private fun action(a: RuleAction) = when (a) {
    RuleAction.NEXT -> "go to the next rule"
    RuleAction.SELECT -> "include in the cohort"
    RuleAction.REJECT -> "exclude"
  }

  private fun criterion(c: Criterion, out: StringBuilder, depth: Int, inline: Boolean) {
    val indent = "  ".repeat(depth)
    if (!inline) out.append(indent).append("- ")
    when (c) {
      is CohortRef -> out.appendLine("in the cohort \"${c.name ?: c.iri}\"")

      is Records -> {
        val head = if (c.exclude) "has NO ${label(c.entity)} records" else "has ${label(c.entity)} records"
        out.appendLine(head + (c.filter?.let { " where ${filter(it)}" } ?: ""))
        modifiers(c.pick, c.alias, c.then, c.exposes, out, depth + 1)
      }

      is Group -> {
        val quantifier = if (c.operator == BoolOp.ALL) "all of" else "any of"
        out.appendLine(if (c.exclude) "does NOT meet $quantifier:" else "meets $quantifier:")
        c.items.forEach { criterion(it, out, depth + 1, inline = false) }
        c.filter?.let { out.append("  ".repeat(depth + 1)).appendLine("where ${filter(it)}") }
        modifiers(c.pick, c.alias, c.then, null, out, depth + 1)
      }
    }
  }

  private fun modifiers(pick: Pick?, alias: String?, then: ThenTest?, exposes: List<Exposed>?, out: StringBuilder, depth: Int) {
    val indent = "  ".repeat(depth)
    pick?.let { out.append(indent).appendLine("take the ${pickPhrase(it)}" + (alias?.let { a -> " (called \"$a\")" } ?: "")) }
    if (pick == null && alias != null) out.append(indent).appendLine("(called \"$alias\")")
    then?.let { out.append(indent).appendLine("and that must have ${filter(it.filter)}") }
    exposes?.forEach { out.append(indent).appendLine("keeping its ${label(it.property)} as \"${it.alias}\"") }
  }

  private fun pickPhrase(p: Pick): String {
    val isDate = localName(IriPrefixes.expand(p.orderBy)).contains("date", ignoreCase = true)
    val word = when {
      p.direction == Order.descending && isDate -> "latest"
      p.direction == Order.descending -> "highest"
      isDate -> "earliest"
      else -> "lowest"
    }
    val what = if (p.count == 1) "record" else "${p.count} records"
    val per = p.partitionBy.joinToString(" and ") { label(it) }
    return "$word $what per $per" + if (isDate) "" else " by ${label(p.orderBy)}"
  }

  // ------------------------------------------------------------------ filters (single line)

  fun filter(f: Filter): String = when (f) {
    is FilterGroup -> {
      val parts = f.items.map { if (it is FilterGroup) "(${filter(it)})" else filter(it) }
      parts.joinToString(if (f.operator == BoolOp.ALL) " and " else " or ")
    }

    is ConceptFilter -> {
      val included = f.concepts.filter { !it.exclude }.joinToString(", ") { concept(it) }
      val excluded = f.concepts.filter { it.exclude }.joinToString(", ") { concept(it) }
      val verb = if (f.negate) "is not one of" else "is one of"
      "${label(f.property)} $verb [$included]" + if (excluded.isNotEmpty()) " excluding [$excluded]" else ""
    }

    is ValueFilter -> {
      val qualifier = listOfNotNull(f.valueTerm, f.qualifier?.label?.lowercase()).joinToString(" ")
      val amount = amount(f.value, f.units, f.relativeTo, f.property)
      val subject = label(f.property)
      when {
        qualifier.isNotEmpty() -> "$subject ${f.operator.value} $qualifier${f.value?.let { " ($it)" } ?: ""}${relativeSuffix(f.relativeTo, f.property)}"
        else -> "$subject ${f.operator.value} $amount"
      }
    }

    is RangeFilter ->
      "${label(f.property)} ${f.from.operator.value} ${amount(f.from.value, f.from.units, f.relativeTo, f.property)}" +
        " and ${f.to.operator.value} ${amount(f.to.value, f.to.units, f.relativeTo, f.property)}"
  }

  /** "-12 MONTHS relative to $searchDate" reads as "12 months before the search date". */
  private fun amount(value: String?, units: TimeUnit?, relativeTo: RelativeTo?, property: String): String {
    val reference = relativeTo?.let { reference(it, property) }
    if (value == null) return reference ?: "(no value)"
    val unitLabel = units?.label?.lowercase()
    if (reference == null) return listOfNotNull(value, unitLabel).joinToString(" ")
    val number = value.toDoubleOrNull()
    return when {
      number == null -> "$value ${unitLabel ?: ""} relative to $reference".replace("  ", " ")
      number == 0.0 -> reference
      else -> {
        val magnitude = value.removePrefix("-").removePrefix("+")
        val direction = if (number < 0) "before" else "after"
        listOfNotNull(magnitude, unitLabel, direction, reference).joinToString(" ")
      }
    }
  }

  private fun relativeSuffix(relativeTo: RelativeTo?, property: String) =
    relativeTo?.let { " relative to ${reference(it, property)}" } ?: ""

  /** [property] is the filter's own property, which an alias reference compares against by default. */
  private fun reference(r: RelativeTo, property: String): String = when {
    r.parameter == "\$searchDate" -> "the search date"
    r.parameter != null -> r.parameter
    else -> "the ${label(r.property ?: property)} of \"${r.alias}\""
  }

  private fun concept(c: ConceptRef): String {
    val name = c.name ?: c.iri
    return when (c.match) {
      ConceptMatch.SELF -> name
      ConceptMatch.DESCENDANTS_OR_SELF -> "$name (and subtypes)"
      ConceptMatch.SET_MEMBERS -> "$name (set)"
      ConceptMatch.SET_MEMBERS_DESCENDANTS_OR_SELF -> "$name (set, and subtypes)"
    }
  }

  /** im:effectiveDate -> "effective date", im:ClinicalEntry -> "clinical entry". */
  fun label(iri: String): String {
    val local = localName(IriPrefixes.expand(iri))
    return local.replace(Regex("(?<=[a-z0-9])(?=[A-Z])"), " ").lowercase()
  }
}
