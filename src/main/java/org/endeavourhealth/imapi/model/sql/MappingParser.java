package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.query.algebra.Str;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingParser {

  private ObjectMapper mapper = new ObjectMapper();
  private TableMap tableMap = new TableMap();

  public TableMap parse(String resourcePath) throws IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
    if (is == null) {
      throw new RuntimeException("Resource not found: " + resourcePath);
    }
    JsonNode root = mapper.readTree(is);
    setProperties(root.get("properties"));
    setTables(root.get("tables"));
    setFunctions(root.get("functions"));
    return tableMap;
  }


  private void setProperties(JsonNode propsArray) {
    List<MappingProperty> propertyList = mapper.convertValue(
      propsArray,
      new TypeReference<List<MappingProperty>>() {
      }
    );
    tableMap.setProperties(propertyList);
  }

  private void setTables(JsonNode dMtables) {
    List<Table> tableList = mapper.convertValue(
      dMtables,
      new TypeReference<List<Table>>() {
      }
    );
    HashMap<String, Table> dMtablesMap = new HashMap<String, Table>();
    for (Table table : tableList) {
      if (table.getPrimaryKey() == null || table.getPrimaryKey().isEmpty()) {
        table.setPrimaryKey("id");
      }
      for (String dataModel : table.getDataModels()) {
        dMtablesMap.put(dataModel, table);
      }
    }

    // After populating all tables
    for (Table table : tableList) {
      for (String fromDataModel : table.getDataModels()) {
        Map<String, Relationship> rels = table.getRelationships();
        if (rels != null) {
          for (Map.Entry<String, Relationship> entry : rels.entrySet()) {
            String toDataModel = entry.getKey();
            Relationship rel = entry.getValue();

            Table toTable = dMtablesMap.get(toDataModel);
            if (toTable != null) {
              if (toTable.getRelationships() == null) {
                toTable.setRelationships(new HashMap<>());
              }
              // Add reverse relationship if not already present
              if (!toTable.getRelationships().containsKey(fromDataModel)) {
                Relationship reverse = new Relationship();
                reverse.setFromField(rel.getToField().replace("{alias}.", ""));
                reverse.setToField(rel.getFromField().replace("{alias}.", ""));
                toTable.getRelationships().put(fromDataModel, reverse);
              }
            }
          }
        }
      }
    }

    tableMap.setTables(dMtablesMap);
  }

  private void setFunctions(JsonNode functions) {
    Map<String, String> functionsMap = mapper.convertValue(
      functions,
      new TypeReference<Map<String, String>>() {
      }
    );
    tableMap.setFunctions(functionsMap);
  }
}

