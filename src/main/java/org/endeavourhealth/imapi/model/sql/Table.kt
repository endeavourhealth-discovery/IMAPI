package org.endeavourhealth.imapi.model.sql;

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import kotlin.String

class Table(
  var dataModels: List<String>? = ArrayList(),
  var table: String = "",
  var primaryKey: String = "",
  var condition: String? = "",
  var dataModel: String = "",
  var fields: HashMap<String, Field> = HashMap(),
  var relationships: HashMap<String, Relationship> = HashMap()
) {
  fun getJoinCondition(
    joinType: String = "JOIN",
    tableFrom: Table? = null,
    tableFromAlias: String? = null,
    tableTo: Table,
    tableToAlias: String? = null,
    fromField: String? = null,
    toField: String? = null,
  ): MySQLJoin {
    if (relationships[tableTo.dataModel] == null) throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    if (fromField != null && fields[fromField] == null) throw SQLConversionException("Field $fromField not found in table $table")
    val innerField = fields[fromField]?.field ?: relationships[tableTo.dataModel]?.toField!!
    return MySQLJoin(
      joinType,
      tableFromAlias ?: tableFrom?.table ?: table,
      tableToAlias ?: tableTo.table,
      toField ?: relationships[tableTo.dataModel]?.fromField,
      fromField ?: innerField
    )
  }
}