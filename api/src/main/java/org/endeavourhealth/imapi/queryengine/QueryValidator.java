package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryValidator {
  private final Map<String, VarType> variables = new HashMap<>();
  private final Map<String, Map<String, Set<String>>> pathMap = new HashMap<>();
  private final Map<String, Map<String, String>> propertyMap = new HashMap<>();
  private int o = 0;

  public void validateQuery(Query query) throws QueryException {
    String mainEntity;
    if (query.getMatch() == null && null == query.getInstanceOf())
      throw new QueryException("Query must have match clause or instanceOf");
    mainEntity = query.getVariable();
    if (mainEntity == null && null != query.getMatch()) {
      mainEntity = query.getMatch().get(0).getVariable();
    }
    if (mainEntity == null)
      mainEntity = "entity";
    processMatches(query, mainEntity);
    if (null != query.getInstanceOf()) {
      query.getInstanceOf().get(0).setVariable(mainEntity);
      variables.put(mainEntity, VarType.NODE);
    }

    if (query.getReturn() == null) {
      String finalMainEntity = mainEntity;
      query.return_(r -> r.setNodeRef(finalMainEntity));
    }
    processReturn(query, mainEntity);
  }

  private void processMatches(Query query, String mainEntity) throws QueryException {
    if (null != query.getMatch()) {
      for (Match match : query.getMatch()) {
        if (match.getVariable() == null && match.getNodeRef() == null)
          match.setVariable(mainEntity);
        validateMatch(match);
      }
    }
  }

  private void processReturn(Query query, String mainEntity) throws QueryException {
    if (query.getReturn() != null) {
      Return aReturn = query.getReturn();
      if (aReturn.getNodeRef() == null) {
        aReturn.setNodeRef(mainEntity);
      }
      validateReturn(aReturn);
    }
  }

  private void validateReturn(Return aReturn) throws QueryException {
    if (aReturn.getNodeRef() != null && variables.get(aReturn.getNodeRef()) == null)
      throw new QueryException("return_ clause uses an unbound node reference variable (" + aReturn.getNodeRef() + ") Should it be a propertyRef?");


    if (aReturn.getProperty() != null) {
      for (ReturnProperty property : aReturn.getProperty()) {
        validateReturnProperty(aReturn.getNodeRef(), property);

      }
    }
  }


  private void validateReturnProperty(String subject, ReturnProperty path) throws QueryException {
    if (path.getPropertyRef() != null && variables.get(path.getPropertyRef()) == null)
      throw new QueryException("return_ clause uses an unbound where reference variable (" + path.getPropertyRef() + ") should it be a node ref?");

    if (path.getReturn() != null) {
      if (pathMap.get(subject) != null) {
        Set<String> nodeVariables = pathMap.get(subject).get(path.getPropertyRef() + "\t" + path.getIri());
        if (path.getReturn().getNodeRef() != null && !nodeVariables.contains(path.getReturn().getNodeRef())) {
          throw new QueryException("return_ path/node variable '" + path.getReturn().getNodeRef() +
            "' must match a match path/node variable");
        }

      }
      validateReturn(path.getReturn());
    } else {
      if (propertyMap.get(subject) != null) {
        String valueVariable = propertyMap.get(subject).get(path.getIri());
        if (valueVariable != null) {
          path.setValueRef(valueVariable);
        }
      }
    }
  }


  private void validateMatch(Match match) throws QueryException {
    if (match.getVariable() != null) {
      variables.put(match.getVariable(), VarType.NODE);
    }
    if (match.getNodeRef() != null && variables.get(match.getNodeRef()) == null)
      throw new QueryException("match clause contains a node reference that has not been declared as a variable");

    if (match.getMatch() != null) {
      for (Match subMatch : match.getMatch()) {
        validateMatch(subMatch);
      }
    }
    if (match.getWhere() != null) {
      for (Where where : match.getWhere()) {
        validateWhere(where, match.getVariable());
      }
    }

  }


  private void validateWhere(Where where, String subject) throws QueryException {
    if (where.getVariable() != null)
      variables.put(where.getVariable(), VarType.PATH);
    if (where.getIri() == null && where.getParameter() == null && where.getWhere() == null)
      throw new QueryException("Where clause has no where iri  (set iri to where iri) ir a parameter");
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
    if (where.getWhere() != null) {
      for (Where property : where.getWhere())
        validateWhere(property, subject);
    }
    if (where.getMatch() != null) {
      if (where.getMatch().getVariable() == null) {
        where.getMatch().setVariable(object);
      }
      validateMatch(where.getMatch());
    }

  }

}
