package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

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
      tableMap = new MappingParser().parse(resourcePath);
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

    initializeQueryTypeOf(definition);

    try {
      StringBuilder sql = new StringBuilder();
      if (definition.getColumnGroup() != null) {
        sql.append(processColumnGroup(definition));
      } else {
        sql.append(processSingleQuery(definition));
      }
      this.sql = sql.toString();
    } catch (SQLConversionException | JsonProcessingException e) {
      log.error("SQL Conversion Error: {}", e.getMessage());
      throw e;
    }
  }

  private void initializeQueryTypeOf(Query definition) {
    if ((definition.getTypeOf() == null || definition.getTypeOf().getIri() == null) && definition.getPath() != null) {
      definition.setTypeOf(definition.getPath().getFirst().getIri());
    }
  }

  private String processColumnGroup(Query definition) throws SQLConversionException, JsonProcessingException {
    StringBuilder sql = new StringBuilder();
    for (Match dataset : definition.getColumnGroup()) {
      sql.append(processDataset(dataset, definition)).append(";\n\n");
    }
    return sql.toString();
  }

  private String processDataset(Match dataset, Query definition) throws SQLConversionException, JsonProcessingException {
    if (null != definition.getTypeOf() && null != definition.getTypeOf().getIri()) {
      return processDatasetWithTypeOf(dataset, definition);
    } else if (null != dataset.getReturn().getFunction() && null != dataset.getIs()) {
      return processDatasetWithFunction(dataset);
    }
    return "";
  }

  private String processDatasetWithTypeOf(Match dataset, Query definition) throws SQLConversionException, JsonProcessingException {
    String typeOf = getDatasetTypeOf(dataset, definition);
    SQLQuery qry = new SQLQuery().create(typeOf, null, tableMap, null);

    applyDatasetFilters(qry, dataset, definition);

    if (dataset.getReturn() != null) {
      addSelectFromReturnRecursively(qry, dataset.getReturn(), null, typeOf, null, false);
    }

    if (null != definition.getIs()) {
      convertIsCohort(qry, definition.getIs().get(0), definition.getTypeOf(), Bool.and);
    }

    return qry.toSql(2);
  }

  private String getDatasetTypeOf(Match dataset, Query definition) {
    return (null != dataset.getPath() && null != dataset.getPath().getFirst())
      ? dataset.getPath().getFirst().getTypeOf().getIri()
      : definition.getTypeOf().getIri();
  }

  private void applyDatasetFilters(SQLQuery qry, Match dataset, Query definition) throws SQLConversionException, JsonProcessingException {
    if (definition.getIs() != null) {
      addDatasetInstanceOf(qry, definition.getIs());
    }
    if (dataset.getAnd() != null || dataset.getOr() != null || dataset.getNot() != null) {
      addDatasetSubQuery(qry, dataset, getDatasetTypeOf(dataset, definition));
    }
    if (null != dataset.getWhere()) {
      addIMQueryToSQLQueryRecursively(qry, dataset, Bool.and);
    }
  }

  private String processDatasetWithFunction(Match dataset) throws SQLConversionException, JsonProcessingException {
    String sqlfn = getFunction(dataset.getReturn().getFunction());
    if (null != sqlfn) {
      String from = getTableNameFromIri(dataset.getIs().getFirst().getIri());
      SQLQuery qry = new SQLQuery().create(null, null, tableMap, from);
      qry.getSelects().add(sqlfn);
      return qry.toSql(2);
    }
    return "";
  }

  private String processSingleQuery(Query definition) throws SQLConversionException, JsonProcessingException {
    validateSingleQueryTypeOf(definition);

    SQLQuery qry = new SQLQuery().create(definition.getTypeOf().getIri(), null, tableMap, null);
    addBooleanMatchesToSQL(qry, definition);

    if (null != definition.getIs()) {
      convertIsCohort(qry, definition.getIs().getFirst(), null, Bool.and);
    }

    return qry.toSql(2);
  }

  private void validateSingleQueryTypeOf(Query definition) throws SQLConversionException {
    if (null == definition.getTypeOf() || null == definition.getTypeOf().getIri()) {
      throw new SQLConversionException("SQL Conversion Error: Cohort Query typeOf is null");
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

  private void addDatasetInstanceOf(SQLQuery qry, List<Node> is) throws SQLConversionException, JsonProcessingException {
    SQLQuery cohortQry = convertMatchToQuery(qry, new Match().setIs(is), Bool.and);
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

  private void addSelectFromReturnRecursively(SQLQuery qry, Return aReturn, ReturnProperty parentProperty,
                                              String gParentTypeOf, String tableAlias, boolean isNested)
    throws SQLConversionException, JsonProcessingException {

    if (aReturn.getProperty() != null) {
      processReturnProperties(qry, aReturn.getProperty(), parentProperty, gParentTypeOf, tableAlias, isNested);
    } else if (aReturn.getFunction() != null && parentProperty != null) {
      processReturnFunction(qry, aReturn.getFunction(), parentProperty);
    }
  }

  private void processReturnProperties(SQLQuery qry, List<ReturnProperty> properties,
                                       ReturnProperty parentProperty, String gParentTypeOf,
                                       String tableAlias, boolean isNested)
    throws SQLConversionException, JsonProcessingException {

    for (ReturnProperty property : properties) {
      if (property.getReturn() != null) {
        addNestedProperty(qry, property, parentProperty, gParentTypeOf);
      } else if (property.getAs() != null) {
        processReturnPropertyAs(qry, property, parentProperty, gParentTypeOf, tableAlias, isNested);
      }
    }
  }

  private void processReturnPropertyAs(SQLQuery qry, ReturnProperty property,
                                       ReturnProperty parentProperty, String gParentTypeOf,
                                       String tableAlias, boolean isNested)
    throws SQLConversionException, JsonProcessingException {

    if (property.getAs().equals("Y-N")) {
      processYNCase(qry, parentProperty, gParentTypeOf, tableAlias);
    } else {
      processRegularAs(qry, property, isNested);
    }
  }

  private void processYNCase(SQLQuery qry, ReturnProperty parentProperty,
                             String gParentTypeOf, String tableAlias)
    throws SQLConversionException {

    if (parentProperty == null) {
      addRootYNCase(qry);
    } else {
      addYNCase(qry, parentProperty, gParentTypeOf, tableAlias);
    }
  }

  private void processRegularAs(SQLQuery qry, ReturnProperty property, boolean isNested)
    throws SQLConversionException, JsonProcessingException {

    if (isNested) {
      addForeignKeysToSelect(qry);
    } else if (null != property.getFunction()) {
      addFunctionSelect(qry, property);
    } else {
      addPropertySelect(qry, property);
    }
  }

  private void addForeignKeysToSelect(SQLQuery qry) {
    for (String fkey : qry.getGetForeignKeys()) {
      if (!qry.getSelects().contains(fkey)) {
        qry.getSelects().add(fkey);
      }
    }
  }

  private void addFunctionSelect(SQLQuery qry, ReturnProperty property)
    throws SQLConversionException, JsonProcessingException {

    String select = getSelectFromFunction(qry, property.getFunction()) + " AS `" + property.getAs() + "`";
    qry.getSelects().add(select);
  }

  private void addPropertySelect(SQLQuery qry, ReturnProperty property) throws SQLConversionException {
    String select = qry.getFieldName(property.getIri(), null, tableMap, false) + " AS `" + property.getAs() + "`";
    qry.getSelects().add(select);
  }

  private void processReturnFunction(SQLQuery qry, FunctionClause function, ReturnProperty parentProperty)
    throws SQLConversionException, JsonProcessingException {

    String fn = getFunction(function);
    fn = fn.replaceAll("\\{propertyName}", getNameFromIri(parentProperty.getIri()));
    qry.getSelects().add(fn);
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
      String value = processConcatArgument(qry, arg);

      if (value != null && !value.isBlank()) {
        if (!sb.isEmpty()) sb.append(", ");
        sb.append(value);
      }
    }

    return "CONCAT(" + sb + ")";
  }

  private String processConcatArgument(SQLQuery qry, Argument arg)
    throws SQLConversionException, JsonProcessingException {

    if (!"text".equals(arg.getParameter())) {
      throw new SQLConversionException("SQL Conversion Error: Function argument type not implemented:",
        mapper.writeValueAsString(arg));
    }

    if (arg.getValuePath() == null) {
      return null;
    }

    String valuePath = arg.getValuePath().getIri();

    if (arg.getValuePath().getPath() != null && !arg.getValuePath().getPath().isEmpty()) {
      String path = arg.getValuePath().getPath().getFirst().getIri();
      String typeOf = arg.getValuePath().getTypeOf().getIri();
      return getPropertyFromPath(qry, valuePath, path, typeOf);
    } else {
      return getDirectPropertyFromArgument(qry, valuePath, null);
    }
  }

  private String getPropertyFromPath(SQLQuery qry, String valuePath, String path, String typeOf)
    throws SQLConversionException, JsonProcessingException {

    try {
      return getDirectPropertyFromArgument(qry, valuePath, path);
    } catch (SQLConversionException e) {
      return getNestedPropertyFromArgument(qry, valuePath, typeOf, path);
    }
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
    Table table = tableMap.getTableFromDataModel(typeOf);
    String tableAlias = getNameFromIri(typeOf) + "_" + qry.getAlias();
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
    if (!tableMap.getPropertiesMap().containsKey(List.of(property.getIri())) && !tableMap.getTables().containsKey(property.getIri())) {
      List<ReturnProperty> propertyPath = new ArrayList<>();
      populatePropertyPath(property, propertyPath);
      List<String> propertyIriPath = propertyPath.subList(0, propertyPath.size() - 1).stream().map(ReturnProperty::getIri).toList();
      Table table = tableMap.getTableFromProperty(propertyIriPath);
      String typeOf = table.getDataModel();
      if (typeOf == null)
        throw new SQLConversionException("Property not mapped to datamodel: " + property.getIri());
      SQLQuery subQuery = qry.subQuery(typeOf, null, tableMap, null);
      String select = subQuery.getFieldName(propertyPath.getLast().getIri(), null, tableMap, false) + " AS `" + propertyPath.getLast().getAs() + "`";
      subQuery.getSelects().add(select);
      subQuery.getSelects().add(table.getPrimaryKey());
      if (subQuery.getWiths() == null)
        subQuery.setWiths(new ArrayList<>());
      qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");
      qry.getSelects().addAll(getSelectsForParentQuery(List.of(subQuery.getSelects().getFirst())));
      String joiner = "JOIN ";
      qry.getJoins().add(createJoin(qry, subQuery, joiner));
    } else {
      Table table = tableMap.getTableFromProperty(List.of(property.getIri()));
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
  }

  private void populatePropertyPath(ReturnProperty property, List<ReturnProperty> propertyPath) {
    propertyPath.add(property);
    if (null != property.getReturn() && null != property.getReturn().getProperty() && !property.getReturn().getProperty().isEmpty())
      populatePropertyPath(property.getReturn().getProperty().getFirst(), propertyPath);
  }

  private void addYNCase(SQLQuery qry, ReturnProperty parentProperty, String gParentTypeOf, String tableAlias) throws SQLConversionException {
    Table parentTable = tableMap.getTableFromProperty(List.of(parentProperty.getIri()));
    Table gParentTable = tableMap.getTableFromProperty(List.of(gParentTypeOf));
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

  private void addIMQueryToSQLQueryRecursively(SQLQuery qry, Match match, Bool bool)
    throws SQLConversionException, JsonProcessingException {
    SQLQuery subQry = convertMatchToQuery(qry, match, bool);
    qry.getWiths().addAll(subQry.getWiths());
    subQry.setWiths(new ArrayList<>());
    if (match.getKeepAs() != null) {
      String alias = match.getKeepAs();
      qry.getWiths().add(alias + " AS (" + subQry.toSql(2) + "\n)");
    } else {
      qry.getWiths().add(subQry.getAlias() + " AS (" + subQry.toSql(2) + "\n)");
    }
    String joiner = (bool == Bool.not) ? "LEFT JOIN " : "JOIN ";
    if (bool == Bool.not) qry.getWheres().add(subQry.getAlias() + ".id IS NULL");
    qry.getJoins().add(createJoin(qry, subQry, joiner));
  }


  private SQLQuery convertMatchToQuery(SQLQuery parentSQL, Match match, Bool bool) throws SQLConversionException, JsonProcessingException {
    SQLQuery qry = createMatchQuery(match, parentSQL);

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
      return qry.subQuery(getDataModelFromKeepAs(match.getNodeRef()), match.getNodeRef() + "_sub", tableMap, null);
    } else if (match.getPath() != null) {
      return qry.subQuery(match.getPath().getFirst().getTypeOf().getIri(), variable, tableMap, null);
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
    if (match.getIs() != null) {
      convertInstanceOf(qry, match.getIs(), bool);
    } else if (null != match.getIs()) {
      convertIsCohort(qry, match.getIs().get(0), null, bool);
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

    qry.initialize(qry.getModel(), qry.getAlias(), tableMap, qry.getAlias() + "_part");
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

  private void convertInstanceOf(SQLQuery qry, List<Node> is, Bool bool) throws SQLConversionException {
    if (is.isEmpty())
      throw new SQLConversionException("SQL Conversion Error: MatchSet must have at least one element");
    String rsltTbl = getTableNameFromIri(is.getFirst().getIri());
    String fromField = getFromField(qry, is.getFirst());
    qry.getJoins().add(((bool == Bool.or || bool == Bool.not) ? "LEFT " : "") + "JOIN " + rsltTbl + " ON " + rsltTbl + ".id = " + fromField);
    if (bool == Bool.not) qry.getWheres().add(rsltTbl + ".id IS NULL");
  }

  private void convertIsCohort(SQLQuery qry, Node isCohort, Node cohortTypeOf, Bool bool) throws SQLConversionException {
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
    applyPropertyConversion(qry, property);

    if (null != property.getFunction()) {
      resolveFunctionArgs(qry, property.getFunction());
    }
  }

  private void applyPropertyConversion(SQLQuery qry, Where property) throws SQLConversionException, JsonProcessingException {
    if (property.getIs() != null) {
      convertMatchPropertyIs(qry, property, property.getIs());
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
      addFunctionWhere(qry, property);
    } else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED PROPERTY PATTERN\n" + mapper.writeValueAsString(property));
    }
  }

  private void addFunctionWhere(SQLQuery qry, Where property) throws SQLConversionException, JsonProcessingException {
    String where = getWhereFromFunction(qry, property);
    qry.getWheres().add(property.isNot() ? " NOT (" + where + ")" : where);
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

  private void convertMatchPropertyIs(SQLQuery qry, Where property, List<Node> list) throws SQLConversionException {
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
      List<String> stringConditions = getIriConditions(csm_alias, list);
      String conditionsSQL = StringUtils.join(stringConditions, " OR ");
      joins = joins.replace("{join_condition}", fieldName).replace("{conditions}", conditionsSQL);
      conditions = conditions.replace("{join_condition}", fieldName).replace("{conditions}", conditionsSQL);
      qry.getWheres().add(property.isNot() ? " NOT (" + conditions + ")" : conditions);
      qry.getJoins().add(joins);
    }
  }

  private List<String> getIriConditions(String csmAlias, List<Node> list) {
    String operator = "=";
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
    String fieldName = qry.getFieldName(property.getIri(), null, tableMap, true);
    String from = null != property.getRange().getFrom().getValue() ? property.getRange().getFrom().getValue() : property.getRange().getFrom().getValueParameter();
    String to = null != property.getRange().getTo().getValue() ? property.getRange().getTo().getValue() : property.getRange().getTo().getValueParameter();
    if ("date".equals(fieldType)) {
      from = "'" + from + "'";
      to = "'" + to + "'";
    }
    String where = fieldName + " BETWEEN " + from + " AND " + to;
    qry.getWheres().add(property.isNot() ? " NOT (" + where + ")" : where);
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

    if (property.getRelativeTo().getParameter() != null) {
      String conditions = qry.getFieldName(property.getIri(), null, tableMap, true)
        + " " + property.getOperator().getValue()
        + " " + convertMatchPropertyRelativeTo(qry, property, property.getRelativeTo().getParameter());
      qry.getWheres().add(property.isNot() ? " NOT (" + conditions + ")" : conditions);
    } else if (property.getRelativeTo().getNodeRef() != null) {
      String nodeRef = property.getRelativeTo().getNodeRef();
      String rhsIri = property.getRelativeTo().getIri();
      String rhsFull = qry.getFieldName(rhsIri, getDataModelFromKeepAs(nodeRef), tableMap, true);
      String rhsColumn = rhsFull.contains(".")
        ? rhsFull.substring(rhsFull.lastIndexOf('.') + 1)
        : rhsFull;
      String lhsField = qry.getFieldName(property.getIri(), null, tableMap, true);
      String rhsField = nodeRef + "." + rhsColumn;
      String operator = property.getOperator().getValue();
      String joinClause = "JOIN " + nodeRef + " ON " + nodeRef + ".patient_id = " + qry.getAlias() + ".patient_id";
      if (!qry.getJoins().contains(joinClause)) {
        qry.getJoins().add(joinClause);
      }
      String condition = lhsField + " " + operator + " " + rhsField;
      qry.getWheres().add(property.isNot() ? " NOT (" + condition + ")" : condition);
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
    SQLConversionException {
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
    qry.getWheres().add(property.isNot() ? " NOT (" + where + ")" : where);
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
    String conditions = qry.getFieldName(property.getIri(), null, tableMap, true) + " IS NULL";
    qry.getWheres().add(property.isNot() ? " NOT (" + conditions + ")" : conditions);
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

  public String getDataModelFromKeepAs(String keepAs) {
    Match match = findMatchByKeepAs(queryRequest.getQuery(), keepAs);
    if (match == null && queryRequest.getQuery().getColumnGroup() != null) {
      for (Match child : queryRequest.getQuery().getColumnGroup()) {
        Match result = findMatchByKeepAs(child, keepAs);
        if (result != null) match = result;
      }
    }
    if (match != null) {
      if (match.getTypeOf() != null) {
        return match.getTypeOf().getIri();
      }
      if (match.getPath() != null) {
        return match.getPath().getFirst().getTypeOf().getIri();
      }
    }
    return null;
  }

  public Match findMatchByKeepAs(Match match, String keepAs) {
    if (match == null) return null;
    if (match.getKeepAs() != null && match.getKeepAs().equals(keepAs)) {
      return match;
    }

    if (match.getAnd() != null) {
      for (Match child : match.getAnd()) {
        Match result = findMatchByKeepAs(child, keepAs);
        if (result != null) return result;
      }
    }

    if (match.getOr() != null) {
      for (Match child : match.getOr()) {
        Match result = findMatchByKeepAs(child, keepAs);
        if (result != null) return result;
      }
    }

    if (match.getNot() != null) {
      for (Match child : match.getNot()) {
        Match result = findMatchByKeepAs(child, keepAs);
        if (result != null) return result;
      }
    }


    return null;
  }


}
