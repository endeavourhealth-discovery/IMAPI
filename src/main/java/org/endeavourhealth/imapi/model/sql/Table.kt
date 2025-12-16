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
    if (relationships[tableTo.dataModel] == null && dataModel != tableTo.dataModel) throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    if (fromField != null && fields[fromField] == null) throw SQLConversionException("Field $fromField not found in table $table")
    val innerField = fields[fromField]?.field ?: relationships[tableTo.dataModel]?.fromField
    ?: if (dataModel == tableTo.dataModel) primaryKey else throw SQLConversionException("No primary key found for table ${tableTo.table}")
    val outerField = relationships[tableTo.dataModel]?.toField
      ?: if (dataModel == tableTo.dataModel) primaryKey else throw SQLConversionException("No primary key found for table ${tableTo.table}")
    return MySQLJoin(
      joinType,
      tableFromAlias ?: tableFrom?.table ?: table,
      tableToAlias ?: tableTo.table,
      toProperty = toField ?: outerField,
      fromProperty = fromField ?: innerField
    )
  }

  fun getJoinCondition(
    joinType: String = "JOIN",
    tableTo: Table,
    tableToAlias: String,
  ): MySQLJoin {
    if (relationships[tableTo.dataModel] == null && dataModel != tableTo.dataModel) throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    return MySQLJoin(
      joinType,
      table,
      tableTo.table,
      tableToAlias,
      relationships[tableTo.dataModel]?.fromField,
      relationships[tableTo.dataModel]?.toField
    )
  }

  fun getJoinCondition(
    joinType: String = "JOIN",
    tableTo: Table,
    tableToAlias: String,
    fromField: String,
    toField: String,
  ): MySQLJoin {
    return MySQLJoin(
      joinType,
      table,
      tableTo.table,
      tableToAlias,
      fromField,
      toField
    )
  }
}