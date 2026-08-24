package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;
import java.util.stream.Collectors;

public class QueryValidator {
  private final Map<String, VarType> variables = new HashMap<>();
  private final Map<String, Map<String, Set<String>>> nodeMap = new HashMap<>();
  private int o = 0;
  private String mainEntity;

  public void validateQuery(Query query) throws QueryException {
    String mainEntity = "entity";
    if (query.getNode() != null) {
      mainEntity = query.getNode();
    } else if (query.getParameter() != null) {
      mainEntity = query.getParameter().replace("$", "");
    }
    this.mainEntity = mainEntity;
    if (query.getAnd() == null && query.getOr() == null && null == query.getIs() && null == query.getWhere() && null == query.getTypeOf())
      throw new QueryException("Query must have match clause or is or where clause");

    variables.put(mainEntity, VarType.NODE);
    processMatch(query);
    validateMatch(query);
    if (query.getColumnGroup()!=null){
      for (Query group: query.getColumnGroup()) {
        processMatch(group);
        validateMatch(group);
      }
    }
  }


  private void processMatch(Query query) throws QueryException {
    if (query.getNode() != null) {
      variables.put(query.getNode(), VarType.NODE);
    } else if (query.getParameter() != null) {
      variables.put(query.getParameter(), VarType.NODE);
    }
    if (query.getPath() != null) {
      for (Path pathMatch : query.getPath()) {
        processPath(pathMatch);
      }
    }
    if (query.getIs() != null) {
      processIs(query.getIs());
    }
    if (query.getReturn() != null) {
      processReturn(query, mainEntity);
    }
    for (List<Query> queries : Arrays.asList(query.getAnd(), query.getOr())) {
      if (queries != null) {
        for (Query subQuery : queries) {
          processMatch(subQuery);
        }
      }
    }
  }

  private void processIs(Node is) throws QueryException {
    if (is.getNode() != null)
      variables.put(is.getNode(), VarType.NODE);
    if
    (is.getMatch() != null) {
      processMatch(is.getMatch());
    }
  }

  private void processPath(Path pathMatch) {
    if (pathMatch.getPathVariable() != null) {
      variables.put(pathMatch.getPathVariable(), VarType.PATH);
    }
    if (pathMatch.getNode() != null) {
      variables.put(pathMatch.getNode(), VarType.NODE);
    }
    if (pathMatch.getPath() != null) {
      for (Path subPath : pathMatch.getPath()) {
        processPath(subPath);
      }
    }
  }

  private void processReturn(Query query, String mainEntity) throws QueryException {
    if (query.getReturn() != null) {
      validateReturnColumns(query);
      for (Return path : query.getReturn()) {
        validateReturn(path, mainEntity);
      }
    }
  }

  private void validateReturnColumns(Query query) throws QueryException {
    List<Return> returns = query.getReturn();
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
    if (path.getNodeRef() == null && path.getPropertyRef() == null)
      path.setNodeRef(subject);
    if (path.getAs() == null) {
      o++;
      path.setAs("o" + o);
      variables.put(path.getAs(), VarType.NODE);
    }
    if (variables.get(path.getNodeRef()) != null && variables.get(path.getNodeRef()) == null)
      throw new QueryException("return_ clause uses an unbound node reference variable (" + path.getNodeRef() + ") should it be a property ref?");
    if (path.getPropertyRef() != null && variables.get(path.getPropertyRef()) == null)
      throw new QueryException("return_ clause uses an unbound where reference variable (" + path.getPropertyRef() + ") should it be a node ref?");
    if (path.getIri()==null &&path.getFunction()==null &&path.getCase()==null &&path.getSemanticMap()==null){
      throw new QueryException("Data set or column must have a definition. Check data sets and match clauses");
    }
    if (path.getReturn() != null) {
      for (Return pathReturn : path.getReturn()) {
        validateReturn(pathReturn, path.getAs());
      }
    }
  }


  private void validateMatch(Query query) throws QueryException {
    if (query.getNode() != null) {
      variables.put(query.getNode(), VarType.NODE);
    }
    if (query.getAs() != null) {
      variables.put(query.getAs(), VarType.NODE);
    }
    if (query.getParameter() != null) {
      variables.put(query.getParameter(), VarType.NODE);
    }
    if (query.getPath() != null) {
      for (Path pathMatch : query.getPath()) {
        validatePath(pathMatch);
      }
    }

    for (List<Query> queries : Arrays.asList(query.getAnd(), query.getOr())) {
      if (queries != null) {
        for (Query subQuery : queries) {
          validateMatch(subQuery);
        }
      }
    }
    if (query.getThen()!=null){
      validateMatch(query.getThen());
    }
    if (query.getWhere() != null) {
      validateWhere(query.getWhere(), query.getNode());
    }
    if (query.getReturn() != null) {
      validateReturnColumns(query);
      for (Return path : query.getReturn()) {
        validateReturn(path, query.getNode());
      }
    }
    if (query.getFrom() != null && variables.get(query.getFrom()) == null)
      throw new QueryException("query clause contains a 'from' that has not been projected from another clause");

  }

  private void validatePath(Path path) {
    if (path.getPathVariable() != null) {
      variables.put(path.getPathVariable(), VarType.PATH);
    }
    if (path.getNode() != null)
      variables.put(path.getNode(), VarType.NODE);
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        validatePath(subPath);
      }
    }
  }

  private void validateAssignable(Assignable assignable) throws QueryException {
    if
    (assignable.getOperator() == null)
      throw new QueryException("Operator must be specified");
    if (assignable.getValue() == null && assignable.getCompare() == null) {
      throw new QueryException("Either Value or a Compare and must be specified");
    }
    if (assignable.getCompare() != null)
      validateCompare(assignable);
  }

  private void validateCompare(Assignable assignable) throws QueryException {
    Compare compare = assignable.getCompare();
    if (compare.getUnits() != null) {
      if (assignable.getValue() == null)
        throw new QueryException("Value must be specified when units are provided");
    }
    if (compare.getRight() != null) validateSource(compare.getRight());
  }

  private void validateSource(ValueSource source) throws QueryException {
    if (source.getNodeRef() != null)
      if (variables.get(source.getNodeRef()) == null)
        throw new QueryException("Variable " + source.getNodeRef() + " not found");

  }


  private void validateWhere(Where where, String subject) throws QueryException {
    if (where.getIri() != null) {
      if (where.getIs() != null) {
        Node is = where.getIs().getFirst();
        if (is.getIri() == null && is.getParameter() == null) {
          throw new QueryException("Where clause must have a value for its property of " + where.getName());
        }
      } else if (where.getCompare() == null
        && where.getRange() == null
        && !where.isNotNull()
        && !where.getIsNull()
        && where.getValue() == null
        && where.getNode() == null)
        throw new QueryException("Clause filter must have a value or a compare clause for the property of " + where.getName());
    }
    if (where.getAnd() != null || where.getOr() != null) {
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
      if (where.getRange().getFrom() == null)
        throw new QueryException("Range must have a from value");
      validateAssignable(where.getRange().getFrom());
      if (where.getRange().getTo() == null)
        throw new QueryException("Range must have a to value");
      validateAssignable(where.getRange().getTo());
    }
    if (where.getPropertyVariable() != null)
      variables.put(where.getPropertyVariable(), VarType.PATH);
    if (where.getIri() == null && where.getParameter() == null && where.getAnd() == null && where.getOr() == null && where.getPropertyVariable() == null && where.getCompare() == null)
      throw new QueryException("Where clause has no criteria (property, compare or parameter");
    if (where.getNodeRef() != null && !variables.containsKey(where.getNodeRef()))
      throw new QueryException("Where clause variable '" + where.getNodeRef() + "' has not been declared in a match path");

  }

}
