package org.endeavourhealth.imapi.model.sql;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Table {
  private List<String> dataModels;
  private String table;
  private String primaryKey;
  private String condition;
  private String dataModel;
  private HashMap<String, Field> fields = new HashMap<>();
  private HashMap<String, Relationship> relationships = new HashMap<>();

  public Table(String table, String primaryKey, String condition, HashMap<String, Field> fields, HashMap<String, Relationship> relationships, String dataModel) {
    this.table = table;
    this.condition = condition;
    this.fields = fields;
    this.relationships = relationships;
    this.dataModel = dataModel;
    this.primaryKey = primaryKey;
  }
}
