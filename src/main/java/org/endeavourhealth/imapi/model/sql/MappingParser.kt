package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

class MappingParser {

  private val mapper = ObjectMapper()
  private val tableMap = TableMap()

  @Throws(IOException::class)
  fun parse(resourcePath: String): TableMap {
    val inputStream = javaClass.classLoader.getResourceAsStream(resourcePath)
      ?: throw RuntimeException("Resource not found: $resourcePath")
    val root: JsonNode = mapper.readTree(inputStream)
    setProperties(root.get("properties"))
    setTables(root.get("tables"))
    setFunctions(root.get("functions"))
    return tableMap
  }

  private fun setProperties(propsArray: JsonNode) {
    val propertyList: List<MappingProperty> =
      mapper.convertValue(propsArray, object : TypeReference<List<MappingProperty>>() {})
    tableMap.properties = propertyList
  }

  private fun setTables(dMtables: JsonNode) {
    val tableList: List<Table> =
      mapper.convertValue(dMtables, object : TypeReference<List<Table>>() {})

    val dMtablesMap = HashMap<String, Table>()
    for (table in tableList) {
      if (table.primaryKey.isEmpty()) table.primaryKey = "id"
      for (dataModel in requireNotNull(table.dataModels)) {
        dMtablesMap[dataModel] = table
      }
    }

    // Add reverse relationships where not already present
    for (table in tableList) {
      for (fromDataModel in requireNotNull(table.dataModels)) {
        for (rel in table.relationships) {
          val toTable = dMtablesMap[rel.dataModel] ?: continue
          val reverseFromField = rel.toField.replace("{alias}.", "")
          val reverseToField = rel.fromField.replace("{alias}.", "")
          val alreadyExists = toTable.relationships.any {
            it.dataModel == fromDataModel && it.fromField == reverseFromField && it.toField == reverseToField
          }
          if (!alreadyExists) {
            toTable.relationships.add(
              Relationship(
                dataModel = fromDataModel,
                fromField = reverseFromField,
                toField = reverseToField,
                viaProperty = rel.viaProperty
              )
            )
          }
        }
      }
    }

    tableMap.tables = dMtablesMap
  }

  private fun setFunctions(functions: JsonNode) {
    val functionsMap: Map<String, String> =
      mapper.convertValue(functions, object : TypeReference<Map<String, String>>() {})
    tableMap.functions = functionsMap
  }
}