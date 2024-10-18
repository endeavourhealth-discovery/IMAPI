package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class SQLQuery {
  private static Integer aliasIndex = 0;

  public SQLQuery create(String model, String variable, HashMap<String, Table> tableMap) {
    aliasIndex = 0;
    SQLQuery result = new SQLQuery();
    result.initialize(model, variable, tableMap);
    return result;
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

  public SQLQuery subQuery(String model, String variable, HashMap<String, Table> tableMap) {
    SQLQuery result = new SQLQuery();
    result.initialize(model, variable, tableMap);
    return result;
  }

  public void initialize(String model, String variable, HashMap<String, Table> tableMap) {
    this.withs = new ArrayList<>();
    this.selects = new ArrayList<>();
    this.joins = new ArrayList<>();
    this.whereBool = "AND";
    this.wheres = new ArrayList<>();
    this.dependencies = new ArrayList<>();

    this.model = model;
    this.map = this.getMap(model, tableMap);
    this.alias = variable != null ? variable : getAlias(map.getTable());

    tableMap.put(this.alias, new Table(this.alias, null, this.map.getFields(), this.map.getRelationships()));
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
        sql += map.getCondition().replaceAll("\\{alias}", alias) + "\n";
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

  public String getFieldName(String field, String table, HashMap<String, Table> tableMap) {
    String alias = table != null ? table : this.alias;
    String fieldName = getField(field, table, tableMap).getField();

    if (fieldName.contains("{alias}")) return fieldName.replaceAll("\\{alias}", alias);
    else return alias + "." + fieldName;
  }

  public String getFieldType(String field, String table, HashMap<String, Table> tableMap) {
    return getField(field, table, tableMap).getType();
  }

  private Field getField(String field, String table, HashMap<String, Table> tableMap) {
    Table map = table != null ? tableMap.get(table) : this.map;

    if (map == null) throw new Error("Unknown table [" + table + "]");

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

  public SQLQuery clone(String alias, HashMap<String, Table> tableMap) {
    String from = this.alias + ".";
    String to = alias + ".";
    SQLQuery clone = this.subQuery(this.model, alias, tableMap);
    clone.withs.addAll(withs);
    clone.selects.addAll(selects.stream().map(j -> j.replaceAll(from, to)).toList());
    clone.joins.addAll(joins.stream().map(j -> j.replaceAll(from, to)).toList());
    clone.wheres.addAll(wheres.stream().map(j -> j.replaceAll(from, to)).toList());
    clone.whereBool = this.whereBool;

    return clone;
  }

  private Table getMap(String model, HashMap<String, Table> tableMap) {
    Table map = tableMap.get(model);

    if (map == null) {
      map = tableMap.get(IM.NAMESPACE + model);
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



