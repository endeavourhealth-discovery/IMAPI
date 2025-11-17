package org.endeavourhealth.imapi.model.sql;

import lombok.*;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
@Getter
@Setter
public class TableMap {
  private List<MappingProperty> properties;
  private Map<String, Table> tables;
  private Map<String, String> functions;
  private transient Map<List<String>, String> propertiesMap;

  public Map<List<String>, String> getPropertiesMap() {
    if (propertiesMap == null) buildPropertiesMap();
    return propertiesMap;
  }

  private void buildPropertiesMap() {
    if (properties == null) return;
    propertiesMap = new HashMap<>();
    for (MappingProperty p : properties) {
      propertiesMap.put(List.copyOf(p.getPath()), p.getDataModel());
    }
  }

  public void setProperties(List<MappingProperty> properties) {
    this.properties = properties;
    buildPropertiesMap();
  }

  public Table getTableFromDataModel(String iri) throws SQLConversionException {
    if (null == iri) throw new SQLConversionException("iri is null");
    Table dmTable = tables.get(iri);
    if (dmTable == null) {
      throw new SQLConversionException("No table for Data model: " + iri + " not found.");
    }
    Table returnTable = new Table();
    returnTable.setDataModel(iri);
    returnTable.setTable(dmTable.getTable());
    returnTable.setCondition(dmTable.getCondition());
    returnTable.setFields(dmTable.getFields());
    returnTable.setRelationships(dmTable.getRelationships());
    returnTable.setPrimaryKey(dmTable.getPrimaryKey());
    return returnTable;
  }

  public Table getTableFromProperty(List<String> iris) throws SQLConversionException {
    if (null == iris || iris.isEmpty()) throw new SQLConversionException("iri is null");
    String dataModel = propertiesMap.get(iris);
    if (null == dataModel) throw new SQLConversionException("No table for properties: " + iris);
    return getTableFromDataModel(dataModel);
  }

  public void putTable(String iri, Table table) {
    tables.put(iri, table);
  }
}
