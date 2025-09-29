package org.endeavourhealth.imapi.model.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class TableMap {
  private HashMap<String, Table> properties;
  private HashMap<String, Table> dataModels;
  private HashMap<String, String> functions;

  public TableMap() {
  }

  public Table getTable(String iri) throws SQLConversionException {
    if (null == iri) throw new SQLConversionException("iri is null");
    Table propTable = properties.get(iri);
    if (propTable != null) {
      String dataModel = propTable.getDataModel();
      Table dmTable = dataModels.get(dataModel);
      if (dmTable == null) {
        throw new SQLConversionException("No table for Data model: " + dataModel + " not found.");
      }
      Table returnTable = new Table();
      returnTable.setDataModel(dataModel);
      returnTable.setTable(propTable.getTable() != null ? propTable.getTable() : dmTable.getTable());
      returnTable.setCondition(propTable.getCondition() != null ? propTable.getCondition() : dmTable.getCondition());
      returnTable.setFields(dmTable.getFields());
      returnTable.setRelationships(dmTable.getRelationships());
      return returnTable;
    }
    Table returnTable = dataModels.get(iri);
    if (returnTable == null) {
      throw new SQLConversionException("No table for Data model: " + iri + " not found.");
    }
    if (null == returnTable.getDataModel())
      returnTable.setDataModel(iri);
    return returnTable;
  }

  public void putTable(String iri, Table table) {
    dataModels.put(iri, table);
  }
}
