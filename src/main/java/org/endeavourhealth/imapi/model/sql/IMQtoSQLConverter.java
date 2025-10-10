package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.mysql.MYSQLConnectionManager.getConnection;

@Slf4j
public class IMQtoSQLConverter {
  private final TableMap tableMap;
  private final QueryRequest queryRequest;
  private final ObjectMapper mapper = new ObjectMapper();

  @Getter
  private String sql;

  public IMQtoSQLConverter(QueryRequest queryRequest) throws SQLConversionException, JsonProcessingException {
    this.queryRequest = queryRequest;
    if (null == queryRequest.getLanguage()) queryRequest.setLanguage(DatabaseOption.MYSQL);

    try {
      String resourcePath = isPostgreSQL() ? "IMQtoSQL.json" : "IMQtoMYSQL.json";
      String text = Files.readString(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(resourcePath)).toURI()));
      tableMap = new ObjectMapper().readValue(text, TableMap.class);
    } catch (Exception e) {
      log.error("Could not parse datamodel map!");
      throw new RuntimeException(e);
    }
    IMQtoSQL();
  }

  private boolean isPostgreSQL() {
    return queryRequest.getLanguage().equals(DatabaseOption.POSTGRESQL);
  }

  public void IMQtoSQL() throws SQLConversionException, JsonProcessingException {
    if (queryRequest.getQuery() == null) throw new SQLConversionException("Query is null");
    Query definition = queryRequest.getQuery();
    if ((definition.getTypeOf() == null || definition.getTypeOf().getIri() == null) && definition.getPath() != null) {
      definition.setTypeOf(definition.getPath().getFirst().getIri());
    }

    try {
      StringBuilder sql = new StringBuilder();
      if (definition.getColumnGroup() != null) {
        for (Match dataset : definition.getColumnGroup()) {
          if (null != definition.getTypeOf() && null != definition.getTypeOf().getIri()) {
            String typeOf = (null != dataset.getPath() && null != dataset.getPath().getFirst())
              ? dataset.getPath().getFirst().getIri()
              : definition.getTypeOf().getIri();
            SQLQuery qry = new SQLQuery().create(typeOf, null, tableMap, null);
            if (definition.getInstanceOf() != null)
              addDatasetInstanceOf(qry, definition.getInstanceOf());
            if (dataset.getAnd() != null || dataset.getOr() != null || dataset.getNot() != null)
              addDatasetSubQuery(qry, dataset, typeOf);
            if (null != dataset.getWhere())
              addIMQueryToSQLQueryRecursively(qry, dataset, Bool.and);
            if (dataset.getReturn() != null) {
              addSelectFromReturnRecursively(qry, dataset.getReturn(), null, typeOf, null, false);
            }
            if (null != definition.getIsCohort()) {
              convertIsCohort(qry, definition.getIsCohort(), definition.getTypeOf(), Bool.and);
            }
            sql.append(qry.toSql(2)).append(";\n\n");
          } else if (null != dataset.getReturn().getFunction() && null != dataset.getIsCohort()) {
            String sqlfn = getFunction(dataset.getReturn().getFunction());
            if (null != sqlfn) {
              String from = getTableNameFromIri(dataset.getIsCohort().getIri());
              SQLQuery qry = new SQLQuery().create(null, null, tableMap, from);
              qry.getSelects().add(sqlfn);
              sql.append(qry.toSql(2)).append(";\n\n");
            }
          }
        }
      } else {
        if (null == definition.getTypeOf() || null == definition.getTypeOf().getIri())
          throw new SQLConversionException("SQL Conversion Error: Cohort Query typeOf is null");
        SQLQuery qry = new SQLQuery().create(definition.getTypeOf().getIri(), null, tableMap, null);
        addBooleanMatchesToSQL(qry, definition);
        if (null != definition.getIsCohort()) {
          convertIsCohort(qry, definition.getIsCohort(), null, Bool.and);
        }
        sql = new StringBuilder(qry.toSql(2));
      }
      this.sql = sql.toString();
    } catch (SQLConversionException | JsonProcessingException e) {
      log.error("SQL Conversion Error: {}", e.getMessage());
      throw e;
    }
  }

  private void addDatasetSubQuery(SQLQuery qry, Match dataset, String typeOf) throws SQLConversionException, JsonProcessingException {
    String variable = getVariableFromMatch(dataset);
    SQLQuery subQuery = qry.subQuery(typeOf, variable, tableMap, null);
    addBooleanMatchesToSQL(subQuery, dataset);
    if (subQuery.getWiths() == null)
      subQuery.setWiths(new ArrayList<>());
    qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");
    String joiner = "JOIN ";
    qry.getJoins().add(createJoin(qry, subQuery, joiner));
  }

  private void addDatasetInstanceOf(SQLQuery qry, List<Node> instanceOf) throws SQLConversionException, JsonProcessingException {
    SQLQuery cohortQry = convertMatchToQuery(qry, new Match().setInstanceOf(instanceOf), Bool.and);
    qry.getWiths().addAll(cohortQry.getWiths());
    cohortQry.setWiths(new ArrayList<>());
    qry.getWiths().add(cohortQry.getAlias() + " AS (" + cohortQry.toSql(2) + "\n)");
    String joiner = "JOIN ";
    qry.getJoins().add(createJoin(qry, cohortQry, joiner));
  }

  private void addBooleanMatchesToSQL(SQLQuery qry, Match definition) throws SQLConversionException, JsonProcessingException {
    if (definition.getAnd() != null) {
      for (Match match : definition.getAnd()) {
        addIMQueryToSQLQueryRecursively(qry, match, Bool.and);
      }
    }
    if (definition.getOr() != null) {
      for (Match match : definition.getOr()) {
        addIMQueryToSQLQueryRecursively(qry, match, Bool.or);
      }
    }
    if (definition.getNot() != null) {
      for (Match match : definition.getNot()) {
        addIMQueryToSQLQueryRecursively(qry, match, Bool.not);
      }
    }
  }

  private void addSelectFromReturnRecursively(SQLQuery qry, Return aReturn, ReturnProperty parentProperty, String gParentTypeOf, String tableAlias, boolean isNested) throws SQLConversionException, JsonProcessingException {
    if (aReturn.getProperty() != null) {
      for (ReturnProperty property : aReturn.getProperty()) {
        if (property.getReturn() != null) {
          addNestedProperty(qry, property, parentProperty, gParentTypeOf);
        } else if (property.getAs() != null) {
          if (property.getAs().equals("Y-N")) {
            if (parentProperty == null) {
              addRootYNCase(qry);
            } else {
              addYNCase(qry, parentProperty, gParentTypeOf, tableAlias);
            }
          } else {
            if (isNested) {
              for (String fkey : qry.getGetForeignKeys())
                if (!qry.getSelects().contains(fkey))
                  qry.getSelects().add(fkey);
            } else if (null != property.getFunction()) {
              String select = getSelectFromFunction(qry, property.getFunction()) + " AS `" + property.getAs() + "`";
              qry.getSelects().add(select);
            } else {
              String select = qry.getFieldName(property.getIri(), null, tableMap, false) + " AS `" + property.getAs() + "`";
              qry.getSelects().add(select);
            }
          }
        }
      }
    } else if (aReturn.getFunction() != null && parentProperty != null) {
      String fn = getFunction(aReturn.getFunction());
      fn = fn.replaceAll("\\{propertyName}", getNameFromIri(parentProperty.getIri()));
      qry.getSelects().add(fn);
    }
  }

  private String getSelectFromFunction(SQLQuery qry, FunctionClause function) throws SQLConversionException, JsonProcessingException {
    if (function.getArgument() == null) {
      throw new SQLConversionException("SQL Conversion Error: Function without arguments:", mapper.writeValueAsString(function));
    }
    if (IM.CONCATENATE.asIri().getIri().equals(function.getIri())) {
      return getConcatSelect(qry, function);
    }
    throw new SQLConversionException("SQL Conversion Error: Function not implemented:", function.getIri());
  }

  private String getConcatSelect(SQLQuery qry, FunctionClause function) throws SQLConversionException, JsonProcessingException {
    StringBuilder sb = new StringBuilder();
    for (Argument arg : function.getArgument()) {
      String value = null;

      if ("text".equals(arg.getParameter())) {
        if (null != arg.getValuePath()) {
          String valuePath = arg.getValuePath().getIri();
          if (null != arg.getValuePath().getPath() && !arg.getValuePath().getPath().isEmpty()) {
            String path = arg.getValuePath().getPath().getFirst().getIri();
            String typeOf = arg.getValuePath().getTypeOf().getIri();
            try {
              value = getDirectPropertyFromArgument(qry, valuePath, path);
            } catch (SQLConversionException e) {
              value = getNestedPropertyFromArgument(qry, valuePath, typeOf, path);
            }
          } else
            value = getDirectPropertyFromArgument(qry, valuePath, null);
        }
      } else {
        throw new SQLConversionException("SQL Conversion Error: Function argument type not implemented:", mapper.writeValueAsString(arg));
      }

      if (value != null && !value.isBlank()) {
        if (!sb.isEmpty()) sb.append(", ");
        sb.append(value);
      }
    }
    return "CONCAT(" + sb + ")";
  }

  private String getDirectPropertyFromArgument(SQLQuery qry, String valuePath, String path) throws SQLConversionException {
    String property = null == path ? valuePath : valuePath + "_" + path;
    return qry.getFieldName(property, null, tableMap, false);
  }


  private String getFunction(FunctionClause function) throws SQLConversionException, JsonProcessingException {
    if (!tableMap.getFunctions().containsKey(function.getIri()))
      throw new SQLConversionException("SQL Conversion Error: Function not recognised: " + mapper.writeValueAsString(function));
    return tableMap.getFunctions().get(function.getIri());
  }

  private void addRootYNCase(SQLQuery qry) throws SQLConversionException {
    if (qry.getWiths().isEmpty()) {
      throw new SQLConversionException("SQL Conversion Error: No subquery found for root-level Y-N case");
    }
    String lastWith = qry.getWiths().getLast();
    String subQueryAlias = lastWith.substring(0, lastWith.indexOf(" AS "));
    String yes_no_select = "CASE WHEN EXISTS ( SELECT 1 FROM " + subQueryAlias + " ) " +
      "THEN 'Y' ELSE 'N' END AS `" + qry.getAlias() + "_exists`";
    qry.getSelects().add(yes_no_select);
  }

  private String getNestedPropertyFromArgument(SQLQuery qry, String valuePath, String typeOf, String path) throws SQLConversionException, JsonProcessingException {
    Table table = tableMap.getTable(valuePath);
    String tableAlias = getNameFromIri(valuePath) + "_" + qry.getAlias();
    String property = table.getFields().get(path).getField();
    String propertyName = property.replace("{alias}.", "");
    property = property.replace("{alias}", tableAlias);
    String with = """
      %s AS (
          SELECT %s, %s AS %s
          FROM %s AS %s
        )
      """.formatted(tableAlias, table.getPrimaryKey(), property, propertyName, table.getTable(), tableAlias);
    qry.getWiths().add(with);
    Relationship rel = qry.getRelationshipTo(typeOf);
    String from = rel.getFromField().contains("{alias}") ? rel.getFromField().replace("{alias}", qry.getAlias()) : qry.getAlias() + "." + rel.getFromField();
    String to = rel.getToField().contains("{alias}") ? rel.getToField().replace("{alias}", tableAlias) : tableAlias + "." + rel.getToField();
    String join = ("JOIN " + tableAlias + " ON " + from + " = " + to);
    qry.getJoins().add(join);
    return property;
  }

  private void addNestedProperty(SQLQuery qry, ReturnProperty property, ReturnProperty parentProperty, String gParentTypeOf) throws SQLConversionException, JsonProcessingException {
    Table table = tableMap.getTable(property.getIri());
    String typeOf = table.getDataModel();
    if (typeOf == null)
      throw new SQLConversionException("Property not mapped to datamodel: " + property.getIri());
    SQLQuery subQuery = qry.subQuery(typeOf, null, tableMap, null);
    addSelectFromReturnRecursively(subQuery, property.getReturn(), property, parentProperty != null ? parentProperty.getIri() : gParentTypeOf, subQuery.getAlias(), true);
    if (subQuery.getWiths() == null)
      subQuery.setWiths(new ArrayList<>());
    qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");
    qry.getSelects().addAll(getSelectsForParentQuery(subQuery.getSelects()));
    String joiner = "JOIN ";
    qry.getJoins().add(createJoin(qry, subQuery, joiner));
  }

  private void addYNCase(SQLQuery qry, ReturnProperty parentProperty, String gParentTypeOf, String tableAlias) throws SQLConversionException {
    Table parentTable = tableMap.getTable(parentProperty.getIri());
    Table gParentTable = tableMap.getTable(gParentTypeOf);
    Relationship rel = parentTable.getRelationships().get(gParentTable.getDataModel());
    if (rel == null)
      throw new SQLConversionException("Relationship between " + parentTable.getTable() + " and " + gParentTable.getTable() + "not found!");
    String whereFrom = rel.getFromField().replace("{alias}", tableAlias);
    String whereTo = gParentTable.getTable() + "." + rel.getToField();
    String yes_no_select = "CASE WHEN EXISTS ( SELECT 1 FROM %s WHERE %s = %s ) THEN 'Y' ELSE 'N' END AS %s";
    yes_no_select = String.format(yes_no_select, parentTable.getTable(), whereFrom, whereTo, tableAlias + "_exists");
    qry.getSelects().add(yes_no_select);
  }

  private List<String> getSelectsForParentQuery(List<String> originalSelects) {
    List<String> returnSelects = new ArrayList<>();
    for (String originalSelect : originalSelects) {
      String[] splits = originalSelect.split(" AS ");
      if (splits.length == 2)
        returnSelects.add(splits[1].strip());
      else returnSelects.add(originalSelect);
    }
    return returnSelects;
  }

  private String getIriLine(Set<String> stringIris) {
    StringJoiner iriLine = new StringJoiner(" ");
    for (String stringIri : stringIris) {
      iri(stringIri);
      iriLine.add(stringIri);
    }
    return "(" + iriLine + ")";
  }

  private void addIMQueryToSQLQueryRecursively(SQLQuery qry, Match match, Bool bool) throws SQLConversionException, JsonProcessingException {
    SQLQuery subQry = convertMatchToQuery(qry, match, bool);
    qry.getWiths().addAll(subQry.getWiths());
    subQry.setWiths(new ArrayList<>());
    qry.getWiths().add(subQry.getAlias() + " AS (" + subQry.toSql(2) + "\n)");

    String joiner = (bool == Bool.not) ? "LEFT JOIN " : "JOIN ";
    if (bool == Bool.not) qry.getWheres().add(subQry.getAlias() + ".id IS NULL");

    qry.getJoins().add(createJoin(qry, subQry, joiner));
    if (null != match.getThen())
      addIMQueryToSQLQueryRecursively(qry, match.getThen(), Bool.and);
  }

  private SQLQuery convertMatchToQuery(SQLQuery parent, Match match, Bool bool) throws SQLConversionException, JsonProcessingException {
    SQLQuery qry = createMatchQuery(match, parent);

    convertMatch(match, qry, bool);

    if (match.getOrderBy() != null) {
      wrapMatchPartition(qry, match.getOrderBy());
    }

    return qry;
  }

  private SQLQuery createMatchQuery(Match match, SQLQuery qry) throws SQLConversionException {
    String variable = getVariableFromMatch(match);
    if (match.getTypeOf() != null && !match.getTypeOf().getIri().equals(qry.getModel())) {
      return qry.subQuery(match.getTypeOf().getIri(), variable, tableMap, null);
    } else if (match.getNodeRef() != null && !match.getNodeRef().equals(qry.getModel())) {
      return qry.subQuery(match.getNodeRef(), variable, tableMap, null);
    } else if (match.getPath() != null) {
      return qry.subQuery(match.getPath().getFirst().getIri(), variable, tableMap, null);
    } else return qry.subQuery(qry.getModel(), variable, tableMap, null);
  }

  private String getVariableFromMatch(Match match) {
    if (match.getVariable() != null) {
      return match.getVariable();
    } else if (match.getKeepAs() != null) {
      return match.getKeepAs();
    } else return null;
  }

  private void convertMatch(Match match, SQLQuery qry, Bool bool) throws SQLConversionException, JsonProcessingException {
    if (match.getInstanceOf() != null) {
      convertInstanceOf(qry, match.getInstanceOf(), bool);
    } else if (null != match.getIsCohort()) {
      convertIsCohort(qry, match.getIsCohort(), null, bool);
    } else if (null != match.getAnd() || null != match.getOr() || null != match.getNot()) {
      convertMatchBoolSubMatch(qry, match, Bool.and);
    }
    if (match.getWhere() != null) convertMatchProperties(qry, match);
  }

  private void wrapMatchPartition(SQLQuery qry, OrderLimit order) throws SQLConversionException {
    if (order.getProperty() == null)
      throw new SQLConversionException("SQL Conversion Error: ORDER MUST HAVE A FIELD SPECIFIED\n" + order);
    SQLQuery inner = qry.clone(qry.getAlias() + "_inner", tableMap);
    String innerSql = qry.getAlias() + "_inner AS (" + inner.toSql(2) + ")";
    SQLQuery partition = qry.subQuery(qry.getModel(), qry.getAlias() + "_part", tableMap, null);
    String partField = isPostgreSQL() ? "((json ->> 'patient')::UUID)" : "patient_id";
    ArrayList<String> o = new ArrayList<>();
    for (OrderDirection property : order.getProperty()) {
      String dir = property.getDirection().toString().toUpperCase().startsWith("DESC") ? "DESC" : "ASC";
      o.add(partition.getFieldName(property.getIri(), null, tableMap, true) + " " + dir);
    }

    partition.getSelects().add("*");
    partition.getSelects().add("ROW_NUMBER() OVER (PARTITION BY " + partField + " ORDER BY " + StringUtils.join(o, ", ") + ") AS rn");

    qry.initialize(qry.getAlias() + "_part", qry.getAlias(), tableMap, null);
    qry.getWiths().add(innerSql);
    qry.getWiths().add(partition.getAlias() + " AS (" + partition.toSql(2) + "\n)");
    qry.getWheres().add(getRowNumberTest(order));
  }

  private String getRowNumberTest(OrderLimit order) {
    if (order.getLimit() > 1) {
      return "rn <= " + order.getLimit();
    }
    return "rn = " + order.getLimit();
  }

  private void convertInstanceOf(SQLQuery qry, List<Node> instanceOf, Bool bool) throws SQLConversionException {
    if (instanceOf.isEmpty())
      throw new SQLConversionException("SQL Conversion Error: MatchSet must have at least one element");
    String rsltTbl = getTableNameFromIri(instanceOf.getFirst().getIri());
    String fromField = getFromField(qry, instanceOf.getFirst());
    qry.getJoins().add(((bool == Bool.or || bool == Bool.not) ? "LEFT " : "") + "JOIN " + rsltTbl + " ON " + rsltTbl + ".id = " + fromField);
    if (bool == Bool.not) qry.getWheres().add(rsltTbl + ".id IS NULL");
  }

  private void convertIsCohort(SQLQuery qry, TTIriRef isCohort, Node cohortTypeOf, Bool bool) throws SQLConversionException {
    String rsltTbl = getTableNameFromIri(isCohort.getIri());
    String fromField = getFromField(qry, cohortTypeOf);
    qry.getJoins().add(((bool == Bool.or || bool == Bool.not) ? "LEFT " : "") + "JOIN " + rsltTbl + " ON " + rsltTbl + ".id = " + fromField);
    if (bool == Bool.not) qry.getWheres().add(rsltTbl + ".id IS NULL");
  }

  private String getFromField(SQLQuery qry, Node typeOf) throws SQLConversionException {
    String fromField = qry.getAlias() + ".id";
    if (null != typeOf && !typeOf.getIri().equals(qry.getMap().getDataModel())) {
      Relationship rel = qry.getMap().getRelationships().get(typeOf.getIri());
      if (rel == null)
        throw new SQLConversionException("SQL Conversion Error: Could not find relationship from [" + qry.getMap().getDataModel() + "] to [" + typeOf.getIri() + "]");
      fromField = rel.getFromField().replace("{alias}", qry.getAlias());
    }
    return fromField;
  }

  private String getTableNameFromIri(String iri) {
    return "`q_" + getNameFromIri(iri) + "`";
  }


  private void convertMatchBoolSubMatch(SQLQuery qry, Match match, Bool bool) throws SQLConversionException, JsonProcessingException {
    if (match.getAnd() != null) {
      List<Match> subMatches = match.getAnd();
      for (Match subMatch : subMatches) {
        convertSubQuery(qry, subMatch, Bool.and, "JOIN ");
      }
    }
    if (match.getOr() != null) {
      List<String> orConditions = new ArrayList<>();
      for (Match subMatch : match.getOr()) {
        SQLQuery subQuery = convertMatchToQuery(qry, subMatch, Bool.or);
        qry.getWiths().addAll(subQuery.getWiths());
        subQuery.setWiths(new ArrayList<>());
        qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");
        qry.getJoins().add(createJoin(qry, subQuery, "LEFT JOIN "));
        orConditions.add(subQuery.getAlias() + ".id IS NOT NULL");
      }
      if (!orConditions.isEmpty()) {
        qry.getWheres().add("(" + String.join(" OR ", orConditions) + ")");
      }
    }
    if (match.getNot() != null) {
      for (Match subMatch : match.getNot()) {
        convertSubQuery(qry, subMatch, Bool.not, "LEFT JOIN ");
      }
    }
  }

  private void convertSubQuery(SQLQuery qry, Match match, Bool bool, String joiner) throws SQLConversionException, JsonProcessingException {
    SQLQuery subQuery = convertMatchToQuery(qry, match, bool);
    qry.getWiths().addAll(subQuery.getWiths());
    subQuery.setWiths(new ArrayList<>());
    qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");
    qry.getJoins().add(createJoin(qry, subQuery, joiner));
    if ("OR".equals(qry.getWhereBool()) && !"LEFT JOIN".equals(joiner)) {
      qry.getWheres().add(subQuery.getAlias() + ".id IS NOT NULL");
    }
  }

  private String createJoin(SQLQuery qry, SQLQuery subQry, String joiner) throws SQLConversionException {
    if (qry.getModel().equals(subQry.getModel())) {
      return (joiner + subQry.getAlias() + " ON " + subQry.getAlias() + ".id = " + qry.getAlias() + ".id");
    } else {
      Relationship rel = subQry.getRelationshipTo(qry.getModel());
      String from = rel.getFromField().contains("{alias}") ? rel.getFromField().replace("{alias}", subQry.getAlias()) : subQry.getAlias() + "." + rel.getFromField();
      String to = rel.getToField().contains("{alias}") ? rel.getToField().replace("{alias}", qry.getAlias()) : qry.getAlias() + "." + rel.getToField();
      return (joiner + subQry.getAlias() + " ON " + from + " = " + to);
    }
  }

  private void convertMatchProperties(SQLQuery qry, Match match) throws SQLConversionException, JsonProcessingException {
    if (match.getWhere() == null) {
      throw new SQLConversionException("INVALID MatchProperty\n" + match);
    }
    convertMatchProperty(qry, match.getWhere());
  }

  private void convertMatchProperty(SQLQuery qry, Where property) throws SQLConversionException, JsonProcessingException {
    if (property.getIs() != null) {
      if (!property.isNot())
        convertMatchPropertyIs(qry, property, property.getIs(), false);
      else
        throw new SQLConversionException("SQL Conversion Error: Where Property IS NOT NOT SUPPORTED\n" + mapper.writeValueAsString(property));
    } else if (property.getRange() != null) {
      convertMatchPropertyRange(qry, property);
    } else if (property.getRelativeTo() != null && !isRelativeToFunctionParam(property)) {
      convertMatchPropertyRelative(qry, property);
    } else if (property.getValue() != null) {
      convertMatchPropertyValue(qry, property);
    } else if (property.getAnd() != null) {
      convertMatchPropertyBool(qry, property, Bool.and);
    } else if (property.getOr() != null) {
      convertMatchPropertyBool(qry, property, Bool.or);
    } else if (property.getIsNull()) {
      convertMatchPropertyNull(qry, property);
    } else if (property.getFunction() != null) {
      qry.getWheres().add(getWhereFromFunction(qry, property));
    } else
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED PROPERTY PATTERN\n" + mapper.writeValueAsString(property));

    if (null != property.getFunction()) {
      resolveFunctionArgs(qry, property.getFunction());
    }
  }

  private String getWhereFromFunction(SQLQuery qry, Where property) throws SQLConversionException, JsonProcessingException {
    String where = "";
    Assignable range = new Value().setValue(property.getValue()).setOperator(property.getOperator()).setUnits(property.getUnits());
    if (IM.NUMERIC_DIFFERENCE.asIri().getIri().equals(property.getFunction().getIri()))
      where = getWhereFromNumericDifference(property, qry.getFieldName(property.getIri(), null, tableMap, true));
    else {
      String mysqlFunction = getFunction(property.getFunction());
      where = mysqlFunction + " " + range.getOperator().getValue() + " " + range.getValue() + ")";
    }
    return where;
  }

  private void resolveFunctionArgs(SQLQuery qry, FunctionClause function) throws SQLConversionException {
    Map<String, String> paramsToValue = new HashMap<>();
    for (Argument argument : function.getArgument()) {
      String value = getArgumentValueForFunction(qry, argument);
      paramsToValue.put(argument.getParameter(), value);
    }
    qry.getWheres().replaceAll(where -> {
      String updated = where;
      for (Map.Entry<String, String> entry : paramsToValue.entrySet()) {
        updated = updated.replace("{" + entry.getKey() + "}", entry.getValue());
      }
      return updated;
    });
  }

  private String getArgumentValueForFunction(SQLQuery qry, Argument argument) throws SQLConversionException {
    if (null != argument.getValuePath()) {
      return qry.getFieldName(argument.getValuePath().getIri(), null, tableMap, true);
    } else if (null != argument.getValueParameter()) {
      return argument.getValueParameter();
    } else if (null != argument.getValueIri()) {
      if ("units".equals(argument.getParameter())) return getUnitName(argument.getValueIri());
      return argument.getValueIri().getIri();
    } else if (null != argument.getValueData()) {
      return argument.getValueData();
    }
    throw new SQLConversionException("Argument type not implemented");
  }

  private void convertMatchPropertyIs(SQLQuery qry, Where property, List<Node> list, boolean inverse) throws SQLConversionException {
    if (list == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyIs\n" + property);
    }
    String propertyName = getNameFromIri(property.getIri());
    String concept_alias = "c_" + propertyName;
    String csm_alias = "csm_" + propertyName;

    String joins = """
            JOIN concept `{concept_alias}` ON `{concept_alias}`.dbid = {join_condition}
            JOIN concept_set_member `{csm_alias}` ON `{csm_alias}`.im1id = `{concept_alias}`.id
      """;
    String conditions = "({conditions})";
    joins = joins.replaceAll("\\{concept_alias}", concept_alias).replaceAll("\\{csm_alias}", csm_alias);

    if (!list.isEmpty()) {
      String fieldName = qry.getFieldName(property.getIri(), null, tableMap, true);
      List<String> stringConditions = getIriConditions(csm_alias, list, inverse);
      String conditionsSQL = StringUtils.join(stringConditions, " OR ");
      joins = joins.replace("{join_condition}", fieldName).replace("{conditions}", conditionsSQL);
      conditions = conditions.replace("{join_condition}", fieldName).replace("{conditions}", conditionsSQL);
      qry.getWheres().add(conditions);
      qry.getJoins().add(joins);
    }
  }

  private List<String> getIriConditions(String csmAlias, List<Node> list, boolean inverse) {
    String operator = inverse ? "!=" : "=";
    return list.stream()
      .map(node -> {
        try {
          String csm_table = "`" + csmAlias + "`";
          String condition = csm_table + "." + getJoiningProperty(node) + " " + operator + " '" + node.getIri() + "'";
          if (node.isDescendantsOf() || node.isMemberOf())
            condition = "(" + condition + " AND " + csm_table + ".self = 0)";
          else if (node.isDescendantsOrSelfOf()) {
            // nothing
          } else if (node.isAncestorsOf()) {
            // not implemented yet
          } else condition = "(" + condition + " AND " + csm_table + ".self = 1)";
          return condition;
        } catch (SQLConversionException e) {
          throw new RuntimeException(e);
        }
      }).toList();
  }

  private String getJoiningProperty(Node node) throws SQLConversionException {
    if (node.isMemberOf()) {
      return "set";
    } else if (node.isDescendantsOf()) {
      return "set";
    } else if (node.isAncestorsOrSelfOf()) {
      return "set";
    } else if (node.isAncestorsOf()) {
      throw new SQLConversionException("SQL Conversion Error: Ancestor joining currently not supported");
    } else return "set";
  }

  private void convertMatchPropertyRange(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getRange() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyRange\n" + property);
    }
    String fieldType = qry.getFieldType(property.getIri(), null, tableMap, true);
    qry.getWheres().add(convertMatchPropertyRangeNode(qry.getFieldName(property.getIri(), null, tableMap, true), fieldType, property.getRange()));
  }

  private String convertMatchPropertyRangeNode(String fieldName, String fieldType, Range range) throws SQLConversionException {

    String from = null != range.getFrom().getValue() ? range.getFrom().getValue() : range.getFrom().getValueParameter();
    String to = null != range.getTo().getValue() ? range.getTo().getValue() : range.getTo().getValueParameter();
    if ("date".equals(fieldType)) {
      from = "'" + from + "'";
      to = "'" + to + "'";
    }
    return fieldName + " BETWEEN " + from + " AND " + to;
  }

  private String convertMatchPropertyDateValue(String fieldName, Assignable range) throws SQLConversionException, JsonProcessingException {
    if (range.getUnits() == null)
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyRange\n" + mapper.writeValueAsString(range));
    return "DATE_SUB($searchDate" + ", INTERVAL " + range.getValue() + " " + getUnitName(range.getUnits()) + ") ";
  }

  private void convertMatchPropertyRelative(SQLQuery qry, Where property) throws SQLConversionException, JsonProcessingException {
    if (property.getIri() == null || property.getRelativeTo() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyRelative\n" + mapper.writeValueAsString(property));
    }

    if (property.getRelativeTo().getParameter() != null)
      qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap, true) + " " + property.getOperator().getValue() + " " + convertMatchPropertyRelativeTo(qry, property, property.getRelativeTo().getParameter()));
    else if (property.getRelativeTo().getNodeRef() != null) {
      qry.getJoins().add("JOIN " + property.getRelativeTo().getNodeRef() + " ON " + property.getRelativeTo().getNodeRef() + ".id = " + qry.getAlias() + ".id");
      qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap, true) + " " + property.getOperator().getValue() + " " + convertMatchPropertyRelativeTo(qry, property, qry.getFieldName(property.getRelativeTo().getIri(), property.getRelativeTo().getNodeRef(), tableMap, true)));
    } else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED RELATIVE COMPARISON\n" + mapper.writeValueAsString(property));
    }
  }

  private boolean isRelativeToFunctionParam(Where property) {
    if (null == property.getFunction() || null == property.getRelativeTo() || (null == property.getRelativeTo().getParameter() && null == property.getRelativeTo().getNodeRef()))
      return false;
    return property.getFunction().getArgument().stream()
      .anyMatch(arg -> (argIsRelativeToParam(arg, property) || argIsRelativeToNodeRef(arg, property)));
  }

  private boolean argIsRelativeToParam(Argument arg, Where property) {
    return null != property.getRelativeTo().getParameter() && property.getRelativeTo().getParameter().equals(arg.getValueParameter());
  }

  private boolean argIsRelativeToNodeRef(Argument arg, Where property) {
    return null != property.getRelativeTo().getNodeRef() && property.getRelativeTo().getNodeRef().equals(arg.getValueNodeRef());
  }

  private String convertMatchPropertyRelativeTo(SQLQuery qry, Where property, String field) throws
    SQLConversionException, JsonProcessingException {
    String fieldType = qry.getFieldType(property.getIri(), null, tableMap, true);
    TTIriRef units = new TTIriRef();
    if (null != property.getUnits()) units = property.getUnits();
    else if (null != property.getRelativeTo().getQualifier()) {
      units = property.getRelativeTo().getQualifier();
    }
    if ("date".equals(fieldType)) {
      if (property.getValue() != null) {
        return "(" + field + " + INTERVAL " + property.getValue() + " " + getUnitName(units) + ")";
      } else return field;
    } else if ("number".equals(fieldType)) {
      return "(" + field + " + INTERVAL " + property.getValue() + " " + getUnitName(units) + ")";
    } else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED RELATIVE TYPE (" + fieldType + ")\n" + property.getIri());
    }
  }

  private void convertMatchPropertyValue(SQLQuery qry, Where property) throws SQLConversionException, JsonProcessingException {
    if (property.getIri() == null || property.getValue() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyValue\n" + property);
    }
    String where = "";
    if ("date".equals(qry.getFieldType(property.getIri(), null, tableMap, true))) {
      Assignable range = new Value().setValue(property.getValue()).setOperator(property.getOperator()).setUnits(property.getUnits());
      if (null != property.getFunction()) {
        where = getWhereFromFunction(qry, property);
      } else if (null != range.getUnits()) {
        where = convertMatchPropertyDateValue(qry.getFieldName(property.getIri(), null, tableMap, true), range);
      } else {
        where = qry.getFieldName(property.getIri(), null, tableMap, true) + " " + property.getOperator().getValue() + " " + property.getValue();
      }
    } else {
      where = qry.getFieldName(property.getIri(), null, tableMap, true) + " " + property.getOperator().getValue() + " " + property.getValue();
    }
    if (property.isAncestorsOf() || property.isDescendantsOf() || property.isDescendantsOrSelfOf()) {
      where += " -- TCT\n";
    }
    qry.getWheres().add(where);
  }

  private String getWhereFromNumericDifference(Where property, String propertyName) throws SQLConversionException {
    String unit = getUnitName(property.getQualifier());
    String valueStr = property.getValue();
    int value = 0;
    if (valueStr != null && !valueStr.isBlank()) {
      try {
        value = Integer.parseInt(valueStr.trim());
      } catch (NumberFormatException e) {
        throw new SQLConversionException("Invalid numeric value for date offset: " + valueStr);
      }
    }
    String sign = (value < 0) ? "-" : "+";
    String interval = "INTERVAL " + Math.abs(value) + " " + (unit.equals("FISCAL_YEAR") ? "YEAR" : unit);
    String relativeTo = property.getRelativeTo().getParameter();
    String offsetExpr = (value == 0)
      ? relativeTo
      : String.format("%s %s %s", relativeTo, sign, interval);

    StringBuilder sql = new StringBuilder();
    switch (unit) {
      case "YEAR" -> sql.append(String.format(
        "YEAR(%s) = YEAR(%s)",
        propertyName, offsetExpr
      ));
      case "MONTH" -> sql.append(String.format(
        "YEAR(%s) = YEAR(%s)\n  AND MONTH(%s) = MONTH(%s)",
        propertyName, offsetExpr, propertyName, offsetExpr
      ));
      case "DAY" -> sql.append(String.format(
        "DATE(%s) = DATE(%s)",
        propertyName, offsetExpr
      ));
      case "FISCAL_YEAR" -> {
        int fiscalStartMonth = 4; // April = start of fiscal year (configurable)
        String fiscalExpr = String.format(
          "CASE WHEN MONTH(%s) >= %d THEN YEAR(%s) ELSE YEAR(%s) - 1 END",
          propertyName, fiscalStartMonth, propertyName, propertyName
        );
        String fiscalOffsetExpr = String.format(
          "CASE WHEN MONTH(%s) >= %d THEN YEAR(%s) ELSE YEAR(%s) - 1 END%s%s",
          relativeTo, fiscalStartMonth, relativeTo, relativeTo,
          (value == 0 ? "" : (value < 0 ? "-" : "+")),
          (value == 0 ? "" : Math.abs(value))
        );
        sql.append(String.format("%s = %s", fiscalExpr, fiscalOffsetExpr));
      }

      default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
    }

    return sql.toString();
  }


  private void convertMatchPropertyBool(SQLQuery qry, Where property, Bool bool) throws
    SQLConversionException, JsonProcessingException {
    SQLQuery subQuery = qry.subQuery(qry.getModel(), qry.getAlias(), tableMap, null);
    if (bool == Bool.and) {
      for (Where p : property.getAnd()) {
        convertMatchProperty(subQuery, p);
      }
    } else if (bool == Bool.or) {
      for (Where p : property.getOr()) {
        convertMatchProperty(subQuery, p);
      }
    }
    if (!subQuery.getWheres().isEmpty())
      qry.getWheres().add("(" + StringUtils.join(subQuery.getWheres(), " " + bool.toString().toUpperCase() + " ") + ")");
    if (!subQuery.getJoins().isEmpty()) {
      for (String join : subQuery.getJoins()) {
        ArrayList<String> joinsToAdd = new ArrayList<>();
        if (!qry.getJoins().contains(join)) joinsToAdd.add(join);
        qry.getJoins().add(StringUtils.join(joinsToAdd, " "));
      }
    }
  }

  private void convertMatchPropertyNull(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyNull\n" + property);
    }

    qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap, true) + " IS NULL");
  }

  private String getUnitName(TTIriRef iriRef) throws SQLConversionException {
    return switch (IM.from(iriRef.getIri())) {
      case IM.YEARS -> "YEAR";
      case IM.MONTHS, IM.MONTH -> "MONTH";
      case IM.DAYS -> "DAY";
      case IM.HOURS -> "HOUR";
      case IM.MINUTES -> "MINUTE";
      case IM.SECONDS -> "SECOND";
      case IM.FISCAL_YEAR -> "FISCAL_YEAR";
      default -> "";
    };
  }

  private List<String> getDBIDs(List<String> im1ids) throws SQLConversionException {
    String sql = """
      SELECT c.dbid, c.id FROM concept c
      WHERE c.id IN (%s);
      """.formatted("'" + StringUtils.join(im1ids, "', '") + "'");
    try (Connection executeConnection = getConnection();
         PreparedStatement statement = executeConnection.prepareStatement(sql)) {
      ResultSet rs = statement.executeQuery();
      List<String> results = new ArrayList<>();
      while (rs.next()) {
        results.add(rs.getString("c.dbid"));
      }
      if (results.isEmpty())
        throw new SQLConversionException("No IM1IDs found for '" + StringUtils.join(im1ids, "',\n'") + "'");
      return results;
    } catch (SQLException e) {
      log.error("Error running SQL [{}]", sql);
      throw new SQLConversionException("SQL Conversion Error: SQLException for getting im1ids\n" + StringUtils.join(im1ids, ","), e);
    }
  }

  public static String toMysqlDate(String dateStr) {
    List<String> POSSIBLE_PATTERNS = Arrays.asList(
      "dd/MM/yyyy",
      "dd-MM-yyyy",
      "yyyy/MM/dd",
      "yyyy-MM-dd"
    );

    for (String pattern : POSSIBLE_PATTERNS) {
      try {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.parse(dateStr, fmt);
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
      } catch (DateTimeParseException ignored) {
        // Try the next pattern
      }
    }
    throw new IllegalArgumentException("Unrecognized date format: " + dateStr);
  }

  public String getResolvedSql(Map<String, Integer> queryIrisToHashCodes) {
    String resolvedSql = this.sql;
    if (queryRequest.getArgument() != null) for (Argument arg : queryRequest.getArgument()) {
      String pattern = Pattern.quote(arg.getParameter());
      if (arg.getValueData() != null)
        resolvedSql = resolvedSql.replaceAll(pattern, "'" + arg.getValueData() + "'");
      else if (arg.getValueIri() != null) {
        iri(arg.getValueIri().getIri());
        resolvedSql = resolvedSql.replaceAll(pattern, "'" + arg.getValueIri().getIri() + "'");
      } else if (arg.getValueIriList() != null) {
        Set<String> setOfIris = arg.getValueIriList().stream().map(TTIriRef::getIri).collect(Collectors.toSet());
        resolvedSql = resolvedSql.replaceAll(pattern, getIriLine(setOfIris));
      }
    }

    if (queryIrisToHashCodes != null && !queryIrisToHashCodes.isEmpty()) {
      for (String iri : queryIrisToHashCodes.keySet()) {
        resolvedSql = resolvedSql.replace("q_" + getNameFromIri(iri), String.valueOf(queryIrisToHashCodes.get(iri)));
      }
    }
    return resolvedSql;
  }

  public String getNameFromIri(String iri) {
    if (iri == null) throw new IllegalArgumentException("iri is null");
    String[] splits = iri.split("#");
    if (splits.length == 2) {
      return splits[1];
    }
    return splits[0];
  }

}
