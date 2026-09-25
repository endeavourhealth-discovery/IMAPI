package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Bool
import org.endeavourhealth.imapi.model.imq.Where
import org.endeavourhealth.imapi.model.sql.MySQLBoolWhere
import org.endeavourhealth.imapi.model.sql.MySQLCompareWhere
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsNullWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.vocabulary.IM

/**
 * Compiles IMQ where clauses into SQL where conditions, adding any joins the conditions require to the CTE.
 */
internal class WhereClauseCompiler(
  private val queryTypeOfTableProvider: () -> Table,
  private val keepAsMap: Map<String, MySQLWith>,
  private val aliases: AliasAllocator,
  private val getTableFromTypeAndProperty: (String?, String?) -> Table,
  private val getDataModelFromKeepAs: (String?) -> String?,
) {
  private val queryTypeOfTable: Table get() = queryTypeOfTableProvider()

  fun addWheresRecursively(
    where: Where,
    with: MySQLWith,
    variableToTableMap: HashMap<String, Table>,
    parentWhere: MySQLWhere? = null,
    bool: Bool? = null,
    table: Table? = null,
  ) {
    where.and?.let { andList ->
      val boolWhere = MySQLBoolWhere()
      when (bool) {
        Bool.and -> parentWhere
          ?.also { it.and = it.and ?: mutableListOf() }
          ?.and
          ?.add(boolWhere)

        Bool.or -> parentWhere
          ?.also { it.or = it.or ?: mutableListOf() }
          ?.or
          ?.add(boolWhere)

        else -> with.wheres.add(boolWhere)
      }
      andList.forEach {
        addWheresRecursively(it, with, variableToTableMap, boolWhere, Bool.and, table)
      }
      return
    }

    where.or?.let { orList ->
      val boolWhere = MySQLBoolWhere()
      when (bool) {
        Bool.and -> parentWhere
          ?.also { it.and = it.and ?: mutableListOf() }
          ?.and
          ?.add(boolWhere)

        Bool.or -> parentWhere
          ?.also { it.or = it.or ?: mutableListOf() }
          ?.or
          ?.add(boolWhere)

        else -> with.wheres.add(boolWhere)
      }

      orList.forEach {
        addWheresRecursively(it, with, variableToTableMap, boolWhere, Bool.or, table)
      }
      return
    }

    val leaf = getMySQLWhereFromWhere(where, variableToTableMap, with, table)
    when (bool) {
      Bool.and -> parentWhere
        ?.also { it.and = it.and ?: mutableListOf() }
        ?.and
        ?.add(leaf)

      Bool.or -> parentWhere
        ?.also { it.or = it.or ?: mutableListOf() }
        ?.or
        ?.add(leaf)

      else -> with.wheres.add(leaf)
    }
  }

  private fun resolveUnitAndTypeFromWhere(where: Where, rangeEndUnitsIri: String? = null): Pair<String?, String?> =
    when {
      where.units?.iri != null -> SqlLiteralUtils.getUnitNameAndType(where.units.iri)
      rangeEndUnitsIri != null -> SqlLiteralUtils.getUnitNameAndType(rangeEndUnitsIri)
      where.qualifier?.iri != null -> SqlLiteralUtils.getUnitNameAndType(where.qualifier.iri)
      else -> null to null
    }

  private fun getMySQLWhereFromWhere(
    where: Where,
    variableToTableMap: HashMap<String, Table>,
    with: MySQLWith,
    table: Table? = null,
  ): MySQLWhere {
    var (currentTable, field) = if (table != null && where.iri != null)
      table to getPropertyNameByTableAndPropertyIri(table, where.iri).field
    else
      getTableAndField(with, where, variableToTableMap)

    if (table != null) currentTable = table
    if (where.propertyRef != null) field = where.propertyRef

    val tableRef = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table

    val result = if (where.`is` != null) {
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
    } else if (where.range != null) {
      val from = where.range.from
      val to = where.range.to
      val isDirectValue = where.compare == null && from.value != null

      val fromWhere: MySQLWhere
      val toWhere: MySQLWhere

      if (isDirectValue) {
        fromWhere = MySQLPropertyValueWhere(
          property = field,
          operator = from.operator.value,
          value = SqlLiteralUtils.toSqlLiteral("${from.value}"),
          table = tableRef
        )
        toWhere = MySQLPropertyValueWhere(
          property = field,
          operator = to.operator.value,
          value = SqlLiteralUtils.toSqlLiteral("${to.value}"),
          table = tableRef
        )
      } else {
        val right = getValueFromRelativeTo(where, variableToTableMap)


        val (fromUnit, fromUnitType) = resolveUnitAndTypeFromWhere(where, where.range.from.units?.iri)
        val (toUnit, toType) = resolveUnitAndTypeFromWhere(where, where.range.to.units?.iri)

        fromWhere = MySQLCompareWhere(
          property = field,
          operator = from.operator.value,
          right = right,
          value = from.value?.let { SqlLiteralUtils.toSqlLiteral(it) } ?: "",
          table = tableRef,
          units = if (fromUnitType == "Unit") fromUnit else null,
          qualifier = if (fromUnitType == "Qualifier") fromUnit else null,
        )

        toWhere = MySQLCompareWhere(
          property = field,
          operator = to.operator.value,
          right = right,
          value = to.value?.let { SqlLiteralUtils.toSqlLiteral(it) } ?: "",
          table = tableRef,
          units = if (toType == "Unit") toUnit else null,
          qualifier = if (toType == "Qualifier") toUnit else null,
        )
      }

      MySQLBoolWhere(and = mutableListOf(fromWhere, toWhere))

    } else if (where.isNull) {
      MySQLPropertyIsNullWhere(
        field,
        not = where.isNot,
        table = tableRef,
      )
    } else if (where.compare != null) {
      val (name, type) = resolveUnitAndTypeFromWhere(where)
      val compareValue = getValueFromRelativeTo(where, variableToTableMap)

      if (where.value != null)
        MySQLCompareWhere(
          property = field,
          operator = where.operator.value,
          right = compareValue,
          value = where.value,
          units = if (type == "Unit") name else null,
          qualifier = if (type == "Qualifier") name else null,
          not = where.isNot,
          table = currentTable.alias ?: currentTable.table,
        ) else MySQLPropertyValueWhere(
        property = field,
        operator = where.operator.value,
        value = compareValue,
        qualifier = if (type == "Qualifier") name else null,
        not = where.isNot,
        table = currentTable.alias ?: currentTable.table,
      )
    } else {
      MySQLPropertyValueWhere(
        field,
        where.operator.value,
        SqlLiteralUtils.toSqlLiteral(where.value),
        not = where.isNot,
        table = tableRef,
      )
    }
    return result
  }

  private fun getTableAndField(
    with: MySQLWith,
    where: Where,
    variableToTableMap: HashMap<String, Table>
  ): Pair<Table, String> {
    val nodeRef = where.compare?.left?.nodeRef ?: where.nodeRef
    val whereIri = where.compare?.left?.iri ?: where.iri

    if (whereIri == null) throw SQLConversionException("No property found for where $whereIri")
    val currentTable =
      if (nodeRef != null) variableToTableMap[nodeRef] else with.table
    if (currentTable == null) throw SQLConversionException("No table found: $nodeRef")

    if (nodeRef == null && with.isCarrierAliased) {
      val alias = whereIri.substringAfterLast('#')
      val field = if (with.fromAlias != null) "${with.fromAlias}.$alias" else alias
      return currentTable to field
    }

    var rawField = getPropertyNameByTableAndPropertyIri(
      currentTable,
      whereIri
    ).field
    if (rawField.isEmpty()) {
      rawField = getPropertyNameByTableAndPropertyIri(currentTable, whereIri).field
        ?: throw SQLConversionException("No field found for property $whereIri")
      val (fk, pk) = with.table.foreignKeyTo(queryTypeOfTable)
      with.joins.add(
        MySQLJoin(
          join = "JOIN",
          with.table.table,
          queryTypeOfTable.table,
          fromProperty = fk,
          toProperty = pk
        )
      )
      rawField = queryTypeOfTable.table + "." + rawField
      return queryTypeOfTable to rawField
    }

    val field =
      if (with.fromAlias != null && nodeRef == null && !rawField.contains(".") && !rawField.contains("(")) {
        "${with.fromAlias}.$rawField"
      } else {
        rawField
      }
    return currentTable to field
  }

  private fun getValueFromRelativeTo(where: Where, nodeToTableMap: HashMap<String, Table>): String {
    val right = where.compare?.right
      ?: throw SQLConversionException("No value provided for where $where")

    right.parameter?.let { return it }

    val nodeRef = right.nodeRef
      ?: return right.propertyRef
        ?: throw SQLConversionException("No property found for relativeTo ${where.compare?.right}")

    (keepAsMap[nodeRef] ?: keepAsMap[aliases.cteNormalise(nodeRef)])?.let { keptWith ->
      val field = if (keptWith.isCarrierAliased) {
        right.propertyRef ?: right.iri?.substringAfterLast('#')
        ?: throw SQLConversionException("No property found for relativeTo $nodeRef")
      } else {
        right.iri?.let { getPropertyNameByTableAndPropertyIri(keptWith.table, it).field }
          ?: right.propertyRef
          ?: throw SQLConversionException("No property found for relativeTo $nodeRef")
      }
      return "${keptWith.alias}.$field"
    }

    var property = ""
    val nodeRefTable = nodeToTableMap[nodeRef]
    if (nodeRefTable != null) {
      property = right.iri?.let { getPropertyNameByTableAndPropertyIri(nodeRefTable, it).field }
        ?: right.propertyRef
          ?: ""
    } else {
      getDataModelFromKeepAs(nodeRef)?.let {
        property = right.iri?.let { iri ->
          getPropertyNameByTableAndPropertyIri(getTableFromTypeAndProperty(it, null), iri).field
        }
          ?: right.propertyRef
            ?: ""
      }
    }
    if (property.isEmpty()) throw SQLConversionException("No property found for relativeTo $nodeRef")
    if (nodeRefTable != null) return "${aliases.sanitiseAlias(nodeRef)}.$property"
    return "`${nodeRef}`.${property}"
  }

  private fun addWhereConceptJoin(
    table: Table,
    fromField: String?,
    with: MySQLWith
  ): Pair<MutableList<MySQLJoin>, String> {
    val joins: MutableList<MySQLJoin> = mutableListOf()

    val conceptTCT = getTableFromTypeAndProperty(IM.CONCEPT.toString() + "TCT", null)
    val bareConceptTableRef = conceptTCT.table.substringAfterLast('.')
    val existingJoinForField = with.joins.firstOrNull {
      it.tableTo == conceptTCT.table && it.fromProperty == fromField
    }
    if (existingJoinForField != null) {
      return joins to (existingJoinForField.tableToAlias?.replace("`", "") ?: bareConceptTableRef)
    }

    val needsAlias = with.joins.any { it.tableTo == conceptTCT.table }
    val alias = if (needsAlias) aliases.ensureUniqueAlias("${bareConceptTableRef}_$fromField") else null
    joins.add(
      table.getJoinCondition(
        tableFromAlias = table.alias,
        tableTo = conceptTCT,
        tableToAlias = alias,
        fromField = fromField,
        toField = "im1dbid"
      )
    )
    return joins to (alias?.replace("`", "") ?: bareConceptTableRef)
  }
}
