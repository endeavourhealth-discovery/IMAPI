package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class QueryValidator {
  private final Map<String, VarType> variables = new HashMap<>();
  private final Map<String, Map<String, Set<String>>> pathMap = new HashMap<>();
  private final Map<String, Map<String, String>> propertyMap = new HashMap<>();
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
    for (List<Match> matches : Arrays.asList(boolMatch.getAnd(), boolMatch.getOr(),boolMatch.getStep())) {
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

  private void processIs(List<Node> is) throws QueryException {
    if (is.size()>1){
      Node first= is.getFirst();
      if (first.getNodeRef()!=null|| first.getMatch()!=null||first.getNode()!=null){
        throw new QueryException("in list cannot have nore than one entry if references are used");
      }
    }
    else {
      Node first= is.getFirst();
      if
      (first.getMatch()!=null){
        processMatch(first.getMatch());
      }
    }
  }

  private void processPath(Path pathMatch) {
    if (pathMatch.getNode() != null) {
      variables.put(pathMatch.getNode(), VarType.PATH);
    }
    if (pathMatch.getPath() != null) {
      for (Path subPath : pathMatch.getPath()) {
        processPath(subPath);
      }
    }
  }

  private void processReturn(Match query, String mainEntity) throws QueryException {
    if (query.getReturn() != null) {
      for (Return path : query.getReturn()) {
        validateReturn(path, mainEntity);
      }
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
    if (path.getNode() == null) {
      o++;
      path.setNode("path" + o);
    }
    variables.put(path.getNode(), VarType.PATH);
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        validatePath(subPath);
      }
    }
  }


  private void validateWhere(Where where, String subject) throws QueryException {
    if (where.getNode() != null)
      variables.put(where.getNode(), VarType.PATH);
    if (where.getIri() == null && where.getParameter() == null && where.getAnd() == null && where.getOr() == null&&where.getPropertyVariable()==null)
      throw new QueryException("Where clause has no where iri  (set iri to where iri) ir a parameter");
    if (where.getNodeRef() != null && !variables.containsKey(where.getNodeRef()))
      throw new QueryException("Where clause variable '" + where.getNodeRef() + "' has not been declared in a match path");

    if (where.getNode() != null)
      variables.put(where.getNode(), VarType.NODE);
    if (where.getPropertyVariable()!=null)
      variables.put(where.getPropertyVariable(), VarType.PATH);
    String object = where.getNode();
    if (object == null) {
      o++;
      object = "o" + o;
      where.setNode(object);
    }
    propertyMap.computeIfAbsent(subject, s -> new HashMap<>())
      .put(where.getIri(), object);
    for (List<Where> wheres : Arrays.asList(where.getAnd(), where.getOr())) {
      if (wheres != null) {
        for (Where property : wheres) {
          validateWhere(property, subject);
        }
      }
    }
  }

}
