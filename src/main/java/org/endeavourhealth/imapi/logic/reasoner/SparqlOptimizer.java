package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SparqlOptimizer {
  private EntityRepository repo = new EntityRepository();
  private Set<String> setIris = new HashSet<>();
  private SetRepository setRepo = new SetRepository();


  public void optimizeQuery(Query query) throws QueryException{
    try {
      optimizeValueSets(query);
      optimizeProperties(query);
    } catch (Exception e) {
      throw new QueryException(e.getMessage(),e);
    }
  }

  private void optimizeProperties(Match match) throws QueryException, TTFilerException, JsonProcessingException {
    if (match.getIs()!=null){
      for (Node node:match.getIs()){
        if (node.getMatch()!=null)
          optimizeProperties(node.getMatch());
      }
    }
    if (match.getWhere()!=null){
      optimizeWhereProperties(match.getWhere());
    }
    if (match.getAnd()!=null){
      for (Match and:match.getAnd()){
        optimizeProperties(and);
      }
    }
    if (match.getOr()!=null){
      for (Match or:match.getOr()){
        optimizeProperties(or);
      }
    }
  }

  private void optimizeValueSets(Match match) throws QueryException, JsonProcessingException, TTFilerException {
    if (match.getIs()!=null){
      for (Node node:match.getIs()){
        if (node.getMatch()!=null)
          optimizeValueSets(node.getMatch());
      }
    }
    if (match.getWhere()!=null){
      optimizeWhereSets(match.getWhere());
    }
    if (match.getAnd()!=null){
      for (Match and:match.getAnd()){
        optimizeValueSets(and);
      }
    }
    if (match.getOr()!=null){
      for (Match or:match.getOr()){
        optimizeValueSets(or);
      }
    }
  }

  private void optimizeWhereSets(Where where) throws QueryException, JsonProcessingException, TTFilerException {
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        optimizeWhereSets(and);
      }
    }
    if (where.getOr() != null) {
      for (Where or : where.getOr()) {
        optimizeWhereSets(or);
      }
    }
    if (where.getIs() != null) {
      if (where.getIs().size() > 1) {
        String setIri = createConceptSet(where.getIs());
        List<Node> newIs = new ArrayList<>();
        newIs.add(Node.iri(setIri).setMemberOf(true));
        where.setIs(newIs);
        setIris.add(setIri);
      }
    }
  }

  private void optimizeWhereProperties(Where where) throws QueryException, TTFilerException, JsonProcessingException {
    if (where.getAnd() != null) {
      for (Where and : where.getAnd()) {
        optimizeWhereProperties(and);
      }
    }
    if (where.getOr() == null) return;
    Map<String, List<Where>> commonWheres = new HashMap<>();
    for (int i = 0; i < where.getOr().size(); i++) {
      Where or = where.getOr().get(i);
      if (or.getIri() != null) {
        if (or.getIs() != null) {
          if (or.getIs().size() == 1) {
            String setIri = or.getIs().getFirst().getIri();
            commonWheres.computeIfAbsent(setIri, k -> new ArrayList<>()).add(or);
          }
        }
      }
    }
    for (String setIri : commonWheres.keySet()) {
      if (commonWheres.get(setIri).size() > 1) {
        Where firstWhere = commonWheres.get(setIri).getFirst();
        firstWhere.addToPropertyList(new Node().setIri(firstWhere.getIri())
          .setDescendantsOrSelfOf(firstWhere.isDescendantsOrSelfOf())
          .setDescendantsOf(firstWhere.isDescendantsOf())
          .setAncestorsOf(firstWhere.isAncestorsOf())
          .setMemberOf(firstWhere.isMemberOf()));
        for (int i = 1; i < commonWheres.get(setIri).size(); i++) {
          Where nextWhere = commonWheres.get(setIri).get(i);
          firstWhere.addToPropertyList(new Node().setIri(nextWhere.getIri())
            .setDescendantsOrSelfOf(nextWhere.isDescendantsOrSelfOf())
            .setDescendantsOf(nextWhere.isDescendantsOf())
            .setAncestorsOf(nextWhere.isAncestorsOf())
            .setMemberOf(nextWhere.isMemberOf()));
          commonWheres.get(setIri).remove(i);
          where.getOr().remove(i);
          i--;
        }
      }
    }
    for (Where or : where.getOr()) {
      if (or.getPropertyList() != null)
        or.setPropertyList(inferPropertyList(or.getPropertyList()));
    }
  }

  private List<Node> inferPropertyList(List<Node> propertyList) throws QueryException {
    Query propertyQuery= new Query()
      .setIs(propertyList);
    Set<Concept>  conceptList= setRepo.getMembersFromDefinition(propertyQuery);
    return conceptList.stream()
      .map(c -> new Node().setIri(c.getIri()))
      .sorted(Comparator.comparing(Node::getIri))
      .toList();
  }


  private String createConceptSet(List<Node> is) throws JsonProcessingException, QueryException, TTFilerException {
    Query setQuery=new Query();
    setQuery.setIs(is);
    TTEntity setEntity= new TTEntity();
    String setIri= Namespace.IM+"ASET_"+getHash(is);
    if (setIris.contains(setIri)) return setIri;
    if (!repo.iriExists(setIri)) {
      TTDocument document= new TTDocument();
      setEntity
        .setIri(setIri)
        .addType(iri(IM.CONCEPT_SET))
        .set(IM.DEFINITION, TTLiteral.literal(setQuery));
      document.addEntity(setEntity);
      try (TTTransactionFiler filer= new TTTransactionFiler(Graph.IM)) {
        TTTransactionFiler.disableIm1Deltas();
        filer.fileDocument(document);
        TTTransactionFiler.enableIm1Deltas();
      }
    }
    return setIri;
  }

  public static String getHash(List<Node> is) throws QueryException {
    is.sort(Comparator.comparing(Node::getIri));
    try {

      String isString= new ObjectMapper().writeValueAsString(is);
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(isString.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (Exception e) {
      throw new QueryException(e.getMessage(),e);
    }
  }

}
