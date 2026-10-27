package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Bool
import org.endeavourhealth.imapi.model.imq.Order
import org.endeavourhealth.imapi.model.imq.OrderLimit
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.sql.MySQLBoolWhere
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLOrderBy
import org.endeavourhealth.imapi.model.sql.MySQLOrderByItem
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table

internal fun getGroupCarryProperties(match: Query): List<String> {
  val props = linkedSetOf<String>()
  match.orderBy?.property?.forEach { props.add(it.iri) }
  match.`return`?.forEach { ret -> ret.iri?.let { props.add(it) } }
  return props.toList()
}

internal fun wrapGroupOrderBy(with: MySQLWith, match: Query): MySQLWith {
  val keyField = with.entityKeyField ?: throw SQLConversionException("Group order-by requires an entity key")
  val orderBy = match.orderBy ?: throw SQLConversionException("wrapGroupOrderBy called without an orderBy")
  val orderClause = orderBy.property.joinToString(", ") { p ->
    "${p.iri.substringAfterLast('#')} ${if (p.direction == Order.descending) "DESC" else "ASC"}"
  }

  val rnLayer = MySQLWith(
    table = with.table,
    selects = mutableListOf(
      MySQLSelect("$ROW_NUMBER_SOURCE_ALIAS.*"),
      MySQLSelect("ROW_NUMBER() OVER(PARTITION BY $ROW_NUMBER_SOURCE_ALIAS.$keyField ORDER BY $orderClause)", ROW_NUMBER_ALIAS)
    ),
    subQuery = with,
    fromAlias = ROW_NUMBER_SOURCE_ALIAS,
    entityKeyField = keyField
  )

  val finalWith = MySQLWith(
    table = with.table,
    alias = with.alias,
    selects = mutableListOf(MySQLSelect("$SUBQUERY_ALIAS.*")),
    subQuery = rnLayer,
    fromAlias = SUBQUERY_ALIAS,
    wheres = mutableListOf(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "<=", orderBy.limit.toString(), table = SUBQUERY_ALIAS)),
    entityKeyField = keyField
  )


  return finalWith
}

internal fun getMySQLOrderBy(
  table: Table, orderBy: OrderLimit, nodeToTableMap: HashMap<String, Table>,
): MySQLOrderBy {
  val items = mutableListOf<MySQLOrderByItem>()
  for (p in orderBy.property) {
    val currentTable =
      if (p.nodeRef != null) nodeToTableMap[p.nodeRef] else table

    if (currentTable == null) throw SQLConversionException("No table exists for ${p.iri}")
    val propertyIri = p.iri
    val mappedField = getPropertyNameByTableAndPropertyIri(currentTable, propertyIri)
    val field = mappedField.field
    // a functional property's column lives on the table joined in under its own alias
    val fieldTable =
      if (mappedField.join != null) currentTable.copy().also { it.alias = functionalJoinAlias(propertyIri, mappedField) }
      else currentTable
    items.add(MySQLOrderByItem(field, if (p.direction == Order.descending) "DESC" else "ASC", table = fieldTable))
  }
  return MySQLOrderBy(items, orderBy.limit)
}

internal fun getOrderByWith(
  with: MySQLWith,
  match: Query,
  mySQLQuery: MySQLQuery,
  queryTypeOfTable: Table
): MySQLWith {
  val previousWith = mySQLQuery.withs.lastOrNull()

  val (fk, pk) = resolveForeignKeyByTableName(with.table, queryTypeOfTable)

  if (fk == null || pk == null) {
    throw SQLConversionException(
      "No relationship between ${with.table.table} and ${queryTypeOfTable.table}"
    )
  }

  val partitionByField = "${with.table.alias ?: with.table.table}.$fk"
  with.selects.add(
    MySQLSelect(
      "ROW_NUMBER() OVER(PARTITION BY $partitionByField ${
        getMySQLOrderBy(with.table, match.orderBy, mySQLQuery.nodeToTableMap).toSql()
      })", ROW_NUMBER_ALIAS
    )
  )

  val isAntiJoin = match.notExists() && previousWith != null
  val select = if (isAntiJoin) carrySelectFrom(previousWith!!) else "$SUBQUERY_ALIAS.*"
  val entityKeyField = if (isAntiJoin) carryEntityKeyFrom(previousWith!!) else with.entityKeyField

  val rnWith = MySQLWith(
    table = with.table,
    alias = with.alias,
    selects = mutableListOf(MySQLSelect(select)),
    wheres = mutableListOf(),
    whereBool = Bool.and,
    subQuery = with,
    entityKeyField = entityKeyField,
    isCarrierAliased = with.isCarrierAliased
  )

  if (previousWith != null) {
    val (resolvedFkLast, pkLast) = resolveForeignKeyByTableName(previousWith.table, queryTypeOfTable)
    val fkLast = previousWith.entityKeyField ?: resolvedFkLast

    if (fkLast == null || pkLast == null) {
      throw SQLConversionException(
        "No relationship between ${with.table.table} and ${previousWith.table.table}"
      )
    }

    val innerQueryJoin = MySQLJoin(
      join = "JOIN",
      tableFrom = with.table.alias ?: with.table.table,
      tableTo = previousWith.alias,
      tableToAlias = previousWith.alias,
      fromProperty = fk,
      toProperty = fkLast,
      reference = true
    )

    with.joins.add(innerQueryJoin)

    if (match.notExists()) {
      rnWith.joins.add(rightJoinSubqueryTo(previousWith, fk, fkLast))
      val or = mutableListOf<MySQLWhere>()
      or.add(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "!=", match.orderBy.limit.toString(), table = SUBQUERY_ALIAS))
      or.add(subqueryKeyIsNull(fk))
      rnWith.wheres.add(MySQLBoolWhere(or = or))
    }
  }

  if (!isAntiJoin) {
    rnWith.wheres.add(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "<=", match.orderBy.limit.toString(), table = SUBQUERY_ALIAS))
  }
  return rnWith
}
