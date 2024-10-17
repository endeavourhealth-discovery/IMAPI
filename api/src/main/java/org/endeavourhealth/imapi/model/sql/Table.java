package org.endeavourhealth.imapi.model.sql;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Table {
  private String table;
  private String condition;
  private HashMap<String, Field> fields = new HashMap<>();
  private HashMap<String, Relationship> relationships = new HashMap<>();

  public Table() {
  }

  public Table(String table, String condition, HashMap<String, Field> fields, HashMap<String, Relationship> relationships) {
    this.table = table;
    this.condition = condition;
    this.fields = fields;
    this.relationships = relationships;
  }
}
