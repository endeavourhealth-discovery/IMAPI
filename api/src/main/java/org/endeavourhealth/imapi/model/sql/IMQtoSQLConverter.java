package org.endeavourhealth.imapi.model.sql;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.List;

public class IMQtoSQLConverter {

  public String IMQtoSQL(Query definition) {
    if (definition.getTypeOf() == null) {
      throw new Error("Query must have a main (model) type");
    }

    if (definition.getMatch() == null) {
      throw new Error("Query must have at least one match");
    }

    try {
      SQLQuery qry = new SQLQuery().create(definition.getTypeOf().getIri(), null);

      for (Match match: definition.getMatch()) {
        SQLQuery subQry = convertMatchToQuery(qry, match);
        qry.getWiths().addAll(subQry.getWiths());
        subQry.setWiths(new ArrayList<>());
        qry.getWiths().add(subQry.getAlias() + " AS (" + subQry.toSql(2) + "\n)");

      String joiner = match.isExclude() ? "LEFT JOIN " : "JOIN ";
        if (match.isExclude()) qry.getWheres().add(subQry.getAlias() + ".id IS NULL");

        if (qry.getModel().equals(subQry.getModel())) {
          qry.getJoins().add(joiner + subQry.getAlias() + " ON " + subQry.getAlias() + ".id = " + qry.getAlias() + ".id");
        } else {
        Relationship rel = subQry.getRelationshipTo(qry.getModel());
          qry.getJoins().add(joiner + subQry.getAlias() + " ON " + subQry.getAlias() + "." + rel.getFromField() + " = " + qry.getAlias() + "." + rel.getToField());
        }
      }
      return qry.toSql(2);
    } catch (Exception e) {
      return e.toString();
//      else return "Unknown Error";
    }
  }

  private SQLQuery convertMatchToQuery(SQLQuery parent, Match match) {
    SQLQuery qry = createMatchQuery(match, parent);

    convertMatch(match, qry);

    if (match.getOrderBy() != null) {
      wrapMatchPartition(qry, match.getOrderBy());
    }

    return qry;
  }

  private SQLQuery createMatchQuery(Match match, SQLQuery qry) {
    if (match.getTypeOf() != null && !match.getTypeOf().getIri().equals(qry.getModel())) {
      return qry.subQuery(match.getTypeOf().getIri(), match.getVariable());
    } else if (match.getNodeRef() != null && !match.getNodeRef().equals(qry.getModel())) {
      return qry.subQuery(match.getNodeRef(), match.getVariable());
    } else return qry.subQuery(qry.getModel(), match.getVariable());
  }

  private void convertMatch(Match match, SQLQuery qry) {
    if (match.getInstanceOf() != null) {
      convertMatchInstanceOf(qry, match);
    } else if (match.getBoolMatch() != null) {
      if (match.getMatch() != null && !match.getMatch().isEmpty()) convertMatchBoolSubMatch(qry, match);
      else if (match.getWhere() != null && !match.getWhere().isEmpty()) convertMatchProperties(qry, match);
      else {
        throw new Error("UNHANDLED BOOL MATCH PATTERN\n" + match);
      }
    } else if (match.getWhere() != null && !match.getWhere().isEmpty()) {
      convertMatchProperties(qry, match);
    } else if (match.getMatch() != null && !match.getMatch().isEmpty()) {
      // Assume bool match "AND"
      match.setBoolMatch(Bool.and);
      convertMatchBoolSubMatch(qry, match);
    } else {
      throw new Error("UNHANDLED MATCH PATTERN\n" + match);
    }
  }

  private void wrapMatchPartition(SQLQuery qry, OrderLimit order) {
    if (order.getProperty() == null) throw new Error("ORDER MUST HAVE A FIELD SPECIFIED\n" + order);

    SQLQuery inner = qry.clone(qry.getAlias() + "_inner");

    String innerSql = qry.getAlias() + "_inner AS (" + inner.toSql(2) + ")";

    SQLQuery partition = qry.subQuery(qry.getAlias() + "_inner", qry.getAlias() + "_part");
    String partField = "patient";

    ArrayList<String> o = new ArrayList<>();

    String dir = order.getProperty().getDirection().toString().toUpperCase().startsWith("DESC") ? "DESC" : "ASC";
    o.add(partition.getFieldName(order.getProperty().getIri(), null) + " " + dir);

    partition.getSelects().add("*");
    partition.getSelects().add("ROW_NUMBER() OVER (PARTITION BY " + partField + " ORDER BY " + StringUtils.join(o, ", ") + ") AS rn");

    qry.initialize(qry.getAlias() + "_part", qry.getAlias());
    qry.getWiths().add(innerSql);
    qry.getWiths().add(partition.getAlias() + " AS (" + partition.toSql(2) + "\n)");
    qry.getWheres().add("rn = 1");
  }

  private void convertMatchInstanceOf(SQLQuery qry, Match match) {
    if (match.getInstanceOf() == null) throw new Error("MatchSet must have at least one element\n" + match);
    String rsltTbl = qry.getAlias() + "_rslt";
    qry.getJoins().add("JOIN query_result " + rsltTbl + " ON " + rsltTbl + ".id = " + qry.getAlias() + ".id");
    qry.getWheres().add(rsltTbl + ".iri = '" + match.getInstanceOf().get(0).getIri() + "'");
  }

  private void convertMatchBoolSubMatch(SQLQuery qry, Match match) {
    if (match.getBoolMatch() == null || match.getMatch() == null) {
      throw new Error("INVALID MatchBoolSubMatch\n" + match);
    }

    qry.setWhereBool(match.getBoolWhere() != null ? match.getBoolWhere().toString().toUpperCase() : "AND");

    // TODO: Boolean "OR" should be a union (more performant)
    String joiner = "OR".equalsIgnoreCase(match.getBoolWhere().toString()) ? "LEFT JOIN " : "JOIN ";

    for (Match subMatch: match.getMatch()) {
      SQLQuery subQuery = convertMatchToQuery(qry, subMatch);

      qry.getWiths().addAll(subQuery.getWiths());

      subQuery.setWiths(new ArrayList<>());
      qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");

      if (subQuery.getModel().equals(qry.getModel())) qry.getJoins().add(joiner + subQuery.getAlias() + " ON " + subQuery.getAlias() + ".id = " + qry.getAlias() + ".id");
      else {
      Relationship rel = subQuery.getRelationshipTo(qry.getModel());
        qry.getJoins().add(joiner + subQuery.getAlias() + " ON " + subQuery.getAlias() + "." + rel.getFromField() + " = " + qry.getAlias() + "." + rel.getToField());
      }

      if ("OR".equals(qry.getWhereBool())) qry.getWheres().add(subQuery.getAlias() + ".id IS NOT NULL");
    }
  }

  private void convertMatchProperties(SQLQuery qry, Match match) {
    if (match.getWhere() == null || match.getWhere().isEmpty()) {
      throw new Error("INVALID MatchProperty\n" + match);
    }

    for (Where property: match.getWhere()) {
      convertMatchProperty(qry, property);
    }
  }

  private void convertMatchProperty(SQLQuery qry, Where property) {
    if (property.getIs() != null) {
      convertMatchPropertyIs(qry, property, property.getIs());
    } else if (property.getRange() != null) {
      convertMatchPropertyRange(qry, property);
    } else if (property.getMatch() != null) {
      convertMatchPropertySubMatch(qry, property);
    } else if (property.getRelativeTo() != null) {
      convertMatchPropertyRelative(qry, property);
    } else if (property.getValue() != null) {
      convertMatchPropertyValue(qry, property);
    } else if (property.getBoolWhere() != null) {
      convertMatchPropertyBool(qry, property);
    } else if (property.getIsNull()) {
      convertMatchPropertyNull(qry, property);
    } else {
      throw new Error("UNHANDLED PROPERTY PATTERN\n" + property);
    }
  }

  private void convertMatchPropertyIs(SQLQuery qry, Where property, List<Node> list) {
    boolean inverse = false;
    if (list == null) {
      throw new Error("INVALID MatchPropertyIs\n" + property);
    }

    ArrayList<String> direct = new ArrayList<>();
    ArrayList<String> ancestors = new ArrayList<>();
    ArrayList<String> descendants = new ArrayList<>();
    ArrayList<String> descendantsSelf = new ArrayList<>();

    for (Node pIs: list) {
      if (pIs.getIri() != null) {
        if (pIs.isAncestorsOf()) ancestors.add(pIs.getIri());
        else if (pIs.isDescendantsOf()) descendants.add(pIs.getIri());
        else if (pIs.isDescendantsOrSelfOf()) descendantsSelf.add(pIs.getIri());
        else direct.add(pIs.getIri());
      } else {
        throw new Error("UNHANDLED 'IN'/'NOT IN' ENTRY\n" + pIs);
      }
    }

    if (!direct.isEmpty()) {
      String where = qry.getFieldName(property.getIri(), null);

      if (direct.size() == 1) where += (inverse ? " <> '" : " = '") + direct.get(0) + "'\n";
      else where += (inverse ? " NOT IN ('" : " IN ('") + StringUtils.join(direct, "',\n'") + "')\n";

      qry.getWheres().add(where);
    }

    String tct = "tct_" + qry.getJoins().size();
    if (!descendants.isEmpty()) {
      qry.getJoins().add("JOIN tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null));
      qry.getWheres().add(
        descendants.size() == 1 ? tct + ".iri = '" + descendants.get(0) + "'" : tct + ".iri IN ('" + StringUtils.join(descendants, "',\n'") + "') AND " + tct + ".level > 0"
      );
    } else if (!descendantsSelf.isEmpty()) {
      qry.getJoins().add("JOIN tct AS " + tct + " ON " + tct + ".child = " + qry.getFieldName(property.getIri(), null));
      qry.getWheres().add(descendantsSelf.size() == 1 ? tct + ".iri = '" + descendantsSelf.get(0) + "'" : tct + ".iri IN ('" + StringUtils.join(descendantsSelf, "',\n'") + "')");
    } else if (!ancestors.isEmpty()) {
      qry.getJoins().add("JOIN tct AS " + tct + " ON " + tct + ".iri = " + qry.getFieldName(property.getIri(), null));
      qry.getWheres().add(
        ancestors.size() == 1 ? tct + ".child = '" + ancestors.get(0) + "'" : tct + ".child IN ('" + StringUtils.join(ancestors, "',\n'") + "') AND " + tct + ".level > 0"
      );
    }
  }

  private void convertMatchPropertyRange(SQLQuery qry, Where property) {
    if (property.getRange() == null) {
      throw new Error("INVALID MatchPropertyRange\n" + property);
    }

    String fieldType = qry.getFieldType(property.getIri(), null);

    if ("date".equals(fieldType)) {
      if (property.getRange().getFrom() != null) qry.getWheres().add(convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null), property.getRange().getFrom()));
      if (property.getRange().getTo() != null) qry.getWheres().add(convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null), property.getRange().getTo()));
    } else if ("number".equals(fieldType)) {
      if (property.getRange().getFrom() != null) qry.getWheres().add(convertMatchPropertyNumberRangeNode(qry.getFieldName(property.getIri(), null), property.getRange().getFrom()));
      if (property.getRange().getTo() != null) qry.getWheres().add(convertMatchPropertyNumberRangeNode(qry.getFieldName(property.getIri(), null), property.getRange().getTo()));
    } else {
      throw new Error("UNHANDLED PROPERTY FIELD TYPE (" + fieldType + ")\n" + property);
    }
  }

  private String convertMatchPropertyNumberRangeNode(String fieldName, Assignable range) {
    if (range.getUnit() != null) return fieldName + " " + range.getOperator() + " " + range.getValue() + " -- CONVERT " + range.getUnit();
    else return fieldName + " " + range.getOperator() + " " + range.getValue();
  }

  private String convertMatchPropertyDateRangeNode(String fieldName, Assignable range) {
    return "(now() - INTERVAL '" + range.getValue() + (range.getUnit() != null ? " " + range.getUnit() : "") + "') " + range.getOperator() + " " + fieldName;
  }

  private void convertMatchPropertySubMatch(SQLQuery qry, Where property) {
    if (property.getMatch() != null) {
      throw new Error("INVALID MatchPropertySubMatch\n" + property);
    }

    if (property.getMatch().getVariable() == null) property.getMatch().setVariable(qry.getAlias(qry.getAlias() + "_sub"));

    SQLQuery subQuery = convertMatchToQuery(qry, property.getMatch());

    qry.getWiths().addAll(subQuery.getWiths());
    subQuery.setWiths(new ArrayList<>());
    qry.getWiths().add(subQuery.getAlias() + " AS (" + subQuery.toSql(2) + "\n)");

    if (qry.getModel().equals(subQuery.getModel())) qry.getJoins().add("JOIN " + subQuery.getAlias() + " ON " + subQuery.getAlias() + ".id = " + qry.getAlias() + ".id");
    else {
      Relationship rel = subQuery.getRelationshipTo(qry.getModel());
      qry.getJoins().add("JOIN " + subQuery.getAlias() + " ON " + subQuery.getAlias() + "." + rel.getFromField() + " = " + qry.getAlias() + "." + rel.getToField());
    }
  }

  private void convertMatchPropertyInSet(SQLQuery qry, Where property) {
    if (property.getIri() == null) throw new Error("INVALID PROPERTY\n" + property);

    if (property.getIs() == null) {
      throw new Error("INVALID MatchPropertyIn\n" + property);
    }

    ArrayList<String> inList = new ArrayList<>();

    for (Node pIn: property.getIs()) {
      if (pIn.getIri() != null) inList.add(pIn.getIri());
      else {
        throw new Error("UNHANDLED 'IN' ENTRY\n" + pIn);
      }
    }

    // OPTIMIZATION
    String mmbrTbl = qry.getAlias() + "_mmbr";

    qry.getJoins().add("JOIN set_member " + mmbrTbl + " ON " + mmbrTbl + ".member = " + qry.getFieldName(property.getIri(), null));

    if (inList.size() == 1) qry.getWheres().add(mmbrTbl + ".iri = '" + StringUtils.join(inList, "',\n'") + "'");
    else qry.getWheres().add(mmbrTbl + ".iri IN ('" + StringUtils.join(inList, "',\n'") + "')");
  }

  private void convertMatchPropertyRelative(SQLQuery qry, Where property) {
    if (property.getIri() == null || property.getRelativeTo() == null) {
      throw new Error("INVALID MatchPropertyRelative\n" + property);
    }

    if (property.getRelativeTo().getParameter() == null) 
      qry.getWheres().add(
        qry.getFieldName(property.getIri(), null) + " " + property.getOperator() + " " + convertMatchPropertyRelativeTo(qry, property, property.getRelativeTo().getParameter())
      );
    else if (property.getRelativeTo().getNodeRef() != null) {
      // Include implied join on noderef
      qry.getJoins().add("JOIN " + property.getRelativeTo().getNodeRef() + " ON " + property.getRelativeTo().getNodeRef() + ".id = " + qry.getAlias() + ".id");
      qry.getWheres().add(
        qry.getFieldName(property.getIri(), null) +
          " " +
          property.getOperator() +
          " " +
          convertMatchPropertyRelativeTo(qry, property, qry.getFieldName(property.getRelativeTo().getIri(), property.getRelativeTo().getNodeRef()))
    );
    } else {
      throw new Error("UNHANDLED RELATIVE COMPARISON\n" + property);
    }
  }

  private String convertMatchPropertyRelativeTo(SQLQuery qry, Where property, String field) {
    String fieldType = qry.getFieldType(property.getIri(), null);
    if ("date".equals(fieldType))
      if (property.getValue() != null) return "(" + field + " + INTERVAL '" + property.getValue() + " " + property.getUnit() + "')";
      else return field;
    else {
      throw new Error("UNHANDLED RELATIVE TYPE (" + fieldType + ")\n" + property);
    }
  }

  private void convertMatchPropertyValue(SQLQuery qry, Where property) {
    if (property.getIri() == null || property.getValue() == null) {
      throw new Error("INVALID MatchPropertyValue\n" + property);
    }

    String where =
      "date".equals(qry.getFieldType(property.getIri(), null))
        ? convertMatchPropertyDateRangeNode(qry.getFieldName(property.getIri(), null), (Assignable) property)
        : qry.getFieldName(property.getIri(), null) + " " + property.getOperator() + " " + property.getValue();

    if (property.getUnit() != null) where += " -- CONVERT " + property.getUnit() + "\n";

    // TODO: TCT
    if (property.isAncestorsOf() || property.isDescendantsOf() || property.isDescendantsOrSelfOf()) {
      where += " -- TCT\n";
    }

    qry.getWheres().add(where);
  }

  private void convertMatchPropertyBool(SQLQuery qry, Where property) {
    if (property.getBoolWhere() == null) {
      throw new Error("INVALID MatchPropertyBool\n" + property);
    }

    if (property.getWhere() != null) {
      SQLQuery subQuery = qry.subQuery(qry.getModel(), qry.getAlias());
      for (Where p: property.getWhere()) {
        convertMatchProperty(subQuery, p);
      }
      qry.getWheres().add("(" + StringUtils.join(subQuery.getWheres(), " " + property.getBoolWhere().toString().toUpperCase() + " ") + ")");
    } else {
      throw new Error("Property BOOL should only contain property conditions");
    }
  }

  private void convertMatchPropertyNull(SQLQuery qry, Where property) {
    if (property.getIri() == null) {
      throw new Error("INVALID MatchPropertyNull\n" + property);
    }

    qry.getWheres().add(qry.getFieldName(property.getIri(), null) + " IS NULL");
  }
}
