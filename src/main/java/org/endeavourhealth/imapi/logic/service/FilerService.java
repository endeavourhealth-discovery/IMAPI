package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.dataaccess.databases.ProvDB;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.reasoner.SetBinder;
import org.endeavourhealth.imapi.logic.reasoner.SetMemberGenerator;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.tripletree.TTDocumentJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;
import org.springframework.stereotype.Component;

import java.lang.Exception;
import java.util.ArrayList;

@Component
public class FilerService {

  private final ProvService provService;
  private final EntityService entityService;
  private final OpenSearchService openSearchService;
  private final TTEntityFiler entityProvFiler;
  private final GraphVocab insertGraph = GraphVocab.
    IM;
  private TTTransactionFiler documentFiler;
  private TTEntityFiler entityFiler;
  private IMDB imdb;

  public FilerService() {
    ProvDB provDB = ProvDB.getConnection();
    entityProvFiler = new TTEntityFilerRdf4j(provDB, GraphVocab.PROV);
    provService = new ProvService();
    entityService = new EntityService();
    openSearchService = new OpenSearchService();
  }

  private static boolean isValidIri(TTEntityJava entity) {
    if (null == entity.getIri()) return false;
    return !entity.getIri().isEmpty();
  }

  private static boolean isValidName(TTEntityJava entity) {
    if (null == entity.getName()) return false;
    return !"".equals(entity.getName());
  }

  private static boolean isValidType(TTEntityJava entity) {
    if (null == entity.getType()) return false;
    if (entity.getType().isEmpty()) return false;
    return entity.getType().getElements().stream().allMatch(TTValueJava::isIriRef);
  }

  private static boolean isValidStatus(TTEntityJava entity) {
    if (null == entity.getStatus()) return false;
    return entity.getStatus().isIriRef();
  }

  private static boolean hasParents(TTEntityJava entity) {
    String[] parentPredicateArray = EnumUtils.asArray(ImVocab.IS_A, ImVocab.IS_CONTAINED_IN, RdfsVocab.SUBCLASS_OF, ImVocab.IS_SUBSET_OF);
    for (String parentPredicate : parentPredicateArray) {
      if (!hasParentPredicateAndIsValidIriRefList(entity, TTIriRefExtensionsKt.iri(new TTIriRef(), parentPredicate)))
        return false;
    }
    return true;
  }

  private static boolean hasParentPredicateAndIsValidIriRefList(TTEntityJava entity, TTIriRef predicate) {
    return !(null != entity.get(predicate) && !entity.get(predicate).isEmpty() && (!entity.get(predicate).getElements().stream().allMatch(TTValueJava::isIriRef)));
  }

  private void setupDocumentFiler(GraphVocab insertGraph) {
    if (null == this.documentFiler) {
      this.documentFiler = new TTTransactionFiler(insertGraph);
    }
  }

  private void setupEntityFiler() {
    setupIMDB();
    if (null == this.entityFiler) {
      this.entityFiler = new TTEntityFilerRdf4j(imdb, insertGraph);
    }
  }

  private void setupIMDB() {
    if (null == this.imdb) {
      this.imdb = IMDB.getConnection();
    }
  }

  public void fileDocument(TTDocumentJava document, String agentName, String taskId) {
    new Thread(() -> {
      try {
        setupDocumentFiler(insertGraph);
        documentFiler.fileDocument(document, taskId);
        fileProvDoc(document, agentName);
      } catch (TTFilerException | JsonProcessingException | QueryException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  public Integer getTaskProgress(String taskId) {
    setupDocumentFiler(GraphVocab.IM);
    return documentFiler.getFilingProgress(taskId);
  }

  public void fileEntity(TTEntityJava entity, String agentName, TTEntityJava usedEntity) throws TTFilerException {
    try {
      setupEntityFiler();
      entityFiler.fileEntity(entity);

      entityFiler.updateIsAs(entity.getIri());

      if (entity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.VALUE_SET)) || entity.isType((TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT_SET)))) {
        new SetMemberGenerator().generateMembers(entity.getIri(), insertGraph);
        new SetBinder().bindSet(entity.getIri(), insertGraph);
      }


      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntityJava provUsedEntity = fileUsedEntity(usedEntity);
      ProvActivity activity = fileProvActivity(entity, agent, provUsedEntity);

      writeDelta(entity, activity, provUsedEntity);
      fileOpenSearch(entity.getIri());
    } catch (Exception e) {
      throw new TTFilerException("Error filing entity: " + e.getMessage(), e);
    }
  }

  public void writeDelta(TTEntityJava entity, ProvActivity activity, TTEntityJava provUsedEntity) throws JsonProcessingException {
    TTDocumentJava document = new TTDocumentJava();
    document.addEntity(entity);
    document.addEntity(activity);
    if (null != provUsedEntity)
      document.addEntity(provUsedEntity);

    setupDocumentFiler(insertGraph);
    documentFiler.writeLog(document);
  }

  private void fileProvDoc(TTDocumentJava document, String agentName) throws JsonProcessingException, TTFilerException {
    for (TTEntityJava entity : document.getEntities()) {
      TTEntityJava usedEntity = null;
      if (entityService.iriExists(entity.getIri())) {
        usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
      }
      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntityJava provUsedEntity = fileUsedEntity(usedEntity);
      fileProvActivity(entity, agent, provUsedEntity);
    }
  }

  private ProvAgent fileProvAgent(TTEntityJava entity, String agentName) throws TTFilerException {
    ProvAgent agent = provService.buildProvenanceAgent(entity, agentName);
    entityProvFiler.fileEntity(agent);
    return agent;
  }

  private TTEntityJava fileUsedEntity(TTEntityJava usedEntity) throws TTFilerException, JsonProcessingException {
    if (null == usedEntity)
      return null;

    TTEntityJava provUsedEntity = provService.buildUsedEntity(usedEntity);
    entityProvFiler.fileEntity(provUsedEntity);

    return provUsedEntity;
  }

  private ProvActivity fileProvActivity(TTEntityJava entity, ProvAgent agent, TTEntityJava provUsedEntity) throws TTFilerException {
    String provUsedIri = provUsedEntity == null ? null : provUsedEntity.getIri();

    ProvActivity activity = provService.buildProvenanceActivity(entity, agent, provUsedIri);
    entityProvFiler.fileEntity(activity);
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

  public TTEntityJava createEntity(EditRequest editRequest, String agentName) throws TTFilerException, JsonProcessingException {
    isValid(editRequest.getEntity(), "Create");
    editRequest.getEntity().setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ADD_QUADS)).setVersion(1);
    fileEntity(editRequest.getEntity(), agentName, null);
    return editRequest.getEntity();
  }

  public TTEntityJava updateEntity(TTEntityJava entity, String agentName) throws TTFilerException, JsonProcessingException {
    isValid(entity, "Update");
    entity.setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.REPLACE_ALL_PREDICATES));
    TTEntityJava usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
    entity.setVersion(usedEntity.getVersion() + 1);
    fileEntity(entity, agentName, usedEntity);
    return entity;
  }

  public void isValid(TTEntityJava entity, String mode) throws TTFilerException, JsonProcessingException {
    ArrayList<String> errorMessages = new ArrayList<>();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      if (!isValidIri(entity)) errorMessages.add("Missing iri.");
      if ("Create".equals(mode) && entityService.iriExists(entity.getIri()))
        errorMessages.add("Iri already exists.");
      if ("Update".equals(mode) && !entityService.iriExists(entity.getIri()))
        errorMessages.add("Iri doesn't exists.");
      if (!isValidName(entity)) errorMessages.add("Name is invalid.");
      if (!isValidType(entity)) errorMessages.add("Types are invalid.");
      if (!isValidStatus(entity)) errorMessages.add("Status is invalid");
      if (!hasParents(entity)) errorMessages.add("Parents are invalid");
      if (!errorMessages.isEmpty()) {
        String errorsAsString = String.join(",", errorMessages);
        throw new TTFilerException(mode + " entity errors: [" + errorsAsString + "] for entity " + om.writeValueAsString(entity));
      }
    }
  }

  public boolean userCanFile(User user, GraphVocab graph) throws JsonProcessingException {
    return graph != null && user.getNamespaces().stream().anyMatch(o -> o.getIri().equals(graph.toString()));
  }

}
