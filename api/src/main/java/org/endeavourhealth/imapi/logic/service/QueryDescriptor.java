package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.Context;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class QueryDescriptor {
  private final Map<String, String> contextTerms;
  private final Map<String, String> nodeRefNames;
  private final EntityTripleRepository repo = new EntityTripleRepository();

  public enum MatchType {
    match,
    then,
    whereMatch
  }

  public QueryDescriptor() {
    contextTerms = new HashMap<>();
    nodeRefNames = new HashMap<>();
    contextTerms.putAll(Map.of(
      "Patient" + Context.single, "Patient",
      "Patient" + Context.plural, "Patients",
      IM.NAMESPACE + "Observation" + Context.type, "",
      IM.NAMESPACE + "observation" + Context.path, "",
      IM.NAMESPACE + "concept" + Context.property, "",
      IM.NAMESPACE + "Prescription" + Context.type, "Prescription for ",
      "DATE" + Context.plural, "",
      "YEAR" + Context.plural, "years",
      "MONTH" + Context.plural, "months",
      "DAY" + Context.plural, "days"));
    contextTerms.putAll(Map.of(
      "YEARS" + Context.plural, "years",
      "MONTHS" + Context.plural, "months",
      "DAYS" + Context.plural, "days"));
  }

  public Query describeQuery(String queryIri) throws JsonProcessingException {
    TTEntity queryEntity = repo.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    query.setName(queryEntity.getName());
    return describeQuery(query);
  }

  public Query describeQuery(Query query) {

    query.setDisplayLabel(query.getName());
    if (query.getBoolMatch() == null)
      query.setBoolMatch(Bool.and);
    if (query.getMatch() != null) {
      for (Match match : query.getMatch()) {
        describeMatch(match, MatchType.match);
      }
    }
    return query;
  }


  public String getTermInContext(String source, Context context) {
    if (contextTerms.get(source + context) != null) {
      return contextTerms.get(source + context);
    } else
      return source;
  }


  private String getTermInContext(Node node, Context context) {
    if (node.getParameter() != null) {
      return node.getParameter();
    }
    if (node.getName() != null) {
      return getTermInContext(node.getName(), context);
    }
    if (node.getIri() != null) {
      if (contextTerms.get(node.getIri() + context) != null) {
        return contextTerms.get(node.getIri() + context);
      }
      String name = repo.getEntityPredicates(node.getIri(), Set.of(RDFS.LABEL)).getEntity().getName();
      if (name == null)
        name = node.getIri();
      contextTerms.put(node.getIri() + context, name);
      return name;
    } else if (node.getVariable() != null) {
      return node.getVariable();
    } else if (node.getNodeRef() != null) {
      if (nodeRefNames.get(node.getNodeRef()) != null) {
        return nodeRefNames.get(node.getNodeRef());
      } else
        return node.getNodeRef();
    } else
      return "";
  }

  private String getTermInContext(PropertyRef ref) {
    String display = "";
    if (ref.isInverse()) {
      display = "is ";
    }
    if (ref.getName() != null) {
      display = display + getTermInContext(ref.getName(), Context.property);
    }
    if (ref.getIri() != null) {
      if (contextTerms.get(ref.getIri() + Context.property) != null) {
        display = display + contextTerms.get(ref.getIri() + Context.property);
        if (ref.isInverse()) {
          display = display + " of ";
        }
      } else {
        String name = repo.getEntityPredicates(ref.getIri(), Set.of(RDFS.LABEL)).getEntity().getName();
        if (name == null)
          name = ref.getIri();
        contextTerms.put(ref.getIri() + Context.property, name);
        display = display + getTermInContext(name, Context.property);
      }
    } else if (ref.getParameter() != null) {
      display = ref.getParameter();
    } else if (ref.getNodeRef() != null) {
      if (nodeRefNames.get(ref.getNodeRef()) != null) {
        display = display + " of " + nodeRefNames.get(ref.getNodeRef());
      } else
        display = display + ref.getNodeRef();
    }
    return display;
  }

  public void describeThen(Match then) {
    then.setDisplayLabel("then test if :");
    if (then.getMatch() != null) {
      describeMatches(then);
    } else
      describeMatch(then, MatchType.then);
  }

  private void describeMatches(Match match) {
    int i = 0;
    for (Match nested : match.getMatch()) {
      describeMatch(nested, MatchType.match);
    }
  }


  public void describeMatch(Match match, MatchType matchType) {
    StringBuilder display = new StringBuilder();
    if (matchType == MatchType.then) {
      display.append("Then test if :");
    }
    if (match.getMatch() != null) {
      describeMatches(match);
    } else {

      if (match.getOrderBy() != null) {
        display.append(describeOrderBy(match.getOrderBy())).append(" ");
      }
      if (matchType != MatchType.whereMatch) {

        if (match.getTypeOf() != null) {
          display.append(getTermInContext(match.getTypeOf(), Context.type));
        }
        if (match.getInstanceOf() != null) {
          display.append("in the cohort :'" + describeInstance(match.getInstanceOf())).append("'");
        }
        ;
      }

      if (match.getVariable() != null) {
        nodeRefNames.put(match.getVariable(), display.toString());
      }
      if (match.getWhere() != null) {
        describeWheres(match);
      }

      if (match.getThen() != null) {
        describeThen(match.getThen());
      }
    }
    if (!display.isEmpty())
      match.setDisplayLabel(display.toString());
  }

  private void describeWheres(Where where) {
    if (where.getBoolWhere() == null) {
      where.setBoolWhere(Bool.and);
    }
    for (Where subWhere : where.getWhere()) {
      describeWhere(subWhere);
    }

  }


  private void describeWheres(Match match) {
    if (match.getBoolWhere() == null)
      match.setBoolWhere(Bool.and);
    for (Where where : match.getWhere()) {
      describeWhere(where);
    }
  }


  private String describeInstance(List<Node> inSets) {
    StringBuilder display = new StringBuilder();
    if (inSets.size() > 1) {
      display.append("either ");
    }
    boolean first = true;
    for (Node set : inSets) {
      if (!first)
        display.append(" or ");
      first = false;

      if (set.getName() != null) {
        display.append("'");
        display.append(set.getName());
        display.append("'");
      } else {
        display.append("'").append(getTermInContext(set, Context.type)).append("'");
      }
    }
    return display.toString();
  }

  private String describeOrderBy(OrderLimit orderBy) {
    String orderDisplay;
    String field = orderBy.getProperty().getIri();
    if (field.toLowerCase().contains("date")) {
      if (orderBy.getProperty().getDirection() == Order.descending)
        orderDisplay = "latest ";
      else
        orderDisplay = "earliest ";
    } else {
      if (orderBy.getProperty().getDirection() == Order.descending)
        orderDisplay = "maximum ";
      else
        orderDisplay = "minimum ";
    }
    if (orderBy.getLimit() > 1)
      orderDisplay = orderDisplay + " " + orderBy.getLimit();
    return orderDisplay;
  }


  private void describeWhere(Where where) {
    StringBuilder display = new StringBuilder();
    if (where.getWhere() != null) {
      describeWheres(where);
    } else {
      String propertyName = getTermInContext(where);
      if (!where.getIri().equals(IM.NAMESPACE + "concept")) {
        display.append(propertyName.equals("") ? "" : propertyName + " ");
      }
      if (where.getRange() != null) {
        display.append(describeRangeProperty(where, where.getIri().toLowerCase().contains("date")));
      } else if (where.getValue() != null || where.getOperator() != null) {
        describeValueWhere(display, where, where.getIri().toLowerCase().contains("date"));
      } else if (where.getIs() != null) {
        display.append(describeIsProperty(where));
      }
      if (where.getMatch() != null) {
        describeMatch(where.getMatch(), MatchType.whereMatch);
      }
    }
    where.setDisplayLabel(display.toString());
  }

  private boolean hasRelativeTo(Where where) {
    if (where.getRelativeTo() != null) {
      if (where.getRelativeTo().getParameter() != null)
        if (where.getRelativeTo().getParameter().equals("$referenceDate")) {
          return false;
        }
      return true;
    }
    return false;
  }

  private void describeValueWhere(StringBuilder display, Where where, boolean isDate) {
    Operator operator = where.getOperator();
    String value = where.getValue() != null ? where.getValue() + " " : "";
    String units = where.getUnit() == null ? "" : getTermInContext(where.getUnit(), Context.plural);
    if (isDate) {
      describeDateRangePart(display, where, operator, value, value.startsWith("-"), units);
    } else {
      describeValueWithOperator(display, operator, value);
    }
    if (hasRelativeTo(where)) {
      describeRelativeTo(display, where.getRelativeTo());
    }
  }


  private String describeRangeProperty(Where where, boolean isDate) {
    StringBuilder display = new StringBuilder();
    Range range = where.getRange();
    Assignable from = range.getFrom();
    Assignable to = range.getTo();
    Operator fromOperator = from.getOperator() == null ? Operator.eq : from.getOperator();
    Operator toOperator = to.getOperator() == null ? Operator.eq : to.getOperator();
    String fromValue = from.getValue() == null ? "" : from.getValue() + " ";
    String toValue = to.getValue() == null ? "" : to.getValue() + " ";
    if (!isDate)
      describeRange(display, where, fromOperator, fromValue, toOperator, toValue);
    else
      describeDateRange(display, where, fromOperator, fromValue, toOperator, toValue);
    return display.toString();
  }

  private void describeRange(StringBuilder display, Where where, Operator fromOperator, String fromValue, Operator toOperator, String toValue) {
    String units = where.getUnit() == null ? "" : getTermInContext(where.getUnit(), Context.plural);
    if (where.getRelativeTo() != null)
      display.append("between ");
    switch (fromOperator) {
      case gt -> display.append("over ").append(fromValue);
      case lt -> display.append("under ").append(fromValue);
      case lte -> display.append("up to ").append(fromValue);
      default -> display.append(fromValue);
    }
    display.append("to ");
    describeValueWithOperator(display, toOperator, toValue);
    display.append(units);
    if (hasRelativeTo(where)) {
      display.append("relative to");
      display.append(getTermInContext(where.getRelativeTo())).append(" of ");
      display.append(where.getRelativeTo().getNodeRef());
    }
  }

  private void describeValueWithOperator(StringBuilder display, Operator toOperator, String toValue) {
    switch (toOperator) {
      case gt -> display.append("over ").append(toValue);//makes no sense
      case gte -> display.append("equal to or more than ").append(toValue);  //makes no sense
      case lt -> display.append("under ").append(toValue);
      default -> display.append(toValue);
    }
  }

  private void describeDateRange(StringBuilder display, Where where, Operator fromOperator, String fromValue, Operator toOperator, String toValue) {
    boolean fromPast = false;
    boolean toPast = false;
    String units = where.getUnit() == null ? "" : getTermInContext(where.getUnit(), Context.plural);
    if (fromValue.startsWith("-")) {
      fromPast = true;
      fromValue = fromValue.replace("-", "");
    }
    if (toValue.startsWith("-")) {
      toPast = true;
      toValue = toValue.replace("-", "");
    }
    describeDateRangePart(display, where, fromOperator, fromValue, fromPast, units);
    display.append("to ");
    describeDateRangePart(display, where, toOperator, toValue, toPast, units);
    if (hasRelativeTo(where)) {
      describeRelativeTo(display, where.getRelativeTo());
    }
  }

  private void describeDateRangePart(StringBuilder display, Where where, Operator toOperator, String toValue, boolean toPast, String units) {
    if (toValue.matches(".d[-/].d[-/].d[s]")) {
      switch (toOperator) {
        case lt:
          display.append("before ").append(toValue);
          break;
        case lte:
          display.append("before or on ").append(toValue);
          break;
        default:
          display.append(toValue);
      }
    } else {
      switch (toOperator) {
        case lt:
          display.append("before ");
          break;
        case lte:
          display.append("up to ");
          break;
        default:
          if (!hasRelativeTo(where) && toPast) {
            display.append("within the last ");
          }
      }
      display.append(toValue.replace("-", "")).append(units);
      if (hasRelativeTo(where)) {
        if (toPast)
          display.append("prior to ");
        else
          display.append("after ");
      }
    }
  }


  private void describeRelativeTo(StringBuilder display, PropertyRef relativeTo) {
    if (relativeTo != null) {
      String propertyName = getTermInContext(relativeTo);
      if (relativeTo.getParameter() != null) {
        display.append(relativeTo.getParameter());
      } else {
        display.append("the ").append(propertyName).append(" of ");

      }

    }
  }


  private String describeIsProperty(Where property) {
    if (property.getValueLabel() != null) {
      return "'" + property.getValueLabel() + "'";
    }
    StringBuilder display = new StringBuilder();
    boolean first = true;
    for (Node set : property.getIs()) {
      if (!first)
        display.append("or ");
      first = false;
      if (set.isMemberOf())
        display.append(" in set ");
      if (set.getName() != null)
        display.append("'").append(set.getName()).append("'");
      else
        display.append("'").append(getTermInContext(set, Context.value)).append("'");
    }
    return display.toString();
  }

}
