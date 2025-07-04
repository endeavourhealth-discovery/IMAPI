package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.*;

public class ECLQueryValidator {
  private Map<String,Boolean> validConcepts;
  private final SetRepository setRepository = new SetRepository();
  private ValidationLevel validationLevel;



  public ECLStatus validateQuery(Query query, ValidationLevel validationLevel, Graph graph) {
    this.validationLevel = validationLevel;
    Set<String> iris= IriCollector.collectIris(query);
    validConcepts= setRepository.getValidConcepts(iris, graph);
    query.setInvalid(isInvalidMatchWheres(query, graph));
    ECLStatus status = new ECLStatus();
    status.setValid(!query.isInvalid());
    if (query.isInvalid()) {
      status.setMessage("Unknown concepts in query");
    }
    return status;
  }

  private boolean isInvalidMatchWheres(Match match, Graph graph) {
    boolean invalid=false;
    if (match.getInstanceOf()!=null){
      for (Node node: match.getInstanceOf()){
        if (node.getIri()!=null) {
          if(!validConcepts.get(node.getIri())) {
            node.setInvalid(true);
            invalid = true;
          }
        }
      }
    }
    if (match.getWhere()!=null){
      Set<String> focusConcepts= new HashSet<>();
      if (validationLevel==ValidationLevel.ECL)
        getFocusConcepts(match,focusConcepts);
      if (isInvalidWhere(match.getWhere(),focusConcepts, graph)){
        invalid= true;
      }
    }
    for (List<Match> matches : Arrays.asList(match.getOr(),match.getAnd(),match.getNot())){
      if (matches!=null){
        for (Match m : matches){
          if (isInvalidMatchWheres(m, graph)){
            invalid=true;
          }
        }
      }
    }
    return invalid;
  }

  private boolean isInvalidWhere(Where where, Set<String> focusConcepts, Graph graph) {
    boolean invalid=false;
    if (where.getIri()!=null){
      if (!validConcepts.get(where.getIri())) {
        invalid= true;
        where.setInvalid(true);
      }
      if (validationLevel==ValidationLevel.ECL) {
        if (!setRepository.isValidPropertyForDomains(where.getIri(), focusConcepts, graph)) {
          where.setInvalid(true);
          invalid = true;
        }
      }
    }
    if (!invalid) {
      for (List<Node> nodes : Arrays.asList(where.getIs(), where.getNotIs())) {
        if (nodes != null) {
          for (Node node : nodes) {
            if (node.getIri() != null) {
              if (!validConcepts.get(node.getIri())) {
                invalid= true;
                node.setInvalid(true);
              }
              if (validationLevel == ValidationLevel.ECL) {
                if (!setRepository.isValidRangeForProperty(where.getIri(), node.getIri(), graph)) {
                  node.setInvalid(true);
                  invalid = true;
                }
              }
            }
          }
        }
      }
    }
    if (!invalid){
      for (List<Where> wheres: Arrays.asList(where.getOr(), where.getAnd())) {
        if (wheres != null) {
          for (Where w : wheres) {
            if (isInvalidWhere(w, focusConcepts, graph)) {
              invalid= true;
            }
          }
        }
      }
    }
    return invalid;
  }

  private void getFocusConcepts(Match match,Set<String> focusConcepts){
    if (match.getInstanceOf()!=null){
      for (Node node : match.getInstanceOf()){
        if (node.getIri()!=null){
          focusConcepts.add(node.getIri());
        }
      }
    }
    for (List<Match> matches : Arrays.asList(match.getOr(),match.getAnd(),match.getNot())){
      if (matches!=null){
        for (Match m : matches){
          getFocusConcepts(m,focusConcepts);
        }
      }
    }
  }

}
