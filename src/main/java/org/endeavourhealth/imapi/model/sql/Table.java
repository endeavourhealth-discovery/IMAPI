package org.endeavourhealth.imapi.model.sql;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Table {
  private String table;
  private String condition;
  private String dataModel;
  private HashMap<String, Field> fields = new HashMap<>();
  private HashMap<String, Relationship> relationships = new HashMap<>();

  public Table() {
  }

  public Table(String table, String condition, HashMap<String, Field> fields, HashMap<String, Relationship> relationships, String dataModel) {
    this.table = table;
    this.condition = condition;
    this.fields = fields;
    this.relationships = relationships;
    this.dataModel = dataModel;

  }
}
