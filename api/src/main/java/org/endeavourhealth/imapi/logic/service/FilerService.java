package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.logic.reasoner.SetExpander;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class FilerService {

  private final TTDocumentFiler documentFiler = new TTTransactionFiler();
  private final TTEntityFiler entityFiler = new TTEntityFilerRdf4j();
  private final TTEntityFiler entityProvFiler = entityFiler;
  private final ProvService provService = new ProvService();
  private final EntityService entityService = new EntityService();
  private final OpenSearchService openSearchService = new OpenSearchService();
  private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();

  public void fileDocument(TTDocument document, String agentName, String taskId) throws TTFilerException, JsonProcessingException, QueryException {
    System.out.println(taskId);
    progressMap.put(taskId, 0);
    System.out.println(progressMap.size());
    System.out.println(progressMap.get(taskId));
    new Thread(() -> {
      try {
        documentFiler.fileDocument(document, taskId, progressMap);
//        fileProvDoc(document, agentName);
      } catch (TTFilerException | JsonProcessingException | QueryException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  public int getTaskProgress(String taskId) {
    System.out.println(taskId);
    System.out.println(progressMap.size());
    System.out.println(progressMap.get(taskId));
    return progressMap.getOrDefault(taskId, -1);
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
        usedEntity = entityService.getFullEntity(entity.getIri()).getEntity();
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
}
