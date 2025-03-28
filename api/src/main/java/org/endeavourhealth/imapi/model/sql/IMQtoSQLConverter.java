package org.endeavourhealth.imapi.model.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IMQtoSQLConverter {
  private static Logger LOG = LoggerFactory.getLogger(IMQtoSQLConverter.class);

  private HashMap<String, Table> tableMap;

  public IMQtoSQLConverter() {
    try {
      // POSTGRES String text = Files.readString(Paths.get(getClass().getClassLoader().getResource("IMQtoSQL.json").toURI()));
      String text = Files.readString(Paths.get(getClass().getClassLoader().getResource("IMQtoMYSQL.json").toURI()));
      tableMap = new ObjectMapper().readValue(text, new TypeReference<>() {
      });
    } catch (Exception e) {
      LOG.error("Could not parse datamodel map!");
      throw new RuntimeException(e);
    }
  }

  public String IMQtoSQL(Query definition) throws SQLConversionException {
    if (definition.getTypeOf() == null) {
      throw new SQLConversionException("Query must have a main (model) type");
    }

    if (definition.getMatch() == null) {
      throw new SQLConversionException("Query must have at least one match");
    }

    try {
      SQLQuery qry = new SQLQuery().create(definition.getTypeOf().getIri(), null, tableMap);
      for (Match match : definition.getMatch()) {
        addIMQueryToSQLQueryRecursively(qry, match);
      }

      return qry.toSql(2);
    } catch (SQLConversionException e) {
      LOG.error("SQL Conversion Error!");
      throw e;
    }
  }

  private void addIMQueryToSQLQueryRecursively(SQLQuery qry, Match match) throws SQLConversionException {
    SQLQuery subQry = convertMatchToQuery(qry, match);
    qry.getWiths().addAll(subQry.getWiths());
    subQry.setWiths(new ArrayList<>());
    qry.getWiths().add(subQry.getAlias() + " AS (" + subQry.toSql(2) + "\n)");

    String joiner = match.isExclude() ? "LEFT JOIN " : "JOIN ";
    if (match.isExclude()) qry.getWheres().add(subQry.getAlias() + ".id IS NULL");

    qry.getJoins().add(createJoin(qry, subQry, joiner));
  }

  private SQLQuery convertMatchToQuery(SQLQuery parent, Match match) throws SQLConversionException {
    SQLQuery qry = createMatchQuery(match, parent);

    convertMatch(match, qry);

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
    } else return qry.subQuery(qry.getModel(), variable, tableMap);
  }

  private String getVariableFromMatch(Match match) {
    if (match.getVariable() != null) {
      return match.getVariable();
    } else if (match.getReturn() != null && match.getReturn().getAs() != null) {
      return match.getReturn().getAs();
    } else return null;
  }

  private void convertMatch(Match match, SQLQuery qry) throws SQLConversionException {
    if (match.getInstanceOf() != null) {
      convertMatchInstanceOf(qry, match);
    } else if (match.getBoolMatch() != null) {
      if (match.getMatch() != null && !match.getMatch().isEmpty()) convertMatchBoolSubMatch(qry, match);
      else if (match.getWhere() != null && !match.getWhere().isEmpty()) convertMatchProperties(qry, match);
      else {
        throw new SQLConversionException("UNHANDLED BOOL MATCH PATTERN\n" + match);
      }
    } else if (match.getWhere() != null && !match.getWhere().isEmpty()) {
      convertMatchProperties(qry, match);
    } else if (match.getMatch() != null && !match.getMatch().isEmpty()) {
      // Assume bool match "AND"
      match.setBoolMatch(Bool.and);
      convertMatchBoolSubMatch(qry, match);
    } else {
      throw new SQLConversionException("UNHANDLED MATCH PATTERN\n" + match);
    }

  }

  private void wrapMatchPartition(SQLQuery qry, OrderLimit order) throws SQLConversionException {
    if (order.getProperty() == null) throw new SQLConversionException("ORDER MUST HAVE A FIELD SPECIFIED\n" + order);

    SQLQuery inner = qry.clone(qry.getAlias() + "_inner", tableMap);

    String innerSql = qry.getAlias() + "_inner AS (" + inner.toSql(2) + ")";

    SQLQuery partition = qry.subQuery(qry.getAlias() + "_inner", qry.getAlias() + "_part", tableMap);

    // TODO: Correct partition field
    // String partField = "patient";
    // POSTGRES String partField = "((json ->> 'patient')::UUID)";
    String partField = "patient_id";


    ArrayList<String> o = new ArrayList<>();

    String dir = order.getProperty().getDirection().toString().toUpperCase().startsWith("DESC") ? "DESC" : "ASC";
    o.add(partition.getFieldName(order.getProperty().getIri(), null, tableMap) + " " + dir);

    partition.getSelects().add("*");
    partition.getSelects().add("ROW_NUMBER() OVER (PARTITION BY " + partField + " ORDER BY " + StringUtils.join(o, ", ") + ") AS rn");

    qry.initialize(qry.getAlias() + "_part", qry.getAlias(), tableMap);
    qry.getWiths().add(innerSql);
    qry.getWiths().add(partition.getAlias() + " AS (" + partition.toSql(2) + "\n)");
    qry.getWheres().add("rn = 1");
  }

  private void convertMatchInstanceOf(SQLQuery qry, Match match) throws SQLConversionException {
    if (match.getInstanceOf() == null)
      throw new SQLConversionException("MatchSet must have at least one element\n" + match);
    String rsltTbl = qry.getAlias() + "_rslt";
    qry.getJoins().add("JOIN query_result " + rsltTbl + " ON " + rsltTbl + ".id = " + qry.getAlias() + ".id");
    qry.getWheres().add(rsltTbl + ".iri = '" + match.getInstanceOf().get(0).getIri() + "'");
  }

  private void convertMatchBoolSubMatch(SQLQuery qry, Match match) throws SQLConversionException {
    if (match.getBoolMatch() == null || match.getMatch() == null) {
      throw new SQLConversionException("INVALID MatchBoolSubMatch\n" + match);
    }

    qry.setWhereBool(match.getBoolWhere() != null ? match.getBoolWhere().toString().toUpperCase() : "AND");

    // TODO: Boolean "OR" should be a union (more performant)
    String joiner = "JOIN ";
    if (match.getBoolWhere() != null) {
      joiner = "OR".equalsIgnoreCase(match.getBoolWhere().toString()) ? "LEFT JOIN " : "JOIN ";
    }

    for (Match subMatch : match.getMatch()) {
      SQLQuery subQuery = convertMatchToQuery(qry, subMatch);

      qry.getWiths().addAll(subQuery.getWiths());

      subQuery.setWiths(new ArrayList<>());
      qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");

      qry.getJoins().add(createJoin(qry, subQuery, joiner));

      if ("OR".equals(qry.getWhereBool())) qry.getWheres().add(subQuery.getAlias() + ".id IS NOT NULL");
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
    if (match.getWhere() == null || match.getWhere().isEmpty()) {
      throw new SQLConversionException("INVALID MatchProperty\n" + match);
    }

    for (Where property : match.getWhere()) {
      convertMatchProperty(qry, property);
    }
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
    } else if (property.getBoolWhere() != null) {
      convertMatchPropertyBool(qry, property);
    } else if (property.getIsNull()) {
      convertMatchPropertyNull(qry, property);
    } else {
      throw new SQLConversionException("UNHANDLED PROPERTY PATTERN\n" + property);
    }
  }

  private void convertMatchPropertyIs(SQLQuery qry, Where property, List<Node> list) throws SQLConversionException {
    boolean inverse = false;
    if (list == null) {
      throw new SQLConversionException("INVALID MatchPropertyIs\n" + property);
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
        throw new SQLConversionException("UNHANDLED 'IN'/'NOT IN' ENTRY\n" + pIs);
      }
    }

    if (!direct.isEmpty()) {
      String where = qry.getFieldName(property.getIri(), null, tableMap);

      if (direct.size() == 1) where += (inverse ? " <> '" : " = '") + direct.get(0) + "'\n";
      else where += (inverse ? " NOT IN ('" : " IN ('") + StringUtils.join(direct, "',\n'") + "')\n";

      qry.getWheres().add(where);
    }

    String tct = "tct_" + qry.getJoins().size();
    if (!descendants.isEmpty()) {
      // POSTGRES qry.getJoins().add("JOIN tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap));
      qry.getJoins().add("JOIN concept_tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap));
      qry.getWheres().add(descendants.size() == 1 ? tct + ".iri = '" + descendants.get(0) + "'" : tct + ".iri IN ('" + StringUtils.join(descendants, "',\n'") + "') AND " + tct + ".level > 0");
    } else if (!descendantsSelf.isEmpty()) {
      // POSTGRES qry.getJoins().add("JOIN tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap));
      qry.getJoins().add("JOIN concept_tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null, tableMap));
      qry.getWheres().add(descendantsSelf.size() == 1 ? tct + ".iri = '" + descendantsSelf.get(0) + "'" : tct + ".iri IN ('" + StringUtils.join(descendantsSelf, "',\n'") + "')");
    } else if (!ancestors.isEmpty()) {
      // POSTGRES qry.getJoins().add("JOIN tct AS " + tct + " ON " + tct + ".iri = " + qry.getFieldName(property.getIri(), null, tableMap));
      qry.getJoins().add("JOIN concept_tct AS " + tct + " ON " + tct + ".iri = " + qry.getFieldName(property.getIri(), null, tableMap));
      qry.getWheres().add(ancestors.size() == 1 ? tct + ".child = '" + ancestors.get(0) + "'" : tct + ".child IN ('" + StringUtils.join(ancestors, "',\n'") + "') AND " + tct + ".level > 0");
    }
  }

  private void convertMatchPropertyRange(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getRange() == null) {
      throw new SQLConversionException("INVALID MatchPropertyRange\n" + property);
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
      throw new SQLConversionException("UNHANDLED PROPERTY FIELD TYPE (" + fieldType + ")\n" + property);
    }
  }

  private String convertMatchPropertyNumberRangeNode(String fieldName, Assignable range) {
    if (range.getUnit() != null)
      return fieldName + " " + range.getOperator().getValue() + " " + range.getValue() + " -- CONVERT " + range.getUnit();
    else return fieldName + " " + range.getOperator().getValue() + " " + range.getValue();
  }

  private String convertMatchPropertyDateRangeNode(String fieldName, Assignable range) {
    if (range.getUnit() != null && "DATE".equals(range.getUnit().getName()))
      return "'" + range.getValue() + "' " + range.getOperator().getValue() + " " + fieldName;
    else {
      // POSTGRES return "(now() - INTERVAL '" + range.getValue() + (range.getUnit() != null ? " " + range.getUnit().getName() : "") + "') " + range.getOperator().getValue() + " " + fieldName;
      return "DATE_SUB(NOW(), INTERVAL " + range.getValue() + (range.getUnit() != null ? " " + range.getUnit().getName() : "") + ") " + range.getOperator().getValue() + " " + fieldName;
    }
  }


  private void convertMatchPropertyInSet(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null) throw new SQLConversionException("INVALID PROPERTY\n" + property);

    if (property.getIs() == null) {
      throw new SQLConversionException("INVALID MatchPropertyIn\n" + property);
    }

    ArrayList<String> inList = new ArrayList<>();

    for (Node pIn : property.getIs()) {
      if (pIn.getIri() != null) inList.add(pIn.getIri());
      else {
        throw new SQLConversionException("UNHANDLED 'IN' ENTRY\n" + pIn);
      }
    }

    // OPTIMIZATION
    String mmbrTbl = qry.getAlias() + "_mmbr";

    qry.getJoins().add("JOIN set_member " + mmbrTbl + " ON " + mmbrTbl + ".member = " + qry.getFieldName(property.getIri(), null, tableMap));

    if (inList.size() == 1) qry.getWheres().add(mmbrTbl + ".iri = '" + StringUtils.join(inList, "',\n'") + "'");
    else qry.getWheres().add(mmbrTbl + ".iri IN ('" + StringUtils.join(inList, "',\n'") + "')");
  }

  private void convertMatchPropertyRelative(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null || property.getRelativeTo() == null) {
      throw new SQLConversionException("INVALID MatchPropertyRelative\n" + property);
    }

    if (property.getRelativeTo().getParameter() != null)
      qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap) + " " + property.getOperator().getValue() + " " + convertMatchPropertyRelativeTo(qry, property, property.getRelativeTo().getParameter()));
    else if (property.getRelativeTo().getNodeRef() != null) {
      // Include implied join on noderef
      qry.getJoins().add("JOIN " + property.getRelativeTo().getNodeRef() + " ON " + property.getRelativeTo().getNodeRef() + ".id = " + qry.getAlias() + ".id");
      qry.getWheres()
        .add(
          qry.getFieldName(property.getIri(), null, tableMap) + " " + property.getOperator().getValue() + " " +
            convertMatchPropertyRelativeTo(qry, property, qry.getFieldName(property.getRelativeTo().getIri(), property.getRelativeTo().getNodeRef(), tableMap))
        );
    } else {
      throw new SQLConversionException("UNHANDLED RELATIVE COMPARISON\n" + property);
    }
  }

  private String convertMatchPropertyRelativeTo(SQLQuery qry, Where property, String field) throws SQLConversionException {
    String fieldType = qry.getFieldType(property.getIri(), null, tableMap);
    if ("date".equals(fieldType)) if (property.getValue() != null) {
      return "(" + field + " + INTERVAL " + property.getValue() + " " + property.getUnit().getName() + ")";
    } else return field;
    else {
      throw new SQLConversionException("UNHANDLED RELATIVE TYPE (" + fieldType + ")\n" + property);
    }
  }

  private void convertMatchPropertyValue(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null || property.getValue() == null) {
      throw new SQLConversionException("INVALID MatchPropertyValue\n" + property);
    }

    String where = "date".equals(qry.getFieldType(property.getIri(), null, tableMap)) ? convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null, tableMap), new Value().setValue(property.getValue()).setUnit(property.getUnit()).setOperator(property.getOperator())) : qry.getFieldName(property.getIri(), null, tableMap) + " " + property.getOperator().getValue() + " " + property.getValue();

    if (property.getUnit() != null) where += " -- CONVERT " + property.getUnit() + "\n";

    // TODO: TCT
    if (property.isAncestorsOf() || property.isDescendantsOf() || property.isDescendantsOrSelfOf()) {
      where += " -- TCT\n";
    }

    qry.getWheres().add(where);
  }

  private void convertMatchPropertyBool(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getBoolWhere() == null) {
      throw new SQLConversionException("INVALID MatchPropertyBool\n" + property);
    }

    if (property.getWhere() != null) {
      SQLQuery subQuery = qry.subQuery(qry.getModel(), qry.getAlias(), tableMap);
      for (Where p : property.getWhere()) {
        convertMatchProperty(subQuery, p);
      }
      qry.getWheres().add("(" + StringUtils.join(subQuery.getWheres(), " " + property.getBoolWhere().toString().toUpperCase() + " ") + ")");
    } else {
      throw new SQLConversionException("Property BOOL should only contain property conditions");
    }
  }

  private void convertMatchPropertyNull(SQLQuery qry, Where property) throws SQLConversionException {
    if (property.getIri() == null) {
      throw new SQLConversionException("INVALID MatchPropertyNull\n" + property);
    }

    qry.getWheres().add(qry.getFieldName(property.getIri(), null, tableMap) + " IS NULL");
  }
}
