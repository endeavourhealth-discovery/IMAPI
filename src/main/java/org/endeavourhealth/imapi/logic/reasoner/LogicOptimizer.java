package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.util.*;

public class LogicOptimizer {
  Set<String> commonMatches;
  final ObjectMapper mapper = new ObjectMapper();




  public static void optimizeQuery(Query query) {
    flattenMatch(query);
    cleanBooleans(query);
  }

  public static void optimiseECLQuery(Query query) {
    optimizeQuery(query);
    optimiseECL(query);
  }

  public static void optimiseECL(Query query) {
    mergeNested(query);
  }


  public void deduplicateQuery(Query query, Namespace namespace) throws JsonProcessingException {
    if (query.getRule() != null) {
      for (Match rule : query.getRule()) {
        deduplicateRule(rule, namespace);
      }
    }
  }

  private void deduplicateRule(Match rule, Namespace namespace) throws JsonProcessingException {
    Map<String, String> criteriaNodeRef = new HashMap<>();
    for (List<Match> matches : Arrays.asList(rule.getAnd(), rule.getOr(), rule.getNot())) {
      if (matches != null) {
        for (int i = 0; i < matches.size(); i++) {
          Match match = matches.get(i);
          Match logicalMatch = getLogicalMatch(match);
          String libraryIri = namespace + "Clause_" + (mapper.writeValueAsString(logicalMatch).hashCode());
          if (criteriaNodeRef.containsKey(libraryIri)) {
            if (matches.size() > i + 1) {
              Match linkedMatch = matches.get(i + 1);
              if (isLinkedMatch(linkedMatch) && linkedMatch.getWhere() != null) {
                Where where = linkedMatch.getWhere();
                if (where.getRelativeTo() != null && where.getRelativeTo().getNodeRef() != null) {
                  where.getRelativeTo().setNodeRef(criteriaNodeRef.get(libraryIri));
                }
                if (where.getAnd() != null) {
                  for (Where andWhere : where.getAnd()) {
                    if (andWhere.getRelativeTo() != null && andWhere.getRelativeTo().getNodeRef() != null) {
                      andWhere.getRelativeTo().setNodeRef(criteriaNodeRef.get(libraryIri));
                    }
                  }
                }
              }
            }
            matches.remove(i);
            i--;
          } else if (match.getReturn() != null && match.getReturn().getAs() != null) {
            criteriaNodeRef.put(libraryIri, match.getReturn().getAs());
          }
        }
      }
    }
  }


  public Match getLogicalMatch(Match match) throws JsonProcessingException {
    String matchJson = mapper.writeValueAsString(match);
    Match logicalMatch = mapper.readValue(matchJson, Match.class);
    if (logicalMatch.getReturn() != null) {
      logicalMatch.getReturn().setAs(null);
    }
    if (logicalMatch.getPath() != null) {
      for (Path path : logicalMatch.getPath()) {
        logicalPath(path);
      }
    }
    if (logicalMatch.getWhere() != null) {
      logicalWhere(logicalMatch.getWhere());
    }
    for (List<Match> matches : Arrays.asList(logicalMatch.getAnd(), logicalMatch.getOr(), logicalMatch.getNot())) {
      if (matches != null) {
        for (int i = 0; i < matches.size(); i++) {
          Match subMatch = matches.get(i);
          Match logicalSubMatch = getLogicalMatch(subMatch);
          matches.set(i, logicalSubMatch);
        }
      }
    }

    return logicalMatch;
  }

  private void logicalWhere(Where where) {
    where.setNodeRef(null);
    for (List<Where> wheres : Arrays.asList(where.getAnd(), where.getOr())) {
      if (wheres != null) {
        for (Where subWhere : wheres) {
          logicalWhere(subWhere);
        }
      }
    }
  }

  private static void logicalPath(Path path) {
    if (path == null) return;
    path.setVariable(null);
    if (path.getPath() != null)
      for (Path subPath : path.getPath()) {
        logicalPath(subPath);
      }
  }

  private static void mergeNested(Match match) {
    if (match.getOr() != null && match.getOr().size() == 1) {
      Match orMatch = match.getOr().getFirst();
      mergeMatch(match, orMatch);
    }
    if (match.getAnd() != null && match.getAnd().size() == 1) {
      Match andMatch = match.getAnd().getFirst();
      mergeMatch(match, andMatch);
    }
  }

  public static boolean isLinkedMatch(Match match) {
    if (match.getWhere() != null) {
      Where where = match.getWhere();
      if (where.getRelativeTo() != null && where.getRelativeTo().getNodeRef() != null) return true;
      if (where.getAnd() != null) {
        for (Where andWhere : where.getAnd()) {
          if (andWhere.getRelativeTo() != null && andWhere.getRelativeTo().getNodeRef() != null) return true;
        }
      }
    }
    return false;
  }

  private static void mergeMatch(Match match, Match nestedMatch) {
    match.setInstanceOf(nestedMatch.getInstanceOf());
    match.setOr(nestedMatch.getOr());
    match.setAnd(nestedMatch.getAnd());
    if (nestedMatch.getNot() != null) {
      if (match.getNot() == null) match.setNot(nestedMatch.getNot());
      else match.getNot().addAll(nestedMatch.getNot());
    }
    if (nestedMatch.getWhere() != null) {
      if (match.getWhere() == null) match.setWhere(nestedMatch.getWhere());
      else {
        Where newAndWhere = new Where();
        newAndWhere.addAnd(match.getWhere());
        newAndWhere.addAnd(nestedMatch.getWhere());
      }
    }
  }


  private static <T extends BoolGroup<T>> void cleanBoolGroup(T group, T parent, Bool parentOp, Integer parentIndex) {
    clean(group, parent, parentOp, parentIndex, Bool.and);
    clean(group, parent, parentOp, parentIndex, Bool.or);
  }

  private static <T extends BoolGroup<T>> void clean(T group, T parent, Bool parentOp, Integer parentIndex, Bool op) {
    List<T> list = (op == Bool.and) ? group.getAnd() : group.getOr();
    if (list == null) return;
    for (int i = 0; i < list.size(); i++) {
      cleanBoolGroup(list.get(i), group, op, i);
    }
    if (list.isEmpty()) {
      if (op == Bool.and) group.setAnd(null);
      else group.setOr(null);
    } else if (list.size() == 1 && parent != null) {
      T only = list.getFirst();
      if (parentOp == Bool.and) parent.getAnd().set(parentIndex, only);
      else if (parentOp == Bool.or) parent.getOr().set(parentIndex, only);
    }
  }


  private static void cleanBooleans(Match match) {
    cleanBoolGroup(match, null, null, null);
    if (match.getWhere() != null) {
      cleanBooleans(match.getWhere());
    }
  }

  private static void cleanBooleans(Where where) {
    cleanBoolGroup(where, null, null, null);
  }

  public void resolveLogic(Match match, DisplayMode displayMode) throws JsonProcessingException {
    if (displayMode == DisplayMode.LOGICAL) {
      getLogicFromRules(match);
      optimiseMatch(match);
    } else {
      optimiseMatch(match);
    }
  }

  private void getLogicFromRules(Match match) {
    if (match.getRule() == null) return;
    Match topOr = null;
    for (Match subMatch : match.getRule()) {
      RuleAction ifTrue = subMatch.getIfTrue();
      RuleAction ifFalse = subMatch.getIfFalse();
      if (ifTrue == ifFalse) {
        throw new IllegalArgumentException("ifTrue and ifFalse cannot be the same");
      }
      switch (ifTrue + "_" + ifFalse) {
        case "SELECT_REJECT":
          if (topOr != null) {
            topOr.addOr(subMatch);
            topOr = null;
          } else match.addAnd(subMatch);
          break;
        case "SELECT_NEXT":
          if (topOr != null) topOr.addOr(subMatch);
          else {
            topOr = new Match();
            match.addAnd(topOr);
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
          if (topOr != null) {
            topOr.addNot(subMatch);
            topOr = null;
          } else {
            topOr = new Match();
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
    if (match.getWhere() == null && match.getIsCohort()==null){
      if (match.getAnd().size() > 1){
      List<Match> originalAnds = match.getAnd();
      List<Match> optimalAnds = new ArrayList<>();
      getCommonAnds(originalAnds, commonMatches, optimalAnds);
      if (commonMatches.isEmpty()) return;
      for (Match andMatch : originalAnds) {
        if (andMatch.getAnd() != null) {
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
    else if (match.getAnd().size() == 1) {
      Match and= match.getAnd().getFirst();
      if (and.getWhere()==null&&and.getReturn()==null){
        if (and.getOr()!=null){
          match.setOr(and.getOr());
          match.setAnd(null);
          match.setReturn(and.getReturn());
          match.setNot(and.getNot());
        } else if (and.getAnd()!=null){
            match.setAnd(and.getAnd());
            match.setOr(null);
            match.setReturn(and.getReturn());
          }
        }
     }
    }
  }

  private void optimizeOrMatches(Match match) throws JsonProcessingException {
    commonMatches = new HashSet<>();
    if (match.getOr() == null) return;
    if (match.getWhere() == null && match.getOr().size() > 1) {
      List<Match> originalOrs = match.getOr();
      List<Match> optimisedAnds = new ArrayList<>();
      List<Match> optimisedOrs = new ArrayList<>();
      getCommonAnds(originalOrs, commonMatches, optimisedAnds);
      if (commonMatches.isEmpty()) return;
      for (Match orMatch : originalOrs) {
        if (orMatch.getAnd() != null) {
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
    Match first = matches.getFirst();
    if (first.getAnd() != null) {
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


  private boolean isCommon(List<Match> matches, String content, int index) throws JsonProcessingException {
    if (index > matches.size() - 1) return true;
    Match next = matches.get(index);
    if (next.getAnd() != null) {
      for (int q = 0; q < next.getAnd().size(); q++) {
        Match candidate = next.getAnd().get(q);
        String testContent = LogicComparer.serializeMatchLogic(candidate);
        if (testContent.equals(content)) {
          if (index < matches.size() - 1)
            return isCommon(matches, content, index + 1);
          else return true;
        }
      }
    }
    return false;
  }


  public static void flattenMatch(Match match) {
    if (match.getAnd() != null) {
      List<Match> flatMatches = new ArrayList<>();
      for (Match child : match.getAnd()) {
        if (child.getAnd() != null && child.getOr() == null && child.getNot() == null) {
          flatMatches.addAll(child.getAnd());
        } else flatMatches.add(child);
        flattenMatch(child);
      }
      match.setAnd(flatMatches);
    }
    if (match.getOr() != null) {
      List<Match> flatMatches = new ArrayList<>();
      for (Match child : match.getOr()) {
        if (child.getOr() != null && child.getAnd() == null && child.getNot() == null) {
          flatMatches.addAll(child.getOr());
        } else flatMatches.add(child);
        flattenMatch(child);
      }
      match.setOr(flatMatches);
    }
    if (match.getWhere() != null) {
      flattenWhere(match.getWhere());
    }
  }

  private static void flattenWhere(Where where) {
    if (where.getAnd() != null) {
      List<Where> flatWheres = new ArrayList<>();
      for (Where child : where.getAnd()) {
        if (child.getAnd() != null && child.getOr() == null && child.getNot() == null) {
          flatWheres.addAll(child.getAnd());
        } else flatWheres.add(child);
        flattenWhere(child);
      }
      where.setAnd(flatWheres);
    }
    if (where.getOr() != null) {
      List<Where> flatWheres = new ArrayList<>();
      for (Where child : where.getOr()) {
        if (child.getOr() != null && child.getAnd() == null && child.getNot() == null) {
          flatWheres.addAll(child.getOr());
        } else flatWheres.add(child);
        flattenWhere(child);
      }
      where.setOr(flatWheres);
    }
  }


  public void getRulesFromLogic(Query query) {
    if (query.getAnd() == null && query.getOr() == null && query.getNot() == null) return;
    if (query.getAnd() != null) {
      for (Match match : query.getAnd()) {
        query.addRule(match);
        match.setIfTrue(RuleAction.NEXT);
        match.setIfFalse(RuleAction.REJECT);
      }
      if (query.getOr() == null && query.getNot() == null) {
        Match lastRule = query.getRule().getLast();
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.REJECT);
      }
    }
    if (query.getOr() != null) {
      Match orRule = new Match();
      query.addRule(orRule);
      orRule.setIfTrue(RuleAction.NEXT);
      orRule.setIfFalse(RuleAction.REJECT);
      for (Match match : query.getOr()) {
        orRule.addOr(match);
      }
      Match lastRule = orRule.getRule().getLast();
      if (query.getNot() == null) {
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.REJECT);
      } else {
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.NEXT);
      }
    }
    if (query.getNot() != null) {
      for (Match match : query.getNot()) {
        query.addRule(match);
        match.setIfTrue(RuleAction.REJECT);
        match.setIfFalse(RuleAction.NEXT);
      }
      Match lastRule = query.getRule().getLast();
      lastRule.setIfTrue(RuleAction.REJECT);
      lastRule.setIfFalse(RuleAction.SELECT);
    }
    query.setAnd(null);
    query.setOr(null);
    query.setNot(null);
  }

}
