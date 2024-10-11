package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

@Getter
public class SQLQuery {
  private static Integer aliasIndex = 0;

  public SQLQuery() {}

  public SQLQuery(String model, String variable) {
    aliasIndex = 0;
    SQLQuery result = new SQLQuery();
    result.initialize(model, variable);
  }

  private ArrayList<String> withs = new ArrayList<>();
  private ArrayList<String> selects = new ArrayList<>();
  private String model = "";
  private Table map = new Table();
  private String alias = "";
  private ArrayList<String> joins = new ArrayList<>();
  private String whereBool = "AND";
  private ArrayList<String> wheres = new ArrayList<>();
  private ArrayList<String> dependencies = new ArrayList<>();

  public SQLQuery subQuery(String model, String variable) {
    SQLQuery result = new SQLQuery();
    result.initialize(model, variable);
    return result;
  }

  public void initialize(String model, String variable) {
    this.withs = new ArrayList<>();
    this.selects = new ArrayList<>();
    this.joins = new ArrayList<>();
    this.whereBool = "AND";
    this.wheres = new ArrayList<>();
    this.dependencies = new ArrayList<>();

    this.model = model;
    this.map = this.getMap(model);
    this.alias = variable == null ? this.getAlias(this.map.table) : variable;

    (mapData.typeTables as any)[this.alias] = { table: this.alias, fields: this.map.fields, relationships: this.map.relationships };
  }

  public String  toSql(Integer indent) {
    String sql = "";
    sql += this.generateWiths();
    sql += this.generateSelects();
    sql += this.generateFroms();
    sql += this.generateWheres();

    return sql.replaceAll("\n", "\n" + " ".repeat(indent));
  }

  private String generateWiths() {
    String sql = "";

    if (withs != null && !withs.isEmpty()) {
      sql += "WITH\n";
      sql += StringUtils.join(withs,",\n");
    }
    return sql;
  }

  private String generateSelects() {
    String sql = "\nSELECT ";

    if (selects != null && !selects.isEmpty()) sql += StringUtils.join(selects, ", ");
    else sql += this.alias + ".*";

    return sql;
  }

  private String generateFroms() {
    String sql = "\nFROM " + map.getTable() + " AS " + alias;

    if (joins != null && !joins.isEmpty()) sql += "\n" + StringUtils.join(joins, "\n");
    return sql;
  }

  private String generateWheres() {
    String sql = "";
    if (map.getCondition() != null || (wheres !=null && !wheres.isEmpty())) {
      sql += "\nWHERE ";

      if (map.getCondition() != null) {
        sql += map.getCondition().replaceAll("{alias}", alias) + "\n";
        if (wheres != null && !wheres.isEmpty()) {
          sql += "AND (\n";
        }
      }

      if (wheres !=null && !wheres.isEmpty()) {
        sql += StringUtils.join(wheres, "\n" + this.whereBool + " ");
        if (map.getCondition() != null) sql += ")\n";
      }
    }
    return sql;
  }

  public String getFieldName(String field, String table) {
    String alias = table != null ? table : this.alias;
    String fieldName = getField(field, table).getField();

    if (fieldName.contains("{alias}")) return fieldName.replaceAll("{alias}", alias);
    else return alias + "." + fieldName;
  }

  public String getFieldType(String field, String table) {
    return getField(field, table).getType();
  }

  private Field getField(String field, String table) {
    Table map = table != null ? (mapData.typeTables as any)[table] : this.map;

    if (map != null) throw new Error("Unknown table [" + table + "]");

    if (map.getFields().get(field) != null) return map.getFields().get(field);

    System.out.println("UNKNOWN FIELD [" + field + "]");
//    console.log(JSON.stringify(map, null, 2));

    // Default to string field in JSON blob
    String fieldName = field.substring(field.indexOf("#") + 1);
    Field returnField = new Field();
    returnField.setField("(({alias}.json ->> '" + fieldName + "')::VARCHAR)");
    returnField.setType("string");
    return returnField;
  }

  public Relationship getRelationshipTo(String targetModel) {
    if (map.getRelationships().get(targetModel) != null) return map.getRelationships().get(targetModel);

    throw new Error("Unknown relationship from [" + this.model + "] to [" + targetModel + "]");
  }

  public SQLQuery clone(String alias) {
    String from = this.alias + ".";
    String to = alias + ".";
    SQLQuery clone = this.subQuery(this.model, alias);
    clone.withs.addAll(withs);
    clone.selects.addAll(selects.stream().map(j -> j.replaceAll(from, to)).toList());
    clone.joins.addAll(joins.stream().map(j -> j.replaceAll(from, to)).toList());
    clone.wheres.addAll(wheres.stream().map(j -> j.replaceAll(from, to)).toList());
    clone.whereBool = this.whereBool;

    return clone;
  }

  private Table getMap(String model) {
    Table map = (mapData.typeTables as any)[model];

    if (map == null) {
      map = (mapData.typeTables as any)["http://endhealth.info/im#" + model];
    }

    if (map != null) {
      return map;
    } else {
      throw new Error("Unmapped table " + model);
    }
  }

  public String getAlias(String tableName) {
    return tableName + SQLQuery.aliasIndex++;
  }
}



