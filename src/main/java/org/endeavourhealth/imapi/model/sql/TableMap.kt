package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException

class TableMap {
  var tables: Map<String, Table> = emptyMap()
  var functions: Map<String, String> = emptyMap()

  var properties: List<MappingProperty> = emptyList()
    set(value) {
      field = value
      buildPropertiesMap()
    }

  var propertiesMap: Map<List<String>, PropertyMapItem> = emptyMap()
    private set

  private fun buildPropertiesMap() {
    propertiesMap = properties.associate { p ->
      val condition = p.condition?.let { Condition(it.field, it.value) }
      p.path.toList() to PropertyMapItem(p.dataModel, condition)
    }
  }

  fun getTableFromDataModel(iri: String?): Table {
    if (iri == null) throw SQLConversionException("iri is null")
    val dmTable = tables[iri]
      ?: throw SQLConversionException("No table for Data model: $iri not found.")
    return Table().apply {
      dataModel = iri
      table = dmTable.table
      condition = dmTable.condition
      fields = dmTable.fields
      relationships = dmTable.relationships
      primaryKey = dmTable.primaryKey
    }
  }

  fun getTableFromProperty(iris: List<String>?): Table? {
    if (iris.isNullOrEmpty()) return null
    val pmi = propertiesMap[iris] ?: return null
    val table = getTableFromDataModel(pmi.dataModel)
    table.condition = pmi.condition
    return table
  }

  fun putTable(iri: String, table: Table) {
    tables = tables + (iri to table)
  }
}