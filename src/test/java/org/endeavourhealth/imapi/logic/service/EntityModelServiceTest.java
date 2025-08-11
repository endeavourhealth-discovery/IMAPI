package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

    TTBundle actual = entityService.getBundle(null, null, List.of(Graph.IM));
    assertNotNull(actual);
    assertNotNull(actual.getEntity());
  }

  @Test
  void getEntityPredicates_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTBundle actual = entityService.getBundle("", null, List.of(Graph.IM));
    assertNotNull(actual);
    assertNotNull(actual.getEntity());
  }

  @Test
  void getEntityReference_NullIri() {
    TTIriRef actual = entityService.getEntityReference(null, List.of(Graph.IM));

    assertNull(actual);

  }

  @Test
  void getEntityReference_NullEntity() {
    when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115", List.of(Graph.IM))).thenReturn(null);
    TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115", List.of(Graph.IM));

    assertNull(actual);

  }

  @Test
  void getEntityReference_NotNullEntity() {
    TTIriRef ttIriRef = new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("http://endhealth.info/im#25451000252115");
    when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115", List.of(Graph.IM))).thenReturn(ttIriRef);
    TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115", List.of(Graph.IM));

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_NullIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateChildren(null, null, 1, 10, true, List.of(Graph.IM));

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_EmptyIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateChildren("", null, 1, 10, true, List.of(Graph.IM));

    assertNotNull(actual);

  }

  @Test
  void getImmediateChildren_NullIndexSize() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateChildren("http://endhealth.info/im#25451000252115", null, null, null, true, List.of(Graph.IM));

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
      0, 20, true, List.of(Graph.IM)))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    List<EntityReferenceNode> actual = entityService.getImmediateChildren
      ("http://snomed.info/sct#62014003", null, 1, 20, true, List.of(Graph.IM));
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
      0, 20, false, List.of(Graph.IM)))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    List<EntityReferenceNode> actual = entityService.getImmediateChildren
      ("http://snomed.info/sct#62014003", null, 1, 20, false, List.of(Graph.IM));

    assertNotNull(actual);

  }

  @Test
  void getImmediateParents_NullIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateParents(null, null, 1, 10, true, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void getImmediateParents_EmptyIri() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateParents("", null, 1, 10, true, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void getImmediateParents_NullIndexSize() {
    List<EntityReferenceNode> actual = entityService
      .getImmediateParents("http://endhealth.info/im#25451000252115", null, null, null, true, List.of(Graph.IM));

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
      0, 20, true, List.of(Graph.IM)))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    TTArray ttArray = new TTArray()
      .add(iri("http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityRepository.getEntityTypes(any(), any())).thenReturn(ttArray);
    List<EntityReferenceNode> actual = entityService.getImmediateParents
      ("http://endhealth.info/im#25451000252115", null, 1, 20, true, List.of(Graph.IM));

    assertNotNull(actual);

  }

  @Test
  void getImmediateParents_NotNullIriAndInactiveFalse() {
    EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
      .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
      .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
    when(entityRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
      0, 10, false, List.of(Graph.IM)))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityRepository.getEntityTypes(any(), any())).thenReturn(ttArray);
    List<EntityReferenceNode> actual = entityService.getImmediateParents
      ("http://endhealth.info/im#25451000252115", null, 1, 10, false, List.of(Graph.IM));

    assertNotNull(actual);

  }

  @Test
  void isWhichType_NullIri() {
    List<TTIriRef> actual = entityService
      .isWhichType(null, Arrays.asList("A", "B"), List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_EmptyIri() {
    List<TTIriRef> actual = entityService
      .isWhichType("", Arrays.asList("A", "B"), List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_EmptyCandidates() {
    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115", Collections.emptyList(), List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_NullCandidates() {
    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115", null, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_NullIriAndCandidates() {
    List<TTIriRef> actual = entityService
      .isWhichType(null, null, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void isWhichType_NotNullIriAndCandidates() {
    TTIriRef ttIriRef = new TTIriRef()
      .setIri("http://www.w3.org/2002/07/owl#Class")
      .setName("Class");

    when(entityRepository.findAncestorsByType(any(), any(), any(), any()))
      .thenReturn(Collections.singletonList(ttIriRef));

    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115",
        Collections.singletonList("http://endhealth.info/im#25451000252115"), List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void usages_NullIri() {
    List<TTEntity> actual = entityService.usages(null, null, null, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void usages_EmptyIri() {
    List<TTEntity> actual = entityService.usages("", null, null, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void usages_XMLContainsIri() {

    when(entityRepository.getByNamespace(any(), any())).thenReturn(Stream.of("http://endhealth.info/im#25451000252115").collect(Collectors.toSet()));

    List<TTEntity> actual = entityService.usages("http://endhealth.info/im#25451000252115", 1, 10, List.of(Graph.IM));

    assertNotNull(actual);
  }

  @Test
  void totalRecords_NullIri() {
    Integer actual = entityService.totalRecords(null, List.of(Graph.IM));
    assertNotNull(actual);
  }

  @Test
  void totalRecords_NotNullIri() {
    when(entityRepository.getConceptUsagesCount(any(), any())).thenReturn(1000);
    when(entityRepository.getByNamespace(any(), any())).thenReturn(Stream.of("http://www.w3.org/2001/XMLSchema#string").collect(Collectors.toSet()));

    Integer actual = entityService.totalRecords("http://endhealth.info/im#25451000252115", List.of(Graph.IM));
    assertEquals(1000, actual);
  }

  @Test
  void totalRecords_XMLIri() {
    when(entityRepository.getByNamespace(any(), any())).thenReturn(Stream.of("http://www.w3.org/2001/XMLSchema#string").collect(Collectors.toSet()));

    Integer actual = entityService.totalRecords("http://www.w3.org/2001/XMLSchema#string", List.of(Graph.IM));
    assertEquals(0, actual);
  }

  @Test
  void getSummary_NullIri() {
    SearchResultSummary actual = entityService.getSummary(null, List.of(Graph.IM));
    assertNull(actual);
  }

  @Test
  void getSummary_NotNullIri() {
    SearchResultSummary summary = new SearchResultSummary();
    when(entityRepository.getEntitySummaryByIri(any(), any())).thenReturn(summary);
    SearchResultSummary actual = entityService.getSummary("anyIri", List.of(Graph.IM));
    assertNotNull(actual);
  }

  @Test
  void getConceptShape_NullIri() {
    TTEntity actual = entityService.getConceptShape(null, List.of(Graph.IM));
    assertNull(actual);
  }

  @Test
  void getConceptShape_NotContainNodeShape() {
    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(RDF.TYPE), new TTArray()
        .add(TTIriRef.iri(IM.CONCEPT))
      );
    when(entityRepository.getBundle(any(), anySet(), anyList())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115", List.of(Graph.IM));
    assertNull(actual);
  }

  @Test
  void getConceptShape_ContainsNodeShape() {
    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(RDF.TYPE), new TTArray()
        .add(TTIriRef.iri(SHACL.NODESHAPE))
      );
    when(entityRepository.getBundle(any(), anySet(), anyList())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115", List.of(Graph.IM));
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullIri() {
    List<String> configs = new ArrayList<>();
    TTEntity actual = entityService.getSummaryFromConfig(null, configs, List.of(Graph.IM));
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_EmptyIri() {
    List<String> configs = new ArrayList<>();
    TTEntity actual = entityService.getSummaryFromConfig("", configs, List.of(Graph.IM));
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullConfig() {
    TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", null, List.of(Graph.IM));
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NotNullIri() {
    TTEntity entity = new TTEntity()
      .set(IM.IS_CHILD_OF.asIri(), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet(), anyList())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", asArrayList(IM.IS_CHILD_OF), null);
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NullIri() {
    TTDocument actual = entityService.getConceptList(null, Graph.IM);
    assertNull(actual);
  }

  @Test
  void getConceptList_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTDocument actual = entityService.getConceptList(Collections.singletonList(""), Graph.IM);
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NotNullIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));
    TTDocument actual = entityService.getConceptList(Collections.singletonList("http://endhealth.info/im#25451000252115"), Graph.IM);
    assertNotNull(actual);
  }
}
