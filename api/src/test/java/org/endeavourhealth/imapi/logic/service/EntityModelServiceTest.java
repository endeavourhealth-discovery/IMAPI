package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EntityModelServiceTest {
  @InjectMocks
  EntityService entityService = new EntityService();

  @Mock
  EntityRepository entityRepository;

  @Mock
  ConfigManager configManager = new ConfigManager();

  @Test
  void getEntityPredicates_nullIriPredicates() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(isNull(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTBundle actual = entityService.getBundle(null, null);
    assertNotNull(actual);
    assertNotNull(actual.getEntity());
  }

  @Test
  void getEntityPredicates_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTBundle actual = entityService.getBundle("", null);
    assertNotNull(actual);
    assertNotNull(actual.getEntity());
  }

  @Test
  void getEntityReference_NullIri() {
    TTIriRef actual = entityService.getEntityReference(null);

    assertNull(actual);

  }

  @Test
  void getEntityReference_NullEntity() {
    when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
    TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115");

    assertNull(actual);

  }

  @Test
  void getEntityReference_NotNullEntity() {
    TTIriRef ttIriRef = new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("http://endhealth.info/im#25451000252115");
    when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttIriRef);
    TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115");

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_NullIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateChildren(null, null, 1, 10, true);

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_EmptyIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateChildren("", null, 1, 10, true);

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_NullIndexSize() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateChildren("http://endhealth.info/im#25451000252115", null, null, null, true);

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_NotNullIriAndInactiveTrue() {

    EntityReferenceNode entityReferenceNode = new EntityReferenceNode("http://snomed.info/sct#62014003")
      .setChildren(Collections.singletonList(
        new EntityReferenceNode("http://snomed.info/sct#62014003",
          "Adverse reaction to Amlodipine Besilate")))
      .setParents(Collections.singletonList(
        new EntityReferenceNode("http://snomed.info/sct#62014003",
          "Adverse reaction to Amlodipine Besilate")));
    when(entityRepository.findImmediateChildrenByIri("http://snomed.info/sct#62014003", null,
      0, 20, true))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    List<EntityReferenceNode> actual = entityService.getImmediateChildren
      ("http://snomed.info/sct#62014003", null, 1, 20, true);
    assertNotNull(actual);
  }

  @Test
  void getImmediateChildren_NotNullIriAndInactiveFalse() {
    EntityReferenceNode entityReferenceNode = new EntityReferenceNode("http://snomed.info/sct#62014003")
      .setChildren(Collections.singletonList(
        new EntityReferenceNode("http://snomed.info/sct#62014003",
          "Adverse reaction to Amlodipine Besilate")))
      .setParents(Collections.singletonList(
        new EntityReferenceNode("http://snomed.info/sct#62014003",
          "Adverse reaction to Amlodipine Besilate")));
    when(entityRepository.findImmediateChildrenByIri("http://snomed.info/sct#62014003", null,
      0, 20, false))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    List<EntityReferenceNode> actual = entityService.getImmediateChildren
      ("http://snomed.info/sct#62014003", null, 1, 20, false);

    assertNotNull(actual);

  }

  @Test
  void getImmediateParents_NullIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateParents(null, null, 1, 10, true);

    assertNotNull(actual);
  }

  @Test
  void getImmediateParents_EmptyIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateParents("", null, 1, 10, true);

    assertNotNull(actual);
  }

  @Test
  void getImmediateParents_NullIndexSize() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateParents("http://endhealth.info/im#25451000252115", null, null, null, true);

    assertNotNull(actual);
  }

  @Test
  void getImmediateParents_NotNullIriAndInactiveTrue() {

    EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
      .setChildren(Collections.singletonList(
        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
          "Adverse reaction to Amlodipine Besilate")))
      .setParents(Collections.singletonList(
        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
          "Adverse reaction to Amlodipine Besilate")));
    when(entityRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
      0, 20, true))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    TTArray ttArray = new TTArray()
      .add(iri("http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityRepository.getEntityTypes(any())).thenReturn(ttArray);
    List<EntityReferenceNode> actual = entityService.getImmediateParents
      ("http://endhealth.info/im#25451000252115", null, 1, 20, true);

    assertNotNull(actual);

  }

  @Test
  void getImmediateParents_NotNullIriAndInactiveFalse() {
    EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
      .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
      .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
    when(entityRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
      0, 10, false))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityRepository.getEntityTypes(any())).thenReturn(ttArray);
    List<EntityReferenceNode> actual = entityService.getImmediateParents
      ("http://endhealth.info/im#25451000252115", null, 1, 10, false);

    assertNotNull(actual);

  }

  @Test
  void isWhichType_NullIri() {
    List<TTIriRef> actual = entityService
      .isWhichType(null, Arrays.asList("A", "B"));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_EmptyIri() {
    List<TTIriRef> actual = entityService
      .isWhichType("", Arrays.asList("A", "B"));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_EmptyCandidates() {
    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115", Collections.emptyList());

    assertNotNull(actual);
  }

  @Test
  void isWhichType_NullCandidates() {
    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115", null);

    assertNotNull(actual);
  }

  @Test
  void isWhichType_NullIriAndCandidates() {
    List<TTIriRef> actual = entityService
      .isWhichType(null, null);

    assertNotNull(actual);
  }

  @Test
  void isWhichType_NotNullIriAndCandidates() {
    TTIriRef ttIriRef = new TTIriRef()
      .setIri("http://www.w3.org/2002/07/owl#Class")
      .setName("Class");

    when(entityRepository.findAncestorsByType(any(), any(), any()))
      .thenReturn(Collections.singletonList(ttIriRef));

    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115",
        Collections.singletonList("http://endhealth.info/im#25451000252115"));

    assertNotNull(actual);
  }

  @Test
  void usages_NullIri() throws JsonProcessingException {
    List<TTEntity> actual = entityService.usages(null, null, null);

    assertNotNull(actual);
  }

  @Test
  void usages_EmptyIri() throws JsonProcessingException {
    List<TTEntity> actual = entityService.usages("", null, null);

    assertNotNull(actual);
  }

  @Test
  void usages_XMLContainsIri() throws JsonProcessingException {

    when(configManager.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://endhealth.info/im#25451000252115"));

    List<TTEntity> actual = entityService.usages("http://endhealth.info/im#25451000252115", 1, 10);

    assertNotNull(actual);
  }

  @Test
  void totalRecords_NullIri() throws JsonProcessingException {
    Integer actual = entityService.totalRecords(null);
    assertNotNull(actual);
  }

  @Test
  void totalRecords_NotNullIri() throws JsonProcessingException {
    when(entityRepository.getConceptUsagesCount(any())).thenReturn(1000);
    when(configManager.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

    Integer actual = entityService.totalRecords("http://endhealth.info/im#25451000252115");
    assertEquals(1000, actual);
  }

  @Test
  void totalRecords_XMLIri() throws JsonProcessingException {
    when(configManager.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

    Integer actual = entityService.totalRecords("http://www.w3.org/2001/XMLSchema#string");
    assertEquals(0, actual);
  }

  @Test
  void getSummary_NullIri() {
    SearchResultSummary actual = entityService.getSummary(null);
    assertNull(actual);
  }

  @Test
  void getSummary_NotNullIri() {
    SearchResultSummary summary = new SearchResultSummary();
    when(entityRepository.getEntitySummaryByIri(any())).thenReturn(summary);
    SearchResultSummary actual = entityService.getSummary("anyIri");
    assertNotNull(actual);
  }

  @Test
  void getConceptShape_NullIri() {
    TTEntity actual = entityService.getConceptShape(null);
    assertNull(actual);
  }

  @Test
  void getConceptShape_NotContainNodeShape() {
    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(RDF.TYPE), new TTArray()
        .add(TTIriRef.iri(IM.CONCEPT))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
    assertNull(actual);
  }

  @Test
  void getConceptShape_ContainsNodeShape() {
    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(RDF.TYPE), new TTArray()
        .add(TTIriRef.iri(SHACL.NODESHAPE))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullIri() {
    List<String> configs = new ArrayList<>();
    TTEntity actual = entityService.getSummaryFromConfig(null, configs);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_EmptyIri() {
    List<String> configs = new ArrayList<>();
    TTEntity actual = entityService.getSummaryFromConfig("", configs);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullConfig() {
    TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", null);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NotNullIri() {
    List<String> configs = new ArrayList<>();
    configs.add(IM.IS_CHILD_OF);

    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", configs);
    assertNotNull(actual);
  }

  @Test
  void getNamespaces_() {
    List<Namespace> namespace = new ArrayList<>();
    when(entityRepository.findNamespaces()).thenReturn(namespace);
    List<Namespace> actual = entityService.getNamespaces();
    assertNotNull(actual);

  }

  @Test
  void getInferredBundle_NullIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(isNull(), anySet(), anyBoolean())).thenReturn(new TTBundle().setEntity(entity));
    TTBundle actual = entityService.getInferredBundle(null);
    assertNotNull(actual);
  }

  @Test
  void getInferredBundle_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), anySet(), anyBoolean())).thenReturn(new TTBundle().setEntity(entity));
    TTBundle actual = entityService.getInferredBundle("");
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NullIri() {
    TTDocument actual = entityService.getConceptList(null);
    assertNull(actual);
  }

  @Test
  void getConceptList_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTDocument actual = entityService.getConceptList(Collections.singletonList(""));
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NotNullIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));
    List<Namespace> namespaces = new ArrayList<>();
    namespaces.add(new Namespace("http://endhealth.info/im#25451000252115", "", ""));
    when(entityRepository.findNamespaces()).thenReturn(namespaces);
    TTDocument actual = entityService.getConceptList(Collections.singletonList("http://endhealth.info/im#25451000252115"));
    assertNotNull(actual);
  }
}
