package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.Field
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table

/**
 * Resolves the (foreignKey, primaryKey) column pair linking [from] to [to]: when the two
 * tables are considered the same entity (by table name or by data model — callers pick which
 * comparison applies), [from]'s own primary key stands in for both sides; otherwise the
 * explicit foreign-key relationship between the tables is used.
 */
internal fun resolveForeignKeyByTableName(from: Table, to: Table): Pair<String?, String?> =
  if (from.table == to.table) from.primaryKey to from.primaryKey
  else from.foreignKeyTo(to)

internal fun resolveForeignKeyByDataModel(from: Table, to: Table): Pair<String?, String?> =
  if (from.dataModel == to.dataModel) from.primaryKey to from.primaryKey
  else from.foreignKeyTo(to)

internal fun getPropertyNameByTableAndPropertyIri(table: Table, propertyIri: String): Field {
  val field = table.fields[propertyIri] ?: throw SQLConversionException(
    "Property $propertyIri not found in table ${table.table}"
  )
  return field
}

internal fun getLastCteEntityKeyField(lastCTE: MySQLWith, queryTypeOfTable: Table): String? {
  lastCTE.entityKeyField?.let { return it }
  return resolveForeignKeyByTableName(lastCTE.table, queryTypeOfTable).first
}
