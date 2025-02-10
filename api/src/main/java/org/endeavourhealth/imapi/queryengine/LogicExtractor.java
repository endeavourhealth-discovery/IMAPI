package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Where;

public class LogicExtractor {

  public Query extractLogicalQuery(Query query) {
    Query logicalQuery = new Query();
    setLogicalMatch(query,logicalQuery);
    return logicalQuery;
  }

  public Match extractLogicalMatch(Match match) {
    Match logicalMatch = new Match();
    setLogicalMatch(match,logicalMatch);
    return logicalMatch;
  }

  private void setLogicalMatch(Match match, Match logicalMatch) {
    if (match.getPath() != null) {
      logicalMatch.setPath(match.getPath());
    }
    if (match.getOrderBy() != null) {
      logicalMatch.setOrderBy(match.getOrderBy());
    }
    if (match.getInstanceOf() != null) {
      logicalMatch.setInstanceOf(match.getInstanceOf());
    }
    if (match.getThen() != null) {
      Match logicalThen= new Match();
      logicalMatch.setThen(logicalThen);
      setLogicalMatch(match.getThen(),logicalThen);
    }
    if (match.getVariable() != null) {
      logicalMatch.setVariable(match.getVariable());
    }

    if (match.getTypeOf() != null) {
      logicalMatch.setTypeOf(new Node().setIri(match.getTypeOf().getIri()));
    }
    if (match.getBoolMatch()!=null) {
      logicalMatch.setBoolMatch(match.getBoolMatch());
    }
    if (match.getMatch() != null) {
      for (Match subMatch:match.getMatch()){
        Match logicalSubMatch= new Match();
        logicalMatch.addMatch(logicalSubMatch);
        setLogicalMatch(subMatch,logicalSubMatch);
      }
    }
    if (match.getBoolWhere()!=null){
      logicalMatch.setBoolWhere(match.getBoolWhere());
    }
    if (match.getWhere() != null) {
      for (Where where:match.getWhere()){
        Where logicalWhere= new Where();
        logicalMatch.addWhere(logicalWhere);
        setLogicalWhere(where,logicalWhere);

      }
    }
  }

  private void setLogicalWhere(Where where, Where logicalWhere) {
    if (where.getIri()!=null)
      logicalWhere.setIri(where.getIri());
    if (where.getIs() != null) {
      logicalWhere.setIs(where.getIs());
    }
    if (where.getParameter()!=null) {
      logicalWhere.setParameter(where.getParameter());
    }
    if (where.getRange() != null) {
        logicalWhere.setRange(where.getRange());
      }
      if (where.getValue() != null) {
        logicalWhere.setValue(where.getValue());
      }
      if (where.getOperator() != null) {
        logicalWhere.setValue(where.getValue());
      }

      if (where.getIsNull()) {
        logicalWhere.setIsNull(where.getIsNull());
      }
      if (where.getIsNotNull()) {
        logicalWhere.setIsNotNull(where.getIsNotNull());
      }
      if (where.getBoolWhere()!=null) {
        logicalWhere.setBoolWhere(where.getBoolWhere());
      }
      if (where.getWhere()!=null){
        for (Where subWhere:where.getWhere()){
          Where logicalSubWhere= new Where();
          logicalWhere.addWhere(logicalSubWhere);
          setLogicalWhere(subWhere,logicalWhere);
        }
      if (where.getMatch() != null) {
        Match logicalMatch= new Match();
        logicalWhere.setMatch(logicalMatch);
        setLogicalMatch(where.getMatch(),logicalMatch);
      }
    }
  }
}
