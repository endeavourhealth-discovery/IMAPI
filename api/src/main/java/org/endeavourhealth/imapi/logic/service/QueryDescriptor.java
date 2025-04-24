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
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class QueryDescriptor {
  private final EntityRepository entityRepository = new EntityRepository();
  private Map<String, TTEntity> iriContext;
  private final EntityRepository repo = new EntityRepository();
  private String namespace= IM.NAMESPACE;
  private static final TimedCache<String, String> queryCache = new TimedCache<>("queryCache", 120, 5, 10);

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null) return null;
    //String queryString = queryCache.get(queryIri);
    //if (queryString != null) return new ObjectMapper().readValue(queryString, Query.class);
    namespace= queryIri.substring(queryIri.lastIndexOf("#") + 1);
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    query = describeQuery(query, displayMode);
    queryCache.put(queryIri, new ObjectMapper().writeValueAsString(query));
    return query;
  }

  public Match describeSingleMatch(Match match) throws QueryException {
    setIriNames(match);
    describeMatch(match);
    return match;
  }

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    if (query.getIri()==null)
      query.setIri(namespace+ UUID.randomUUID());
    if (!query.hasRules()){
      if (query.getBool()==null&&query.getMatch()!=null) query.setBool(Bool.and);
    }
    setIriNames(query);
    if (query.getInstanceOf() != null) {
      describeInstance(query.getInstanceOf());
    }
    if (query.getTypeOf() != null) {
      String typeName = getTermInContext(query.getTypeOf().getIri(), Context.PLURAL);
      query.getTypeOf().setName(typeName);
      query.getTypeOf().setDescription(" with the following features : ");
    }
    if (query.getMatch() != null) {
      describeMatches(query);
    }

    if (query.getQuery() != null) {
      describeQueries(query);
    }
    if (query.getReturn() != null) {
      describeReturn(query.getReturn());
    }
    if (query.getGroupBy()!=null){
      describeGroupBys(query.getGroupBy());
    }
    if (query.getOrderBy()!=null){
      describeOrderBy(query.getOrderBy());
    }
    if (displayMode== DisplayMode.LOGICAL) {
      if (query.hasRules()) {
        new LogicOptimizer().resolveLogic(query, displayMode);
      }
    }
    else if (displayMode==DisplayMode.RULES){
      if (!query.hasRules())
        new LogicOptimizer().createRules(query);
    }
    return query;
  }

  private void describeGroupBys(List<GroupBy> groupBys){
    for (GroupBy groupBy:groupBys){
      if (groupBy.getIri()!=null) groupBy.setName(getTermInContext(groupBy.getIri()));
    }
  }


  private void describeReturn(Return ret) {
    if (ret.getProperty() != null) {
      for (ReturnProperty prop : ret.getProperty()) {
        if (prop.getIri() != null) prop.setName(getTermInContext(prop.getIri()));
        if (prop.getReturn() != null) {
          describeReturn(prop.getReturn());
        }
      }
    }
  }


  private void describeQueries(Query query) throws QueryException, JsonProcessingException {
    for (Query subQuery : query.getQuery()) {
      describeQuery(subQuery,DisplayMode.ORIGINAL);
    }
  }
  private void setIriNames(Match match) throws QueryException {
    Set<String> iriSet = new HashSet<>();
    setIriSet(match, iriSet);
    try {
      iriContext = repo.getEntitiesWithPredicates(iriSet, Set.of(IM.PREPOSITION, IM.CODE, RDF.TYPE));
    } catch (Exception e) {
      throw new QueryException(e.getMessage() + " Query content error found by query Descriptor", e);
    }
  }


  private void setIriNames(Query query) throws QueryException {
    Set<String> iriSet = new HashSet<>();
    setIriSet(query, iriSet);
    if (query.getQuery() != null) {
      for (Query subQuery : query.getQuery()) {
        setIriSet(subQuery, iriSet);
      }
    }
    if (query.getReturn() != null) {
      setIriSet(query.getReturn(), iriSet);
    }
    try {
      iriContext = repo.getEntitiesWithPredicates(iriSet, Set.of(IM.PREPOSITION, IM.CODE, RDF.TYPE));
    } catch (Exception e) {
      throw new QueryException(e.getMessage() + " Query content error found by query Descriptor", e);
    }
  }

  private void setIriSet(Return ret, Set<String> iriSet) {
    if (ret.getProperty() != null) {
      for (ReturnProperty prop : ret.getProperty()) {
        if (prop.getIri() != null) iriSet.add(prop.getIri());
        if (prop.getReturn() != null) {
          setIriSet(prop.getReturn(), iriSet);
        }
      }
    }
  }




  private void setIriSet(Match match, Set<String> iriSet) {

    if (match.getTypeOf() != null) {
      iriSet.add(match.getTypeOf().getIri());
    }
    if (match.getPath() != null) {
      setIriSet(match.getPath(),iriSet);
    }
    if (match.getInstanceOf() != null) {
      match.getInstanceOf().forEach(i -> iriSet.add(i.getIri()));
    }
    if (match.getMatch() != null) {
      for (Match subMatch : match.getMatch()) {
        setIriSet(subMatch, iriSet);
      }
    }

    if (match.getWhere() != null) {
      for (Where where : match.getWhere()) {
        setIriSet(where, iriSet);
      }
    }
    if (match.getReturn()!=null){
      setIriSet( match.getReturn(),iriSet);
    }
  }

  private void setIriSet(Path path, Set<String> iriSet) {
    if (path.getIri()!=null){
      iriSet.add(path.getIri());
    }
    if (path.getWhere()!=null) setIriSet(path.getWhere(),iriSet);
  }

  private void setIriSet(Where where, Set<String> iriSet) {
    if (where.getIri() != null) {
      iriSet.add(where.getIri());
    }
    if (where.getWhere() != null) {
      for (Where subWhere : where.getWhere()) {
        setIriSet(subWhere, iriSet);
      }
    }
    if (where.getIs() != null) {
      for (Node node : where.getIs())
        iriSet.add(node.getIri());
    }
    setIriSet((Assignable) where, iriSet);
    if (where.getRange() != null) {
      if (where.getRange().getFrom() != null) {
        setIriSet(where.getRange().getFrom(), iriSet);
      }
      if (where.getRange().getFrom() != null) {
        setIriSet(where.getRange().getFrom(), iriSet);
      }

    }
  }

  private void setIriSet(Assignable assignable, Set<String> iriSet) {
    if (assignable.getUnit() != null) {
      iriSet.add(assignable.getUnit().getIri());
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
          if (entity.get(iri(IM.NAMESPACE + "plural")) == null) {
            if (!term.toString().toLowerCase().endsWith("s")) term.append("s");
          } else {
            term = new StringBuilder(entity.get(iri(IM.NAMESPACE + "plural")).asLiteral().getValue());
          }
        } else if (!term.toString().toLowerCase().endsWith("s")) term.append("s");
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
    if (node.getName() != null) {
      return node.getName();
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




  private void describeMatches(Match match) {
    int index = 0;
    for (Match subMatch : match.getMatch()) {
      if (subMatch.getTypeOf()==null) subMatch.setTypeOf(match.getTypeOf());
      describeMatch(subMatch);
      index++;
    }
    if (match.getWhere() != null) {
      if (match.getWhere().size() > 1 && match.getBool() == null) match.setBool(Bool.and);
      describeWheres(match.getWhere());
    }
  }


  private void describeMatches(Query query) {
    int index = 0;
    for (Match subMatch : query.getMatch()) {
      if (subMatch.getTypeOf()==null){
        subMatch.setTypeOf(query.getTypeOf());
      }
      describeMatch(subMatch);
      index++;
    }
  }


  public void describeMatch(Match match) {
    if (match.getIri()==null)
      match.setIri(namespace+ UUID.randomUUID());
    if (match.getOrderBy()!=null){
      describeOrderBy(match.getOrderBy());
    }

    if (match.getReturn() != null) {
      describeReturn(match.getReturn());
    }
    if (match.isUnion()){
      match.setHeader(getUnionHeader(match));
    }
    else if (match.getReturn()!=null){
      if (match.getMatch()==null||match.getWhere()!=null) {
        match.setPreface(getPreface(match));
      }
    }
    if (match.getName() == null) {
      if (match.getDescription() != null) {
        match.setName(match.getDescription());
      }
    }

    if (match.getPath() != null) describePath(match.getPath());
    if (match.getTypeOf() != null) {
      match.getTypeOf().setName(getTermInContext(match.getTypeOf(), Context.PLURAL));
    }
    if (match.getInstanceOf() != null) {
      describeInstance(match.getInstanceOf());
    }

    if (match.getMatch() != null) {
      describeMatches(match);
    }

    if (match.getWhere() != null) {
      describeWheres(match.getWhere());
    }



  }

  private String getPreface(Match match) {
    StringBuilder preface= new StringBuilder();
    preface.append("Select the ");
    addReturnText(match,preface);
    return preface.toString();
  }

  private void addReturnText(Match match, StringBuilder preface) {
      if (match.getOrderBy()!=null){
        preface.append(match.getOrderBy().getDescription()).append(" ");
      }
      if (match.getReturn().getProperty()!=null)
        preface.append(match.getReturn().getProperty()
          .stream().map(prop -> getTermInContext(prop.getIri(), Context.PLURAL).split(" \\(")[0])
          .collect(Collectors.joining(", ")));
  }



  private String getUnionHeader(Match match) {
    StringBuilder header= new StringBuilder();
    header.append("With the ");
    addReturnText(match,header);
    header.append(" ");
    header.append("from one of the following:");
    return header.toString();
  }


  private void describeWheres(List<Where> wheres) {
    Where conceptWhere = getConceptWhere(wheres);
    if (conceptWhere != null) {
      wheres.remove(conceptWhere);
      wheres.addFirst(conceptWhere);
      describeWhere(conceptWhere);
    }
    for (Where where : wheres) {
      if (where != conceptWhere) {
        describeWhere(where);

      }
    }
  }


  private String getPreposition(IriLD node) {
    if (node.getIri() != null) {
      TTEntity entity = iriContext.get(node.getIri());
      if (entity.get(iri(IM.PREPOSITION)) != null) {
        return entity.get(iri(IM.PREPOSITION)).asLiteral().getValue();
      } else return null;
    } else return null;
  }


  private void describePath(Path path) {
      String label = getTermInContext(path.getIri(), Context.PLURAL);
      String preposition = getPreposition(path);
      path.setName(label + (preposition != null ? " " + preposition : ""));
      if (path.getWhere() != null) {
        describeWhere(path.getWhere());
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
    String orderDisplay;
    String field = orderBy.getProperty().getIri();
    if (field.toLowerCase().contains("date")) {
      if (orderBy.getProperty().getDirection() == Order.descending) orderDisplay = "latest ";
      else orderDisplay = "earliest ";
    } else {
      if (orderBy.getProperty().getDirection() == Order.descending) orderDisplay = "maximum ";
      else orderDisplay = "minimum ";
    }
    if (orderBy.getLimit() > 1)
      orderDisplay = orderDisplay + " " + orderBy.getLimit();
    orderBy.setDescription(orderDisplay);
  }

  private Where getConceptWhere(List<Where> wheres) {
    for (Where where : wheres) {
      if (where.getIri() != null) if (where.getIri().equals(IM.NAMESPACE + "concept")) return where;
    }
    return null;
  }


  private void describeWhere(Where where) {
    if (where.getWhere() != null) {
      if (where.getBool() == null) where.setBool(Bool.and);
      describeWheres(where.getWhere());
    } else {
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
    if (value != null) if (value.startsWith("-")) past = true;
    String relativity = null;
    if (null != operator) switch (operator) {
      case gt:
        if (date) {
          if (!isRange) if (value != null) {
            if (value.equals("0")) {
              qualifier = "after ";
            } else {
              qualifier = "is within ";
              if (past && relativeTo) relativity = " before ";
              if (!past && relativeTo) relativity = " of ";
            }
          }
        } else {
          if (!isRange) qualifier = "greater than ";
          if (relativeTo && value != null)
            relativity = " on ";
        }
        break;
      case gte:
        inclusive = true;
        if (date) {
          if (!isRange) {
            qualifier = "on or after";
          }
          if (past && relativeTo) relativity = " before ";
        } else {
          if (!isRange) {
            qualifier = "equal to or more than ";
            if (relativeTo && value != null)
              relativity = " on ";
          }
        }
        break;
      case lt:
        if (date) {
          if (!isRange) {
            qualifier = "before ";
          }
          if (past && relativeTo) relativity = " before ";
        } else {
          if (!isRange) {
            qualifier = "under ";
            if (relativeTo && value != null)
              relativity = " on ";
          }
        }
        break;
      case lte:
        inclusive = true;
        if (date) {
          if (!isRange) {
            qualifier = "on or before ";
          }
          if (past && relativeTo) relativity = " before ";
        } else {
          if (!isRange) {
            qualifier = "equal to or less than ";
            if (relativeTo && value != null)
              relativity = " on ";
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
        }
        break;
    }
    if (qualifier != null) {
      assignable.setQualifier(qualifier);
    }
    if (value != null&&!value.equals("0")) {
      assignable.setValueLabel(value.replace("-", ""));
      if (unit != null) {
        assignable.setValueLabel(assignable.getValueLabel() + " " + getTermInContext(unit.getIri(), Context.LOWERCASE));
      }
    }
    if (inclusive && qualifier == null) {
      if (assignable.getValueLabel() != null) {
        assignable.setValueLabel(assignable.getValueLabel() + " (inc.)");
      }
    }
    if (relativity != null) assignable.setValueLabel(assignable.getValueLabel() + relativity);
  }


  private void describeFrom(Where where, Value from){
    String qualifier = null;
    boolean inclusive = false;
    boolean past = false;
    Operator operator = from.getOperator();
    String value = from.getValue();
    TTIriRef units=from.getUnit();
    boolean date = false;
    if (where.getIri() != null) {
      date = where.getIri().toLowerCase().contains("date");
    }
    if (value != null) if (value.startsWith("-")) past = true;
    qualifier = "between ";
    if (null != operator) if (operator == Operator.gte) {
      inclusive = true;
    }
    if (value != null&&!value.equals("0")) {
      qualifier = qualifier + value.replace("-", "");
    }
    if (units != null) {
        qualifier= qualifier + " " + getTermInContext(units.getIri(), Context.LOWERCASE);
    }

    if (inclusive) {
      qualifier= qualifier + " (inc.)";
      }
    if (past) qualifier= qualifier + " before";
    where.setQualifier(qualifier);
  }


  private void describeTo(Where where, Value from){
    String qualifier = null;
    boolean inclusive = false;
    boolean past = false;
    Operator operator = from.getOperator();
    String value = from.getValue();
    TTIriRef units=from.getUnit();
    boolean date = false;
    if (where.getIri() != null) {
      date = where.getIri().toLowerCase().contains("date");
    }
    if (value != null) if (value.startsWith("-")) past = true;
    String relativity = null;
    if (null != operator) switch (operator) {
      case gt:
        qualifier = "and ";
        break;
      case gte:
        inclusive = true;
        qualifier ="and ";
        break;
      case lt:
        if (date) {
          qualifier = "and before ";
        } else qualifier="below ";
        break;
      case lte:
        inclusive = true;
        if (date) {
          qualifier = "and ";
        } else qualifier="below ";
        break;
      case eq:
        if (date) {
          qualifier = "";
        } else qualifier="between ";
        break;
    }
    if (value != null&&!value.equals("0")) {
      qualifier = qualifier + value.replace("-", "");
    }
    if (units != null) {
      qualifier= qualifier + " " + getTermInContext(units.getIri(), Context.LOWERCASE);
    }
    if (value != null&&!value.equals("0")) {
      if (date) {
        if (!value.contains("-")) {
          qualifier = qualifier + " after ";
        } else qualifier = qualifier + " before ";
      }
    }

    if (inclusive) {
      qualifier= qualifier + " (inc.)";
    }
    where.setQualifier(where.getQualifier()+" "+qualifier);
  }



  private void describeValueWhere(Where where) {
    boolean date = false;
    if (where.getIri() != null) date = where.getIri().toLowerCase().contains("date");
    Operator operator = where.getOperator();
    describeValue(where, operator, date, where.getValue(), where.getUnit(), where.getRelativeTo() != null, false);
    describeRelativeTo(where);
  }


  private void describeRangeWhere(Where where) {
    describeFrom(where,where.getRange().getFrom());
    describeTo(where,where.getRange().getTo());
    describeRelativeTo(where);
  }


  private void describeRelativeTo(Where where) {
    RelativeTo relativeTo = where.getRelativeTo();
    if (relativeTo != null) {
      String relation = null;
      if (relativeTo.getIri() != null) {
        String propertyName = getTermInContext(relativeTo);
        relation = propertyName + " of ";
      }
      if (relativeTo.getParameter() != null) {
        if (relativeTo.getParameter().toLowerCase().contains("referencedate")) {
          relation = (relation != null ? relation : "") + "the reference date";
        } else if (relativeTo.getParameter().toLowerCase().contains("baselinedate")) {
          relation = (relation != null ? relation : "") + "the achievement date";
        } else relation = (relation != null ? relation : "") + relativeTo.getParameter();
      }
      if (relation != null) relativeTo.setQualifier(relation);
    }
  }

  private void describeWhereIs(Where where) {
    for (Node set : where.getIs()) {
      if (iriContext.get(set.getIri()) != null) {
        String modifier = "";
        TTEntity nodeEntity = (iriContext.get(set.getIri()));
        set.setCode(nodeEntity.getCode());
        if (nodeEntity.getType().get(0).asIriRef().getIri().contains("Set")) {
          modifier = set.isExclude() ? " but not  in : " : " in ";
        } else if (nodeEntity.getType().get(0).asIriRef().getIri().contains("Query"))
          modifier = set.isExclude() ? "not in cohort : " : "in cohort : ";
        else if (set.isExclude()) modifier = "exclude ";
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

      valueLabel.append(set.getQualifier() != null ? set.getQualifier() + " " : "").append(set.getName());

      where.setValueLabel(valueLabel.toString());
    }
  }
}


