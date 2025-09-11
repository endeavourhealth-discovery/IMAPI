package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.cache.TimedCache;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.Context;
import org.endeavourhealth.imapi.utility.Pluraliser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class QueryDescriptor {
  private static final TimedCache<String, String> queryCache = new TimedCache<>("queryCache", 120, 5, 10);
  private final EntityRepository entityRepository = new EntityRepository();
  private final EntityRepository repo = new EntityRepository();
  private final Map<String, String> nodeRefToLabel = new HashMap<>();
  private Map<String, TTEntity> iriContext;
  private StringBuilder shortDescription = new StringBuilder();

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, asHashSet(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null) return null;
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    if (query.getIri() == null)
      query.setIri(queryIri);
    query = describeQuery(query, displayMode);
    queryCache.put(queryIri, new ObjectMapper().writeValueAsString(query));
    return query;
  }

  public Match describeSingleMatch(Match match, String typeOf) throws QueryException {
    setIriNames(match);
    describeMatch(match, typeOf);
    return match;
  }

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    setIriNames(query);
    if (query.getUuid() == null) query.setUuid(UUID.randomUUID().toString());
    if (displayMode == DisplayMode.RULES && query.getRule() == null) {
      new LogicOptimizer().getRulesFromLogic(query);
    } else if (displayMode == DisplayMode.LOGICAL && query.getRule() != null) {
      new LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL);
    }
    describeMatch(query, null);
    if (query.getGroupBy() != null) {
      describeGroupBys(query.getGroupBy());
    }
    if (query.getQuery() != null) {
      for (Query matchQuery : query.getQuery())
        describeQuery(matchQuery, displayMode);
    }
    return query;
  }

  private void describeGroupBys(List<GroupBy> groupBys) {
    for (GroupBy groupBy : groupBys) {
      if (groupBy.getIri() != null) groupBy.setName(getTermInContext(groupBy.getIri()));
    }
  }

  private void describeReturn(Return ret) {
    if (ret.getAs() != null && ret.getAsDescription() != null) {
      nodeRefToLabel.put(ret.getAs(), ret.getAsDescription());
    }
    if (ret.getProperty() != null) {
      for (ReturnProperty prop : ret.getProperty()) {
        if (prop.getIri() != null) prop.setName(getTermInContext(prop.getIri()));
        if (prop.getReturn() != null) {
          describeReturn(prop.getReturn());
        }
      }
    }
  }

  private void setIriNames(Match match) throws QueryException {
    Set<String> iriSet = IriCollector.collectIris(match);
    try {
      iriContext = repo.getEntitiesWithPredicates(iriSet, asHashSet(IM.PREPOSITION, IM.CODE, RDF.TYPE, IM.DISPLAY_LABEL));
    } catch (Exception e) {
      throw new QueryException(e.getMessage() + " Query content error found by query Descriptor", e);
    }
  }

  private void setIriNames(Query query) throws QueryException {
    Set<String> iriSet = IriCollector.collectIris(query);
    try {
      iriContext = repo.getEntitiesWithPredicates(iriSet, asHashSet(IM.PREPOSITION, IM.CODE, RDF.TYPE, IM.DISPLAY_LABEL));
    } catch (Exception e) {
      throw new QueryException(e.getMessage() + " Query content error found by query Descriptor", e);
    }
  }


  public String getTermInContext(String source, Context... contexts) {
    if (source.isEmpty()) return "";
    StringBuilder term = new StringBuilder(source);
    TTEntity entity = iriContext.get(source);
    if (entity != null) {
      term = new StringBuilder(entity.getName());
    }
    for (Context context : contexts) {
      if (context == Context.PLURAL) {
        if (entity != null) {
          if (entity.get(iri(Namespace.IM + "plural")) == null) {
            term = new StringBuilder(Pluraliser.pluralise(term.toString()));
          } else {
            term = new StringBuilder(entity.get(iri(Namespace.IM + "plural")).asLiteral().getValue());
          }
        } else term = new StringBuilder(Pluraliser.pluralise(term.toString()));
      }
      if (context == Context.LOWERCASE) {
        term = new StringBuilder(term.toString().toLowerCase());
      }
    }

    return term.toString();
  }

  private String getTermInContext(Element node, Context... context) {
    if (node.getParameter() != null) {
      return node.getParameter();
    }
    if (node.getIri() != null) {
      return getTermInContext(node.getIri(), context);
    } else if (node.getVariable() != null) {
      return node.getVariable();
    } else if (node.getNodeRef() != null) {
      return node.getNodeRef();
    } else return "";
  }

  private String getTermInContext(RelativeTo ref) {
    String display = "";
    if (ref.getName() != null) {
      display = display + ref.getName();
    } else if (ref.getIri() != null) {
      display = getTermInContext(ref.getIri());
    } else if (ref.getParameter() != null) {
      display = ref.getParameter();
    }
    return display;
  }


  public void describeMatch(Match match, String inheritedType) {
    String typeOf;
    if (match.getTypeOf() == null) {
      if (inheritedType != null && match.getPath() == null) {
        match.setTypeOf(new Node().setIri(inheritedType));
        typeOf = inheritedType;
      } else typeOf = null;
    } else {
      typeOf = match.getTypeOf().getIri();
    }
    if (match.getUuid() == null) match.setUuid(UUID.randomUUID().toString());


    if (match.getReturn() != null) {
      describeReturn(match.getReturn());
      if (match.getReturn().getOrderBy() != null) {
        describeOrderBy(match.getReturn().getOrderBy());
      }
    }
    if (match.getName() == null && match.getDescription() != null) {
      match.setName(match.getDescription());
    }


    if (match.getTypeOf() != null) {
      match.getTypeOf().setName(getTermInContext(match.getTypeOf(), Context.PLURAL));
    }
    if (match.getInstanceOf() != null) {
      describeInstance(match.getInstanceOf());
    }
    if (match.getIsCohort() != null) {
      match.getIsCohort().setName(getTermInContext(match.getIsCohort().getIri(), Context.MATCH));
    }
    if (match.getThen() != null) {
      describeMatch(match.getThen(), typeOf);
    }
    if (match.getRule() != null) {
      for (Match subMatch : match.getRule()) {
        describeMatch(subMatch, typeOf);
      }
    }
    if (match.getOr() != null) {
      for (Match subMatch : match.getOr()) {
        describeMatch(subMatch, typeOf);
      }
    }
    if (match.getAnd() != null) {
      for (Match subMatch : match.getAnd()) {
        describeMatch(subMatch, typeOf);
      }
    }
    if (match.getNot() != null) {
      for (Match subMatch : match.getNot()) {
        describeMatch(subMatch, typeOf);
      }
    }
    if (match.getPath() != null) {
      for (Path path : match.getPath()) {
        describePath(path);
      }
    }

    if (match.getWhere() != null) {
      describeWhere(match.getWhere());
    }

  }

  private void describePath(Path path) {
    if (path.getIri() != null) {
      path.setName(getTermInContext(path.getIri(), Context.PLURAL));
    }
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        describePath(subPath);
      }
    }
  }

  private String getPreface(Match match) {
    StringBuilder preface = new StringBuilder();
    preface.append("Select the ");
    addReturnText(match, preface);
    return preface.toString();
  }

  private void addReturnText(Match match, StringBuilder preface) {
    if (match.getReturn() != null) {
      if (match.getReturn().getOrderBy() != null) {
        preface.append(match.getReturn().getOrderBy().getDescription()).append(" ");
      }
    }
    if (match.getReturn().getProperty() != null)
      preface.append(match.getReturn().getProperty()
        .stream().map(prop -> getTermInContext(prop.getIri(), Context.PLURAL).split(" \\(")[0])
        .collect(Collectors.joining(", ")));
  }


  private String getUnionHeader(Match match) {
    StringBuilder header = new StringBuilder();
    header.append("With the ");
    addReturnText(match, header);
    header.append(" ");
    header.append("from one of the following:");
    return header.toString();
  }

  private void describeWheres(List<Where> wheres) {
    for (Where where : wheres) {
      describeWhere(where);
    }
  }


  private void describeInstance(List<Node> inSets) {
    for (Node set : inSets) {
      String qualifier = "";
      if (set.isExclude()) {
        qualifier = "but not ";
      }
      if (set.isMemberOf()) {
        qualifier = qualifier + "in ";
      } else qualifier = qualifier + "is a";
      String label = getTermInContext(set);
      set.setName(label);
      set.setQualifier(qualifier);
    }
  }


  private void describeOrderBy(OrderLimit orderBy) {
    String orderDisplay = "";
    for (OrderDirection property : orderBy.getProperty()) {
      String field = property.getIri();
      if (field.toLowerCase().contains("date")) {
        if (property.getDirection() == Order.descending) orderDisplay = "latest ";
        else orderDisplay = "earliest ";
      } else {
        if (property.getDirection() == Order.descending) orderDisplay = "maximum ";
        else orderDisplay = "minimum ";
      }
    }
    if (orderBy.getLimit() > 1)
      orderDisplay = orderDisplay + " " + orderBy.getLimit();
    orderBy.setDescription(orderDisplay);
    shortDescription.append(orderDisplay);
  }

  private void describeWhere(Where where) {
    if (where.getUuid() == null) where.setUuid(UUID.randomUUID().toString());
    if (where.getAnd() != null) {
      describeWheres(where.getAnd());
    }
    if (where.getOr() != null) {
      describeWheres(where.getOr());
    } else if (where.getAnd() == null) {
      where.setName(getTermInContext(where, Context.PROPERTY));
      if (where.getRange() != null) {
        describeRangeWhere(where);
      }
      if (where.getValue() != null || where.getOperator() != null) {
        describeValueWhere(where);
      }
      if (where.getIs() != null) {
        describeWhereIs(where);
      }
      if (where.getIsNull()) {
        where.setValueLabel("is not recorded");
      }
      if (where.getIsNotNull()) {
        where.setValueLabel("is recorded");
      }
    }
  }

  private void describeValue(Assignable assignable, Operator operator, boolean date, String value, TTIriRef unit, boolean relativeTo, boolean isRange) {
    String qualifier = null;
    boolean inclusive = false;
    boolean past = false;
    assignable.setValueLabel("");
    if (value != null) if (value.startsWith("-")) past = true;
    String relativity = null;
    if (null != operator) switch (operator) {
      case gt:
        if (date) {
          if (!isRange) {
            if (value != null) {
              if (value.equals("0")) {
                qualifier = "after ";
              } else {
                qualifier = "is within ";
                if (past && relativeTo) relativity = " before the ";
                if (!past && relativeTo) relativity = " of the ";
              }
            } else qualifier = "after";
          }
        } else {
          if (!isRange) qualifier = "greater than ";
          if (relativeTo && value != null)
            relativity = " relative to the ";
        }
        break;
      case gte:
        inclusive = true;
        if (date) {
          if (!isRange) {
            qualifier = "on or after";
            relativity = " the ";
          }
          if (past && relativeTo) relativity = " before ";
        } else {
          if (!isRange) {
            qualifier = "equal to or more than ";
            if (relativeTo && value != null)
              relativity = " relative to the ";
          }
        }
        break;
      case lt:
        if (date) {
          if (!isRange) {
            qualifier = "before ";
            if (relativeTo) relativity = " the ";
          }
          if (past && relativeTo) relativity = " before the ";
        } else {
          if (!isRange) {
            qualifier = "under ";
            if (relativeTo && value != null)
              relativity = " relative to the ";
          }
        }
        break;
      case lte:
        inclusive = true;
        if (date) {
          if (!isRange) {
            qualifier = "on or before ";

          }
          if (past && relativeTo) relativity = " before the ";
        } else {
          if (!isRange) {
            qualifier = "equal to or less than ";
            if (relativeTo && value != null)
              relativity = " relative to the ";
          }
        }
        break;
      case contains:
        qualifier = "contains ";
        break;
      case start:
        qualifier = "starts with ";
        break;
      case eq:
        if (date) if (!isRange) {
          qualifier = " on ";
          if (relativeTo) relativity = " the ";
        }
        break;
    }
    if (qualifier != null) {
      assignable.setQualifier(qualifier);
    }
    if (value != null) {
      if (!date || !value.equals("0")) {
        assignable.setValueLabel(value.replace("-", ""));
        if (unit != null) {
          assignable.setValueLabel(assignable.getValueLabel() + " " + getTermInContext(unit.getIri(), Context.PLURAL));
        }
      }
    }
    if (inclusive && qualifier == null) {
      if (assignable.getValueLabel() != null) {
        assignable.setValueLabel(assignable.getValueLabel() + " (inc.)");
      }
    }
    if (relativity != null) assignable.setValueLabel(assignable.getValueLabel() + relativity);
  }


  private void describeFrom(Where where, Value from) {
    String qualifier;
    boolean inclusive = false;
    boolean past = false;
    Operator operator = from.getOperator();
    String value = from.getValue();
    if (value != null) if (value.startsWith("-")) past = true;
    qualifier = "is between ";
    if (null != operator) if (operator == Operator.gte) {
      inclusive = true;
    }
    if (value != null) {
      qualifier = qualifier + value.replace("-", "");
    }
    if (from.getUnits() != null) {
      qualifier = qualifier + " " + getTermInContext(from.getUnits().getIri(), Context.PLURAL);
    }
    if (inclusive) {
      qualifier = qualifier + " (inc.)";
    }
    if (past) qualifier = qualifier + " before";
    where.setQualifier(qualifier);
  }

  private void describeTo(Where where, Value to) {
    String qualifier = null;
    boolean inclusive = false;
    Operator operator = to.getOperator();
    String value = to.getValue();
    boolean date = false;
    if (where.getIri() != null) {
      date = where.getIri().toLowerCase().contains("date");
    }
    if (null != operator) switch (operator) {
      case gt:
        qualifier = "and ";
        break;
      case gte:
        inclusive = true;
        qualifier = "and ";
        break;
      case lt:
        if (date) {
          qualifier = "and before ";
        } else qualifier = "below ";
        break;
      case lte:
        inclusive = true;
        if (date) {
          qualifier = "and ";
        } else qualifier = "below ";
        break;
      case eq:
        if (date) {
          qualifier = "";
        } else qualifier = "between ";
        break;
    }
    if (value != null) {
      qualifier = qualifier + value.replace("-", "");
    }
    if (to.getUnits() != null) {
      qualifier = qualifier + " " + getTermInContext(to.getUnits().getIri(), Context.PLURAL);
    }
    if (inclusive) {
      qualifier = qualifier + " (inc.)";
    }

    if (value != null) {
      if (date) {
        if (!value.contains("-")) {
          qualifier = qualifier + " after ";
        } else qualifier = qualifier + " before ";
      }
    }

    where.setQualifier(where.getQualifier() + " and " + qualifier);
  }


  private void describeValueWhere(Where where) {
    boolean date = false;
    if (where.getIri() != null) date = where.getIri().toLowerCase().contains("date");
    Operator operator = where.getOperator();
    describeValue(where, operator, date, where.getValue(), where.getUnits(), where.getRelativeTo() != null, false);
    describeRelativeTo(where);
  }

  private void describeRangeWhere(Where where) {
    describeFrom(where, where.getRange().getFrom());
    describeTo(where, where.getRange().getTo());
    describeRelativeTo(where);
  }

  private void describeRelativeTo(Where where) {
    RelativeTo relativeTo = where.getRelativeTo();
    if (relativeTo != null) {
      String relation = getRelation(where.getRelativeTo());
      if (relation != null) relativeTo.setQualifier(relation);
    }
  }

  private String getRelation(RelativeTo relativeTo) {
    if (relativeTo.getNodeRef() != null) {
      if (nodeRefToLabel.get(relativeTo.getNodeRef()) != null) {
        relativeTo.setTargetLabel(nodeRefToLabel.get(relativeTo.getNodeRef()));
      } else relativeTo.setTargetLabel("the above");
    }
    String relation = null;
    if (relativeTo.getIri() != null) {
      String propertyName = getTermInContext(relativeTo);
      relation = propertyName + " of ";
    }
    if (relativeTo.getParameter() != null) {
      if (relativeTo.getParameter().toLowerCase().contains("searchdate")) {
        relation = (relation != null ? relation : "") + "search date";
      } else if (relativeTo.getParameter().toLowerCase().contains("achievementdate")) {
        relation = (relation != null ? relation : "") + "achievement date";
      } else relation = (relation != null ? relation : "") + relativeTo.getParameter();
    }
    return relation;
  }


  private void describeWhereIs(Where where) {
    for (Node set : where.getIs()) {
      if (iriContext.get(set.getIri()) != null) {
        TTEntity nodeEntity = (iriContext.get(set.getIri()));
        set.setCode(nodeEntity.getCode());
        String modifier = set.isExclude() ? " but not: " : " ";
        set.setQualifier(modifier);
      }
      String value = getTermInContext(set);
      set.setName(value);
      if (iriContext.get(set.getIri()) != null) {
        set.setCode(iriContext.get(set.getIri()).getCode());
      }
    }
    StringBuilder valueLabel = new StringBuilder();
    for (int i = 0; i < where.getIs().size(); i++) {
      if (i > 1) {
        valueLabel.append(" ...+ more");
        break;
      } else {
        if (i > 0) valueLabel.append(", or ");
      }
      Node set = where.getIs().get(i);
      valueLabel.append(set.getQualifier() != null ? set.getQualifier() + " " : "").append(getShortName(set.getName()));
    }
    where.setValueLabel(valueLabel.toString());
    if (where.getShortLabel() != null)
      shortDescription.append(where.getShortLabel()).append(" ");
    else shortDescription.append(where.getValueLabel()).append(" ");
  }

  public void generateUUIDs(Match match) {
    if (match.getUuid() == null) {
      match.setUuid(UUID.randomUUID().toString());
    }
    if (match.getWhere() != null) {
      generateUUIDs(match.getWhere());
    }
    for (List<Match> matches : Arrays.asList(match.getRule(), match.getOr(), match.getAnd(), match.getNot())) {
      if (matches != null) {
        for (Match subMatch : matches) {
          generateUUIDs(subMatch);
        }
      }
    }
  }

  public static String getShortName(String name) {
    if (name == null) return null;
    int length = name.length();
    StringBuilder startShort = new StringBuilder(name.substring(0, Math.min(length, 50)));
    if (length > 50) {
      boolean bracket = false;
      for (int i = 50; i < Math.min(length, 70); i++) {
        Character c = name.charAt(i);
        if (c == '(') bracket = true;
        if (c == ')') bracket = false;
        if (c == ' ') {
          if (!bracket) return startShort + "...";
          else startShort.append(c);
        } else startShort.append(c);
      }
      return startShort.toString() + "...";
    } else return startShort.toString();
  }

  public void generateUUIDs(Where where) {
    if (where.getUuid() == null) {
      where.setUuid(UUID.randomUUID().toString());
    }
    for (List<Where> wheres : Arrays.asList(where.getOr(), where.getAnd(), where.getNot())) {
      if (wheres != null) {
        for (Where subWhere : wheres) {
          generateUUIDs(subWhere);
        }
      }
    }
  }

  public String getDescriptions(Match match) {
    if (match.getDescription() != null) return match.getDescription();
    StringBuilder description = new StringBuilder();
    String operators = "or,and,not";
    int opIndex = -1;
    for (List<Match> matches : Arrays.asList(match.getOr(), match.getAnd(), match.getNot())) {
      opIndex++;
      if (matches != null) {
        for (Match subMatch : matches) {
          if (subMatch.getDescription() != null) {
            if (description.isEmpty()) description.append(subMatch.getDescription());
            else
              description.append(", ").append(operators.split(",")[opIndex]).append(" ").append(subMatch.getDescription());
          }
        }
      }
    }
    return description.toString();
  }

  public String getShortDescription(Match match) throws QueryException {
    shortDescription = new StringBuilder();
    setIriNames(match);
    if (match.getReturn() != null && match.getReturn().getOrderBy() != null) {
      describeOrderBy(match.getReturn().getOrderBy());
      shortDescription.append(" ");
    }
    if (match.getWhere() != null) {
      describeWhere(match.getWhere());
    }
    return shortDescription.toString();
  }
}




