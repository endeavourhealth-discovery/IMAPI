package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.reasoner.SetExpander;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class FilerService {

  private final TTTransactionFiler documentFiler = new TTTransactionFiler();
  private final TTEntityFiler entityFiler = new TTEntityFilerRdf4j();
  private final TTEntityFiler entityProvFiler = entityFiler;
  private final ProvService provService = new ProvService();
  private final EntityService entityService = new EntityService();
  private final OpenSearchService openSearchService = new OpenSearchService();

  public void fileDocument(TTDocument document, String agentName, String taskId) throws TTFilerException, JsonProcessingException, QueryException {
    new Thread(() -> {
      try {
        documentFiler.fileDocument(document, taskId);
        fileProvDoc(document, agentName);
      } catch (TTFilerException | JsonProcessingException | QueryException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  public Integer getTaskProgress(String taskId) {
    return documentFiler.getFilingProgress(taskId);
  }

  public void fileEntity(TTEntity entity, TTIriRef graph, String agentName, TTEntity usedEntity) throws TTFilerException {
    try {
      entityFiler.fileEntity(entity, graph);

      if (entity.isType(iri(IM.CONCEPT)))
        entityFiler.updateIsAs(entity.getIri());

      if (entity.isType(iri(IM.VALUESET)))
        new SetExpander().expandSet(entity.getIri());


      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntity provUsedEntity = fileUsedEntity(usedEntity);
      ProvActivity activity = fileProvActivity(entity, agent, provUsedEntity);

      writeDelta(entity, activity, provUsedEntity);
      fileOpenSearch(entity.getIri());
    } catch (Exception e) {
      throw new TTFilerException("Error filing entity", e);
    }
  }

  public void writeDelta(TTEntity entity, ProvActivity activity, TTEntity provUsedEntity) throws Exception {
    TTDocument document = new TTDocument();
    document.addEntity(entity);
    document.addEntity(activity);
    if (null != provUsedEntity)
      document.addEntity(provUsedEntity);

    documentFiler.writeLog(document);
  }

  private void fileProvDoc(TTDocument document, String agentName) throws JsonProcessingException, TTFilerException {
    for (TTEntity entity : document.getEntities()) {
      TTEntity usedEntity = null;
      if (entityService.iriExists(entity.getIri())) {
        usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
      }
      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntity provUsedEntity = fileUsedEntity(usedEntity);
      fileProvActivity(entity, agent, provUsedEntity);
    }
  }

  private ProvAgent fileProvAgent(TTEntity entity, String agentName) throws TTFilerException {
    ProvAgent agent = provService.buildProvenanceAgent(entity, agentName);
    entityProvFiler.fileEntity(agent, iri(GRAPH.PROV));
    return agent;
  }

  private TTEntity fileUsedEntity(TTEntity usedEntity) throws TTFilerException, JsonProcessingException {
    if (null == usedEntity)
      return null;

    TTEntity provUsedEntity = provService.buildUsedEntity(usedEntity);
    entityProvFiler.fileEntity(provUsedEntity, iri(GRAPH.PROV));

    return provUsedEntity;
  }

  private ProvActivity fileProvActivity(TTEntity entity, ProvAgent agent, TTEntity provUsedEntity) throws TTFilerException {
    String provUsedIri = provUsedEntity == null ? null : provUsedEntity.getIri();

    ProvActivity activity = provService.buildProvenanceActivity(entity, agent, provUsedIri);
    entityProvFiler.fileEntity(activity, iri(GRAPH.PROV));
    return activity;

  }

  private void fileOpenSearch(String iri) throws TTFilerException {
    try {
      EntityDocument doc = entityService.getOSDocument(iri);
      openSearchService.fileDocument(doc);
    } catch (Exception e) {
      throw new TTFilerException("Unable to file opensearch", e);
    }
  }

  public TTEntity createEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
    isValid(entity, "Create");
    TTIriRef graph = iri(GRAPH.DISCOVERY);
    entity.setCrud(iri(IM.ADD_QUADS)).setVersion(1);
    fileEntity(entity, graph, agentName, null);
    return entity;
  }

  public TTEntity updateEntity(TTEntity entity, String agentName) throws TTFilerException, JsonProcessingException {
    isValid(entity, "Update");
    TTIriRef graph = iri(GRAPH.DISCOVERY);
    entity.setCrud(iri(IM.UPDATE_ALL));
    TTEntity usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
    entity.setVersion(usedEntity.getVersion() + 1);
    fileEntity(entity, graph, agentName, usedEntity);
    return entity;
  }

  public void isValid(TTEntity entity, String mode) throws TTFilerException, JsonProcessingException {
    ArrayList<String> errorMessages = new ArrayList();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      if (Boolean.TRUE.equals(!isValidIri(entity))) errorMessages.add("Missing iri.");
      if ("Create".equals(mode) && entityService.iriExists(entity.getIri())) errorMessages.add("Iri already exists.");
      if ("Update".equals(mode) && !entityService.iriExists(entity.getIri())) errorMessages.add("Iri doesn't exists.");
      if (Boolean.TRUE.equals(!isValidName(entity))) errorMessages.add("Name is invalid.");
      if (Boolean.TRUE.equals(!isValidType(entity))) errorMessages.add("Types are invalid.");
      if (Boolean.TRUE.equals(!isValidStatus(entity))) errorMessages.add("Status is invalid");
      if (Boolean.TRUE.equals(!hasParents(entity))) errorMessages.add("Parents are invalid");
      if (!errorMessages.isEmpty()) {
        String errorsAsString = String.join(",", errorMessages);
        throw new TTFilerException(mode + " entity errors: [" + errorsAsString + "] for entity " + om.writeValueAsString(entity));
      }
    }
  }

  private static Boolean isValidIri(TTEntity entity) {
    if (null == entity.getIri()) return false;
    return !"".equals(entity.getIri());
  }

  private static Boolean isValidName(TTEntity entity) {
    if (null == entity.getName()) return false;
    return !"".equals(entity.getName());
  }

  private static Boolean isValidType(TTEntity entity) {
    if (null == entity.getType()) return false;
    if (entity.getType().isEmpty()) return false;
    return entity.getType().getElements().stream().allMatch(TTValue::isIriRef);
  }

  private static Boolean isValidStatus(TTEntity entity) {
    if (null == entity.getStatus()) return false;
    return entity.getStatus().isIriRef();
  }

  private static Boolean hasParents(TTEntity entity) {
    if (null == entity.get(iri(IM.IS_A)) && null == entity.get(iri(IM.IS_CONTAINED_IN)) && null == entity.get(iri(RDFS.SUBCLASS_OF)) && null == entity.get(iri(IM.IS_SUBSET_OF)))
      return false;
    if (null != entity.get(iri(IM.IS_A)) && !entity.get(iri(IM.IS_A)).isEmpty()) {
      if (!entity.get(iri(IM.IS_A)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    if (null != entity.get(iri(IM.IS_CONTAINED_IN)) && !entity.get(iri(IM.IS_CONTAINED_IN)).isEmpty()) {
      if (!entity.get(iri(IM.IS_CONTAINED_IN)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    if (null != entity.get(iri(RDFS.SUBCLASS_OF)) && !entity.get(iri(RDFS.SUBCLASS_OF)).isEmpty()) {
      if (!entity.get(iri(RDFS.SUBCLASS_OF)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    if (null != entity.get(iri(IM.IS_SUBSET_OF)) && !entity.get(iri(IM.IS_SUBSET_OF)).isEmpty()) {
      if (!entity.get(iri(IM.IS_SUBSET_OF)).getElements().stream().allMatch(TTValue::isIriRef)) return false;
    }
    return true;
  }
}
