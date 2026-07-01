package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.interfacemanager.model.EntityReferenceNode;
import org.endeavourhealth.interfacemanager.model.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityModelServiceTest {
  @Mock
  EntityRepository entityRepository;

  EntityService entityService;

  @BeforeEach
  void initMocks() {
    entityService = new EntityService(entityRepository);
  }

  @Test
  void getEntityPredicates_nullIriPredicates() {
    TTEntityJava entity = new TTEntityJava();
    when(entityRepository.getBundle(isNull(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTBundle actual = entityService.getBundle(null, null);
    assertNotNull(actual);
    assertNotNull(actual.getEntity());
  }

  @Test
  void getEntityPredicates_EmptyIri() {
    TTEntityJava entity = new TTEntityJava();
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
    TTIriRef ttIriRef = TTIriRefExtensionsKt.iri(new TTIriRef(), ).iri("http://endhealth.info/im#25451000252115").name("http://endhealth.info/im#25451000252115");
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
    TTArrayJava ttArrayJava = new TTArrayJava()
      .add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityRepository.getEntityTypes(any())).thenReturn(ttArrayJava);
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
    TTArrayJava ttArrayJava = new TTArrayJava().add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityRepository.getEntityTypes(any())).thenReturn(ttArrayJava);
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
    TTIriRef ttIriRef = TTIriRefExtensionsKt.iri(new TTIriRef(), )
      .iri("http://www.w3.org/2002/07/owl#Class")
      .name("Class");

    when(entityRepository.findAncestorsByType(any(), any(), any()))
      .thenReturn(Collections.singletonList(ttIriRef));

    List<TTIriRef> actual = entityService
      .isWhichType("http://endhealth.info/im#25451000252115",
        Collections.singletonList("http://endhealth.info/im#25451000252115"));

    assertNotNull(actual);
  }

  @Test
  void usages_NullIri() {
    List<TTEntityJava> actual = entityService.usages(null, null, null);

    assertNotNull(actual);
  }

  @Test
  void usages_EmptyIri() {
    List<TTEntityJava> actual = entityService.usages("", null, null);

    assertNotNull(actual);
  }

  @Test
  void usages_XMLContainsIri() {

    when(entityRepository.getByNamespace(any())).thenReturn(Stream.of("http://endhealth.info/im#25451000252115").collect(Collectors.toSet()));

    List<TTEntityJava> actual = entityService.usages("http://endhealth.info/im#25451000252115", 1, 10);

    assertNotNull(actual);
  }

  @Test
  void totalRecords_NullIri() {
    Integer actual = entityService.totalRecords(null);
    assertNotNull(actual);
  }

  @Test
  void totalRecords_NotNullIri() {
    when(entityRepository.getConceptUsagesCount(any())).thenReturn(1000);
    when(entityRepository.getByNamespace(any())).thenReturn(Stream.of("http://www.w3.org/2001/XMLSchema#string").collect(Collectors.toSet()));

    Integer actual = entityService.totalRecords("http://endhealth.info/im#25451000252115");
    assertEquals(1000, actual);
  }

  @Test
  void totalRecords_XMLIri() {
    when(entityRepository.getByNamespace(any())).thenReturn(Stream.of("http://www.w3.org/2001/XMLSchema#string").collect(Collectors.toSet()));

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
    TTEntityJava actual = entityService.getConceptShape(null);
    assertNull(actual);
  }

  @Test
  void getConceptShape_NotContainNodeShape() {
    TTEntityJava entity = new TTEntityJava("http://endhealth.info/im#25451000252115")
      .set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), new TTArrayJava()
        .add(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntityJava actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
    assertNull(actual);
  }

  @Test
  void getConceptShape_ContainsNodeShape() {
    TTEntityJava entity = new TTEntityJava("http://endhealth.info/im#25451000252115")
      .set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), new TTArrayJava()
        .add(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODESHAPE))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntityJava actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullIri() {
    List<String> configs = new ArrayList<>();
    TTEntityJava actual = entityService.getSummaryFromConfig(null, configs);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_EmptyIri() {
    List<String> configs = new ArrayList<>();
    TTEntityJava actual = entityService.getSummaryFromConfig("", configs);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullConfig() {
    TTEntityJava actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", null);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NotNullIri() {
    TTEntityJava entity = new TTEntityJava()
      .set(EnumUtils.asIri(ImVocab.IS_CHILD_OF), new TTArrayJava()
        .add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://endhealth.info/im#parent1", "Parent 1"))
        .add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntityJava actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", EnumUtils.asArrayList(ImVocab.
      IS_CHILD_OF));
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NullIri() {
    TTDocumentJava actual = entityService.getConceptList(null);
    assertNull(actual);
  }

  @Test
  void getConceptList_EmptyIri() {
    TTEntityJava entity = new TTEntityJava();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTDocumentJava actual = entityService.getConceptList(Collections.singletonList(""));
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NotNullIri() {
    TTEntityJava entity = new TTEntityJava();
    when(entityRepository.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));
    TTDocumentJava actual = entityService.getConceptList(Collections.singletonList("http://endhealth.info/im#25451000252115"));
    assertNotNull(actual);
  }
}
