package org.endeavourhealth.imapi.queryassistant

/** An IRI used somewhere in a spec: the full IRI and a JSON-path style location for error messages. */
data class IriUse(val iri: String, val path: String)

/**
 * Every entity/property IRI the spec refers to (expanded), excluding the spec's own [CohortSpec.iri].
 * Used to check that a model-written spec only uses IRIs that were looked up and exist.
 */
fun CohortSpec.referencedIris(): List<IriUse> {
  val out = mutableListOf<IriUse>()
  fun add(iri: String, path: String) {
    out += IriUse(IriPrefixes.expand(iri), path)
  }

  fun filter(f: Filter, path: String) {
    when (f) {
      is FilterGroup -> f.items.forEachIndexed { i, item -> filter(item, "$path.items[$i]") }
      is ConceptFilter -> {
        add(f.property, "$path.property")
        f.concepts.forEachIndexed { i, c -> add(c.iri, "$path.concepts[$i].iri") }
      }
      is ValueFilter -> {
        add(f.property, "$path.property")
        f.relativeTo?.property?.let { add(it, "$path.relativeTo.property") }
      }
      is RangeFilter -> {
        add(f.property, "$path.property")
        f.relativeTo?.property?.let { add(it, "$path.relativeTo.property") }
      }
    }
  }

  fun modifiers(pick: Pick?, then: ThenTest?, path: String) {
    pick?.let { p ->
      add(p.orderBy, "$path.pick.orderBy")
      p.partitionBy.forEachIndexed { i, iri -> add(iri, "$path.pick.partitionBy[$i]") }
    }
    then?.let { filter(it.filter, "$path.then.filter") }
  }

  fun criterion(c: Criterion, path: String) {
    when (c) {
      is CohortRef -> add(c.iri, "$path.iri")
      is Records -> {
        add(c.entity, "$path.entity")
        c.filter?.let { filter(it, "$path.filter") }
        c.exposes?.forEachIndexed { i, e -> add(e.property, "$path.exposes[$i].property") }
        modifiers(c.pick, c.then, path)
      }
      is Group -> {
        c.items.forEachIndexed { i, item -> criterion(item, "$path.items[$i]") }
        c.filter?.let { filter(it, "$path.filter") }
        modifiers(c.pick, c.then, path)
      }
    }
  }

  add(baseType, "$.baseType")
  rules?.forEachIndexed { i, r -> criterion(r.match, "$.rules[$i].match") }
  allOf?.forEachIndexed { i, c -> criterion(c, "$.allOf[$i]") }
  return out
}

/** Fills missing cohort and concept names from [names] (keyed by full IRI), so the model need not write them. */
fun CohortSpec.withNames(names: Map<String, String>): CohortSpec {
  fun name(iri: String, current: String?) = current ?: names[IriPrefixes.expand(iri)]

  fun filter(f: Filter): Filter = when (f) {
    is FilterGroup -> f.copy(items = f.items.map(::filter))
    is ConceptFilter -> f.copy(concepts = f.concepts.map { it.copy(name = name(it.iri, it.name)) })
    is ValueFilter, is RangeFilter -> f
  }

  fun then(t: ThenTest?) = t?.copy(filter = filter(t.filter))

  fun criterion(c: Criterion): Criterion = when (c) {
    is CohortRef -> c.copy(name = name(c.iri, c.name))
    is Records -> c.copy(filter = c.filter?.let(::filter), then = then(c.then))
    is Group -> c.copy(items = c.items.map(::criterion), filter = c.filter?.let(::filter), then = then(c.then))
  }

  return copy(
    rules = rules?.map { it.copy(match = criterion(it.match)) },
    allOf = allOf?.map(::criterion),
  )
}
