package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.dataaccess.databases.ProvDB;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.reasoner.SetMemberGenerator;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EditRequest;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArray;

@Component
public class FilerService {

  private ProvService provService;
  private EntityService entityService;
  private OpenSearchService openSearchService;
  private UserService userService;
  private TTTransactionFiler documentFiler;
  private TTEntityFiler entityFiler;
  private TTEntityFiler entityProvFiler;
  private IMDB imdb;

  public FilerService() {
    ProvDB provDB = ProvDB.getConnection();
    entityProvFiler = new TTEntityFilerRdf4j(provDB, Graph.PROV);
    provService = new ProvService();
    entityService = new EntityService();
    openSearchService = new OpenSearchService();
    userService = new UserService();
  }

  private static boolean isValidIri(TTEntity entity) {
    if (null == entity.getIri()) return false;
    return !entity.getIri().isEmpty();
  }

  private static boolean isValidName(TTEntity entity) {
    if (null == entity.getName()) return false;
    return !"".equals(entity.getName());
  }

  private static boolean isValidType(TTEntity entity) {
    if (null == entity.getType()) return false;
    if (entity.getType().isEmpty()) return false;
    return entity.getType().getElements().stream().allMatch(TTValue::isIriRef);
  }

  private static boolean isValidStatus(TTEntity entity) {
    if (null == entity.getStatus()) return false;
    return entity.getStatus().isIriRef();
  }

  private static boolean hasParents(TTEntity entity) {
    String[] parentPredicateArray = asArray(IM.IS_A, IM.IS_CONTAINED_IN, RDFS.SUBCLASS_OF, IM.IS_SUBSET_OF);
    for (String parentPredicate : parentPredicateArray) {
      if (!hasParentPredicateAndIsValidIriRefList(entity, iri(parentPredicate))) return false;
    }
    return true;
  }

  private static boolean hasParentPredicateAndIsValidIriRefList(TTEntity entity, TTIriRef predicate) {
    return !(null != entity.get(predicate) && !entity.get(predicate).isEmpty() && (!entity.get(predicate).getElements().stream().allMatch(TTValue::isIriRef)));
  }

  private void setupDocumentFiler(Graph insertGraph) {
    if (null == this.documentFiler) {
      this.documentFiler = new TTTransactionFiler(insertGraph);
    }
  }

  private void setupEntityFiler(Graph insertGraph) {
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

  public void fileDocument(TTDocument document, String agentName, String taskId, Graph insertGraph) {
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
    setupDocumentFiler(Graph.IM);
    return documentFiler.getFilingProgress(taskId);
  }

  public void fileEntity(TTEntity entity, String agentName, TTEntity usedEntity, Graph insertGraph) throws TTFilerException {
    try {
      setupEntityFiler(insertGraph);
      entityFiler.fileEntity(entity);

      if (entity.isType(iri(IM.CONCEPT)))
        entityFiler.updateIsAs(entity);

      if (entity.isType(iri(IM.VALUESET)))
        new SetMemberGenerator().generateMembers(entity.getIri(), insertGraph);


      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntity provUsedEntity = fileUsedEntity(usedEntity);
      ProvActivity activity = fileProvActivity(entity, agent, provUsedEntity);

      writeDelta(entity, activity, provUsedEntity, insertGraph);
      fileOpenSearch(entity.getIri());
    } catch (Exception e) {
      throw new TTFilerException("Error filing entity", e);
    }
  }

  public void writeDelta(TTEntity entity, ProvActivity activity, TTEntity provUsedEntity, Graph insertGraph) throws JsonProcessingException {
    TTDocument document = new TTDocument();
    document.addEntity(entity);
    document.addEntity(activity);
    if (null != provUsedEntity)
      document.addEntity(provUsedEntity);

    setupDocumentFiler(insertGraph);
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
    entityProvFiler.fileEntity(agent);
    return agent;
  }

  private TTEntity fileUsedEntity(TTEntity usedEntity) throws TTFilerException, JsonProcessingException {
    if (null == usedEntity)
      return null;

    TTEntity provUsedEntity = provService.buildUsedEntity(usedEntity);
    entityProvFiler.fileEntity(provUsedEntity);

    return provUsedEntity;
  }

  private ProvActivity fileProvActivity(TTEntity entity, ProvAgent agent, TTEntity provUsedEntity) throws TTFilerException {
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

  public TTEntity createEntity(EditRequest editRequest, String agentName, Graph insertGraph) throws TTFilerException, JsonProcessingException {
    isValid(editRequest.getEntity(), "Create");
    editRequest.getEntity().setCrud(iri(IM.ADD_QUADS)).setVersion(1);
    fileEntity(editRequest.getEntity(), agentName, null, insertGraph);
    return editRequest.getEntity();
  }

  public TTEntity updateEntity(TTEntity entity, String agentName, Graph updateGraph) throws TTFilerException, JsonProcessingException {
    isValid(entity, "Update");
    entity.setCrud(iri(IM.REPLACE_ALL_PREDICATES));
    TTEntity usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
    entity.setVersion(usedEntity.getVersion() + 1);
    fileEntity(entity, agentName, usedEntity, updateGraph);
    return entity;
  }

  public void isValid(TTEntity entity, String mode) throws TTFilerException, JsonProcessingException {
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

  public boolean userCanFile(String agentId, Graph graph) throws JsonProcessingException {
    List<String> orgList = userService.getUserOrganisations(agentId);
    return orgList != null && graph != null && orgList.contains(graph.toString());
  }

}
