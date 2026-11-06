package org.endeavourhealth.imapi.queryassistant

import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.tripletree.TTIriRef

class QuerySpecException(val errors: List<String>) :
  RuntimeException("Invalid query spec:\n" + errors.joinToString("\n") { " - $it" })

/**
 * Deterministically compiles a [CohortSpec] into an IMQ [Query].
 * All validation problems are collected (with a JSON-path style location) and thrown together,
 * so a caller such as an LLM agent can fix them in one pass.
 */
class QuerySpecCompiler {

  fun compile(spec: CohortSpec): Query {
    val ctx = Context(explicitAliases(spec))
    val query = Query().setName(spec.name).setDescription(spec.description)
    spec.iri?.let { query.setIri(x(it)) }
    query.setTypeOf(Node().setIri(x(spec.baseType)))

    when {
      spec.rules != null && spec.allOf != null -> ctx.error("$", "set either 'rules' or 'allOf', not both")
      spec.rules != null -> {
        if (spec.rules.isEmpty()) ctx.error("$.rules", "must not be empty")
        query.setRule(spec.rules.mapIndexed { i, rule ->
          criterion(rule.match, "$.rules[$i].match", ctx).setIfTrue(rule.ifTrue).setIfFalse(rule.ifFalse)
        })
      }
      spec.allOf != null -> {
        if (spec.allOf.isEmpty()) ctx.error("$.allOf", "must not be empty")
        query.setAnd(spec.allOf.mapIndexed { i, c -> criterion(c, "$.allOf[$i]", ctx) })
      }
      else -> ctx.error("$", "one of 'rules' or 'allOf' is required")
    }

    ctx.checkReferences()
    if (ctx.errors.isNotEmpty()) throw QuerySpecException(ctx.errors)
    return query
  }

  // ------------------------------------------------------------------ criteria

  private fun criterion(c: Criterion, path: String, ctx: Context): Query = when (c) {
    is CohortRef -> Query().setIs(Node().setIri(x(c.iri)).setName(c.name).setIsCohort(true))

    is Records -> {
      val q = Query().setTypeOf(Node().setIri(x(c.entity)))
      c.filter?.let { q.setWhere(filter(it, "$path.filter", ctx)) }
      c.exposes?.let { exposes ->
        q.setReturn(exposes.map { e ->
          Return().setIri(x(e.property)).setAs(e.alias).setName(e.name).setNodeRef(e.nodeRef)
        })
      }
      applyModifiers(q, c.pick, c.alias, c.then, c.exclude, path, ctx)
    }

    is Group -> {
      if (c.items.isEmpty()) ctx.error("$path.items", "must not be empty")
      val items = c.items.mapIndexed { i, item -> criterion(item, "$path.items[$i]", ctx) }
      val q = Query()
      if (c.operator == BoolOp.ALL) q.setAnd(items) else q.setOr(items)
      c.filter?.let { q.setWhere(filter(it, "$path.filter", ctx)) }
      applyModifiers(q, c.pick, c.alias, c.then, c.exclude, path, ctx)
    }
  }

  private fun applyModifiers(
    q: Query, pick: Pick?, alias: String?, then: ThenTest?, exclude: Boolean, path: String, ctx: Context,
  ): Query {
    pick?.let { q.setOrderBy(orderBy(it, "$path.pick", ctx)) }
    val effectiveAlias = alias ?: if (then != null) ctx.generateAlias() else null
    effectiveAlias?.let { q.setAs(it); ctx.define(it, path) }
    then?.let {
      if (pick == null) ctx.error("$path.then", "'then' tests picked records, so 'pick' is required")
      val thenQuery = Query().setFrom(effectiveAlias).setWhere(filter(it.filter, "$path.then.filter", ctx))
      it.alias?.let { a -> thenQuery.setAs(a); ctx.define(a, "$path.then") }
      q.setThen(thenQuery)
    }
    if (exclude) q.setNotExists(true)
    return q
  }

  private fun orderBy(pick: Pick, path: String, ctx: Context): OrderLimit {
    if (pick.count < 1) ctx.error("$path.count", "must be >= 1")
    if (pick.partitionBy.isEmpty()) ctx.error("$path.partitionBy", "must not be empty")
    val order = OrderLimit().setLimit(pick.count)
    pick.partitionBy.map(::x).forEach { order.addPartition(IriLD().setIri(it).setName(localName(it))) }
    order.addProperty(OrderDirection().setIri(x(pick.orderBy)).setDirection(pick.direction))
    return order
  }

  // ------------------------------------------------------------------ filters

  private fun filter(f: Filter, path: String, ctx: Context): Where = when (f) {
    is FilterGroup -> {
      if (f.items.isEmpty()) ctx.error("$path.items", "must not be empty")
      val items = f.items.mapIndexed { i, item -> filter(item, "$path.items[$i]", ctx) }
      if (f.operator == BoolOp.ALL) Where().setAnd(items) else Where().setOr(items)
    }

    is ConceptFilter -> {
      if (f.concepts.isEmpty()) ctx.error("$path.concepts", "must not be empty")
      Where().setIri(x(f.property))
        .setIs(f.concepts.map { conceptNode(it) })
        .setNot(f.negate)
        .setValueLabel(f.label)
        .setShortLabel(f.shortLabel)
    }

    is ValueFilter -> {
      val property = x(f.property)
      val w = propertyWhere(property, f.relativeTo)
      w.setOperator(f.operator).setValue(f.value)
      f.units?.let { w.setUnits(units(it)) }
      f.qualifier?.let { w.setQualifier(TTIriRef(it.iri, it.label)) }
      f.valueTerm?.let { w.setValueTerm(it) }
      f.relativeTo?.let { w.setCompare(compare(property, it, "$path.relativeTo", ctx)) }
      w
    }

    is RangeFilter -> {
      val property = x(f.property)
      val w = propertyWhere(property, f.relativeTo)
      w.setRange(Range().setFrom(bound(f.from)).setTo(bound(f.to)))
      f.relativeTo?.let { w.setCompare(compare(property, it, "$path.relativeTo", ctx)) }
      w
    }
  }

  /** When comparing to another result set, IMQ carries the property only in compare.left. [property] is expanded. */
  private fun propertyWhere(property: String, relativeTo: RelativeTo?): Where =
    if (relativeTo?.alias != null) Where() else Where().setIri(property)

  private fun compare(property: String, rel: RelativeTo, path: String, ctx: Context): Compare {
    val left = ValueSource().setIri(property)
    return when {
      rel.parameter != null && rel.alias != null -> {
        ctx.error(path, "set either 'parameter' or 'alias', not both"); Compare()
      }
      rel.parameter != null -> {
        if (rel.property != null) ctx.error("$path.property", "only valid together with 'alias'")
        Compare().setLeft(left).setRight(ValueSource().setParameter(rel.parameter))
      }
      rel.alias != null -> {
        ctx.reference(rel.alias, path)
        val rightProperty = rel.property?.let(::x) ?: property
        Compare().setLeft(left).setRight(
          ValueSource().setIri(rightProperty).setNodeRef(rel.alias).setPropertyRef(localName(rightProperty))
        )
      }
      else -> {
        ctx.error(path, "one of 'parameter' or 'alias' is required"); Compare()
      }
    }
  }

  private fun conceptNode(c: ConceptRef): Node {
    val node = Node().setIri(x(c.iri)).setName(c.name)
    if (c.match.memberOf) node.setMemberOf(true)
    if (c.match.descendantsOrSelfOf) node.setDescendantsOrSelfOf(true)
    if (c.exclude) node.setExclude(true)
    return node
  }

  private fun bound(b: Bound): Value {
    val v = Value().setOperator(b.operator).setValue(b.value)
    b.units?.let { v.setUnits(units(it)) }
    return v
  }

  private fun units(u: TimeUnit) = TTIriRef(u.iri, u.label)

  private fun x(iri: String) = IriPrefixes.expand(iri)

  // ------------------------------------------------------------------ context

  /** Aliases written in the spec, so generated ones can avoid them regardless of position. */
  private fun explicitAliases(spec: CohortSpec): Set<String> {
    val out = mutableSetOf<String>()
    fun visit(c: Criterion) {
      when (c) {
        is CohortRef -> {}
        is Records -> { c.alias?.let(out::add); c.then?.alias?.let(out::add) }
        is Group -> { c.alias?.let(out::add); c.then?.alias?.let(out::add); c.items.forEach(::visit) }
      }
    }
    spec.rules?.forEach { visit(it.match) }
    spec.allOf?.forEach(::visit)
    return out
  }

  private class Context(private val reserved: Set<String>) {
    val errors = mutableListOf<String>()
    private val defined = mutableMapOf<String, String>()
    private val referenced = mutableListOf<Pair<String, String>>()
    private var aliasCounter = 0

    fun error(path: String, message: String) {
      errors += "$path: $message"
    }

    fun define(alias: String, path: String) {
      val previous = defined.putIfAbsent(alias, path)
      if (previous != null) error(path, "alias '$alias' is already defined at $previous")
    }

    fun reference(alias: String, path: String) {
      referenced += alias to path
    }

    fun generateAlias(): String {
      var alias: String
      do alias = "match_${++aliasCounter}" while (alias in defined || alias in reserved)
      return alias
    }

    fun checkReferences() {
      referenced.filter { (alias, _) -> alias !in defined }
        .forEach { (alias, path) -> error(path, "alias '$alias' is not defined anywhere in the query") }
    }
  }
}

internal fun localName(iri: String): String =
  iri.substringAfterLast('#', iri.substringAfterLast('/'))
