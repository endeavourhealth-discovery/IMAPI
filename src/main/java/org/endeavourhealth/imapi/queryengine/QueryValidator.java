package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;
import java.util.stream.Collectors;

public class QueryValidator {
  private final Map<String, VarType> variables = new HashMap<>();
  private final Map<String, Map<String, Set<String>>> nodeMap = new HashMap<>();
  private int o = 0;

  public void validateQuery(Match query) throws QueryException {
    String mainEntity="entity";
    if (query.getNode()!=null){
       mainEntity=query.getNode();
    } else if (query.getParameter()!=null){
        mainEntity=query.getParameter().replace("$","");
    }
    if (query.getAnd() == null && query.getOr() == null && null == query.getIs() && null == query.getWhere()&&null==query.getTypeOf())
      throw new QueryException("Query must have match clause or is or where clause");

    variables.put(mainEntity, VarType.NODE);
    processMatches(query);
    if (null != query.getWhere()) {
      validateWhere(query.getWhere(), mainEntity);
    }

    processReturn(query, mainEntity);
  }

  private void processMatches(Match boolMatch) throws QueryException {
    processMatch(boolMatch);
    validateMatch(boolMatch);
    for (List<Match> matches : Arrays.asList(boolMatch.getAnd(), boolMatch.getOr())) {
      if (matches != null) {
        for (Match match : matches) {
          processMatch(match);
          validateMatch(match);
        }
      }
    }
  }

  private void processMatch(Match match) throws QueryException {
    if (match.getNode() != null) {
      variables.put(match.getNode(), VarType.NODE);
    } else if (match.getParameter() != null) {
      variables.put(match.getParameter(), VarType.NODE);
    }
    if (match.getPath() != null) {
      for (Path pathMatch : match.getPath()) {
        processPath(pathMatch);
      }
    }
    if (match.getIs()!=null){
      processIs(match.getIs());
    }
  }

  private void processIs(Node is) throws QueryException {
    if (is.getNodeRef()!=null|| is.getMatch()!=null||is.getNode()!=null){
        throw new QueryException("in list cannot have nore than one entry if references are used");
    }
    else {
      if (is.getNode()!=null)
        variables.put(is.getNode(), VarType.NODE);
      if
      (is.getMatch()!=null){
        processMatch(is.getMatch());
      }
    }
  }

  private void processPath(Path pathMatch) {
    if (pathMatch.getPathVariable() != null) {
      variables.put(pathMatch.getPathVariable(), VarType.PATH);
    }
    if (pathMatch.getNode()!=null){
      variables.put(pathMatch.getNode(), VarType.NODE);
    }
    if (pathMatch.getPath() != null) {
      for (Path subPath : pathMatch.getPath()) {
        processPath(subPath);
      }
    }
  }

  private void processReturn(Match query, String mainEntity) throws QueryException {
    if (query.getReturn() != null) {
      validateReturnColumns(query);
      for (Return path : query.getReturn()) {
        validateReturn(path, mainEntity);
      }
    }
  }
  private void validateReturnColumns(Match match) throws QueryException {
    List<Return> returns = match.getReturn();
    if (returns == null || returns.isEmpty()) {
      return;
    }
    Set<String> seen = new HashSet<>();
    Set<String> duplicates = returns.stream()
      .map(Return::getAs)
      .filter(Objects::nonNull)
      .filter(as -> !seen.add(as))
      .collect(Collectors.toCollection(LinkedHashSet::new));

    if (!duplicates.isEmpty()) {
     throw new QueryException("Duplicate column names");
    }
  }



  private void validateReturn(Return path, String subject) throws QueryException {
    if (path.getNodeRef()==null&&path.getPropertyRef()==null)
      path.setNodeRef(subject);
    if (path.getAs()==null) {
      o++;
      path.setAs("o" + o);
      variables.put(path.getAs(), VarType.NODE);
    }
    if (variables.get(path.getNodeRef()) != null && variables.get(path.getNodeRef()) == null)
      throw new QueryException("return_ clause uses an unbound node reference variable (" + path.getNodeRef() + ") should it be a property ref?");
    if (path.getPropertyRef() != null && variables.get(path.getPropertyRef()) == null)
      throw new QueryException("return_ clause uses an unbound where reference variable (" + path.getPropertyRef() + ") should it be a node ref?");
    if (path.getReturn() != null) {
      for (Return pathReturn : path.getReturn()) {
        validateReturn(pathReturn, path.getAs());
      }
    }
  }


  private void validateMatch(Match match) throws QueryException {
    if (match.getNode() != null) {
      variables.put(match.getNode(), VarType.NODE);
    }
    if (match.getParameter() != null) {
      variables.put(match.getParameter(), VarType.NODE);
    }
    if (match.getPath() != null) {
      for (Path pathMatch : match.getPath()) {
        validatePath(pathMatch);
      }
    }
    if (match.getNodeRef() != null && variables.get(match.getNodeRef()) == null)
      throw new QueryException("match clause contains a node reference that has not been declared as a variable");
    for (List<Match> matches : Arrays.asList(match.getAnd(), match.getOr())) {
      if (matches != null) {
        for (Match subMatch : matches) {
          validateMatch(subMatch);
        }
      }
    }
    if (match.getWhere() != null) {
      validateWhere(match.getWhere(), match.getNode());
    }

  }

  private void validatePath(Path path) {
    if (path.getPathVariable() != null) {
      variables.put(path.getPathVariable(), VarType.PATH);
    }
    if (path.getNode()!=null)
      variables.put(path.getNode(), VarType.NODE);
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        validatePath(subPath);
      }
    }
  }

  private void validateAssignable(Assignable assignable) throws QueryException {
    if
    (assignable.getOperator()==null)
      throw new QueryException("Operator must be specified");
    if (assignable.getValue()==null &&assignable.getCompare()==null){
      throw new QueryException("Either Value or a Compare with must be specified");
    }
    if (assignable.getCompare()!=null)
      validateCompare(assignable);
  }

  private void validateCompare(Assignable assignable) throws QueryException {
    Compare compare=assignable.getCompare();
    if (compare.getUnits()!=null){
      if (assignable.getValue()==null)
        throw new QueryException("Value must be specified when units are provided");
    }
    if (compare.getRight()!=null) validateSource(compare.getRight());
  }

  private void validateSource(ValueSource source) throws QueryException {
    if (source.getNodeRef()!=null)
      if (variables.get(source.getNodeRef()) == null)
        throw new QueryException("Variable "+source.getNodeRef()+" not found");

  }


  private void validateWhere(Where where, String subject) throws QueryException {
    if (where.getIri() != null){
      if (where.getIs()!=null){
        Node is = where.getIs().getFirst();
        if (is.getIri()==null&&is.getParameter()==null){
          throw new QueryException("Where clause must have a value for its property of "+where.getName());
        }
      }
      else if (where.getCompare()==null
      && where.getRange()==null
        &&!where.isNotNull()
        &&!where.getIsNull()
        && where.getValue()==null
      && where.getNode()==null)
        throw new QueryException("Clause filter must have a value or a compare clause for the property of "+where.getName());
    }
    if (where.getAnd() != null||where.getOr()!=null){
      for (List<Where> wheres : Arrays.asList(where.getAnd(), where.getOr())) {
        if (wheres != null) {
          for (Where property : wheres) {
            validateWhere(property, subject);
          }
        }
      }
      return;
    }
    if (where.getRange() != null) {
      if (where.getRange().getFrom()==null)
        throw new QueryException("Range must have a from value");
      validateAssignable(where.getRange().getFrom());
      if (where.getRange().getTo()==null)
        throw new QueryException("Range must have a to value");
      validateAssignable(where.getRange().getTo());
      }
    if (where.getPropertyVariable() != null)
      variables.put(where.getPropertyVariable(), VarType.PATH);
    if (where.getIri() == null && where.getParameter() == null && where.getAnd() == null && where.getOr() == null&&where.getPropertyVariable()==null&&where.getCompare()==null)
      throw new QueryException("Where clause has no criteria (property, compare or parameter");
    if (where.getNodeRef() != null && !variables.containsKey(where.getNodeRef()))
      throw new QueryException("Where clause variable '" + where.getNodeRef() + "' has not been declared in a match path");

  }

}
