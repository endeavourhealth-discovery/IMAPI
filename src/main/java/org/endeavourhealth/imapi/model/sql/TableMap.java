package org.endeavourhealth.imapi.model.sql;

import lombok.*;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
public class TableMap {
  private List<MappingProperty> properties;

  public Map<String, Table> getTables() {
    return tables;
  }

  public Map<String, String> getFunctions() {
    return functions;
  }

  public List<MappingProperty> getProperties() {
    return properties;
  }

  public void setTables(Map<String, Table> tables) {
    this.tables = tables;
  }

  public void setFunctions(Map<String, String> functions) {
    this.functions = functions;
  }

  public void setPropertiesMap(Map<List<String>, PropertyMapItem> propertiesMap) {
    this.propertiesMap = propertiesMap;
  }

  private Map<String, Table> tables;
  private Map<String, String> functions;
  private Map<List<String>, PropertyMapItem> propertiesMap;

  public Map<List<String>, PropertyMapItem> getPropertiesMap() {
    if (propertiesMap == null) buildPropertiesMap();
    return propertiesMap;
  }

  private void buildPropertiesMap() {
    if (properties == null) return;
    propertiesMap = new HashMap<>();
    for (MappingProperty p : properties) {
      if (p.getCondition() != null)
        propertiesMap.put(List.copyOf(p.getPath()), new PropertyMapItem(p.getDataModel(), new Condition(p.getCondition().getField(), p.getCondition().getValue())));
      else propertiesMap.put(List.copyOf(p.getPath()), new PropertyMapItem(p.getDataModel(), null));
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
    PropertyMapItem pmi = propertiesMap.get(iris);
    if (null == pmi) return null;
    Table table = getTableFromDataModel(pmi.getDataModel());
    table.setCondition(pmi.getCondition());
    return table;
  }

  public void putTable(String iri, Table table) {
    tables.put(iri, table);
  }
}
