package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.Context;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class QueryDescriptor {
  private final EntityRepository entityRepository = new EntityRepository();
  private Map<String,TTEntity> iriContext;
  private final EntityRepository repo = new EntityRepository();

  public Query describeQuery(String queryIri) throws JsonProcessingException, QueryException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    return describeQuery(query);
  }

  public Query describeQuery(Query query) throws QueryException {
    setIriNames(query);
    if (query.getTypeOf()!=null){
      query.getTypeOf().setName(getTermInContext(query.getTypeOf(), Context.PLURAL));
      query.getTypeOf().setDescription(" with the following features : ");
    }
    if (query.getMatch()!=null){
      describeMatches(query);
    }
    if (query.getWhere()!=null){
      describeWheres(query);
    }
    if (query.getQuery()!=null){
      describeQueries(query);
    }
    if (query.getReturn()!=null){
      describeReturns(query);
    }
    return query;
  }

  private void describeReturns(Query query) {
   for (Return ret:query.getReturn()){
     describeReturn(ret);
   }
  }

  private void describeReturn(Return ret) {
    if (ret.getProperty()!=null){
      for (ReturnProperty prop:ret.getProperty()){
        if (prop.getIri()!=null)
          prop.setName(getTermInContext(prop.getIri()));
        if (prop.getReturn()!=null){
          describeReturn(prop.getReturn());
        }
      }
    }


  }


  private void describeQueries(Query query) throws QueryException {
    for (Query subQuery:query.getQuery()){
      describeQuery(subQuery);
    }
  }

  private void setIriNames(Query query) throws QueryException {
    Set<String> iriSet= new HashSet<>();
    setIriSet(query,iriSet);
    if (query.getQuery()!=null){
      for (Query subQuery:query.getQuery()){
        setIriSet(subQuery,iriSet);
      }
    }
    if (query.getReturn()!=null){
      for (Return ret: query.getReturn()) {
        setIriSet(ret, iriSet);
      }
    }
    try {
      iriContext = repo.getEntitiesWithPredicates(iriSet, Set.of(IM.PREPOSITION));
    }
    catch (Exception e)
    {
      throw new QueryException(e.getMessage()+ " Query content error found by query Descriptor",e);
    }
  }

  private void setIriSet(Return ret,Set<String> iriSet) {
    if (ret.getProperty() != null) {
      for (ReturnProperty prop : ret.getProperty()) {
        if (prop.getIri() != null)
          iriSet.add(prop.getIri());
        if (prop.getReturn() != null) {
          setIriSet(prop.getReturn(), iriSet);
        }
      }
    }
  }


  private void setIriSet(Match match,Set<String> iriSet) {
    if (match.getTypeOf()!=null){
      iriSet.add(match.getTypeOf().getIri());
    }
    if (match.getPath()!=null){
      for (IriLD path:match.getPath()) {
        iriSet.add(path.getIri());
      }
    }
    if (match.getInstanceOf()!=null){
      match.getInstanceOf().forEach(i-> iriSet.add(i.getIri()));
    }
    if (match.getMatch()!=null){
      for (Match subMatch:match.getMatch()){
        setIriSet(subMatch,iriSet);
      }
    }
    if (match.getThen()!=null)
      setIriSet(match.getThen(),iriSet);
    if (match.getWhere()!=null){
      for (Where where:match.getWhere()){
        setIriSet(where,iriSet);
      }
    }
  }
  private void setIriSet(Where where,Set<String> iriSet) {
    if (where.getIri()!=null) {
      iriSet.add(where.getIri());
    }
    if (where.getMatch()!=null){
      setIriSet(where.getMatch(),iriSet);
    }
    if (where.getWhere()!=null){
      for (Where subWhere:where.getWhere()){
        setIriSet(subWhere,iriSet);
      }
    }
    if (where.getIs()!=null){
      if (where.getValueLabel()==null)
        for (Node node:where.getIs())
          iriSet.add(node.getIri());
    }
  }


  public String getTermInContext(String source, Context ...contexts) {
    if (source.equals(""))
      return "";
    StringBuilder term= new StringBuilder(source);
    TTEntity entity= iriContext.get(source);
    if (entity!=null){
      term = new StringBuilder(entity.getName());
    }
    for (Context context:contexts) {
      if (context == Context.PLURAL) {
        if (entity != null) {
          if (entity.get(iri(IM.NAMESPACE + "plural")) == null) {
            if (!term.toString().toLowerCase().endsWith("s"))
              term.append("s");
          } else {
            term = new StringBuilder(entity.get(iri(IM.NAMESPACE + "plural")).asLiteral().getValue());
          }
        } else if (!term.toString().toLowerCase().endsWith("s"))
          term.append("s");
      }
      if (context==Context.LOWERCASE){
        term = new StringBuilder(term.toString().toLowerCase());
      }
    }
    return term.toString();
  }

  private String getTermInContext(Node node, Context... context) {
    if (node.getParameter() != null) {
      return node.getParameter();
    }
    if (node.getName()!=null){
      return node.getName();
    }
    if (node.getIri() != null) {
      return getTermInContext(node.getIri(),context);
    }
    else if (node.getVariable() != null) {
      return node.getVariable();
    }
    else if (node.getNodeRef() != null) {
      return node.getNodeRef();
    }
    else
      return "";
  }

  private String getTermInContext(PropertyRef ref) {
    String display = "";
    if (ref.isInverse()) {
      display = "is ";
    }
    if (ref.getName() != null) {
      display = display + ref.getName();
    }
    else if (ref.getIri() != null) {
      display = getTermInContext(ref.getIri());
    }
    else if (ref.getParameter() != null) {
      display = ref.getParameter();
    }
    return display;
  }


  public void describeThen(Match then) {
    then.setIncludeIf("then test if");
    describeMatch(then);
  }


  private void describeMatches(Match match) {
    Bool operator= match.getBoolMatch();
    if (operator==null)
      operator= Bool.and;
    int index=-1;
    for (Match subMatch : match.getMatch()) {
      index++;
      if (operator==Bool.or&&index==0)
        subMatch.setInlineOperator("either");
      if (index>0){
        subMatch.setInlineOperator(operator.toString());
      }
      describeMatch(subMatch);

    }
  }


  public void describeMatch(Match match) {
    if (match.getName()==null) {
      if (match.getDescription() != null) {
        match.setName(match.getDescription());
      }
    }
    if (match.getPath()!=null){
      describePath(match.getPath());
    }
    if (match.getOrderBy()!=null){
      describeOrderBy(match.getOrderBy());
    }
    if (match.getTypeOf()!=null) {
      match.getTypeOf().setName(getTermInContext(match.getTypeOf(), Context.PLURAL));
    }
    if (match.getInstanceOf() != null) {
      describeInstance(match.getInstanceOf());
    }

    if (match.getMatch() != null) {
      describeMatches(match);
    }
    if (match.getWhere()!=null){
      describeWheres(match);
    }
    if (match.getThen()!=null) {
      describeThen(match.getThen());
    }
    if (match.getVariable()!=null){
      match.setVariable(match.getVariable());
    }


  }



  private void describeWheres(Where where) {
    if (where.getBoolWhere() == null) {
      where.setBoolWhere(Bool.and);
    }
    Bool operator = where.getBoolWhere();
    int index=-1;
    for (Where subWhere : where.getWhere()) {
      index++;
      if (index>0) {
        subWhere.setInlineOperator(operator.toString());
      }
      describeWhere(subWhere);
    }
    if (where.getMatch()!=null) {
      describeMatch(where.getMatch());

    }

  }

  private void describeWheres(Match match) {
    if (match.getWhere().size()>1&& match.getBoolWhere() == null)
      match.setBoolWhere(Bool.and);
    Bool operator = match.getBoolWhere();
    int index=-1;
    Where conceptWhere= getConceptWhere(match);
    if (conceptWhere!=null){
      match.getWhere().remove(conceptWhere);
      match.getWhere().add(0,conceptWhere);
      describeWhere(conceptWhere);
    }
    for (Where where : match.getWhere()) {
      index++;
      if (where != conceptWhere) {
        if (index > 0) {
          where.setInlineOperator(operator.toString());
        }
        describeWhere(where);
      }
    }
  }


  private String getPreposition(IriLD node) {
    if (node.getIri()!= null){
      TTEntity entity = iriContext.get(node.getIri());
      if (entity.get(iri(IM.PREPOSITION))!=null) {
        return entity.get(iri(IM.PREPOSITION)).asLiteral().getValue();
      }
      else return null;
    }
    else return null;
  }


  private void describePath(List<IriLD> paths) {
    for (IriLD path : paths) {
      String label = getTermInContext(path.getIri(),Context.PLURAL);
      String preposition= getPreposition(path);
      path.setDescription(label+ (preposition!=null ? " "+preposition:""));
    }
  }


  private void describeInstance(List<Node> inSets) {
    for (Node set : inSets) {
      String qualifier="";
      if (set.isExclude()){
        qualifier="but not ";
      }
      if (set.isMemberOf()) {
        qualifier = qualifier+ "in cohort:";
      }
      else
        qualifier = qualifier+ "is a";
      String label = getTermInContext(set);
      set.setName(label);
      set.setQualifier(qualifier);

    }
  }

  private void describeOrderBy(OrderLimit orderBy) {
    String orderDisplay;
    String field = orderBy.getProperty().getIri();
    if (field.toLowerCase().contains("date")) {
      if (orderBy.getProperty().getDirection() == Order.descending)
        orderDisplay = "get latest ";
      else
        orderDisplay = "get earliest ";
    } else {
      if (orderBy.getProperty().getDirection() == Order.descending)
        orderDisplay = "get maximum ";
      else
        orderDisplay = "get minimum ";
    }
    if (orderBy.getLimit() > 1)
      orderDisplay = orderDisplay + " " + orderBy.getLimit();
    orderBy.setDescription(orderDisplay);
  }

  private Where getConceptWhere(Match match){
    for (Where where:match.getWhere()){
      if (where.getIri()!=null)
        if (where.getIri().equals(IM.NAMESPACE+"concept"))
          return where;
    }
    return null;
  }

  private void describeWhere(Where where) {
    if (where.getWhere() != null) {
      describeWheres(where);
    }
    else {
      if (!where.getIri().equals(IM.NAMESPACE + "concept")) {
        where.setDescription(getTermInContext(where,Context.PROPERTY));
      }
      if (where.getRange() != null) {
        describeRangeWhere(where);
      }
      if (where.getValue() != null||where.getOperator()!=null) {
        describeValueWhere(where);
      }
      if (where.getIs() != null) {
        describeIsWhere(where);
      }
      if (where.getIsNull()) {
        where.setValueLabel("is not recorded");
      }
      if (where.getIsNotNull()) {
        where.setValueLabel("is not recorded");
      }
      if (where.getMatch() != null) {
        describeMatch(where.getMatch());
      }
    }

  }

  private void describeValue(Assignable assignable,Operator operator,boolean date,String value,String units,boolean relativeTo,boolean isRange) {
    String qualifier = null;
    boolean inclusive=false;
    boolean past = false;
    if (value != null)
      if (value.startsWith("-"))
        past = true;
    String relativity = null;
    switch (operator) {
      case gt:
        if (date) {
          if (!isRange)
            if (value != null) {
              qualifier = "within ";
              if (past && relativeTo)
                relativity = " before";
              if (!past && relativeTo)
                relativity = " of";
            }
            else
              qualifier="after";
        }
        else {
          if (!isRange)
            qualifier = "greater than ";
        }
        break;
      case gte:
        inclusive=true;
        if (date) {
          if (!isRange) {
            qualifier = "on or after";
          }
          if (past && relativeTo)
            relativity = " before";
        }
        else {
          if (!isRange) {
            qualifier = "equal to or more than ";
          }
        }
        break;
      case lt:
        if (date) {
          if (!isRange) {
            qualifier = "before ";
          }
          if (past && relativeTo)
            relativity = " before";
        } else {
          if (!isRange) {
            qualifier = "under ";
          }
        }
        break;
      case lte:
        inclusive=true;
        if (date) {
          if (!isRange) {
            qualifier = "on or before ";
          }
          if (past && relativeTo)
            relativity = " before";
        } else {
          if (!isRange) {
            qualifier = "equal to or less than ";
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
        if (date)
          if (!isRange) {
            qualifier = "on";
          }
        break;
    }
    if (qualifier!=null) {
      assignable.setQualifier(qualifier);
    }
    if (value != null) {
      assignable.setValueLabel(value.replace("-", ""));
      if (units != null) {
        assignable.setValueLabel(assignable.getValueLabel() + " " + getTermInContext(units, Context.PLURAL, Context.LOWERCASE));
      }
    }
    if (inclusive  && qualifier==null) {
      if (assignable.getValueLabel() != null) {
        assignable.setValueLabel(assignable.getValueLabel() + " (inc.)");
      }
    }
    if (relativity!=null)
      assignable.setValueLabel(assignable.getValueLabel()+ relativity);
  }




  private void describeValueWhere(Where where) {
    boolean date= false;
    if (where.getIri()!=null)
      date= where.getIri().toLowerCase().contains("date");
    Operator operator= where.getOperator();
    describeValue(where,operator,date,where.getValue(),where.getUnit(),where.getRelativeTo()!=null,false);
    describeRelativeTo(where);
  }



  private void describeRangeWhere(Where where) {
    boolean date= false;
    if (where.getIri()!=null) {
      date = where.getIri().toLowerCase().contains("date");
    }
    Range range = where.getRange();
    Assignable from = range.getFrom();
    describeValue(from,from.getOperator(),date,from.getValue(),from.getUnit(),where.getRelativeTo()!=null,true);
    where.setQualifier("between "+(from.getQualifier()!=null ?from.getQualifier():""));
    where.setValueLabel(from.getValueLabel());
    Assignable to = range.getTo();
    describeValue(to,to.getOperator(),date,to.getValue(),to.getUnit(),where.getRelativeTo()!=null,true);
    if (to.getValue()!=null){
      if (from.getValueLabel()!=null) {
        where.setValueLabel(where.getValueLabel() + " and " + (to.getQualifier()!=null? to.getQualifier()+" ": "")+
          to.getValueLabel());
      }
    }
    describeRelativeTo(where);
  }


  private void describeRelativeTo(Where where) {
    PropertyRef relativeTo= where.getRelativeTo();
    if (relativeTo != null) {
      String relation=null;
      if (relativeTo.getIri()!=null) {
        String propertyName = getTermInContext(relativeTo);
        relation = propertyName + " of ";
      }
      if (relativeTo.getParameter() != null) {
        if (relativeTo.getParameter().toLowerCase().contains("referencedate")) {
          relation = (relation != null ? relation : "") + "the reference date";
        }
        else
          relation = (relation!=null ?relation : "") + relativeTo.getParameter();
      }
      if (relation!=null)
        relativeTo.setQualifier(relation);
    }
  }

  private void describeIsWhere(Where where) {
    if (where.getValueLabel() == null) {
      for (Node set : where.getIs()) {
        String type="";
        if (set.isExclude())
          type="exclude";
        if (set.isMemberOf()) {
          if (set.isExclude()) type= type+" not ";
          else type="";
        }
        String value=getTermInContext(set);
        set.setQualifier(type);
        set.setName(value);

      }
      StringBuilder valueLabel= new StringBuilder();
      for (int i=0; i<where.getIs().size(); i++){
        if (i>1){
          valueLabel.append(" ...+ more");
          break;
        }
        else {
          if (i > 0)
            valueLabel.append(", ");
        }
        Node set= where.getIs().get(i);
        valueLabel.append(set.getQualifier() != null ? set.getQualifier() + " " : "").append(set.getName());
      }
      where.setValueLabel(valueLabel.toString());
    }
  }



}
