package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class QueryValidator {
  private final Map<String, VarType> variables = new HashMap<>();
  private final Map<String, Map<String, Set<String>>> pathMap = new HashMap<>();
  private final Map<String, Map<String, String>> propertyMap = new HashMap<>();
  private int o = 0;

  public void validateQuery(Query query) throws QueryException {
    String mainEntity="entity";
    if (query.getVariable()!=null){
      mainEntity=query.getVariable();
    }
    if (query.getAnd() == null &&query.getOr()==null && null == query.getInstanceOf()&&null== query.getWhere())
      throw new QueryException("Query must have match clause or instanceOf or where clause");
    if (mainEntity == null) {
      mainEntity= query.getParameter();
    }
    variables.put(mainEntity, VarType.NODE);
    processMatches(query, mainEntity);
    if (null != query.getInstanceOf()) {
      query.getInstanceOf().get(0).setVariable(mainEntity);
      variables.put(mainEntity, VarType.NODE);
    }
    if (null != query.getWhere()) {
      if (query.getVariable()==null){
        query.setVariable(mainEntity);
      }
      validateWhere(query.getWhere(), query.getVariable());
    }

    if (query.getReturn() == null) {
      String finalMainEntity = mainEntity;
      query.return_(r -> r.setNodeRef(finalMainEntity));
    }
    processReturn(query, mainEntity);
  }

  private void processMatches(Match boolMatch, String mainEntity) throws QueryException {
    processMatch(mainEntity, boolMatch);
    validateMatch(boolMatch);
    for (List<Match> matches : Arrays.asList(boolMatch.getAnd(),boolMatch.getOr(),boolMatch.getNot())) {
      if (matches != null) {
        for (Match match : matches) {
          processMatch(mainEntity, match);
          validateMatch(match);
        }
      }
    }
  }

  private void processMatch(String mainEntity, Match match) {
    if (match.getVariable() != null) {
      variables.put(match.getVariable(), VarType.NODE);
    } else if (match.getParameter() != null) {
      variables.put(match.getParameter(), VarType.NODE);
    } else if (match.getNodeRef() == null) {
      match.setVariable(mainEntity);
    }
    if (match.getPath()!=null){
      for (Path pathMatch : match.getPath()){
        processPath(pathMatch);
      }
    }
  }

  private void processPath(Path pathMatch) {
    if (pathMatch.getVariable()!=null){
      variables.put(pathMatch.getVariable(), VarType.PATH);
    }
    if (pathMatch.getPath()!=null){
      for (Path subPath : pathMatch.getPath()){
        processPath(subPath);
      }
    }
  }

  private void processReturn(Query query, String mainEntity) throws QueryException {
    if (query.getReturn() != null) {
      Return aReturn = query.getReturn();
      if (aReturn.getNodeRef() == null&&aReturn.getAs()==null)
        aReturn.setNodeRef(mainEntity);
      validateReturn(aReturn);
    }
  }

  private void validateReturn(Return aReturn) throws QueryException {
    if (aReturn.getNodeRef() != null) {
      if (variables.get(aReturn.getNodeRef()) == null)
        throw new QueryException("return_ clause uses an unbound node reference variable (" + aReturn.getNodeRef() + ") Should it be a propertyRef?");
    }
    if (aReturn.getValueRef()!=null){
      if (variables.get(aReturn.getValueRef())==null)
        throw new QueryException("return_ clause uses an unbound value reference variable (" + aReturn.getValueRef() + ") should it be a node ref?");
    }
    if (aReturn.getPropertyRef()!=null){
      if (variables.get(aReturn.getPropertyRef())==null)
        throw new QueryException("return_ clause uses an unbound property reference variable (" + aReturn.getPropertyRef() + ") should it be a node ref?");
    }


    if (aReturn.getProperty() != null) {
      for (ReturnProperty property : aReturn.getProperty()) {
        validateReturnProperty(property);

      }
    }
  }


  private void validateReturnProperty(ReturnProperty path) throws QueryException {
    if (path.getPropertyRef() != null && variables.get(path.getPropertyRef()) == null)
      throw new QueryException("return_ clause uses an unbound where reference variable (" + path.getPropertyRef() + ") should it be a node ref?");
    if (path.getValueRef()!=null && variables.get(path.getValueRef())==null)
      throw new QueryException("return_ clause uses an unbound value reference variable (" + path.getValueRef() + ") should it be a property  ref?");
    if (path.getReturn() != null) {
      validateReturn(path.getReturn());
    }
  }


  private void validateMatch(Match match) throws QueryException {
    if (match.getVariable() != null) {
      variables.put(match.getVariable(), VarType.NODE);
    }
    if (match.getParameter()!=null){
      variables.put(match.getParameter(), VarType.NODE);
    }
    if (match.getPath()!=null){
      for (Path pathMatch : match.getPath()){
        validatePath(pathMatch);
      }
    }
    if (match.getNodeRef() != null && variables.get(match.getNodeRef()) == null)
      throw new QueryException("match clause contains a node reference that has not been declared as a variable");
    for (List<Match> matches:Arrays.asList(match.getAnd(),match.getOr())) {
      if (matches != null) {
        for (Match subMatch : matches) {
          validateMatch(subMatch);
        }
      }
    }
    if (match.getWhere() != null) {
        validateWhere(match.getWhere(), match.getVariable());
      }

  }

  private void validatePath(Path path){
    if (path.getVariable()==null){
      o++;
      path.setVariable("path" + o);
    }
    variables.put(path.getVariable(), VarType.PATH);
    if (path.getPath()!=null){
      for (Path subPath : path.getPath()){
        validatePath(subPath);
      }
    }
  }


  private void validateWhere(Where where, String subject) throws QueryException {
    if (where.getVariable() != null)
      variables.put(where.getVariable(), VarType.PATH);
    if (where.getIri() == null && where.getParameter() == null && where.getAnd() == null&&where.getOr()==null)
      throw new QueryException("Where clause has no where iri  (set @id to where iri) ir a parameter");
    if (where.getNodeRef() != null && !variables.containsKey(where.getNodeRef()))
      throw new QueryException("Where clause variable '" + where.getNodeRef() + "' has not been declared in a match path");

    if (where.getValueVariable() != null)
      variables.put(where.getValueVariable(), VarType.NODE);
    String object = where.getValueVariable();
    if (object == null) {
      o++;
      object = "o" + o;
      where.setValueVariable(object);
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
