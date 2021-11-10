package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EntityServiceTest {
    @InjectMocks
    EntityService entityService;

    @Mock
    EntityRepository entityRepository;

    @Mock
    EntityTripleRepository entityTripleRepository;

    @Mock
    EntityTctRepository entityTctRepository;

    @Mock
    EntitySearchRepository entitySearchRepository;

    @Mock
    ValueSetRepository valueSetRepository;

    @Mock
    TermCodeRepository termCodeRepository;

    @Mock
    EntityTypeRepository entityTypeRepository;

    @Mock
    ConfigService configService;

    @Test
    void getEntityPredicates_nullIriPredicates() {

        TTBundle actual = entityService.getEntityPredicates(null,null, 0);
        assertNotNull(actual);
        assertNotNull(actual.getEntity());
    }

    @Test
    void getEntityPredicates_EmptyIri() {

        TTBundle actual = entityService.getEntityPredicates("", null, 0);
        assertNotNull(actual);
        assertNotNull(actual.getEntity());
    }

    @Test
    void getEntityPredicates_notNullIriPredicates() {
        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.ROLE_GROUP).setFunctional(false));
        tplList.add(new Tpl()
                .setDbid(7)
                .setPredicate(IM.ROLE).setFunctional(false)
                .setParent(3));
        tplList.add(new Tpl()
                .setDbid(8)
                .setPredicate(OWL.ONPROPERTY)
                .setParent(7)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(9)
                .setPredicate(OWL.SOMEVALUESFROM)
                .setParent(7)
                .setObject(iri("http://endhealth.info/im#25451000252115")));

        when(entityTripleRepository.getTriplesRecursive(any(), anySet(), anyInt())).thenReturn(tplList);
        TTEntity actual = entityService.getEntityPredicates("http://endhealth.info/im#25451000252115",Set.of(IM.IS_A.getIri(),RDFS.LABEL.getIri()), 0).getEntity();
        assertNotNull(actual);
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
                .getImmediateChildren(null, 1, 10, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_EmptyIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateChildren("", 1, 10, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_NullIndexSize() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateChildren("http://endhealth.info/im#25451000252115", null, null, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_NotNullIriAndInactiveTrue() {

        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0, 20,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 20, true);
        assertNotNull(actual);
    }

    @Test
    void getImmediateChildren_NotNullIriAndInactiveFalse() {
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                                0, 20,false))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 20, false);

        assertNotNull(actual);

    }

    @Test
    void getImmediateParents_NullIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents(null, 1, 10, true);

        assertNotNull(actual);
    }

    @Test
    void getImmediateParents_EmptyIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents("", 1, 10, true);

        assertNotNull(actual);
    }

    @Test
    void getImmediateParents_NullIndexSize() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents("http://endhealth.info/im#25451000252115", null, null, true);

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
        when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115",
                0, 20,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 20, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateParents_NotNullIriAndInactiveFalse() {
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115",
                0,10,false))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 10, false);

        assertNotNull(actual);

    }

    @Test
    void isWhichType_NullIri() {
        List<TTIriRef> actual = entityService
                .isWhichType(null, Arrays.asList("A","B"));

        assertNotNull(actual);
    }

    @Test
    void isWhichType_EmptyIri() {
        List<TTIriRef> actual = entityService
                .isWhichType("", Arrays.asList("A","B"));

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

        when(entityTctRepository.findAncestorsByType(any(),any(),any()))
                .thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115",
                        Collections.singletonList("http://endhealth.info/im#25451000252115"));

        assertNotNull(actual);
    }

    @Test
    void usages_NullIri() throws SQLException, JsonProcessingException {
        List<TTIriRef> actual = entityService.usages(null,null,null);

        assertNotNull(actual);
    }

    @Test
    void usages_EmptyIri() throws SQLException, JsonProcessingException {
        List<TTIriRef> actual = entityService.usages("",null,null);

        assertNotNull(actual);
    }

    @Test
    void usages_NotNullIri() throws SQLException, JsonProcessingException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        when(entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate( any(), any(),any(),any())).thenReturn(Collections.singletonList(ttIriRef));
        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

        List<TTIriRef> actual = entityService.usages("http://endhealth.info/im#25451000252115",1,10);

        assertNotNull(actual);
    }

    @Test
    void totalRecords_NullIri() throws SQLException, JsonProcessingException {
        Integer actual = entityService.totalRecords(null);
        assertNotNull(actual);
    }

    @Test
    void totalRecords_NotNullIri() throws SQLException, JsonProcessingException {
        when(entityTripleRepository.getCountOfActiveSubjectByObjectExcludeByPredicate( any(),any())).thenReturn(1000);
        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

        Integer actual = entityService.totalRecords("http://endhealth.info/im#25451000252115");
        assertEquals(1000, actual);
    }

    @Test
    void totalRecords_XMLIri() throws SQLException, JsonProcessingException {
        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

        Integer actual = entityService.totalRecords("http://www.w3.org/2001/XMLSchema#string");
        assertEquals(0, actual);
    }

    @Test
    void advancedSearch_NullRequest() throws Exception {
        List<SearchResultSummary> actual = entityService.advancedSearch(null);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NullTermFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest().setTermFilter(null);

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_EmptyTermFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest().setTermFilter("");

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NullSchemeFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(null);

        SearchResultSummary entitySearch = new SearchResultSummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
                .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
                .thenReturn(Collections.singletonList(entitySearch));

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NotNullSchemeFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        SearchResultSummary entitySearch = new SearchResultSummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(entitySearch));

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NotNullMarkIfDescendentOf() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setMarkIfDescendentOf(Arrays.asList(":DiscoveryCommonDataModel", ":SemanticEntity", ":VSET_ValueSet"))
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
                        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        SearchResultSummary entitySearch = new SearchResultSummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(entitySearch));

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void getValueSetMembers_NullIri() {
        ExportValueSet actual = entityService.getValueSetMembers(null, true,false, null);

        assertNull(actual);

    }

    @Test
    void getValueSetMembers_EmptyIri() {
        ExportValueSet actual = entityService.getValueSetMembers("", true,false, null);

        assertNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandMemberTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        Set<String> hasSubsetPredicates = new HashSet<>();
        hasSubsetPredicates.add(IM.DEFINITION.getIri());

        ArrayList<Tpl> mockHasSubsetReturn = new ArrayList<>();
        mockHasSubsetReturn.add(new Tpl().setPredicate(iri(IM.HAS_SUBSET.getIri(), IM.HAS_SUBSET.getName())));

        when(entityTripleRepository.getTriplesRecursive(eq(valueSetIri.getIri()),eq(hasSubsetPredicates),anyInt()))
            .thenReturn(mockHasSubsetReturn);

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), true, false, 0, null, valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandSubsetTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        Set<String> hasSubsetPredicates = new HashSet<>();
        hasSubsetPredicates.add(IM.DEFINITION.getIri());

        ArrayList<Tpl> mockHasSubsetReturn = new ArrayList<>();
        mockHasSubsetReturn.add(new Tpl().setPredicate(iri(IM.HAS_SUBSET.getIri(), IM.HAS_SUBSET.getName())));

        when(entityTripleRepository.getTriplesRecursive(eq(valueSetIri.getIri()), eq(hasSubsetPredicates), anyInt()))
                .thenReturn(mockHasSubsetReturn);

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), false, true, 0, null, valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandFalse() {

        ExportValueSet actual = entityService.getValueSetMembers("http://endhealth.info/im#25451000252115", false, false,0,null, "http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    void isValuesetMember_NullIriAndMember() {
        ValueSetMembership actual = entityService.isValuesetMember(null, null);

        assertNull(actual);
    }

    @Test
    void isValuesetMember_EmptyIriAndMember() {
        ValueSetMembership actual = entityService.isValuesetMember("", "");

        assertNull(actual);
    }

    @Test
    void isValuesetMember_NotNullIriAndHasMember() {
        TTIriRef ttIriRef1 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        TTIriRef ttIriRef2 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.DEFINITION.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.DEFINITION.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.NOT_MEMBER.getIri()));
        when(valueSetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = entityService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.DEFINITION.getIri());

        assertNotNull(actual);
    }

    @Test
    void isValuesetMember_NotNullIriAndNotMember() {
        TTIriRef ttIriRef1 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        TTIriRef ttIriRef2 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.DEFINITION.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.DEFINITION.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.NOT_MEMBER.getIri()));
        when(valueSetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = entityService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.NOT_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    void getEntityTermCodes_NullIri() {
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes(null);
        assertNotNull(actual);
    }

    @Test
    void getEntityTermCodes_EmptyIri() {
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes("");
        assertNotNull(actual);
    }

    @Test
    void getEntityTermCodes_NotNullIri() {
        org.endeavourhealth.imapi.model.TermCode termCode = new org.endeavourhealth.imapi.model.TermCode()
                .setCode("24951000252112")
                .setName("Adverse reaction to Testogel")
                .setScheme("http://endhealth.info/im#25451000252115")
                .setEntityTermCode("32231000252116");
        when(termCodeRepository.findAllByIri(any())).thenReturn(Collections.singletonList(termCode));
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }
/*
    @Test
    void download_ExcelNullIri() throws SQLException{
        XlsHelper actual = entityService.getExcelDownload(null, new ArrayList<>(), true, true, true ,true, false, true, true, true, true,  true);

        assertNull(actual);
    }

    @Test
    void download_ExcelEmptyIri() throws SQLException{
        XlsHelper actual = entityService.getExcelDownload("", new ArrayList<>(), true, true, true ,true, false, true, true, true, true, true);

        assertNull(actual);
    }

    @Test
    void download_JSONNullIri() {
        DownloadDto actual = entityService.getJsonDownload(null, new ArrayList<>(), true, true, true ,true, false, true, true, true, true, true);

        assertNull(actual);
    }

    @Test
    void download_JSONEmptyIri() {
        DownloadDto actual = entityService.getJsonDownload("", new ArrayList<>(), true, true, true ,true, false, true, true, true, true, true);

        assertNull(actual);
    }

    @Test
    void download_AllSelectionsTrueExcelFormat() throws SQLException{
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",0,null,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTIriRef ttEntity = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttEntity);

        Set<String> hasSubsetPredicates = new HashSet<>();
        hasSubsetPredicates.add(IM.DEFINITION.getIri());

        ArrayList<Tpl> mockHasSubsetReturn = new ArrayList<>();
        mockHasSubsetReturn.add(new Tpl().setPredicate(iri(IM.HAS_SUBSET.getIri(), IM.HAS_SUBSET.getName())));

        when(entityTripleRepository.getTriplesRecursive(any(),eq(hasSubsetPredicates), anyInt()))
                .thenReturn(mockHasSubsetReturn);
        XlsHelper actual = entityService.getExcelDownload("http://endhealth.info/im#25451000252115", new ArrayList<>(), true,
                true, true ,true, false, true, true, true, true,true);

        assertNotNull(actual);

    }

    @Test
    void download_AllSelectionsFalseExcelFormat() throws SQLException{

        XlsHelper actual = entityService.getExcelDownload("http://endhealth.info/im#25451000252115", new ArrayList<>(), false,
                false, false ,false, false, false, false, false, false,false);

        assertNotNull(actual);

    }

    @Test
    void download_AllSelectionsTrueJsonFormat() {

        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0,null,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTIriRef ttEntity = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttEntity);

        Set<String> hasSubsetPredicates = new HashSet<>();
        hasSubsetPredicates.add(IM.DEFINITION.getIri());

        ArrayList<Tpl> mockHasSubsetReturn = new ArrayList<>();
        mockHasSubsetReturn.add(new Tpl().setPredicate(iri(IM.HAS_SUBSET.getIri(), IM.HAS_SUBSET.getName())));

        when(entityTripleRepository.getTriplesRecursive(any(),eq(hasSubsetPredicates), anyInt()))
                .thenReturn(mockHasSubsetReturn);
        DownloadDto actual = entityService.getJsonDownload("http://endhealth.info/im#25451000252115", new ArrayList<>(), true,
                true, true ,true, false, true, true, true, true, true);

        assertNotNull(actual);

    }

    @Test
    void download_AllSelectionsFalseJsonFormat() {

        DownloadDto actual = entityService.getJsonDownload("http://endhealth.info/im#25451000252115", new ArrayList<>(), false,
                false, false ,false, false, false, false, false, false, false);

        assertNotNull(actual);

    }
*/

    @Test
    void getDataModelProperties_NullEntity(){
        List<DataModelProperty> actual = entityService.getDataModelProperties((TTEntity) null);
        assertNotNull(actual);
    }

    @Test
    void getDataModelProperties_NotNullEntity(){
        List<DataModelProperty> actual = entityService.getDataModelProperties(new TTEntity()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.PROPERTY_GROUP, new TTArray().add(new TTNode()
                                .set(IM.INHERITED_FROM, new TTIriRef())).add(new TTNode()
                                .set(SHACL.PROPERTY,new TTArray().add(new TTNode()
                                                .set(SHACL.PATH, new TTIriRef())
                                                .set(SHACL.CLASS,new TTIriRef())
                                                .set(SHACL.DATATYPE, new TTIriRef())
                                                .set(SHACL.MAXCOUNT,new TTLiteral())
                                                .set(SHACL.MINCOUNT,new TTLiteral())))))
        );
        assertNotNull(actual);
    }

    @Test
    void valueSetMembersCSV_NullIri() {
        String actual = entityService.valueSetMembersCSV(null, true,true);
        assertNotNull(actual);
    }

    @Test
    void valueSetMembersCSV_NotNullIriExpandTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        Set<String> hasSubsetPredicates = new HashSet<>();
        hasSubsetPredicates.add(IM.DEFINITION.getIri());

        ArrayList<Tpl> mockHasSubsetReturn = new ArrayList<>();
        mockHasSubsetReturn.add(new Tpl().setPredicate(iri(IM.HAS_SUBSET.getIri(), IM.HAS_SUBSET.getName())));

        when(entityTripleRepository.getTriplesRecursive(eq(valueSetIri.getIri()),eq(hasSubsetPredicates), eq(EntityService.UNLIMITED)))
                .thenReturn(mockHasSubsetReturn);

        String actual = entityService.valueSetMembersCSV(valueSetIri.getIri(), true, true);
        assertNotNull(actual);
    }

    @Test
    void valueSetMembersCSV_NotNullIriExpandFalse() {
        Set<String> hasSubsetPredicates = new HashSet<>();
        hasSubsetPredicates.add(IM.DEFINITION.getIri());

        ArrayList<Tpl> mockHasSubsetReturn = new ArrayList<>();
        mockHasSubsetReturn.add(new Tpl().setPredicate(iri(IM.HAS_SUBSET.getIri(), IM.HAS_SUBSET.getName())));

        when(entityTripleRepository.getTriplesRecursive(any(),eq(hasSubsetPredicates), anyInt()))
                .thenReturn(mockHasSubsetReturn);

        TTIriRef ttIriRef= new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(ttIriRef);

        String actual = entityService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", false, false);
        assertNotNull(actual);
    }

    @Test
    void getGraphData_NullIri() {
        GraphDto actual = entityService.getGraphData(null);
        assertNotNull(actual);
    }

    @Test
    void getGraphData_NotNullEntity() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.ROLE_GROUP));
//        tplList.add(new Tpl()
//                .setDbid(7)
//                .setPredicate(IM.ROLE)
//                .setParent(3));
//        tplList.add(new Tpl()
//                .setDbid(8)
//                .setPredicate(OWL.ONPROPERTY)
//                .setParent(7)
//                .setObject(iri("http://endhealth.info/im#25451000252115")));
//        tplList.add(new Tpl()
//                .setDbid(9)
//                .setPredicate(OWL.SOMEVALUESFROM)
//                .setParent(7)
//                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(10)
                .setPredicate(IM.PROPERTY_GROUP));
        tplList.add(new Tpl()
                .setDbid(11)
                .setPredicate(IM.INHERITED_FROM)
                .setParent(10)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(12)
                .setPredicate(SHACL.PROPERTY)
                .setParent(10));
        tplList.add(new Tpl()
                .setDbid(13)
                .setPredicate(SHACL.PATH)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#25451000252116","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(14)
                .setPredicate(SHACL.CLASS)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#Class","Class")));
        tplList.add(new Tpl()
                .setDbid(15)
                .setPredicate(SHACL.DATATYPE)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#DataType","DataType")));
        tplList.add(new Tpl()
                .setDbid(16)
                .setPredicate(SHACL.MINCOUNT)
                .setParent(12)
                .setLiteral("1"));
        tplList.add(new Tpl()
                .setDbid(17)
                .setPredicate(SHACL.MAXCOUNT)
                .setParent(12)
                .setLiteral("10"));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getGraphData_RoleGroup() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.ROLE_GROUP));
        tplList.add(new Tpl()
                .setDbid(4)
                .setPredicate(OWL.ONPROPERTY)
                .setParent(3)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(6)
                .setPredicate(OWL.SOMEVALUESFROM)
                .setParent(3)
                .setObject(iri("http://endhealth.info/im#25451000252115")));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getGraphData_LeafNodes() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(10)
                .setPredicate(IM.PROPERTY_GROUP));
        tplList.add(new Tpl()
                .setDbid(12)
                .setPredicate(SHACL.PROPERTY)
                .setParent(10));
        tplList.add(new Tpl()
                .setDbid(13)
                .setPredicate(SHACL.PATH)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#25451000252116","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(14)
                .setPredicate(SHACL.CLASS)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#Class","Class")));
        tplList.add(new Tpl()
                .setDbid(15)
                .setPredicate(SHACL.DATATYPE)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#DataType","DataType")));
        tplList.add(new Tpl()
                .setDbid(16)
                .setPredicate(SHACL.MINCOUNT)
                .setParent(12)
                .setLiteral("1"));
        tplList.add(new Tpl()
                .setDbid(17)
                .setPredicate(SHACL.MAXCOUNT)
                .setParent(12)
                .setLiteral("10"));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getGraphData_ParentIsList() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getEntityDefinitionDto_NullIri() {
        EntityDefinitionDto actual = entityService.getEntityDefinitionDto(null);
        assertNotNull(actual);

    }

    @Test
    void getEntityDefinitionDto_NotNullIri() {
        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(RDF.TYPE).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(4)
                .setPredicate(RDFS.COMMENT)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(5)
                .setPredicate(IM.HAS_STATUS)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        EntityDefinitionDto actual = entityService.getEntityDefinitionDto(null);
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
        when(entitySearchRepository.getSummary(any())).thenReturn(summary);
        SearchResultSummary actual = entityService.getSummary(null);
        assertNotNull(actual);
    }

    @Test
    void getConceptShape_NullIri() {
        TTEntity actual = entityService.getConceptShape(null);
        assertNull(actual);
    }

    @Test
    void getConceptShape_NotContainNodeShape() {
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDF.TYPE));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(SHACL.PROPERTY));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(SHACL.OR));
        when(entityTripleRepository.getTriplesRecursive(any(), anySet(), anyInt())).thenReturn(tplList);
        TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
        assertNull(actual);
    }

    @Test
    void getConceptShape_ContainsNodeShape() {
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setFunctional(false)
                .setPredicate(RDF.TYPE)
                .setObject(iri("http://www.w3.org/ns/shacl#NodeShape","Node shape")));
        tplList.add(new Tpl()
                .setDbid(2)
                .setFunctional(false)
                .setPredicate(SHACL.PROPERTY));
        tplList.add(new Tpl()
                .setDbid(3)
                .setFunctional(false)
                .setPredicate(SHACL.OR));
        when(entityTripleRepository.getTriplesRecursive(any(), anySet(), anyInt())).thenReturn(tplList);
        TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }
}
