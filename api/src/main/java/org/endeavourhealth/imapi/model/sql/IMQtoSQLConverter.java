package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class IMQtoSQLConverter {
  private TableMap tableMap;
  private String lang;
  private Map<String, String> iriToUuidMap;
  private QueryRequest queryRequest;

  public IMQtoSQLConverter(QueryRequest queryRequest, String lang, Map<String, String> iriToUuidMap) {
    this.queryRequest = queryRequest;
    this.iriToUuidMap = iriToUuidMap;
    this.lang = lang != null ? lang : "MYSQL";
    try {
      String resourcePath = isPostgreSQL() ? "IMQtoSQL.json" : "IMQtoMYSQL.json";
      String text = Files.readString(Paths.get(getClass().getClassLoader().getResource(resourcePath).toURI()));
      tableMap = new ObjectMapper().readValue(text, TableMap.class);
    } catch (Exception e) {
      log.error("Could not parse datamodel map!");
      throw new RuntimeException(e);
    }
  }

  private boolean isPostgreSQL() {
    return this.lang.equals("POSTGRESQL");
  }

  public String IMQtoSQL() throws SQLConversionException {
    if (queryRequest.getQuery() == null) throw new SQLConversionException("Query is null");
    Query definition = queryRequest.getQuery();
    if (definition.getTypeOf() == null) {
      throw new SQLConversionException("SQL Conversion Error: Query must have a main (model) type");
    }

    if (definition.getAnd() == null && definition.getOr() == null && definition.getNot() == null) {
      throw new SQLConversionException("Query must have at least one match");
    }

    try {
      SQLQuery qry = new SQLQuery().create(definition.getTypeOf().getIri(), null, tableMap);
      if (definition.getAnd() != null) {
        for (Match match : definition.getAnd()) {
          addIMQueryToSQLQueryRecursively(qry, match, Bool.and);
          if (match.getThen() != null) addIMQueryToSQLQueryRecursively(qry, match.getThen(), Bool.and);
        }
      }
      if (definition.getOr() != null) {
        for (Match match : definition.getOr()) {
          addIMQueryToSQLQueryRecursively(qry, match, Bool.or);
          if (match.getThen() != null) addIMQueryToSQLQueryRecursively(qry, match.getThen(), Bool.and);
        }
      }
      if (definition.getNot() != null) {
        for (Match match : definition.getNot()) {
          addIMQueryToSQLQueryRecursively(qry, match, Bool.not);
          if (match.getThen() != null) addIMQueryToSQLQueryRecursively(qry, match.getThen(), Bool.and);
        }
      }
      String sql = qry.toSql(2);
      return replaceArgumentsWithValue(sql);
    } catch (SQLConversionException e) {
      log.error("SQL Conversion Error!");
      throw e;
    }
  }

  private String replaceArgumentsWithValue(String sql) {
    if (queryRequest.getReferenceDate() != null) {
      sql = sql.replaceAll("\\$referenceDate", "'" + queryRequest.getReferenceDate() + "'");
    }
    if (queryRequest.getArgument() != null)
      for (Argument arg : queryRequest.getArgument()) {
        if (arg.getValueData() != null)
          sql = sql.replaceAll("\\$" + arg.getParameter(), "'" + arg.getValueData() + "'");
        else if (arg.getValueIri() != null)
          sql = sql.replaceAll("\\$" + arg.getParameter(), "'" + arg.getValueIri().getIri() + "'");
      }
    return sql;
  }

  private void addIMQueryToSQLQueryRecursively(SQLQuery qry, Match match, Bool bool) throws SQLConversionException {
    SQLQuery subQry = convertMatchToQuery(qry, match, bool);
    qry.getWiths().addAll(subQry.getWiths());
    subQry.setWiths(new ArrayList<>());
    qry.getWiths().add(subQry.getAlias() + " AS (" + subQry.toSql(2) + "\n)");

    String joiner = (bool == Bool.not) ? "LEFT JOIN " : "JOIN ";
    if (bool == Bool.not) qry.getWheres().add(subQry.getAlias() + ".id IS NULL");

    qry.getJoins().add(createJoin(qry, subQry, joiner));
  }

  private SQLQuery convertMatchToQuery(SQLQuery parent, Match match, Bool bool) throws SQLConversionException {
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
      return qry.subQuery(match.getTypeOf().getIri(), variable, tableMap);
    } else if (match.getNodeRef() != null && !match.getNodeRef().equals(qry.getModel())) {
      return qry.subQuery(match.getNodeRef(), variable, tableMap);
    } else if (match.getPath() != null) {
      return qry.subQuery(match.getPath().getFirst().getIri(), variable, tableMap);
    } else return qry.subQuery(qry.getModel(), variable, tableMap);
  }

  private String getVariableFromMatch(Match match) {
    if (match.getVariable() != null) {
      return match.getVariable();
    } else if (match.getReturn() != null && match.getReturn().getAs() != null) {
      return match.getReturn().getAs();
    } else return null;
  }

  private void convertMatch(Match match, SQLQuery qry, Bool bool) throws SQLConversionException {
    if (match.getInstanceOf() != null) {
      convertMatchInstanceOf(qry, match, bool);
    } else if (match.getAnd() != null) {
      convertMatchBoolSubMatch(qry, match, Bool.and);
    }
    if (match.getOr() != null) {
      convertMatchBoolSubMatch(qry, match, Bool.or);
    }
    if (match.getNot() != null) {
      convertMatchBoolSubMatch(qry, match, Bool.not);
    }
    if (match.getWhere() != null) convertMatchProperties(qry, match);
  }

  private void wrapMatchPartition(SQLQuery qry, OrderLimit order) throws SQLConversionException {
    if (order.getProperty() == null)
      throw new SQLConversionException("SQL Conversion Error: ORDER MUST HAVE A FIELD SPECIFIED\n" + order);
    SQLQuery inner = qry.clone(qry.getAlias() + "_inner", tableMap);
    String innerSql = qry.getAlias() + "_inner AS (" + inner.toSql(2) + ")";
    SQLQuery partition = qry.subQuery(qry.getAlias() + "_inner", qry.getAlias() + "_part", tableMap);
    String partField = isPostgreSQL() ? "((json ->> 'patient')::UUID)" : "patient_id";
    ArrayList<String> o = new ArrayList<>();
    for (OrderDirection property : order.getProperty()) {
      String dir = property.getDirection().toString().toUpperCase().startsWith("DESC") ? "DESC" : "ASC";
      o.add(partition.getFieldName(property.getIri(), null, tableMap) + " " + dir);
    }

    partition.getSelects().add("*");
    partition.getSelects().add("ROW_NUMBER() OVER (PARTITION BY " + partField + " ORDER BY " + StringUtils.join(o, ", ") + ") AS rn");

    qry.initialize(qry.getAlias() + "_part", qry.getAlias(), tableMap);
    qry.getWiths().add(innerSql);
    qry.getWiths().add(partition.getAlias() + " AS (" + partition.toSql(2) + "\n)");
    qry.getWheres().add("rn = 1");
  }

  private void convertMatchInstanceOf(SQLQuery qry, Match match, Bool bool) throws SQLConversionException {
    if (match.getInstanceOf() == null)
      throw new SQLConversionException("SQL Conversion Error: MatchSet must have at least one element\n" + match);
    String subQueryIri = match.getInstanceOf().getFirst().getIri();
    String rsltTbl = "query." + iriToUuidMap.getOrDefault(subQueryIri, "uuid");
    qry.getJoins().add(((bool == Bool.or || bool == Bool.not) ? "LEFT " : "") + "JOIN " + rsltTbl + " ON "
      + rsltTbl + ".id = " + qry.getAlias() + ".id");
    if (bool == Bool.not) qry.getWheres().add(rsltTbl + ".iri IS NULL");
    qry.getWheres().add(rsltTbl + ".iri = '" + match.getInstanceOf().getFirst().getIri() + "'");
  }

  private void convertMatchBoolSubMatch(SQLQuery qry, Match match, Bool bool) throws SQLConversionException {
    if (match.getAnd() != null) {
      List<Match> subMatches = match.getAnd();
      for (Match subMatch : subMatches) {
        convertSubQuery(qry, subMatch, Bool.and, "JOIN ");
      }
    } else if (match.getOr() != null) {
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
    } else if (match.getNot() != null) {
      for (Match subMatch : match.getNot()) {
        convertSubQuery(qry, subMatch, Bool.not, "LEFT JOIN ");
      }
    }
  }

  private void convertSubQuery(SQLQuery qry, Match match, Bool bool, String joiner) throws SQLConversionException {
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

  private void convertMatchProperties(SQLQuery qry, Match match) throws SQLConversionException {
    if (match.getWhere() == null) {
      throw new SQLConversionException("INVALID MatchProperty\n" + match);
    }
    convertMatchProperty(qry, match.getWhere());
  }

  private void convertMatchProperty(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIs() != null) {
      convertMatchPropertyIs(qry, property, property.getIs());
    } else if (property.getRange() != null) {
      convertMatchPropertyRange(qry, property);
    } else if (property.getRelativeTo() != null) {
      convertMatchPropertyRelative(qry, property);
    } else if (property.getValue() != null) {
      convertMatchPropertyValue(qry, property);
    } else if (property.getAnd() != null) {
      convertMatchPropertyBool(qry, property, Bool.and);
    } else if (property.getOr() != null) {
      convertMatchPropertyBool(qry, property, Bool.or);
    } else if (property.getIsNull()) {
      convertMatchPropertyNull(qry, property);
    } else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED PROPERTY PATTERN\n" + property);
    }
  }

  private void convertMatchPropertyIs(SQLQuery qry, Where property, List<Node> list) throws SQLConversionException {
    boolean inverse = false;
    if (list == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyIs\n" + property);
    }

    ArrayList<String> direct = new ArrayList<>();
    ArrayList<String> ancestors = new ArrayList<>();
    ArrayList<String> descendants = new ArrayList<>();
    ArrayList<String> descendantsSelf = new ArrayList<>();

    for (Node pIs : list) {
      if (pIs.getIri() != null) {
        if (pIs.isAncestorsOf()) ancestors.add(pIs.getIri());
        else if (pIs.isDescendantsOf()) descendants.add(pIs.getIri());
        else if (pIs.isDescendantsOrSelfOf()) descendantsSelf.add(pIs.getIri());
        else direct.add(pIs.getIri());
      } else if (pIs.getParameter() != null) {
        descendantsSelf.add(pIs.getIri());
      } else {
        throw new SQLConversionException("SQL Conversion Error: UNHANDLED 'IN'/'NOT IN' ENTRY\n" + pIs);
      }
    }

    if (!direct.isEmpty()) {
      String where = qry.getFieldName(property.getIri(), null, tableMap);

      if (direct.size() == 1) where += (inverse ? " <> '" : " = '") + direct.getFirst() + "'\n";
      else where += (inverse ? " NOT IN ('" : " IN ('") + StringUtils.join(direct, "',\n'") + "')\n";

      qry.getWheres().add(where);
    }

    String tct = "tct_" + qry.getJoins().size();
    if (!descendants.isEmpty()) {
      String join = isPostgreSQL()
        ? "JOIN tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap)
        : "JOIN concept_tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap);
      qry.getJoins().add(join);
      qry.getWheres().add(descendants.size() == 1 ? tct + ".iri = '" + descendants.getFirst() + "'" : tct + ".iri IN ('" + StringUtils.join(descendants, "',\n'") + "') AND " + tct + ".level > 0");
    } else if (!descendantsSelf.isEmpty()) {
      String join = isPostgreSQL()
        ? "JOIN tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap)
        : "JOIN concept_tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap);
      qry.getJoins().add(join);
      qry.getWheres().add(descendantsSelf.size() == 1 ? tct + ".iri = '" + descendantsSelf.getFirst() + "'" : tct + ".iri IN ('" + StringUtils.join(descendantsSelf, "',\n'") + "')");
    } else if (!ancestors.isEmpty()) {
      String join = isPostgreSQL()
        ? "JOIN tct AS " + tct + " ON " + tct + ".iri = " + qry.getFieldName(property.getIri(), null, tableMap)
        : "JOIN concept_tct AS " + tct + " ON " + tct + ".iri = " + qry.getFieldName(property.getIri(), null, tableMap);
      qry.getJoins().add(join);
      qry.getWheres().add(ancestors.size() == 1 ? tct + ".child = '" + ancestors.getFirst() + "'" : tct + ".child IN ('" + StringUtils.join(ancestors, "',\n'") + "') AND " + tct + ".level > 0");
    }
  }

  private void convertMatchPropertyRange(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getRange() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyRange\n" + property);
    }

    String fieldType = qry.getFieldType(property.getIri(), null, tableMap);

    if ("date".equals(fieldType)) {
      if (property.getRange().getFrom() != null)
        qry.getWheres().add(convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null, tableMap), property.getRange().getFrom()));
      if (property.getRange().getTo() != null)
        qry.getWheres().add(convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null, tableMap), property.getRange().getTo()));
    } else if ("number".equals(fieldType)) {
      if (property.getRange().getFrom() != null)
        qry.getWheres().add(convertMatchPropertyNumberRangeNode(qry.getFieldName(property.getIri(), null, tableMap), property.getRange().getFrom()));
      if (property.getRange().getTo() != null)
        qry.getWheres().add(convertMatchPropertyNumberRangeNode(qry.getFieldName(property.getIri(), null, tableMap), property.getRange().getTo()));
    } else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED PROPERTY FIELD TYPE (" + fieldType + ")\n" + property);
    }
  }

  private String convertMatchPropertyNumberRangeNode(String fieldName, Assignable range) {
    if (range.getUnit() != null)
      return fieldName + " " + range.getOperator().getValue() + " " + range.getValue() + " -- CONVERT " + range.getUnit();
    else return fieldName + " " + range.getOperator().getValue() + " " + range.getValue();
  }

  private String convertMatchPropertyDateRangeNode(String fieldName, Assignable range) throws SQLConversionException {
    if (range.getUnit() != null && "DATE".equals(range.getUnit().getName()))
      return "'" + range.getValue() + "' " + range.getOperator().getValue() + " " + fieldName;
    else {
      String referenceDate = queryRequest.getReferenceDate() != null ? "'" + queryRequest.getReferenceDate() + "'" : "NOW()";
      String returnString = isPostgreSQL()
        ? "(" + referenceDate + " - INTERVAL '" + range.getValue() + (range.getUnit() != null ? " " + getUnitName(range.getUnit()) : "") + "') " + range.getOperator().getValue() + " " + fieldName
        : "DATE_SUB(" + referenceDate + ", INTERVAL " + range.getValue() + (range.getUnit() != null ? " " + getUnitName(range.getUnit()) : "") + ") " + range.getOperator().getValue() + " " + fieldName;
      return returnString;
    }
  }

  private void convertMatchPropertyInSet(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null)
      throw new SQLConversionException("SQL Conversion Error: INVALID PROPERTY\n" + property);

    if (property.getIs() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyIn\n" + property);
    }

    ArrayList<String> inList = new ArrayList<>();

    for (Node pIn : property.getIs()) {
      if (pIn.getIri() != null) inList.add(pIn.getIri());
      else {
        throw new SQLConversionException("SQL Conversion Error: UNHANDLED 'IN' ENTRY\n" + pIn);
      }
    }
    String mmbrTbl = qry.getAlias() + "_mmbr";

    qry.getJoins().add("JOIN set_member " + mmbrTbl + " ON " + mmbrTbl + ".member = " + qry.getFieldName(property.getIri(), null, tableMap));

    if (inList.size() == 1) qry.getWheres().add(mmbrTbl + ".iri = '" + StringUtils.join(inList, "',\n'") + "'");
    else qry.getWheres().add(mmbrTbl + ".iri IN ('" + StringUtils.join(inList, "',\n'") + "')");
  }

  private void convertMatchPropertyRelative(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null || property.getRelativeTo() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyRelative\n" + property);
    }

    if (property.getRelativeTo().getParameter() != null)
      qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap) + " " + property.getOperator().getValue() + " " + convertMatchPropertyRelativeTo(qry, property, property.getRelativeTo().getParameter()));
    else if (property.getRelativeTo().getNodeRef() != null) {
      qry.getJoins().add("JOIN " + property.getRelativeTo().getNodeRef() + " ON " + property.getRelativeTo().getNodeRef() + ".id = " + qry.getAlias() + ".id");
      qry.getWheres()
        .add(
          qry.getFieldName(property.getIri(), null, tableMap) + " " + property.getOperator().getValue() + " " +
            convertMatchPropertyRelativeTo(qry, property, qry.getFieldName(property.getRelativeTo().getIri(), property.getRelativeTo().getNodeRef(), tableMap))
        );
    } else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED RELATIVE COMPARISON\n" + property);
    }
  }

  private String convertMatchPropertyRelativeTo(SQLQuery qry, Where property, String field) throws
    SQLConversionException {
    String fieldType = qry.getFieldType(property.getIri(), null, tableMap);
    if ("date".equals(fieldType)) if (property.getValue() != null) {
      return "(" + field + " + INTERVAL " + property.getValue() + " " + getUnitName(property.getUnit()) + ")";
    } else return field;
    else {
      throw new SQLConversionException("SQL Conversion Error: UNHANDLED RELATIVE TYPE (" + fieldType + ")\n" + property);
    }
  }

  private void convertMatchPropertyValue(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null || property.getValue() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyValue\n" + property);
    }
    String where = "date".equals(qry.getFieldType(property.getIri(), null, tableMap)) ? convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null, tableMap), new Value().setValue(property.getValue()).setUnit(property.getUnit()).setOperator(property.getOperator())) : qry.getFieldName(property.getIri(), null, tableMap) + " " + property.getOperator().getValue() + " " + property.getValue();
    if (property.getUnit() != null) where += " -- CONVERT " + property.getUnit() + "\n";
    if (property.isAncestorsOf() || property.isDescendantsOf() || property.isDescendantsOrSelfOf()) {
      where += " -- TCT\n";
    }
    qry.getWheres().add(where);
  }

  private void convertMatchPropertyBool(SQLQuery qry, Where property, Bool bool) throws SQLConversionException {
    SQLQuery subQuery = qry.subQuery(qry.getModel(), qry.getAlias(), tableMap);
    if (bool == Bool.and) {
      for (Where p : property.getAnd()) {
        convertMatchProperty(subQuery, p);
      }
    } else if (bool == Bool.or) {
      for (Where p : property.getOr()) {
        convertMatchProperty(subQuery, p);
      }
    }
    qry.getWheres().add("(" + StringUtils.join(subQuery.getWheres(), " " + bool.toString().toUpperCase() + " ") + ")");
  }


  private void convertMatchPropertyNull(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null) {
      throw new SQLConversionException("SQL Conversion Error: INVALID MatchPropertyNull\n" + property);
    }

    qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap) + " IS NULL");
  }

  private String getUnitName(TTIriRef iriRef) throws SQLConversionException {
    if (iriRef.getName() != null)
      return iriRef.getName();
    return switch (iriRef.getIri()) {
      case IM.YEARS -> "Year";
      case IM.MONTHS -> "Month";
      default -> throw new SQLConversionException("SQL Conversion Error: No unit name found for\n" + iriRef.getIri());
    };
  }
}
