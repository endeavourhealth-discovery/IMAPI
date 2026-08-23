package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class LogicOptimizer {
  final ObjectMapper mapper = new ObjectMapper();
  Set<String> commonMatches;
  private Map<String, Query> keepMatches = new HashMap<>();

  public static void optimizeQuery(Query query) {
    cleanBooleans(query);
    cleanColumnGroups(query);
  }

  public static void cleanColumnGroups(Query query) {
    if (query.getColumnGroup() == null) return;
    for (Query subQuery : query.getColumnGroup()){
      cleanBooleans(subQuery);
    }
  }

  public static void optimiseECLQuery(Query query) {
    optimizeQuery(query);
    optimiseECL(query);
  }

  public static void optimiseECL(Query query) {
    mergeNested(query);
  }

  private static void logicalPath(Path path) {
    if (path == null) return;
    path.setNode(null);
    if (path.getPath() != null)
      for (Path subPath : path.getPath()) {
        logicalPath(subPath);
      }
  }

  private static void mergeNested(Query query) {
    if (query.getOr() != null && query.getOr().size() == 1) {
      Query orQuery = query.getOr().getFirst();
      mergeMatch(query, orQuery);
    }
    if (query.getAnd() != null && query.getAnd().size() == 1) {
      Query andQuery = query.getAnd().getFirst();
      mergeMatch(query, andQuery);
    }
  }

  public static boolean isLinkedMatch(Query query) {
    if (query.getWhere() != null) {
      return isLinkedWhere(query.getWhere());
    } else return false;
  }

  public static boolean isLinkedWhere(Where where) {

    if (where.getCompare() != null) {
      if (where.getCompare().getLeft() != null) {
        if (where.getCompare().getLeft().getNodeRef() != null) {
          return true;
        }
      }
      if (where.getCompare().getRight() != null) {
        if (where.getCompare().getRight().getNodeRef() != null) {
          return true;
        }
      }
    }
    if (where.getAnd() != null) {
      for (Where andWhere : where.getAnd()) {
        if (isLinkedWhere(andWhere)) {
          return true;
        }
      }
    }
    return false;
  }

  private static void mergeMatch(Query query, Query nestedQuery) {
    query.setIs(nestedQuery.getIs());
    query.setOr(nestedQuery.getOr());
    query.setAnd(nestedQuery.getAnd());
    query.setNotExists(nestedQuery.notExists());
    if (nestedQuery.getWhere() != null) {
      if (query.getWhere() == null) query.setWhere(nestedQuery.getWhere());
      else {
        Where newAndWhere = new Where();
        newAndWhere.addAnd(query.getWhere());
        newAndWhere.addAnd(nestedQuery.getWhere());
      }
    }
  }



  public static void cleanBooleans(Query group) {
    List<Query> queries = group.getAnd();
    if (queries != null) {
      if (queries.isEmpty()) group.setAnd(null);
      else flatten(queries);
    } else {
      queries = group.getOr();
      if (queries != null) {
        if (queries.isEmpty()) group.setOr(null);
        else flatten(queries);
      }
    }
  }

  private static void flatten(List<Query> list) {
    for (int i = 0; i < list.size(); i++) {
      Query query = list.get(i);
      if (query.getWhere() == null && query.getOrderBy() == null && query.getAnd() == null && query.getOr() == null
       && query.getReturn() == null && query.getIs() == null) {
        list.remove(i);
        i--;
      } else cleanBooleans(query);
    }
  }


  private static void flattenWhere(Where where) {
    if (where.getAnd() != null) {
      List<Where> flatWheres = new ArrayList<>();
      for (Where child : where.getAnd()) {
        if (child.getAnd() != null && child.getOr() == null) {
          flatWheres.addAll(child.getAnd());
        } else flatWheres.add(child);
        flattenWhere(child);
      }
      where.setAnd(flatWheres);
    }
    if (where.getOr() != null) {
      List<Where> flatWheres = new ArrayList<>();
      for (Where child : where.getOr()) {
        if (child.getOr() != null && child.getAnd() == null) {
          flatWheres.addAll(child.getOr());
        } else flatWheres.add(child);
        flattenWhere(child);
      }
      where.setOr(flatWheres);
    }
  }

  public static Operator invertComparisonOperator(String op) {
    return switch (op) {
      case ">=" -> Operator.lte;
      case ">" -> Operator.lt;
      case "<=" -> Operator.gte;
      case "<" -> Operator.gt;
      case "=" -> Operator.eq;
      default -> throw new IllegalArgumentException("Invalid comparison operator: " + op);
    };
  }

  public static void optimiseAgeWheres(Query query) {
    if (query.getAnd() != null)
      for (Query child : query.getAnd()) optimiseAgeWheres(child);
    if (query.getOr() != null)
      for (Query child : query.getOr()) optimiseAgeWheres(child);

    if (query.getWhere() != null)
      query.setWhere(rewriteAgeWhere(query.getWhere()));
    if (query.getThen() != null && query.getThen().getWhere() != null)
      query.getThen().setWhere(rewriteAgeWhere(query.getThen().getWhere()));
  }

  private static Where rewriteAgeWhere(Where where) {
    if (where.getAnd() != null)
      where.getAnd().replaceAll(LogicOptimizer::rewriteAgeWhere);
    if (where.getOr() != null)
      where.getOr().replaceAll(LogicOptimizer::rewriteAgeWhere);

    if (!"http://endhealth.info/im#age".equals(where.getIri())) return where;
    if (where.getUnits() == null) return where;
    if (where.getValue() == null) return where;
    if (where.getOperator() == null) return where;

    Where rewritten = new Where();
    rewritten.setOperator(invertComparisonOperator(where.getOperator().getValue()));
    rewritten.setValue(where.getValue());
    rewritten.setNot(where.isNot());

    ValueSource left = new ValueSource();
    left.setIri("http://endhealth.info/im#dateOfBirth");

    ValueSource right = new ValueSource();
    right.setParameter("$searchDate");

    Compare compare = new Compare();
    compare.setLeft(left);
    compare.setRight(right);
    compare.setUnits(where.getUnits());

    rewritten.setCompare(compare);

    return rewritten;
  }

  public static void optimiseNegativeIntervalWheres(Query query) {
    if (query.getAnd() != null)
      for (Query child : query.getAnd()) optimiseNegativeIntervalWheres(child);
    if (query.getOr() != null)
      for (Query child : query.getOr()) optimiseNegativeIntervalWheres(child);

    if (query.getWhere() != null)
      query.setWhere(rewriteNegativeIntervalWhere(query.getWhere()));
    if (query.getThen() != null && query.getThen().getWhere() != null)
      query.getThen().setWhere(rewriteNegativeIntervalWhere(query.getThen().getWhere()));
  }

  private static Where rewriteNegativeIntervalWhere(Where where) {
    if (where.getAnd() != null)
      where.getAnd().replaceAll(LogicOptimizer::rewriteNegativeIntervalWhere);

    if (where.getOr() != null)
      where.getOr().replaceAll(LogicOptimizer::rewriteNegativeIntervalWhere);

    if (where.getRange() != null) {
      if (where.getRange().getFrom() != null)
        where.getRange().setFrom(rewriteNegativeIntervalValue(where.getRange().getFrom()));

      if (where.getRange().getTo() != null)
        where.getRange().setTo(rewriteNegativeIntervalValue(where.getRange().getTo()));

      return where;
    }

    if (where.getCompare() == null) return where;
    if (where.getValue() == null || !where.getValue().startsWith("-")) return where;
    if (where.getCompare().getUnits() == null) return where;

    Compare compare = where.getCompare();
    String positiveValue = where.getValue().substring(1);

    boolean leftIsSearchDate = compare.getLeft() != null
      && "$searchDate".equals(compare.getLeft().getParameter());
    boolean rightIsSearchDate = compare.getRight() != null
      && "$searchDate".equals(compare.getRight().getParameter());

    if (leftIsSearchDate) {
      Compare swapped = new Compare();
      swapped.setLeft(compare.getRight());
      swapped.setRight(compare.getLeft());
      swapped.setUnits(compare.getUnits());
      where.setCompare(swapped);
      where.setOperator(invertComparisonOperator(where.getOperator().getValue()));
      where.setValue(positiveValue);
    } else if (rightIsSearchDate) {
      where.setValue(positiveValue);
    }

    return where;
  }

  private static Value rewriteNegativeIntervalValue(Value value) {
    if (value.getCompare() == null) return value;
    if (value.getValue() == null || !value.getValue().startsWith("-")) return value;
    if (value.getCompare().getUnits() == null) return value;

    Compare compare = value.getCompare();
    String positiveValue = value.getValue().substring(1);

    boolean leftIsSearchDate = compare.getLeft() != null
      && "$searchDate".equals(compare.getLeft().getParameter());
    boolean rightIsSearchDate = compare.getRight() != null
      && "$searchDate".equals(compare.getRight().getParameter());

    if (leftIsSearchDate) {
      Compare swapped = new Compare();
      swapped.setLeft(compare.getRight());
      swapped.setRight(compare.getLeft());
      swapped.setUnits(compare.getUnits());
      value.setCompare(swapped);
      value.setOperator(invertComparisonOperator(value.getOperator().getValue()));
      value.setValue(positiveValue);
    } else if (rightIsSearchDate) {
      value.setValue(positiveValue);
    }

    return value;
  }

  public Query getLogicalMatch(Query query) throws JsonProcessingException {
    String matchJson = mapper.writeValueAsString(query);
    Query logicalQuery = mapper.readValue(matchJson, Query.class);
    logicalQuery.setUuid(null);
    if (logicalQuery.getPath() != null) {
      for (Path path : logicalQuery.getPath()) {
        logicalPath(path);
      }
    }
    if (logicalQuery.getWhere() != null) {
      logicalWhere(logicalQuery.getWhere());
    }
    for (List<Query> queries : Arrays.asList(logicalQuery.getAnd(), logicalQuery.getOr())) {
      if (queries != null) {
        for (int i = 0; i < queries.size(); i++) {
          Query subQuery = queries.get(i);
          Query logicalSubQuery = getLogicalMatch(subQuery);
          queries.set(i, logicalSubQuery);
        }
      }
    }

    return logicalQuery;
  }

  private void logicalWhere(Where where) {
    where.setUuid(null);
    where.setNodeRef(null);
    for (List<Where> wheres : Arrays.asList(where.getAnd(), where.getOr())) {
      if (wheres != null) {
        for (Where subWhere : wheres) {
          logicalWhere(subWhere);
        }
      }
    }
  }

  public void resolveLogic(Query query, DisplayMode displayMode) throws QueryException {
    try {
      if (displayMode == DisplayMode.LOGICAL) {
        getLogicFromRules(query);
        optimiseMatch(query);
      } else {
        optimiseMatch(query);
      }
      flattenMatch(query);
    } catch (Exception e) {
      throw new QueryException("Error resolving logic", e);
    }
  }

  private void getLogicFromRules(Query query) {
    if (query.getRule() == null) return;
    Query or=null;
    Query and=query;
    for (int i=0;i<query.getRule().size();i++) {
      Query subQuery = query.getRule().get(i);
      RuleAction ifTrue = subQuery.getIfTrue();
      RuleAction ifFalse = subQuery.getIfFalse();
      if (ifTrue == ifFalse) {
        throw new IllegalArgumentException("ifTrue and ifFalse cannot be the same");
      }
      switch (ifTrue + "_" + ifFalse) {
        case "SELECT_REJECT":
          if (i<query.getRule().size()-1)
            throw new IllegalArgumentException("Select /Reject must be last rule");
          if (or!=null)
            or.addOr(subQuery);
          else and.addAnd(subQuery);
          break;
        case "SELECT_NEXT":
          if (or!=null)
            or.addOr(subQuery);
          else {
            or= new Query();
            and.addAnd(or);
            or.addOr(subQuery);
          }
          break;
        case "NEXT_REJECT":
          if (or!=null) {
            or.addOr(subQuery);
            or=null;
          }
          else and.addAnd(subQuery);
          break;
        case "NEXT_SELECT":
          subQuery.setNotExists(true);
          if (or!=null)
            or.addOr(subQuery);
          else {
            or= new Query();
            and.addAnd(or);
            or.addOr(subQuery);
          }
          break;
        case "REJECT_SELECT":
          if (i<query.getRule().size()-1)
            throw new IllegalArgumentException("Reject /select must be last rule");
          if (or!=null)
            or.addOr(subQuery);
          else and.addAnd(subQuery);
          break;

        case "REJECT_NEXT":
          subQuery.setNotExists(true);
          if (or!=null){
            and=new Query();
            or.addOr(and);
            and.addAnd(subQuery);
            or=null;
          }
          else and.addAnd(subQuery);
          break;

      }
    }
    query.setRule(null);

  }

  public void optimiseMatch(Query query) throws JsonProcessingException {
    optimizeAndMatches(query);
    optimizeOrMatches(query);
  }

  private void flattenMatch(Query query) {
    if (query.getOr() != null && !query.isNotExists()) {
      List<Query> flatOrs = new ArrayList<>();
      flattenOrs(query, flatOrs);
      if (!flatOrs.isEmpty()) query.setOr(flatOrs);
    } else if (query.getAnd() != null && !query.isNotExists()) {
      List<Query> flatAnds = new ArrayList<>();
      flattenAnds(query, flatAnds);
      if (!flatAnds.isEmpty()) query.setAnd(flatAnds);
    }
  }

  private void flattenAnds(Query query, List<Query> flatAnds) {
    for (Query subQuery : query.getAnd()) {
      if (subQuery.getAnd() == null) {
        flatAnds.add(subQuery);
        flattenMatch(subQuery);
      } else {
        if (subQuery.isNotExists()) {
          flatAnds.add(subQuery);
        } else flattenAnds(subQuery, flatAnds);
      }
    }
  }

  private void flattenOrs(Query query, List<Query> flatOrs) {
    for (Query subQuery : query.getOr()) {
      if (subQuery.getOr() == null) {
        flatOrs.add(subQuery);
        flattenMatch(subQuery);
      } else {
        if (subQuery.isNotExists()) {
          flatOrs.add(subQuery);
        } else flattenOrs(subQuery, flatOrs);
      }
    }
  }

  private void optimizeAndMatches(Query query) throws JsonProcessingException {
    commonMatches = new HashSet<>();

    if (query.getAnd() == null) return;
    if (query.getWhere() == null && query.getIs() == null) {
      if (query.getAnd().size() > 1) {
        List<Query> originalAnds = query.getAnd();
        List<Query> optimalAnds = new ArrayList<>();
        getCommonAnds(originalAnds, commonMatches, optimalAnds);
        if (commonMatches.isEmpty()) return;
        for (Query andQuery : originalAnds) {
          if (andQuery.getAnd() != null) {
            for (Query subQuery : andQuery.getAnd()) {
              String content = LogicComparer.serializeMatchLogic(subQuery);
              if (!commonMatches.contains(content)) {
                optimalAnds.add(subQuery);
              }
            }
          }
        }
        query.setAnd(optimalAnds);
      } else if (query.getAnd().size() == 1) {
        Query and = query.getAnd().getFirst();
        if (and.getWhere() == null && and.getReturn() == null) {
          if (and.getOr() != null) {
            query.setOr(and.getOr());
            query.setAnd(null);
            query.setReturn(and.getReturn());
            query.setNotExists(and.notExists());
          } else if (and.getAnd() != null) {
            query.setAnd(and.getAnd());
            query.setOr(null);
            query.setReturn(and.getReturn());
          }
        }
      }
    }
  }

  private void optimizeOrMatches(Query query) throws JsonProcessingException {
    commonMatches = new HashSet<>();
    if (query.getOr() == null) return;
    if (query.getWhere() == null && query.getOr().size() > 1) {
      List<Query> originalOrs = query.getOr();
      List<Query> optimisedAnds = new ArrayList<>();
      List<Query> optimisedOrs = new ArrayList<>();
      getCommonAnds(originalOrs, commonMatches, optimisedAnds);
      if (commonMatches.isEmpty()) return;
      for (Query orQuery : originalOrs) {
        if (orQuery.getAnd() != null) {
          Query newOr = new Query();
          optimisedOrs.add(newOr);
          for (Query andQuery : orQuery.getAnd()) {
            String content = LogicComparer.serializeMatchLogic(andQuery);
            if (!commonMatches.contains(content)) {
              newOr.addAnd(andQuery);
            }
          }
        }
      }
      if (!optimisedAnds.isEmpty()) {
        query.setAnd(optimisedAnds);
        if (!optimisedOrs.isEmpty()) {
          Query topOr = new Query();
          query.addAnd(topOr);
          topOr.setOr(optimisedOrs);
        }
      }
    }

  }

  private void getCommonAnds(List<Query> queries, Set<String> commonMatches, List<Query> ands) throws JsonProcessingException {
    Query first = queries.getFirst();
    if (first.getAnd() != null) {
      for (int q = 0; q < first.getAnd().size(); q++) {
        Query candidate = first.getAnd().get(q);
        String content = LogicComparer.serializeMatchLogic(candidate);
        if (!commonMatches.contains(content)) {
          if (isCommon(queries, content, 1)) {
            ands.add(candidate);
            commonMatches.add(content);
          }
        }
      }

    }
  }

  private boolean isCommon(List<Query> queries, String content, int index) throws JsonProcessingException {
    if (index > queries.size() - 1) return true;
    Query next = queries.get(index);
    if (next.getAnd() != null) {
      for (int q = 0; q < next.getAnd().size(); q++) {
        Query candidate = next.getAnd().get(q);
        String testContent = LogicComparer.serializeMatchLogic(candidate);
        if (testContent.equals(content)) {
          if (index < queries.size() - 1)
            return isCommon(queries, content, index + 1);
          else return true;
        }
      }
    }
    return false;
  }

  public void getRulesFromLogic(Query query) {
    if (query.getAnd() == null && query.getOr() == null) return;
    if (query.getAnd() != null) {
      for (Query subQuery : query.getAnd()) {
        query.addRule(subQuery);
        if (subQuery.notExists()) {
          subQuery.setIfTrue(RuleAction.REJECT);
          subQuery.setIfFalse(RuleAction.NEXT);
        } else {
          subQuery.setIfTrue(RuleAction.NEXT);
          subQuery.setIfFalse(RuleAction.REJECT);
        }
      }
      if (query.getOr() == null) {
        Query lastRule = query.getRule().getLast();
        if (lastRule.notExists()) {
          lastRule.setIfTrue(RuleAction.REJECT);
          lastRule.setIfFalse(RuleAction.SELECT);

        } else {
          lastRule.setIfTrue(RuleAction.SELECT);
          lastRule.setIfFalse(RuleAction.REJECT);
        }
      }
    }
    if (query.getOr() != null) {
      Query orRule = new Query();
      query.addRule(orRule);
      orRule.setIfTrue(RuleAction.NEXT);
      orRule.setIfFalse(RuleAction.REJECT);
      for (Query subQuery : query.getOr()) {
        orRule.addOr(subQuery);
      }
      Query lastRule = orRule.getRule().getLast();
      if (lastRule.notExists()) {
        lastRule.setIfTrue(RuleAction.REJECT);
        lastRule.setIfFalse(RuleAction.SELECT);
      } else {
        lastRule.setIfTrue(RuleAction.SELECT);
        lastRule.setIfFalse(RuleAction.REJECT);
      }
    }
    query.setAnd(null);
    query.setOr(null);
  }



}