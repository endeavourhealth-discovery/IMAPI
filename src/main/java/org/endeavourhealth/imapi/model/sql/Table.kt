package org.endeavourhealth.imapi.model.sql;

import org.endeavourhealth.imapi.errorhandling.SQLConversionException

data class Table(
  var dataModels: List<String>? = ArrayList(),
  var table: String = "",
  var primaryKey: String = "",
  var condition: Condition? = null,
  var dataModel: String = "",
  var fields: HashMap<String, Field> = HashMap(),
  var relationships: MutableList<Relationship> = mutableListOf(),
) {
  var alias: String? = null

  fun relationshipTo(targetDataModel: String, viaProperty: String? = null): Relationship? {
    val candidates = relationships.filter { it.dataModel == targetDataModel }
    if (candidates.isEmpty()) return null
    if (viaProperty != null) {
      candidates.firstOrNull { it.viaProperty == viaProperty }?.let { return it }
    }
    return candidates.firstOrNull { it.viaProperty == null } ?: candidates.first()
  }

  fun getJoinCondition(
    joinType: String = "JOIN",
    tableFrom: Table? = null,
    tableFromAlias: String? = null,
    tableTo: Table,
    tableToAlias: String? = null,
    fromField: String? = null,
    toField: String? = null,
    reference: Boolean? = false,
    viaProperty: String? = null,
  ): MySQLJoin {
    val rel = relationshipTo(tableTo.dataModel, viaProperty)
    if (rel == null && dataModel != tableTo.dataModel) {
      if (fromField == null && toField == null)
        throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    }
    val innerField = fromField ?: rel?.fromField
    ?: if (dataModel == tableTo.dataModel) primaryKey else throw SQLConversionException("No primary key found for table ${tableTo.table}")
    val outerField = rel?.toField
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
    reference: Boolean? = false,
    viaProperty: String? = null,
  ): MySQLJoin {
    val rel = relationshipTo(tableTo.dataModel, viaProperty)
    if (rel == null && (dataModel != tableTo.dataModel && tableTo.table != table)) {
      throw SQLConversionException("Relationship between $table and ${tableTo.table} not found")
    }
    return MySQLJoin(
      joinType,
      table,
      tableTo.table,
      tableToAlias,
      rel?.fromField ?: primaryKey,
      rel?.toField ?: primaryKey,
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

  fun foreignKeyTo(target: Table, viaProperty: String? = null): Pair<String?, String?> {
    val rel = relationshipTo(target.dataModel, viaProperty)
      ?: return null to null
    return rel.fromField to rel.toField
  }
}
