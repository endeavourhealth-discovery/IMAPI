package org.endeavourhealth.imapi.transforms

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
  if (match.orderBy == null) return emptyList()
  val props = linkedSetOf<String>()
  match.orderBy.property.forEach { props.add(it.iri) }
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
      MySQLSelect("u.*"),
      MySQLSelect("ROW_NUMBER() OVER(PARTITION BY u.$keyField ORDER BY $orderClause)", ROW_NUMBER_ALIAS)
    ),
    subQuery = with,
    fromAlias = "u",
    entityKeyField = keyField
  )

  val finalWith = MySQLWith(
    table = with.table,
    alias = with.alias,
    selects = mutableListOf(MySQLSelect("sq.*")),
    subQuery = rnLayer,
    fromAlias = "sq",
    wheres = mutableListOf(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "<=", orderBy.limit.toString(), table = "sq")),
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
    val propertyIri = p.iri.substringAfterLast(' ')
    val field = getPropertyNameByTableAndPropertyIri(
      currentTable,
      propertyIri
    ).field ?: throw SQLConversionException("No field found for property $propertyIri")
    items.add(MySQLOrderByItem(field, if (p.direction == Order.descending) "DESC" else "ASC", table = currentTable))
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

  val select = if (match.notExists() && previousWith != null) {
    if (previousWith.table.dataModel == COHORT_DATA_MODEL_IRI) "${previousWith.alias}.*, ${previousWith.alias}.$ENTITY_ID_FIELD as $PATIENT_ID_FIELD"
    else "${previousWith.alias}.*"
  } else "sq.*"


  val entityKeyField = if (match.notExists() && previousWith != null) {
    if (previousWith.table.dataModel == COHORT_DATA_MODEL_IRI) PATIENT_ID_FIELD
    else previousWith.entityKeyField
  } else with.entityKeyField

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
    val (fkLast, pkLast) = resolveForeignKeyByTableName(previousWith.table, queryTypeOfTable)

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
      val notExistJoinCondition = MySQLJoin(
        join = "RIGHT JOIN",
        tableFrom = "sq",
        tableTo = previousWith.alias,
        tableToAlias = previousWith.alias,
        fromProperty = fk,
        toProperty = fkLast,
        reference = true
      )
      rnWith.joins.add(notExistJoinCondition)
      val or = mutableListOf<MySQLWhere>()
      or.add(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "!=", match.orderBy.limit.toString(), table = "sq"))
      or.add(MySQLPropertyValueWhere(fk, "IS", "NULL", table = "sq"))
      rnWith.wheres.add(MySQLBoolWhere(or = or))
    }
  }

  if (!match.notExists() || previousWith == null) {
    rnWith.wheres.add(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "<=", match.orderBy.limit.toString(), table = "sq"))
  }
  return rnWith
}
