package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TaskFilerException;
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
import org.endeavourhealth.imapi.model.workflow.EntityApproval;
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class FilerService {

  private final TTTransactionFiler documentFiler = new TTTransactionFiler();
  private final TTEntityFiler entityFiler = new TTEntityFilerRdf4j();
  private final TTEntityFiler entityProvFiler = entityFiler;
  private final ProvService provService = new ProvService();
  private final EntityService entityService = new EntityService();
  private final OpenSearchService openSearchService = new OpenSearchService();
  private final UserService userService = new UserService();
  private final WorkflowService workflowService = new WorkflowService();

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
    String[] parentPredicateArray = new String[]{IM.IS_A, IM.IS_CONTAINED_IN, RDFS.SUBCLASS_OF, IM.IS_SUBSET_OF};
    for (String parentPredicate : parentPredicateArray) {
      if (!hasParentPredicateAndIsValidIriRefList(entity, iri(parentPredicate))) return false;
    }
    return true;
  }

  private static Boolean hasParentPredicateAndIsValidIriRefList(TTEntity entity, TTIriRef predicate) {
    return !(null != entity.get(predicate) && !entity.get(predicate).isEmpty() && (!entity.get(predicate).getElements().stream().allMatch(TTValue::isIriRef)));
  }

  public void fileDocument(TTDocument document, String agentName, String taskId) {
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
      entityFiler.fileEntity(entity, graph.getIri());

      if (entity.isType(iri(IM.CONCEPT)))
        entityFiler.updateIsAs(entity, graph.getIri());

      if (entity.isType(iri(IM.VALUESET)))
        new SetMemberGenerator().generateMembers(entity.getIri(), graph.getIri());


      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntity provUsedEntity = fileUsedEntity(usedEntity);
      ProvActivity activity = fileProvActivity(entity, agent, provUsedEntity);

      writeDelta(entity, activity, provUsedEntity);
      fileOpenSearch(entity.getIri(), graph.getIri());
    } catch (Exception e) {
      throw new TTFilerException("Error filing entity", e);
    }
  }

  public void writeDelta(TTEntity entity, ProvActivity activity, TTEntity provUsedEntity) throws JsonProcessingException {
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
      if (entityService.iriExists(entity.getIri(), null)) {
        usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
      }
      ProvAgent agent = fileProvAgent(entity, agentName);
      TTEntity provUsedEntity = fileUsedEntity(usedEntity);
      fileProvActivity(entity, agent, provUsedEntity);
    }
  }

  private ProvAgent fileProvAgent(TTEntity entity, String agentName) throws TTFilerException {
    ProvAgent agent = provService.buildProvenanceAgent(entity, agentName);
    entityProvFiler.fileEntity(agent, GRAPH.PROV);
    return agent;
  }

  private TTEntity fileUsedEntity(TTEntity usedEntity) throws TTFilerException, JsonProcessingException {
    if (null == usedEntity)
      return null;

    TTEntity provUsedEntity = provService.buildUsedEntity(usedEntity);
    entityProvFiler.fileEntity(provUsedEntity, GRAPH.PROV);

    return provUsedEntity;
  }

  private ProvActivity fileProvActivity(TTEntity entity, ProvAgent agent, TTEntity provUsedEntity) throws TTFilerException {
    String provUsedIri = provUsedEntity == null ? null : provUsedEntity.getIri();

    ProvActivity activity = provService.buildProvenanceActivity(entity, agent, provUsedIri);
    entityProvFiler.fileEntity(activity, GRAPH.PROV);
    return activity;

  }

  private void fileOpenSearch(String iri, String graph) throws TTFilerException {
    try {
      EntityDocument doc = entityService.getOSDocument(iri, graph);
      openSearchService.fileDocument(doc);
    } catch (Exception e) {
      throw new TTFilerException("Unable to file opensearch", e);
    }
  }

  public TTEntity createEntity(EditRequest editRequest, String agentName, String graph) throws TTFilerException, JsonProcessingException, UserNotFoundException, TaskFilerException {
    if (null == graph) graph = GRAPH.DISCOVERY;
    isValid(editRequest.getEntity(), "Create", graph);
    TTIriRef graphIri = iri(graph);
    editRequest.getEntity().setCrud(iri(IM.ADD_QUADS)).setVersion(1);
    fileEntity(editRequest.getEntity(), graphIri, agentName, null);
    EntityApproval entityApproval = new EntityApproval();
    entityApproval
      .setEntityIri(iri(editRequest.getEntity().getIri()))
      .setApprovalType(ApprovalType.CREATE)
      .setCreatedBy(agentName)
      .setHostUrl(editRequest.getHostUrl())
      .setState(TaskState.TODO)
      .setType(TaskType.ENTITY_APPROVAL);
    workflowService.createEntityApproval(entityApproval);
    return editRequest.getEntity();
  }

  public TTEntity updateEntity(TTEntity entity, String agentName, String graph) throws TTFilerException, JsonProcessingException {
    if (null == graph) graph = GRAPH.DISCOVERY;
    isValid(entity, "Update", graph);
    TTIriRef graphIri = iri(graph);
    entity.setCrud(iri(IM.UPDATE_ALL));
    TTEntity usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
    entity.setVersion(usedEntity.getVersion() + 1);
    fileEntity(entity, graphIri, agentName, usedEntity);
    return entity;
  }

  public TTEntity updateEntityWithWorkflow(EditRequest editRequest, String agentName, HttpServletRequest request, String graph) throws TTFilerException, JsonProcessingException, UserNotFoundException, TaskFilerException {
    TTEntity entity = createEntity(editRequest, agentName, graph);
    EntityApproval entityApproval = new EntityApproval();
    entityApproval
      .setEntityIri(iri(editRequest.getEntity().getIri()))
      .setApprovalType(ApprovalType.EDIT)
      .setCreatedBy(agentName)
      .setHostUrl(editRequest.getHostUrl())
      .setState(TaskState.TODO)
      .setType(TaskType.ENTITY_APPROVAL);
    workflowService.updateEntityApproval(entityApproval, request);
    return entity;
  }

  public void isValid(TTEntity entity, String mode, String graph) throws TTFilerException, JsonProcessingException {
    ArrayList<String> errorMessages = new ArrayList<>();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      if (!isValidIri(entity)) errorMessages.add("Missing iri.");
      if ("Create".equals(mode) && entityService.iriExists(entity.getIri(), graph))
        errorMessages.add("Iri already exists.");
      if ("Update".equals(mode) && !entityService.iriExists(entity.getIri(), graph))
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

  public Boolean userCanFile(String agentId, TTIriRef iriRef) throws JsonProcessingException {
    List<String> orgList = userService.getUserOrganisations(agentId);
    return orgList != null && iriRef != null && orgList.contains(iriRef.getIri());
  }

}
