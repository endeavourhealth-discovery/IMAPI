package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.query.algebra.Str;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MappingParser {

  public TableMap parse(String resourcePath) throws IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
    if (is == null) {
      throw new RuntimeException("Resource not found: " + resourcePath);
    }
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(is);
    TableMap tableMap = new TableMap();

    JsonNode propsArray = root.get("properties");
    List<MappingProperty> propertyList = mapper.convertValue(
      propsArray,
      new TypeReference<List<MappingProperty>>() {
      }
    );
    tableMap.setProperties(propertyList);

    JsonNode dataModels = root.get("dataModels");
    Map<String, Table> dataModelsMap = mapper.convertValue(
      dataModels,
      new TypeReference<Map<String, Table>>() {
      }
    );
    tableMap.setDataModels(dataModelsMap);

    JsonNode functions = root.get("functions");
    Map<String, String> functionsMap = mapper.convertValue(
      functions,
      new TypeReference<Map<String, String>>() {
      }
    );
    tableMap.setFunctions(functionsMap);
    return tableMap;
  }
}
