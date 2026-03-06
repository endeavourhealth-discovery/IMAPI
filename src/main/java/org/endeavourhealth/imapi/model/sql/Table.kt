package org.endeavourhealth.imapi.model.sql;

import org.apache.lucene.search.DoubleValuesSource.fromField
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import kotlin.String

class Table(
  var dataModels: List<String>? = ArrayList(),
  var table: String = "",
  var primaryKey: String = "",
  var condition: Condition? = null,
  var dataModel: String = "",
  var fields: HashMap<String, Field> = HashMap(),
  var relationships: HashMap<String, Relationship> = HashMap(),
) {
  var alias: String? = null

  fun getJoinCondition(
    joinType: String = "JOIN",
    tableFrom: Table? = null,
    tableFromAlias: String? = null,
    tableTo: Table,
    tableToAlias: String? = null,
    fromField: String? = null,
    toField: String? = null,
    reference: Boolean? = false
  ): MySQLJoin {
    if (relationships[tableTo.dataModel] == null && dataModel != tableTo.dataModel) {
      if (fromField == null && toField == null)
        throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    }
    val innerField = fromField ?: relationships[tableTo.dataModel]?.fromField
    ?: if (dataModel == tableTo.dataModel) primaryKey else throw SQLConversionException("No primary key found for table ${tableTo.table}")
    val outerField = relationships[tableTo.dataModel]?.toField
      ?: toField
      ?: if (dataModel == tableTo.dataModel) primaryKey else throw SQLConversionException("No primary key found for table ${tableTo.table}")
    return MySQLJoin(
      join = joinType,
      tableFrom = tableFromAlias ?: tableFrom?.table ?: table,
      tableTo = tableTo.table,
      tableToAlias = tableToAlias,
      toProperty = toField ?: outerField,
      fromProperty = fromField ?: innerField,
      reference = reference
    )
  }

  fun getJoinCondition(
    joinType: String = "JOIN",
    tableTo: Table,
    tableToAlias: String,
    reference: Boolean? = false
  ): MySQLJoin {
    if (relationships[tableTo.dataModel] == null && (dataModel != tableTo.dataModel && tableTo.table != table)) {
      throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    }
    return MySQLJoin(
      joinType,
      table,
      tableTo.table,
      tableToAlias,
      relationships[tableTo.dataModel]?.fromField ?: primaryKey,
      relationships[tableTo.dataModel]?.toField ?: primaryKey,
      reference = reference
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

  fun foreignKeyTo(target: Table): Pair<String?, String?> {
    val rel = relationships[target.dataModel]
      ?: return null to null
    return rel.fromField to rel.toField
  }
}