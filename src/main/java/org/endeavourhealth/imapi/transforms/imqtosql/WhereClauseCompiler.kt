package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Bool
import org.endeavourhealth.imapi.model.imq.Value
import org.endeavourhealth.imapi.model.imq.ValueSource
import org.endeavourhealth.imapi.model.imq.Where
import org.endeavourhealth.imapi.model.sql.Field
import org.endeavourhealth.imapi.model.sql.MySQLBoolWhere
import org.endeavourhealth.imapi.model.sql.MySQLCompareWhere
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsNullWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.model.sql.TABLE_PLACEHOLDER
import org.endeavourhealth.imapi.model.sql.qualifyField
import org.endeavourhealth.imapi.model.sql.quoteIdentifier
import org.endeavourhealth.imapi.vocabulary.IM

/**
 * Compiles IMQ where clauses into SQL where conditions, adding any joins the conditions require to the CTE.
 */
internal class WhereClauseCompiler(
  private val ctx: ConversionContext,
  private val getDataModelFromKeepAs: (String?) -> String?,
) {
  private val queryTypeOfTable: Table get() = ctx.queryTypeOfTable
  private val aliases: AliasAllocator get() = ctx.aliases
  private val keepAsMap: Map<String, MySQLWith> get() = ctx.keepAsMap

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table =
    ctx.getTable(typeIri, propertyIri)

  fun addWheresRecursively(
    where: Where,
    with: MySQLWith,
    variableToTableMap: HashMap<String, Table>,
    parentWhere: MySQLWhere? = null,
    bool: Bool? = null,
    table: Table? = null,
  ) {
    val children = where.and ?: where.or
    if (children == null) {
      attach(getMySQLWhereFromWhere(where, variableToTableMap, with, table), with, parentWhere, bool)
      return
    }

    val boolWhere = MySQLBoolWhere()
    attach(boolWhere, with, parentWhere, bool)
    val childBool = if (where.and != null) Bool.and else Bool.or
    children.forEach { addWheresRecursively(it, with, variableToTableMap, boolWhere, childBool, table) }
  }

  /** Adds [child] to its parent's `and`/`or` list, or to the CTE's top-level wheres when there is no enclosing group. */
  private fun attach(child: MySQLWhere, with: MySQLWith, parent: MySQLWhere?, bool: Bool?) {
    when (bool) {
      Bool.and -> parent?.let { p -> (p.and ?: mutableListOf<MySQLWhere>().also { p.and = it }).add(child) }
      Bool.or -> parent?.let { p -> (p.or ?: mutableListOf<MySQLWhere>().also { p.or = it }).add(child) }
      else -> with.wheres.add(child)
    }
  }

  private fun resolveUnit(where: Where, rangeEndUnitsIri: String? = null): SqlLiteralUtils.UnitSpec? =
    (where.units?.iri ?: rangeEndUnitsIri ?: where.qualifier?.iri)?.let { SqlLiteralUtils.getUnitNameAndType(it) }

  private fun getMySQLWhereFromWhere(
    where: Where,
    variableToTableMap: HashMap<String, Table>,
    with: MySQLWith,
    table: Table? = null,
  ): MySQLWhere {
    val (resolvedTable, resolvedField) = if (table != null && where.iri != null)
      table to getPropertyNameByTableAndPropertyIri(table, where.iri).field
    else
      getTableAndField(with, where, variableToTableMap)

    val currentTable = table ?: resolvedTable
    val field = where.propertyRef ?: resolvedField
    val tableRef = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table

    return when {
      where.`is` != null -> {
        val (conceptJoins, conceptAlias) = addWhereConceptJoin(currentTable, field, with)
        for (join in conceptJoins) {
          if (!with.joins.contains(join)) with.joins.add(join)
        }
        MySQLPropertyIsWhere(
          field,
          where.`is`,
          "=",
          not = where.isNot,
          table = tableRef,
          conceptTable = conceptAlias,
        )
      }

      where.range != null -> {
        val from = where.range.from ?: throw SQLConversionException("Range on $field has no 'from' bound")
        val to = where.range.to ?: throw SQLConversionException("Range on $field has no 'to' bound")
        // a range of literal values needs no relative-to lookup; otherwise both bounds compare against it
        val right = if (where.compare == null && from.value != null) null else getValueFromRelativeTo(where, variableToTableMap)
        MySQLBoolWhere(
          and = mutableListOf(
            getRangeBoundWhere(where, from, field, tableRef, right),
            getRangeBoundWhere(where, to, field, tableRef, right),
          )
        )
      }

      where.isNull -> MySQLPropertyIsNullWhere(field, not = where.isNot, table = tableRef)

      where.compare != null -> {
        val unit = resolveUnit(where)
        val compareValue = getValueFromRelativeTo(where, variableToTableMap)
        val compareTableRef = currentTable.alias ?: currentTable.table

        if (where.value != null)
          MySQLCompareWhere(
            property = field,
            operator = where.operator.value,
            right = compareValue,
            value = SqlLiteralUtils.requireNumber(where.value),
            units = unit?.unitName,
            qualifier = unit?.qualifierName,
            not = where.isNot,
            table = compareTableRef,
          ) else MySQLPropertyValueWhere(
          property = field,
          operator = where.operator.value,
          value = compareValue,
          qualifier = unit?.qualifierName,
          not = where.isNot,
          table = compareTableRef,
        )
      }

      else -> MySQLPropertyValueWhere(
        field,
        where.operator.value,
        SqlLiteralUtils.toSqlLiteral(where.value),
        not = where.isNot,
        table = tableRef,
      )
    }
  }

  /** One end of a range: a plain comparison with a literal, or a comparison against [right] (the relative-to value). */
  private fun getRangeBoundWhere(where: Where, bound: Value, field: String, tableRef: String, right: String?): MySQLWhere {
    if (right == null) {
      return MySQLPropertyValueWhere(
        property = field,
        operator = bound.operator.value,
        value = SqlLiteralUtils.toSqlLiteral(bound.value ?: throw SQLConversionException("Range on $field has no value")),
        table = tableRef
      )
    }
    val unit = resolveUnit(where, bound.units?.iri)
    return MySQLCompareWhere(
      property = field,
      operator = bound.operator.value,
      right = right,
      value = bound.value?.let { SqlLiteralUtils.toSqlLiteral(it) } ?: "",
      table = tableRef,
      units = unit?.unitName,
      qualifier = unit?.qualifierName,
    )
  }

  private fun getTableAndField(
    with: MySQLWith,
    where: Where,
    variableToTableMap: HashMap<String, Table>
  ): Pair<Table, String> {
    val nodeRef = where.compare?.nodeRef ?: where.compare.nodeRef
    val whereIri = where.iri ?: where.iri

    if (whereIri == null) throw SQLConversionException("No property found for where $where")
    val currentTable =
      if (nodeRef != null) variableToTableMap[nodeRef] else with.table
    if (currentTable == null) throw SQLConversionException("No table found: $nodeRef")

    if (nodeRef == null && with.isCarrierAliased) {
      val alias = whereIri.substringAfterLast('#')
      val field = if (with.fromAlias != null) "${with.fromAlias}.$alias" else alias
      return currentTable to field
    }

    val mappedField = getPropertyNameByTableAndPropertyIri(currentTable, whereIri)
    if (mappedField.join != null) {
      val fromRef = (if (nodeRef == null) with.fromAlias else null) ?: currentTable.alias ?: currentTable.table
      return addFunctionalPropertyJoin(with, currentTable, fromRef, whereIri, mappedField) to mappedField.field
    }

    val rawField = mappedField.field
    if (rawField.isEmpty()) {
      // the property lives on the query's own type table, so join to it (once) and qualify by that table
      val (fk, pk) = with.table.foreignKeyTo(queryTypeOfTable)
      val join = MySQLJoin(
        join = "JOIN",
        with.table.table,
        queryTypeOfTable.table,
        fromProperty = fk,
        toProperty = pk
      )
      if (!with.joins.contains(join)) with.joins.add(join)
      return queryTypeOfTable to qualifyField(queryTypeOfTable.table, rawField)
    }

    val field =
      if (with.fromAlias != null && nodeRef == null && (rawField.contains(TABLE_PLACEHOLDER) || !rawField.contains("."))) {
        qualifyField(with.fromAlias, rawField)
      } else {
        rawField
      }
    return currentTable to field
  }

  /**
   * Joins the table holding a functional property's column onto the CTE (once per alias) and returns that
   * table, aliased, so conditions on the property are qualified by the join alias.
   */
  fun addFunctionalPropertyJoin(
    with: MySQLWith,
    fromTable: Table,
    fromRef: String,
    propertyIri: String,
    field: Field,
  ): Table {
    val fieldJoin = field.join ?: throw SQLConversionException("Property $propertyIri has no join")
    val joinTable = getTableFromTypeAndProperty(fieldJoin.dataModel, null)
    val alias = functionalJoinAlias(propertyIri, field)
    joinTable.alias = alias
    if (with.joins.none { it.tableToAlias == alias }) {
      val join = fromTable.getJoinCondition(
        tableFromAlias = fromRef,
        tableTo = joinTable,
        tableToAlias = alias,
      )
      fieldJoin.condition?.let {
        join.wheres.add(MySQLPropertyValueWhere(it.field, "=", it.value, table = alias))
      }
      with.joins.add(join)
    }
    return joinTable
  }

  /** The SQL reference for the value a `compare` is made against: a parameter, a literal property, or a node's property. */
  private fun getValueFromRelativeTo(where: Where, nodeToTableMap: HashMap<String, Table>): String {
    val right = where.compare

    right.parameter?.let { return it }

    val nodeRef = right.nodeRef
      ?: return right.propertyRef
        ?: throw SQLConversionException("No property found for relativeTo ${where.compare}")

    (keepAsMap[nodeRef] ?: keepAsMap[aliases.cteNormalise(nodeRef)])?.let { keptWith ->
      return qualifyField(keptWith.alias, getKeptWithField(keptWith, right, nodeRef))
    }

    val nodeRefTable = nodeToTableMap[nodeRef]
    val property = if (nodeRefTable != null) {
      getRelativeProperty(nodeRefTable, right)
    } else {
      getDataModelFromKeepAs(nodeRef)?.let { getRelativeProperty(getTableFromTypeAndProperty(it, null), right) }
    }
    if (property.isNullOrEmpty()) throw SQLConversionException("No property found for relativeTo $nodeRef")

    return if (nodeRefTable != null) qualifyField(aliases.sanitiseAlias(nodeRef), property)
    else qualifyField(quoteIdentifier(nodeRef), property)
  }

  /** The column a kept (named) CTE exposes for [right]'s property. */
  private fun getKeptWithField(keptWith: MySQLWith, right: ValueSource, nodeRef: String): String {
    if (keptWith.isCarrierAliased) {
      return right.propertyRef ?: right.iri?.substringAfterLast('#')
      ?: throw SQLConversionException("No property found for relativeTo $nodeRef")
    }
    val iri = right.iri
    if (iri != null) {
      val keptField = getPropertyNameByTableAndPropertyIri(keptWith.table, iri)
      // functional properties are exposed by the referenced CTE under the property name
      return if (keptField.join != null) iri.substringAfterLast('#') else keptField.field
    }
    return right.propertyRef ?: throw SQLConversionException("No property found for relativeTo $nodeRef")
  }

  private fun getRelativeProperty(table: Table, right: ValueSource): String? =
    right.iri?.let { getPropertyNameByTableAndPropertyIri(table, it).field } ?: right.propertyRef

  private fun addWhereConceptJoin(
    table: Table,
    fromField: String?,
    with: MySQLWith
  ): Pair<List<MySQLJoin>, String> {
    val conceptTCT = getTableFromTypeAndProperty(IM.CONCEPT.toString() + "TCT", null)
    val bareConceptTableRef = conceptTCT.table.substringAfterLast('.')
    val existingJoinForField = with.joins.firstOrNull {
      it.tableTo == conceptTCT.table && it.fromProperty == fromField
    }
    if (existingJoinForField != null) {
      return emptyList<MySQLJoin>() to (existingJoinForField.tableToAlias?.replace("`", "") ?: bareConceptTableRef)
    }

    val needsAlias = with.joins.any { it.tableTo == conceptTCT.table }
    val alias = if (needsAlias) aliases.ensureUniqueAlias("${bareConceptTableRef}_$fromField") else null
    val join = table.getJoinCondition(
      tableFromAlias = table.alias,
      tableTo = conceptTCT,
      tableToAlias = alias,
      fromField = fromField,
      toField = "im1dbid"
    )
    return listOf(join) to (alias?.replace("`", "") ?: bareConceptTableRef)
  }
}
