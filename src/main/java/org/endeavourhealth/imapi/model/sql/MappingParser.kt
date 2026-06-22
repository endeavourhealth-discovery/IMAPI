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
        for ((toDataModel, rel) in table.relationships) {
          val toTable = dMtablesMap[toDataModel] ?: continue
          if (!toTable.relationships.containsKey(fromDataModel)) {
            toTable.relationships[fromDataModel] = Relationship(
              fromField = rel.toField.replace("{alias}.", ""),
              toField = rel.fromField.replace("{alias}.", "")
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