package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class LogicOptimizer {
  Set<String> commonMatches;

  public void resolveLogic(Match match, DisplayMode displayMode) throws JsonProcessingException {
    if (displayMode== DisplayMode.LOGICAL) {
      getLogicFromRules(match);
        flattenMatch(match);
        optimiseMatch(match);
    }
    else {
      optimiseMatch(match);
    }
  }

  private void getLogicFromRules(Match match) {
    if (match.getRule()==null) return;
    Match topOr=null;
    for (Match subMatch: match.getRule()) {
      RuleAction ifTrue= subMatch.getIfTrue();
      RuleAction ifFalse= subMatch.getIfFalse();
      if (ifTrue == ifFalse) {
        throw new IllegalArgumentException("ifTrue and ifFalse cannot be the same");
      }
      switch (ifTrue + "_" + ifFalse) {
        case "SELECT_REJECT":
          if (topOr!=null) {
            topOr.addOr(subMatch);
            topOr=null;
          }
          else match.addAnd(subMatch);
          break;
        case "SELECT_NEXT":
          if (topOr!=null) topOr.addOr(subMatch);
          else {
            topOr= new Match();
            match.addAnd(topOr);;
            topOr.addOr(subMatch);
          }
          break;
        case "REJECT_SELECT":
         match.addNot(subMatch);
          break;
        case "REJECT_NEXT":
          match.addNot(subMatch);
          break;
        case "NEXT_SELECT":
          if (topOr!=null) {
            topOr.addNot(subMatch);
            topOr=null;
          }
          else {
            topOr= new Match();
            match.addAnd(topOr);
            topOr.addNot(subMatch);
          }
          break;
        case "NEXT_REJECT":
          match.addAnd(subMatch);
          break;
      }
    }
    match.setRule(null);
  }

  public void optimiseMatch(Match match) throws JsonProcessingException {
    optimizeAndMatches(match);
    optimizeOrMatches(match);
  }
  private void optimizeAndMatches(Match match) throws JsonProcessingException {
    commonMatches = new HashSet<>();

    if (match.getAnd() == null) return;
    if (match.getWhere() == null && match.getAnd().size() > 1) {
      List<Match> originalAnds = match.getAnd();
      List<Match> optimalAnds = new ArrayList<>();
      getCommonAnds(originalAnds,commonMatches,optimalAnds);
      if (commonMatches.isEmpty()) return;
      for (Match andMatch:originalAnds) {
        if (andMatch.getAnd()!=null) {
          for (Match subMatch : andMatch.getAnd()) {
            String content = LogicComparer.serializeMatchLogic(subMatch);
            if (!commonMatches.contains(content)) {
              optimalAnds.add(subMatch);
            }
          }
        }
      }
      match.setAnd(optimalAnds);
    }
  }

  private void optimizeOrMatches(Match match) throws JsonProcessingException {
    commonMatches = new HashSet<>();
    if (match.getOr() == null) return;
    if (match.getWhere() == null && match.getOr().size() > 1) {
      List<Match> originalOrs = match.getOr();
      List<Match> optimisedAnds = new ArrayList<>();
      List<Match> optimisedOrs = new ArrayList<>();
      getCommonAnds(originalOrs,commonMatches,optimisedAnds);
      if (commonMatches.isEmpty()) return;
      for (Match orMatch:originalOrs) {
        if (orMatch.getAnd()!=null) {
          Match newOr = new Match();
          optimisedOrs.add(newOr);
          for (Match andMatch : orMatch.getAnd()) {
            String content = LogicComparer.serializeMatchLogic(andMatch);
            if (!commonMatches.contains(content)) {
              newOr.addAnd(andMatch);
            }
          }
        }
      }
      if (!optimisedAnds.isEmpty()) {
        match.setAnd(optimisedAnds);
        if (!optimisedOrs.isEmpty()) {
          Match topOr = new Match();
          match.addAnd(topOr);
          topOr.setOr(optimisedOrs);
        }
      }
    }

  }



  private void getCommonAnds(List<Match> matches, Set<String> commonMatches, List<Match> ands) throws JsonProcessingException {
    Match first=matches.getFirst();
    if (first.getAnd()!=null) {
      for (int q = 0; q < first.getAnd().size(); q++) {
        Match candidate = first.getAnd().get(q);
        String content = LogicComparer.serializeMatchLogic(candidate);
        if (!commonMatches.contains(content)) {
          if (isCommon(matches, content, 1)) {
            ands.add(candidate);
            commonMatches.add(content);
          }
        }
      }

    }
  }




  private boolean isCommon(List<Match> matches, String content,int index) throws JsonProcessingException {
    if (index>matches.size()-1) return true;
    Match next = matches.get(index);
    if (next.getAnd()!=null) {
        for (int q = 0; q < next.getAnd().size(); q++) {
          Match candidate = next.getAnd().get(q);
          String testContent= LogicComparer.serializeMatchLogic(candidate);
          if (testContent.equals(content)) {
            if (index<matches.size()-1)
              return isCommon(matches,content,index+1);
            else return true;
          }
        }
    }
    return false;
  }


  public static  void flattenMatch(Match match){
   if (match.getAnd()!=null) {
     List<Match> flatMatches= new ArrayList<>();
     for (Match child : match.getAnd()) {
       if (child.getAnd() != null && child.getOr() == null && child.getNot() == null) {
         flatMatches.addAll(child.getAnd());
       } else flatMatches.add(child);
       flattenMatch(child);
     }
     match.setAnd(flatMatches);
   }
    if (match.getOr()!=null) {
      List<Match> flatMatches= new ArrayList<>();
      for (Match child : match.getOr()) {
        if (child.getOr() != null && child.getAnd() == null && child.getNot() == null) {
          flatMatches.addAll(child.getOr());
        } else flatMatches.add(child);
        flattenMatch(child);
        }
      match.setOr(flatMatches);
      }
    if (match.getWhere()!=null) {
      flattenWhere(match.getWhere());
    }
  }

  private static void flattenWhere(Where where) {
    if (where.getAnd()!=null) {
      List<Where> flatWheres= new ArrayList<>();
      for (Where child : where.getAnd()) {
        if (child.getAnd() != null && child.getOr() == null && child.getNot() == null) {
          flatWheres.addAll(child.getAnd());
        } else flatWheres.add(child);
        flattenWhere(child);
      }
      where.setAnd(flatWheres);
    }
    if (where.getOr()!=null) {
      List<Where> flatWheres= new ArrayList<>();
      for (Where child : where.getOr()) {
        if (child.getOr() != null && child.getAnd() == null && child.getNot() == null) {
          flatWheres.addAll(child.getOr());
        } else flatWheres.add(child);
        flattenWhere(child);
      }
      where.setOr(flatWheres);
    }
  }


  public void createRules(Query query) {
    if (query.getAnd() == null&&query.getOr()==null&&query.getNot()==null) return;
    if (query.getAnd()!=null) {
      for (Match match : query.getAnd()) {
        query.addRule(match);
        match.setIfTrue(RuleAction.NEXT);
        match.setIfFalse(RuleAction.REJECT);
      }
      if (query.getOr()==null&& query.getNot()==null){
        Match lastRule= query.getRule().get(query.getRule().size()-1);
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.REJECT);
        return;
      }
    }
    if (query.getOr()!=null){
      Match orRule= new Match();
      query.addRule(orRule);
      orRule.setIfTrue(RuleAction.NEXT);
      orRule.setIfFalse(RuleAction.REJECT);
      for (Match match : query.getOr()) {
        orRule.addOr(match);
      }
      Match lastRule= orRule.getRule().get(orRule.getRule().size()-1);
      if (query.getNot()==null){
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.REJECT);
        return;
      }
      else {
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.NEXT);
      }
    }
    if (query.getNot()!=null) {
      for (Match match : query.getNot()) {
        query.addRule(match);
        match.setIfTrue(RuleAction.REJECT);
        match.setIfFalse(RuleAction.NEXT);
      }
      Match lastRule= query.getRule().get(query.getRule().size()-1);
      lastRule.setIfTrue(RuleAction.REJECT);
      lastRule.setIfFalse(RuleAction.SELECT);
    }
    query.setAnd(null);
    query.setOr(null);
    query.setNot(null);
  }

}
