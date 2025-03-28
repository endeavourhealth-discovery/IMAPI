package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class LogicOptimizer {
  Set<String> commonMatches;

  public void resolveLogic(Match match, DisplayMode displayMode) throws JsonProcessingException {
    if (displayMode== DisplayMode.LOGICAL) {
      if (match.hasRules()){
        getLogicFromRules(match);
        match.setHasRules(false);
        flattenMatch(match);
        optimiseMatch(match);
      }
    }
    else {
      optimiseMatch(match);
    }
  }

  private void getLogicFromRules(Match match) {
    if (!match.hasRules()) return;
    List<Match> newMatches= new ArrayList<>();
    Match topOr=null;
    for (Match subMatch: match.getMatch()) {
      RuleAction ifTrue= subMatch.getIfTrue();
      RuleAction ifFalse= subMatch.getIfFalse();
      subMatch.setIsRule(false);
      if (ifTrue == ifFalse) {
        throw new IllegalArgumentException("ifTrue and ifFalse cannot be the same");
      }
      switch (ifTrue + "_" + ifFalse) {
        case "SELECT_REJECT":
          if (topOr!=null) {
            topOr.addMatch(subMatch);
            topOr=null;
          }
          else newMatches.add(subMatch);
          break;
        case "SELECT_NEXT":
          if (topOr!=null) topOr.addMatch(subMatch);
          else {
            topOr= new Match();
            topOr.setBool(Bool.or);
            newMatches.add(topOr);
            topOr.addMatch(subMatch);
          }
          break;
        case "REJECT_SELECT":
          subMatch.setExclude(true);
            if (topOr!=null) {
              topOr.addMatch(subMatch);
              topOr=null;
            }
            else{
              newMatches.add(subMatch);
            }
          break;
        case "REJECT_NEXT":
          subMatch.setExclude(true);
          if (topOr!=null) {
            topOr.addMatch(subMatch);
            topOr=null;
          }
          else {
            topOr= new Match();
            topOr.setBool(Bool.or);
            newMatches.add(topOr);
            topOr.addMatch(subMatch);
          }
          break;
        case "NEXT_SELECT":
          subMatch.setExclude(true);
          if (topOr!=null) topOr.addMatch(subMatch);
          else {
            topOr= new Match();
            topOr.setBool(Bool.or);
            newMatches.add(topOr);
            topOr.addMatch(subMatch);
          }
          break;
        case "NEXT_REJECT":
          newMatches.add(subMatch);
          break;
      }
    }
    if (newMatches.size()==1){
      match.setBool(newMatches.get(0).getBool());
      match.setMatch(newMatches.get(0).getMatch());
      if (match.getBool()==null&&match.getMatch().size()>1 ) match.setBool(Bool.and);
      }
    else match.setBool(Bool.and);
  }

  private void optimiseMatch(Match match) throws JsonProcessingException {
    optimizeAndMatches(match);
    optimizeOrMatches(match);
  }
  private void optimizeAndMatches(Match match) throws JsonProcessingException {
    commonMatches = new HashSet<>();
    if (match.getMatch() == null) return;
    if (match.getBool() == Bool.and && match.getWhere() == null && match.getMatch().size() > 1) {
      List<Match> matches = match.getMatch();
      List<Match> ands = new ArrayList<>();
      getCommonAnds(matches,commonMatches,ands);
      if (commonMatches.isEmpty()) return;
      for (Match andMatch:matches) {
        for (Match subMatch : andMatch.getMatch()) {
          String content = LogicComparer.serializeMatchLogic(subMatch);
          if (!commonMatches.contains(content)) {
            ands.add(subMatch);
          }
        }
      }
      match.setMatch(ands);
    }
    else {
      if (match.getMatch() != null) {
        for (Match child : match.getMatch()) {
          optimiseMatch(child);
        }
      }
    }
  }

  private void optimizeOrMatches(Match match) throws JsonProcessingException {
    commonMatches = new HashSet<>();
    if (match.getMatch() == null) return;
    if (match.getBool() == Bool.or && match.getWhere() == null && match.getMatch().size() > 1) {
      List<Match> matches = match.getMatch();
      List<Match> ands = new ArrayList<>();
      List<Match> ors = new ArrayList<>();
      getCommonAnds(matches,commonMatches,ands);
      if (commonMatches.isEmpty()) return;
      for (Match orMatch:matches) {
        if (orMatch.getBool() == Bool.and) {
          Match newOr = new Match();
          newOr.setBool(Bool.and);
          ors.add(newOr);
          for (Match andMatch : orMatch.getMatch()) {
            String content = LogicComparer.serializeMatchLogic(andMatch);
            if (!commonMatches.contains(content)) {
              newOr.addMatch(andMatch);
            }
          }
        }
      }
      if (!ands.isEmpty()) {
        match.setBool(Bool.and);
        match.setMatch(ands);
        if (!ors.isEmpty()) {
          Match topOr = new Match();
          match.addMatch(topOr);
          topOr.setBool(Bool.or);
          topOr.setMatch(ors);
        }
      }
    }
    else {
      if (match.getMatch() != null) {
        for (Match child : match.getMatch()) {
          optimiseMatch(child);
        }
      }
    }
  }



  private void getCommonAnds(List<Match> matches, Set<String> commonMatches, List<Match> ands) throws JsonProcessingException {
    Match first=matches.get(0);
    if (first.getBool() == Bool.and&&first.getMatch()!=null) {
      for (int q = 0; q < first.getMatch().size(); q++) {
        Match candidate = first.getMatch().get(q);
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
    if (next.getBool() == Bool.and&&next.getMatch()!=null) {
        for (int q = 0; q < next.getMatch().size(); q++) {
          Match candidate = next.getMatch().get(q);
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
    if (match.isUnion()) return;
    if (match.getBool() == Bool.or) {
      flattenOrs(match);
    }

    else if (match.hasRules()){
      for (Match child:match.getMatch()){
        flattenMatch(child);
      }
    }

    else if (match.getMatch()!=null){
      List<Match> flatMatches = new ArrayList<>();
      flattenAnds(match.getMatch(),flatMatches);
      match.setMatch(flatMatches);
    }


  }

    private static void flattenAnds(List<Match> topMatches, List<Match> flatMatches){
      for (Match topMatch : topMatches) {
        //Top level match, no nested match
        if (topMatch.getMatch() == null) {
          flatMatches.add(topMatch);
        }
        else if (topMatch.isUnion()){
          flatMatches.add(topMatch);
        }
        else if (topMatch.getBool() != Bool.or) {
          flattenAnds(topMatch.getMatch(),flatMatches);
        }
        else {
          flatMatches.add(topMatch);
          flattenOrs(topMatch);
        }
      }

    }

    private static void flattenOrs(Match topMatch) {
      List<Match> newOrs = new ArrayList<>();
      for (Match match : topMatch.getMatch()) {
        if (match.getMatch() != null) {
          if (match.getBool().equals(Bool.and)) {
            List<Match> subFlatMatches = new ArrayList<>();
            flattenAnds(match.getMatch(), subFlatMatches);
            if (subFlatMatches.size() == 1) {
              newOrs.add(subFlatMatches.get(0));
            } else {
              match.setMatch(subFlatMatches);
              newOrs.add(match);
            }
          }
        } else newOrs.add(match);

      }
      topMatch.setMatch(newOrs);
    }

  public void createRules(Query query) {
    List<Match> newMatches = new ArrayList<>();
    if (query.getMatch() == null) return;
    if (query.getBool()==Bool.or){
      addOrsAsRules(newMatches, query);
    }
    else {
      for (Match match : query.getMatch()) {
        match.setIsRule(true);
        if (!match.isExclude()) {
          match.setIfTrue(RuleAction.NEXT);
          match.setIfFalse(RuleAction.REJECT);
        } else {
          match.setIfTrue(RuleAction.REJECT);
          match.setIfFalse(RuleAction.NEXT);
          match.setExclude(false);
        }
      }
      Match lastMatch = query.getMatch().get(query.getMatch().size() - 1);
      if (!lastMatch.isExclude()) {
        lastMatch.setIfTrue(RuleAction.SELECT);
        lastMatch.setIfFalse(RuleAction.REJECT);
      } else {
        lastMatch.setIfTrue(RuleAction.REJECT);
        lastMatch.setIfFalse(RuleAction.SELECT);
        lastMatch.setExclude(false);
      }
    }

    query.setHasRules(true);
  }

  private void addOrsAsRules(List<Match> newMatches, Match match) {
    for (Match subMatch : match.getMatch()) {
      subMatch.setIsRule(true);
      subMatch.setIfTrue(RuleAction.SELECT);
      subMatch.setIfFalse(RuleAction.NEXT);
      newMatches.add(subMatch);
    }
    Match lastMatch = match.getMatch().get(match.getMatch().size() - 1);
    lastMatch.setIfTrue(RuleAction.SELECT);
    lastMatch.setIfFalse(RuleAction.REJECT);
  }
}
