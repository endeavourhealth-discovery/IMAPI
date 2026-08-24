package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.imq.*;

import java.util.*;

public class ECLQueryValidator {
  private final SetRepository setRepository = new SetRepository();
  private Map<String, Boolean> validConcepts;
  private ValidationLevel validationLevel;


  public ECLStatus validateQuery(Query query, ValidationLevel validationLevel) {
    this.validationLevel = validationLevel;
    Set<String> iris = IriCollector.collectIris(query);
    if (iris.isEmpty()) {
      ECLStatus status = new ECLStatus();
      status.setValid(false);
      status.setMessage("No concepts in query");
      return status;
    }
    validConcepts = setRepository.getValidConcepts(iris);
    query.setInvalid(isInvalidMatchWheres(query));
    ECLStatus status = new ECLStatus();
    status.setValid(!query.isInvalid());
    if (query.isInvalid()) {
      status.setMessage("Unknown concepts in query");
    }
    return status;
  }

  private boolean isInvalidMatchWheres(Query query) {
    boolean invalid = false;
    if (query.getIs() != null) {
      Node node = query.getIs();
      if (node.getIri() != null) {
        if (!validConcepts.get(node.getIri())) {
          node.setInvalid(true);
          invalid = true;
        }
      }
    }
    if (query.getWhere() != null) {
      Set<String> focusConcepts = new HashSet<>();
      if (validationLevel == ValidationLevel.ECL)
        getFocusConcepts(query, focusConcepts);
      if (isInvalidWhere(query.getWhere(), focusConcepts)) {
        invalid = true;
      }
    }
    for (List<Query> queries : Arrays.asList(query.getOr(), query.getAnd())) {
      if (queries != null) {
        for (Query m : queries) {
          if (isInvalidMatchWheres(m)) {
            invalid = true;
          }
        }
      }
    }
    return invalid;
  }

  private boolean isInvalidWhere(Where where, Set<String> focusConcepts) {
    boolean invalid = false;
    if (where.getIri() != null) {
      if (!validConcepts.get(where.getIri())) {
        invalid = true;
        where.setInvalid(true);
      }
      if (validationLevel == ValidationLevel.ECL) {
        if (!setRepository.isValidPropertyForDomains(where.getIri(), focusConcepts)) {
          where.setInvalid(true);
          invalid = true;
        }
      }
    }
    if (!invalid) {
      for (List<Node> nodes : Arrays.asList(where.getIs())) {
        if (nodes != null) {
          for (Node node : nodes) {
            if (node.getIri() != null) {
              if (!validConcepts.get(node.getIri())) {
                invalid = true;
                node.setInvalid(true);
              }
              if (validationLevel == ValidationLevel.ECL) {
                if (!setRepository.isValidRangeForProperty(where.getIri(), node.getIri())) {
                  node.setInvalid(true);
                  invalid = true;
                }
              }
            }
          }
        }
      }
    }
    if (!invalid) {
      for (List<Where> wheres : Arrays.asList(where.getOr(), where.getAnd())) {
        if (wheres != null) {
          for (Where w : wheres) {
            if (isInvalidWhere(w, focusConcepts)) {
              invalid = true;
            }
          }
        }
      }
    }
    return invalid;
  }

  private void getFocusConcepts(Query query, Set<String> focusConcepts) {
    if (query.getIs() != null) {
      Node node = query.getIs();
      if (node.getIri() != null) {
        focusConcepts.add(node.getIri());
      }
    }
    for (List<Query> queries : Arrays.asList(query.getOr(), query.getAnd())) {
      if (queries != null) {
        for (Query m : queries) {
          getFocusConcepts(m, focusConcepts);
        }
      }
    }
  }

}
