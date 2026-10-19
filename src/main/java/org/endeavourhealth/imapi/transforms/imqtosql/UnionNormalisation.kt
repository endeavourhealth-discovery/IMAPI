package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith

/**
 * The selects that carry each of [carryProperties] through a union branch, reading them from [sourceRef]. A branch
 * that does not expose a property (or is a cohort reference) contributes NULL so every branch has the same columns.
 */
private fun carrySelects(with: MySQLWith, carryProperties: List<String>, sourceRef: String): List<MySQLSelect> {
  val availableAliases = getAvailableAliases(with)
  return carryProperties.map { propIri ->
    val alias = propIri.substringAfterLast('#')
    if (with.isCohortRef || alias !in availableAliases) MySQLSelect("NULL", alias)
    else MySQLSelect("$sourceRef.$alias", alias)
  }
}

internal fun selectFromEmittedWith(with: MySQLWith, carryProperties: List<String>): MySQLWith {
  val keyField = with.entityKeyField ?: return with
  val selects = mutableListOf(MySQLSelect("${with.alias}.$keyField"))
  selects.addAll(carrySelects(with, carryProperties, with.alias))
  return MySQLWith(
    table = with.table,
    alias = with.alias,
    selects = selects,
    fromAlias = with.alias,
    entityKeyField = keyField
  )
}

internal fun normaliseForUnion(with: MySQLWith, carryProperties: List<String> = emptyList()): MySQLWith {
  val keyField = with.entityKeyField ?: return with

  if (carryProperties.isEmpty()) {
    val onlySelect = with.selects.singleOrNull()
    if (onlySelect != null && onlySelect.alias == null && !onlySelect.name.contains("*")) return with
  }

  val selects = mutableListOf(MySQLSelect("$UNION_SOURCE_ALIAS.$keyField"))
  selects.addAll(carrySelects(with, carryProperties, UNION_SOURCE_ALIAS))
  return MySQLWith(
    table = with.table,
    alias = with.alias,
    selects = selects,
    subQuery = with,
    fromAlias = UNION_SOURCE_ALIAS,
    entityKeyField = keyField
  )
}

internal fun getAvailableAliases(with: MySQLWith): Set<String> {
  val effectiveSelects = if (with.selects.size == 1 && with.selects.first().name.contains("*")) {
    with.subQuery?.selects ?: emptyList()
  } else with.selects
  return effectiveSelects.mapNotNull { it.alias }.filterNot { it == ROW_NUMBER_ALIAS }.toSet()
}
