package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class IriCollector {

  public static Set<String> collectIris(Query query){
    Set<String> iris = new HashSet<>();
    collectQueryIris(query, iris);
    return iris;
  }
  public static Set<String> collectIris(Match match){
    Set<String> iris = new HashSet<>();
    collectMatchIris(match, iris);
    return iris;
  }

  private static void collectQueryIris(Query query, Set<String> iris){
    if (query.getColumnGroup() != null) {
      for (Match subQuery : query.getColumnGroup()) {
        collectMatchIris(subQuery, iris);
      }
    }
    collectMatchIris(query, iris);
  }


  private static void collectReturnIris(Return prop, Set<String> iriSet) {
    if (prop.getIri() != null) iriSet.add(prop.getIri());
    if (prop.getUnits()!=null)
      iriSet.add(prop.getUnits().getIri());
    if (prop.getCase()!=null){
      if (prop.getCase().getWhen()!=null)
        for (When when:prop.getCase().getWhen()){
          if (when.getWhere()!=null)
            collectWhereIris(when.getWhere(),iriSet);
        }
    }
    if (prop.getFunction()!=null){
      collectFunctionIris(prop.getFunction(),iriSet);
    }
  }

  private static void collectPathIris(Path path, Set<String> iriSet) {
    if (path.getIri() != null)
      iriSet.add(path.getIri());
    if (path.getTypeOf()!=null){
      iriSet.add(path.getTypeOf().getIri());
    }
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        collectPathIris(subPath, iriSet);
      }
    }
  }


  private static void collectMatchIris(Match match, Set<String> iriSet) {

    if (match.getIri() != null) {
      iriSet.add(match.getIri());
    }
    if (match.getTypeOf() != null) {
      iriSet.add(match.getTypeOf().getIri());
    }
    if (match.getPath() != null) {
      for (Path path : match.getPath()) {
        collectPathIris(path, iriSet);
      }
    }
    if (match.getIs() != null) {
      Node node = match.getIs();
      if (node.getMatch()!=null)
        collectMatchIris(node.getMatch(),iriSet);
      else if (node.getIri()!=null)
        iriSet.add(node.getIri());
    }
    if (match.getRule() != null) {
      for (Match subMatch : match.getRule()) {
        collectMatchIris(subMatch, iriSet);
      }
    }
    if (match.getOr() != null) {
      for (Match subMatch : match.getOr()) {
        collectMatchIris(subMatch, iriSet);
      }
    }
    if (match.getAnd() != null) {
      for (Match subMatch : match.getAnd()) {
        collectMatchIris(subMatch, iriSet);
      }
    }


    if (match.getWhere() != null) {
      collectWhereIris(match.getWhere(), iriSet);
    }

    if (match.getThen() != null) {
      collectWhereIris(match.getThen(), iriSet);
    }
    if (match.getReturn() != null) {
      for (Return prop : match.getReturn()) {
        collectReturnIris(prop, iriSet);
      }
    }
    if (match.getOrderBy()!=null){
      collectOrderByIris(match.getOrderBy(),iriSet);
    }
  }

  private static void collectOrderByIris(OrderLimit orderBy, Set<String> iriSet) {
    if (orderBy.getProperty()!=null){
      for (OrderDirection property : orderBy.getProperty()) {
        iriSet.add(property.getIri());
      }
    }
  }


  private static void collectWhereIris(Where where, Set<String> iriSet) {
    if (where.getIri() != null) {
      iriSet.add(where.getIri());
    }
    if (where.getQualifier() != null) {
      iriSet.add(where.getQualifier().getIri());
    }

    if (where.getAnd() != null) {
      for (Where subWhere : where.getAnd()) {
        collectWhereIris(subWhere, iriSet);
      }
    }
    if (where.getOr() != null) {
      for (Where subWhere : where.getOr()) {
        collectWhereIris(subWhere, iriSet);
      }
    }
    if (where.getIs() != null) {
      for (Node node : where.getIs())
        if (node.getIri() != null)
          iriSet.add(node.getIri());
    }
    collectAssignableIris(where, iriSet);

    if (where.getRange() != null) {
      if (where.getRange().getFrom() != null) {
        collectAssignableIris(where.getRange().getFrom(), iriSet);
      }
      if (where.getRange().getTo() != null) {
        collectAssignableIris(where.getRange().getTo(), iriSet);
      }
    }
    if (where.getQualifier() != null) {
      iriSet.add(where.getQualifier().getIri());
    }
  }

  private static void collectFunctionIris(FunctionClause function,Set<String> iriSet) {
    if (function.getIri() != null) {
      iriSet.add(function.getIri());
    }
    if (function.getArgument() != null) {
      for (Argument argument : function.getArgument()) {
        if (argument.getValuePath()!=null){
          collectPathIris(argument.getValuePath(),iriSet);
        }
        if (argument.getValueIri() != null) iriSet.add(argument.getValueIri().getIri());
        if (argument.getValueIriList() != null) {
          for (TTIriRef valueIri : argument.getValueIriList()) iriSet.add(valueIri.getIri());
        }
      }
    }
  }

  private static void collectAssignableIris(Assignable assignable, Set<String> iriSet) {

    if (assignable.getCompare() != null) {
      collectCompareIris(assignable.getCompare(), iriSet);
    }

  }

  private static void collectCompareIris(Compare compare, Set<String> iriSet) {
    collectValueSourceIris(compare.getLeft(),iriSet);
    collectValueSourceIris(compare.getRight(),iriSet);
    if (compare.getUnits()!=null)
      iriSet.add(compare.getUnits().getIri());
  }

  private static void collectValueSourceIris(ValueSource source,Set<String> iriSet) {
    if (source.getIri()!=null){
      iriSet.add(source.getIri());
    }

  }

}
