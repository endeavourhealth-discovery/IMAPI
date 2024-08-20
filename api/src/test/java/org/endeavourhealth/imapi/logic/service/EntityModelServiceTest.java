package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.DownloadParams;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EntityModelServiceTest {
  @InjectMocks
  EntityService entityService;

  @Mock
  EntityRepository entityRepository;

  @Mock
  EntityRepository2 entityRepository2;

  @Mock
  EntityTripleRepository entityTripleRepository;

  @Mock
  EntityTctRepository entityTctRepository;

  @Mock
  EntityTypeRepository entityTypeRepository;

  @Mock
  ConfigManager configManager;

  @Test
  void getEntityPredicates_nullIriPredicates() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(isNull(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTBundle actual = entityService.getBundle(null, null);
    assertNotNull(actual);
    assertNotNull(actual.getEntity());
  }

  @Test
  void getEntityPredicates_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

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
    when(entityTripleRepository.findImmediateChildrenByIri("http://snomed.info/sct#62014003", null,
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
    when(entityTripleRepository.findImmediateChildrenByIri("http://snomed.info/sct#62014003", null,
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
    when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
      0, 20, true))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    TTArray ttArray = new TTArray()
      .add(iri("http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
    List<EntityReferenceNode> actual = entityService.getImmediateParents
      ("http://endhealth.info/im#25451000252115", null, 1, 20, true);

    assertNotNull(actual);

  }

  @Test
  void getImmediateParents_NotNullIriAndInactiveFalse() {
    EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
      .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
      .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
    when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
      0, 10, false))
      .thenReturn(Collections.singletonList(entityReferenceNode));
    TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115", "Adverse reaction caused by drug (disorder)"));
    when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
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

    when(entityTctRepository.findAncestorsByType(any(), any(), any()))
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
    when(entityTripleRepository.getConceptUsagesCount(any())).thenReturn(1000);
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
  void getEntityTermCodes_NullIri() {
    List<SearchTermCode> actual = entityService.getEntityTermCodes(null, false);
    assertNotNull(actual);
  }

  @Test
  void getEntityTermCodes_EmptyIri() {
    List<SearchTermCode> actual = entityService.getEntityTermCodes("", false);
    assertNotNull(actual);
  }

  @Test
  void getEntityTermCodes_NotNullIri() {
    SearchTermCode termCode = new SearchTermCode()
      .setCode("24951000252112")
      .setTerm("Adverse reaction to Testogel")
      .setStatus(new TTIriRef().setIri(IM.ACTIVE).setName(TTIriRef.iri(IM.ACTIVE).getName()));
    when(entityRepository2.getBundle(any(), any())).thenReturn(new TTBundle().setEntity(new TTEntity().set(TTIriRef.iri(IM.HAS_TERM_CODE), new TTArray().add(new TTNode().set(TTIriRef.iri(IM.CODE), new TTLiteral(termCode.getCode())).set(TTIriRef.iri(RDFS.LABEL), new TTLiteral(termCode.getTerm())).set(TTIriRef.iri(IM.HAS_STATUS), new TTArray().add(termCode.getStatus()))))));
    List<SearchTermCode> actual = entityService.getEntityTermCodes("http://endhealth.info/im#25451000252115", false);
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NullEntity() {
    List<DataModelProperty> actual = entityService.getDataModelProperties((TTEntity) null);
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotNullEntity() {
    List<DataModelProperty> actual = entityService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(SHACL.PROPERTY), new TTArray().add(new TTNode()
        .set(TTIriRef.iri(IM.INHERITED_FROM), new TTIriRef())
        .set(TTIriRef.iri(SHACL.PATH), new TTIriRef())
        .set(TTIriRef.iri(SHACL.CLASS), new TTIriRef())
        .set(TTIriRef.iri(SHACL.DATATYPE), new TTIriRef())
        .set(TTIriRef.iri(SHACL.MAXCOUNT), new TTLiteral())
        .set(TTIriRef.iri(SHACL.MINCOUNT), new TTLiteral())
      )));
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotInheritedFrom() {
    List<DataModelProperty> actual = entityService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(SHACL.PROPERTY), new TTArray().add(new TTNode()))
    );
    assertNotNull(actual);
  }

  @Test
  void getGraphData_NullIri() {
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(new TTEntity()));

    GraphDto actual = entityService.getGraphData(null);
    assertNotNull(actual);
  }

  @Test
  void getGraphData_NotNullEntity() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));
    GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_RoleGroup() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_LeafNodes() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));
    GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_ParentIsList() {
    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(RDFS.SUBCLASS_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
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
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
    assertNull(actual);
  }

  @Test
  void getConceptShape_ContainsNodeShape() {
    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(RDF.TYPE), new TTArray()
        .add(TTIriRef.iri(SHACL.NODESHAPE))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_NullIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    TTEntity actual = entityService.getSummaryFromConfig(null, configs);
    assertNotNull(actual);
  }

  @Test
  void getSummaryFromConfig_EmptyIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
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
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", configs);
    assertNotNull(actual);
  }

  @Test
  void getJsonDownload_NullIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadParams params = new DownloadParams();
    DownloadDto actual = entityService.getJsonDownload(null, configs, params);
    assertNull(actual);
  }

  @Test
  void getJsonDownload_EmptyIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadParams params = new DownloadParams();
    DownloadDto actual = entityService.getJsonDownload("", configs, params);
    assertNull(actual);
  }

  @Test
  void getJsonDownload_OnlySummary() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    DownloadParams params = new DownloadParams();

    DownloadDto actual = entityService.getJsonDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);
  }

  @Test
  void getJsonDownload_All() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    TTEntity entity = new TTEntity()
      .setIri("http://endhealth.info/im#myConcept")
      .setName("My concept")
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    DownloadParams params = new DownloadParams();
    params
      .setIncludeInactive(true)
      .setIncludeTerms(true)
      .setIncludeIsChildOf(true)
      .setIncludeHasChildren(true)
      .setIncludeInferred(true)
      .setIncludeMembers(true)
      .setExpandMembers(true)
      .setExpandSubsets(true)
      .setIncludeHasSubtypes(true)
      .setIncludeProperties(true);

    DownloadDto actual = entityService.getJsonDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);
  }

  @Test
  void getExcelDownload_NullIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadParams params = new DownloadParams();
    XlsHelper actual = entityService.getExcelDownload(null, configs, params);
    assertNull(actual);

  }

  @Test
  void getExcelDownload_EmptyIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadParams params = new DownloadParams();
    XlsHelper actual = entityService.getExcelDownload("", configs, params);
    assertNull(actual);

  }

  @Test
  void getExcelDownload_SummaryOnly() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    DownloadParams params = new DownloadParams();

    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    XlsHelper actual = entityService.getExcelDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);

  }

  @Test
  void getExcelDownload_All() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    DownloadParams params = new DownloadParams();
    params
      .setIncludeInactive(true)
      .setIncludeTerms(true)
      .setIncludeIsChildOf(true)
      .setIncludeHasChildren(true)
      .setIncludeInferred(true)
      .setIncludeMembers(true)
      .setExpandMembers(true)
      .setExpandSubsets(true)
      .setIncludeHasSubtypes(true)
      .setIncludeProperties(true);

    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    XlsHelper actual = entityService.getExcelDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);
  }

  @Test
  void getNamespaces_() {
    List<Namespace> namespace = new ArrayList<>();
    when(entityTripleRepository.findNamespaces()).thenReturn(namespace);
    List<Namespace> actual = entityService.getNamespaces();
    assertNotNull(actual);

  }

  @Test
  void getInferredBundle_NullIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(isNull(), anySet(), anyBoolean())).thenReturn(new TTBundle().setEntity(entity));
    TTBundle actual = entityService.getInferredBundle(null);
    assertNotNull(actual);
  }

  @Test
  void getInferredBundle_EmptyIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(any(), anySet(), anyBoolean())).thenReturn(new TTBundle().setEntity(entity));
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
    when(entityRepository2.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));

    TTDocument actual = entityService.getConceptList(Collections.singletonList(""));
    assertNotNull(actual);
  }

  @Test
  void getConceptList_NotNullIri() {
    TTEntity entity = new TTEntity();
    when(entityRepository2.getBundle(any(), isNull())).thenReturn(new TTBundle().setEntity(entity));
    List<Namespace> namespaces = new ArrayList<>();
    namespaces.add(new Namespace("http://endhealth.info/im#25451000252115", "", ""));
    when(entityTripleRepository.findNamespaces()).thenReturn(namespaces);
    TTDocument actual = entityService.getConceptList(Collections.singletonList("http://endhealth.info/im#25451000252115"));
    assertNotNull(actual);
  }

  @Test
  void getSimpleMaps_NullIri() {
    List<SimpleMap> actual = entityService.getMatchedFrom(null);
    assertNotNull(actual);
  }

  @Test
  void getSimpleMaps_EmptyIri() {
    Collection<SimpleMap> actual = entityService.getMatchedFrom("");
    assertNotNull(actual);
  }

  @Test
  void getSimpleMaps_NotNullIri() {
    Collection<SimpleMap> actual = entityService.getMatchedFrom("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getSetExport_NullIri() {
    SetExporterOptions options = new SetExporterOptions(null, false, true, true, true, true, false, List.of());
    assertThrows(IllegalArgumentException.class, () -> entityService.getSetExport(options));
  }

  @Test
  void getSetExport_EmptyIri() {
    SetExporterOptions options = new SetExporterOptions("", false, true, true, true, true, false, List.of());
    assertThrows(IllegalArgumentException.class, () -> entityService.getSetExport(options));
  }
}
