package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.imq.*;

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
    if (query.getDataSet() != null) {
      for (Query subQuery : query.getDataSet()) {
        collectQueryIris(subQuery, iris);
      }
    }
    collectMatchIris(query, iris);
  }
  private static void collectReturnIris(Return ret, Set<String> iriSet) {
    if (ret.getProperty() != null) {
      for (ReturnProperty prop : ret.getProperty()) {
        if (prop.getIri() != null) iriSet.add(prop.getIri());
        if (prop.getReturn() != null) {
          collectReturnIris(prop.getReturn(), iriSet);
        }
      }
    }
  }

  private static void collectPathIris(Path path, Set<String> iriSet) {
    if (path.getIri() != null)
      iriSet.add(path.getIri());
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
    if (match.getInstanceOf() != null) {
      match.getInstanceOf().forEach(i -> iriSet.add(i.getIri()));
    }
    if (match.getThen() != null) {
      collectMatchIris(match.getThen(), iriSet);
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
    if (match.getNot() != null) {
      for (Match subMatch : match.getNot()) {
        collectMatchIris(subMatch, iriSet);
      }
    }

    if (match.getWhere() != null) {
      collectWhereIris(match.getWhere(), iriSet);
    }
    if (match.getReturn() != null) {
       collectReturnIris(match.getReturn(), iriSet);
       if (match.getReturn().getOrderBy()!=null){
         collectOrderByIris(match.getReturn().getOrderBy(),iriSet);
       }
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
    if (where.getValue() != null) {
      collectAssignableIris(where, iriSet);
    }
  }

  private static void collectAssignableIris(Assignable assignable, Set<String> iriSet) {
    if (assignable.getUnit() != null) {
      iriSet.add(assignable.getUnit().getIri());
    }
  }
}
