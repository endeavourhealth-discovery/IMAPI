package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith

internal fun selectFromEmittedWith(with: MySQLWith, carryProperties: List<String>): MySQLWith {
  val keyField = with.entityKeyField ?: return with
  val selects = mutableListOf(MySQLSelect("${with.alias}.$keyField"))
  if (carryProperties.isNotEmpty()) {
    val availableAliases = getAvailableAliases(with)
    for (propIri in carryProperties) {
      val alias = propIri.substringAfterLast('#')
      selects.add(
        if (with.isCohortRef || alias !in availableAliases) MySQLSelect("NULL", alias)
        else MySQLSelect("${with.alias}.$alias", alias)
      )
    }
  }
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
    return MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = mutableListOf(MySQLSelect("t.$keyField")),
      subQuery = with,
      fromAlias = "t",
      entityKeyField = keyField
    )
  }

  val availableAliases = getAvailableAliases(with)
  val selects = mutableListOf(MySQLSelect("t.$keyField"))
  for (propIri in carryProperties) {
    val alias = propIri.substringAfterLast('#')
    selects.add(
      if (with.isCohortRef || alias !in availableAliases) MySQLSelect("NULL", alias)
      else MySQLSelect("t.$alias", alias)
    )
  }
  return MySQLWith(
    table = with.table,
    alias = with.alias,
    selects = selects,
    subQuery = with,
    fromAlias = "t",
    entityKeyField = keyField
  )
}

internal fun getAvailableAliases(with: MySQLWith): Set<String> {
  val effectiveSelects = if (with.selects.size == 1 && with.selects.first().name.contains("*")) {
    with.subQuery?.selects ?: emptyList()
  } else with.selects
  return effectiveSelects.mapNotNull { it.alias }.filterNot { it == ROW_NUMBER_ALIAS }.toSet()
}
