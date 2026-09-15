package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.model.imq.Bool
import org.endeavourhealth.imapi.model.sql.MySQLBoolWhere
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsNullWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.jooq.CommonTableExpression
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.Select
import org.jooq.Table
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType

/**
 * Phase 2 of the IMQ -> SQL pipeline: a mechanical, mapping-free renderer of the IR produced by
 * [IMQPreparer] into MySQL SQL via jOOQ.
 *
 * No IMQ or IMQtoMYSQL.json decisions are made here - every table, alias, join key and
 * condition has already been resolved by phase 1. This class only knows how to turn that
 * already-resolved shape into jOOQ's Condition/Table/Field/CommonTableExpression objects.
 *
 * MySQL-specific expressions that phase 1 already rendered as raw SQL text (date arithmetic in
 * MySQLCompareWhere, concept-hierarchy OR-blocks in MySQLPropertyIsWhere, window functions,
 * CASE expressions, JSON_OBJECT) are passed through via jOOQ's plain-SQL escape hatch
 * (DSL.field/DSL.condition on a raw string) rather than reconstructed as typed jOOQ
 * expressions - jOOQ has no typed equivalent for e.g. QUARTER()/fiscal-year arithmetic.
 */
class IRToJooqConverter {
  private val create: DSLContext = DSL.using(SQLDialect.MYSQL)

  /** Dataset (column-group) queries produce several statements, joined the same way IMQtoSQLConverterKotlin does. */
  fun toSql(queries: List<MySQLQuery>): String =
    queries.joinToString(separator = "\n----------------------------------------\n", transform = ::toSql)

  fun toSql(query: MySQLQuery): String {
    val ctes = query.withs.map(::toCte)

    var from: Table<Record> = DSL.table(DSL.name(query.withs.last().alias))
    for (join in query.joins) from = applyJoin(from, join)

    val select: Select<Record> = create
      .with(ctes)
      .select(query.selects.map(::toField))
      .from(from)

    val rendered = select.sql
    return if (query.insert != null) "INSERT INTO ${query.insert}\n$rendered;" else "$rendered;"
  }

  /** Builds the per-step "is the patient present at this CTE" diagnostic script. */
  fun toPatientTraceSql(trace: IMQPreparer.PatientTraceIR): String {
    val ctes = trace.withs.map(::toCte)
    val queryIriLiteral: Field<String> = DSL.inline(trace.queryIri)
    val patientLiteral: Field<String> = DSL.inline(trace.patientId)

    fun rowFields(index: Int, with: MySQLWith): List<Field<*>> {
      val keyField = with.entityKeyField
      val patientFound: Field<*> = if (trace.isPatientRooted && keyField != null) {
        DSL.field(
          DSL.exists(
            create.selectOne()
              .from(DSL.table(DSL.name(with.alias)))
              .where(DSL.field(DSL.name(with.alias, keyField)).eq(patientLiteral))
          )
        )
      } else {
        DSL.inline(null, SQLDataType.VARCHAR)
      }
      return listOf(
        queryIriLiteral.`as`("query_iri"),
        patientLiteral.`as`("patient_id"),
        DSL.inline(index).`as`("step_no"),
        DSL.inline(with.alias).`as`("cte_name"),
        patientFound.`as`("patient_found"),
      )
    }

    var statement: Select<Record> = create.with(ctes).select(rowFields(0, trace.withs[0]))
    for (i in 1 until trace.withs.size) {
      statement = statement.unionAll(create.select(rowFields(i, trace.withs[i])))
    }

    return "INSERT INTO dataset.patient_exists (query_iri, patient_id, step_no, cte_name, patient_found)\n${statement.sql};"
  }

  private fun toCte(with: MySQLWith): CommonTableExpression<Record> =
    DSL.name(with.alias).`as`(toSelect(with))

  private fun toSelect(with: MySQLWith): Select<Record> {
    if (with.groupByColumns.isNotEmpty() || with.havingClause != null || with.orderBy != null) {
      throw UnsupportedOperationException(
        "GROUP BY / HAVING / with-level ORDER BY are never produced by IMQPreparer today and are not supported by this renderer"
      )
    }

    if (with.unionWiths.isNotEmpty()) {
      val parts = with.unionWiths.map(::toSelect)
      return parts.reduce { acc, next -> if (with.unionAll) acc.unionAll(next) else acc.union(next) }
    }

    val baseFrom: Table<Record> = if (with.subQuery != null) {
      toSelect(with.subQuery!!).asTable(with.fromAlias ?: "sq")
    } else {
      var t: Table<Record> = DSL.table(with.table.table)
      val alias = with.table.alias
      if (alias != null && alias != with.table.table) t = t.`as`(alias)
      t
    }

    var from = baseFrom
    for (join in with.joins) from = applyJoin(from, join)

    val condition = combineWheres(with.wheres, with.whereBool)
    val select = create.selectDistinct(with.selects.map(::toField)).from(from)
    return if (condition != null) select.where(condition) else select
  }

  private fun applyJoin(from: Table<Record>, join: MySQLJoin): Table<Record> {
    val target: Table<Record> = if (join.reference == true) {
      DSL.table(DSL.name(join.tableToAlias ?: join.tableTo))
    } else {
      DSL.table(join.tableTo).let { t -> join.tableToAlias?.let(t::`as`) ?: t }
    }

    // Raw text, not DSL.name(a, b): a schema-qualified table like "dataset.concept_tct" would
    // otherwise be quoted as one literal identifier containing a dot instead of schema.table.
    val fromRef: Field<Any> = DSL.field("${join.tableFrom}.${join.fromProperty}")
    val toRef: Field<Any> = DSL.field("${join.tableToAlias ?: join.tableTo}.${join.toProperty}")
    var condition: Condition = fromRef.eq(toRef)
    join.wheres.forEach { condition = condition.and(toCondition(it)) }

    return when (join.join) {
      "LEFT JOIN" -> from.leftJoin(target).on(condition)
      "RIGHT JOIN" -> from.rightJoin(target).on(condition)
      else -> from.join(target).on(condition)
    }
  }

  private fun toField(select: MySQLSelect): Field<*> {
    val base = DSL.field(select.name)
    return select.alias?.let { base.`as`(it) } ?: base
  }

  private fun combineWheres(wheres: List<MySQLWhere>, bool: Bool): Condition? {
    if (wheres.isEmpty()) return null
    val conditions = wheres.map(::toCondition)
    return if (bool == Bool.or) conditions.reduce(Condition::or) else conditions.reduce(Condition::and)
  }

  private fun toCondition(where: MySQLWhere): Condition {
    val parts = mutableListOf<Condition>()
    leafCondition(where)?.let { parts.add(it) }
    where.and?.takeIf { it.isNotEmpty() }?.let { list -> parts.add(list.map(::toCondition).reduce(Condition::and)) }
    where.or?.takeIf { it.isNotEmpty() }?.let { list -> parts.add(list.map(::toCondition).reduce(Condition::or)) }

    return when (parts.size) {
      0 -> DSL.noCondition()
      1 -> parts.first()
      else -> parts.reduce(Condition::and)
    }
  }

  private fun leafCondition(where: MySQLWhere): Condition? = when (where) {
    is MySQLBoolWhere -> null
    is MySQLPropertyValueWhere -> {
      val field: Field<Any> = DSL.field(qualifiedField(where.table, where.property))
      val cond = comparisonCondition(field, where.operator, where.value)
      if (where.not == true) cond.not() else cond
    }

    is MySQLPropertyIsNullWhere -> {
      val field: Field<Any> = DSL.field(qualifiedField(where.table, where.property))
      if (where.not == true) field.isNotNull else field.isNull
    }
    // MySQL-specific date/concept-hierarchy templating phase 1 already resolved to raw SQL -
    // no typed jOOQ equivalent, so this is the plain-SQL escape hatch.
    else -> DSL.condition(where.baseSql())
  }

  private fun comparisonCondition(field: Field<Any>, operator: String, value: String): Condition {
    val literal: Field<Any> = DSL.field(value)
    return when (operator) {
      "=" -> field.eq(literal)
      ">" -> field.gt(literal)
      ">=" -> field.ge(literal)
      "<" -> field.lt(literal)
      "<=" -> field.le(literal)
      "IS" -> if (value.trim().equals("NULL", ignoreCase = true)) field.isNull else DSL.condition("$field IS $value")
      "IN" -> DSL.condition("$field IN $value")
      "!=" -> field.ne(literal)
      else -> DSL.condition("$field $operator $value")
    }
  }

  // Raw text, not DSL.name(a, b): a dotted/schema-qualified table (e.g. "dataset.concept_tct")
  // would otherwise be quoted as one literal identifier instead of schema.table. A property
  // that's already a full expression (contains "(" or ".") is left unqualified, matching how
  // IMQPreparer's own sqlTemplate/getTableAndField logic treats pre-resolved expressions
  // (e.g. the "age" field's TIMESTAMPDIFF(...) mapping).
  private fun qualifiedField(table: String?, property: String): String =
    if (table != null && !property.contains("(") && !property.contains(".")) "$table.$property" else property
}
