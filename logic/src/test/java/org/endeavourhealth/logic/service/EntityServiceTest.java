package org.endeavourhealth.logic.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.springframework.http.HttpEntity;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntityServiceTest {
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

//    @Test
//    public void getEntity_NullIri() throws SQLException, JsonProcessingException {
//        TTEntity actual = entityService.getEntity(null) ;
//        assertNull(actual);
//
//    }
//    @Test
//    public void getEntity_NotNullIri() throws SQLException, JsonProcessingException {
//        TTEntity entity = new TTEntity();
//        when(entityRepository.getEntityByIri("http://endhealth.info/im#25451000252115")).thenReturn(entity);
//        TTEntity actual = entityService.getEntity("http://endhealth.info/im#25451000252115") ;
//
//        assertNotNull(actual);
//
//    }
//
//    @Test
//    public void getEntity_NullNameNullEntity() throws SQLException, JsonProcessingException {
//        when(entityRepository.getEntityByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
//        TTEntity actual = entityService.getEntity("http://endhealth.info/im#25451000252115") ;
//        assertNull(actual);
//
//    }
//
//    @Test
//    public void getEntity_NullName() throws SQLException, JsonProcessingException {
//        TTEntity ttEntity = new TTEntity()
//                .setIri("http://endhealth.info/im#25451000252115")
//                .setName("Adverse reaction to Amlodipine Besilate")
//                .setType(new TTArray())
//                .set(IM.IS_A, new TTIriRef().setIri("http://snomed.info/sct#62014003").setName("Adverse reaction caused by drug (disorder)"));
//
//        when(entityRepository.getEntityByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttEntity);
//        TTIriRef unnamedParent = new TTIriRef().setIri("http://snomed.info/sct#62014003").setName("Adverse reaction caused by drug (disorder)");
//        when(entityRepository.findAllByIriIn( anySet()))
//                .thenReturn(Collections.singletonList(unnamedParent));
//        String name = "Adverse reaction caused by drug (disorder)";
//        TTEntity actual = entityService.getEntity("http://endhealth.info/im#25451000252115") ;
//
//        assertEquals(name,actual.get(IM.IS_A).asIriRef().getName());
//
//    }

    @Test
    public void getEntityReference_NullIri() throws SQLException {
        TTIriRef actual = entityService.getEntityReference(null);

        assertNull(actual);

    }

    @Test
    public void getEntityReference_NullEntity() throws SQLException {
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115");

        assertNull(actual);

    }

    @Test
    public void getEntityReference_NotNullEntity() throws SQLException, JsonProcessingException {
        TTIriRef ttIriRef = new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("http://endhealth.info/im#25451000252115");
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttIriRef);
        TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NullIri() throws SQLException {
        List<EntityReferenceNode> actual = entityService
                .getImmediateChildren(null, 1, 10, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NotNullIriAndInactiveTrue() throws SQLException {

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
                ("http://endhealth.info/im#25451000252115", 1, 20, true,true);
        assertNotNull(actual);
    }

    @Test
    public void getImmediateChildren_NotNullIriAndInactiveFalse() throws SQLException {
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
                ("http://endhealth.info/im#25451000252115", 1, 20, true,false);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NullIri() throws SQLException {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents(null, 1, 10, true,true);

        assertNotNull(actual);
    }

    @Test
    public void getImmediateParents_NotNullIriAndInactiveTrue() throws SQLException {

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
                ("http://endhealth.info/im#25451000252115", 1, 20, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NotNullIriAndInactiveFalse() throws SQLException {
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115",
                0,10,false))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 10, true,false);

        assertNotNull(actual);

    }

    @Test
    public void isWhichType_NullIri() throws SQLException {
        List<TTIriRef> actual = entityService
                .isWhichType(null, Arrays.asList("A","B"));

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_EmptyCandidates() throws SQLException {
        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115", Collections.emptyList());

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_NullCandidates() throws SQLException {
        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115", null);

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_NullIriAndCandidates() throws SQLException {
        List<TTIriRef> actual = entityService
                .isWhichType(null, null);

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_NotNullIriAndCandidates() throws SQLException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://www.w3.org/2002/07/owl#Class")
                .setName("Class");

        when(entityTctRepository.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(any(),any(),any()))
                .thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115",
                        Collections.singletonList("http://endhealth.info/im#25451000252115"));

        assertNotNull(actual);
    }

    @Test
    public void usages_NullIri() throws SQLException {
        List<TTIriRef> actual = entityService.usages(null);

        assertNotNull(actual);
    }

    @Test
    public void usages_NotNullIri() throws SQLException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        when(entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate( any(), any())).thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = entityService.usages("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullRequest() throws Exception {
        List<EntitySummary> actual = entityService.advancedSearch(null);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullTermFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest().setTermFilter(null);

        List<EntitySummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullSchemeFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(null);

        EntitySummary entitySearch = new EntitySummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
                .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
                .thenReturn(Collections.singletonList(entitySearch));

        List<EntitySummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NotNullSchemeFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        EntitySummary entitySearch = new EntitySummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(entitySearch));

        List<EntitySummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NotNullMarkIfDescendentOf() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setMarkIfDescendentOf(Arrays.asList(":DiscoveryCommonDataModel", ":SemanticEntity", ":VSET_ValueSet"))
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
                        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        EntitySummary entitySearch = new EntitySummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(entitySearch));

        List<EntitySummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void getValueSetMembers_NullIri() throws SQLException {
        ExportValueSet actual = entityService.getValueSetMembers(null, true);

        assertNull(actual);

    }

    @Test
    public void getValueSetMembers_ExpandTrue() throws SQLException {
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000652115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(valueSetRepository.expandMember(any(), any())).thenReturn(Collections.singletonList(valueSetMember));
        ExportValueSet actual = entityService.getValueSetMembers("http://endhealth.info/im#25451000252115", true);

        assertNotNull(actual);

    }

    @Test
    public void getValueSetMembers_ExpandFalse() throws SQLException {

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        ExportValueSet actual = entityService.getValueSetMembers("http://endhealth.info/im#25451000252115", false);

        assertNotNull(actual);

    }

    @Test
    public void isValuesetMember_NullIriAndMember() throws SQLException {
        ValueSetMembership actual = entityService.isValuesetMember(null, null);

        assertNull(actual);
    }

    @Test
    public void isValuesetMember_NotNullIriAndHasMember() throws SQLException {
        TTIriRef ttIriRef1 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        TTIriRef ttIriRef2 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(entityTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.HAS_MEMBER.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.NOT_MEMBER.getIri()));
        when(valueSetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = entityService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.HAS_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    public void isValuesetMember_NotNullIriAndNotMember() throws SQLException {
        TTIriRef ttIriRef1 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        TTIriRef ttIriRef2 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(entityTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.HAS_MEMBER.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.NOT_MEMBER.getIri()));
        when(valueSetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = entityService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.NOT_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    public void getEntityTermCodes_NullIri() throws SQLException {
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes(null);
        assertNotNull(actual);
    }

    @Test
    public void getEntityTermCodes_NotNullIri() throws SQLException {
        org.endeavourhealth.imapi.model.TermCode termCode = new org.endeavourhealth.imapi.model.TermCode()
                .setCode("24951000252112")
                .setTerm("Adverse reaction to Testogel")
                .setScheme(new TTIriRef()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"))
                .setEntity_term_code("32231000252116");
        when(termCodeRepository.findAllByEntity_Iri(any())).thenReturn(Collections.singletonList(termCode));
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void download_NullIri() throws SQLException, JsonProcessingException {
        HttpEntity actual = entityService.download(null, "excel", true, true, true ,true, false,
                true, true);

        assertNull(actual);

    }

    @Test
    public void download_EmptyIri() throws SQLException, JsonProcessingException {
        HttpEntity actual = entityService.download("", "excel", true, true, true ,true, false,
                true, true);

        assertNull(actual);

    }

    @Test
    public void download_NullFormat() throws SQLException, JsonProcessingException {
        HttpEntity actual = entityService.download("http://endhealth.info/im#25451000252115", null, true,
                true, true ,true, false, true, true);

        assertNull(actual);

    }

    @Test
    public void download_EmptyFormat() throws SQLException, JsonProcessingException {
        HttpEntity actual = entityService.download("http://endhealth.info/im#25451000252115", "", true,
                true, true ,true, false, true, true);

        assertNull(actual);

    }

    @Test
    public void download_AllSelectionsTrueExcelFormat() throws SQLException, JsonProcessingException {
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0,null,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        when(entityTripleRepository.findImmediateParentsByIri( "http://endhealth.info/im#25451000252115",
                0,null,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTIriRef ttEntity = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttEntity);
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        HttpEntity actual = entityService.download("http://endhealth.info/im#25451000252115", "excel", true,
                true, true ,true, false, true, true);

        assertNotNull(actual);

    }

    @Test
    public void download_AllSelectionsTrueJsonFormat() throws SQLException, JsonProcessingException {

        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0,null,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        when(entityTripleRepository.findImmediateParentsByIri( "http://endhealth.info/im#25451000252115",
                0,null,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTIriRef ttEntity = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttEntity);
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        HttpEntity actual = entityService.download("http://endhealth.info/im#25451000252115", "json", true,
                true, true ,true, false, true, true);

        assertNotNull(actual);

    }

    @Test
    public void valueSetMembersCSV_NullIri() throws SQLException {
        String actual = entityService.valueSetMembersCSV(null, true);
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NotNullIriExpandTrue() throws SQLException {
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000552115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000652115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(valueSetRepository.expandMember(any(), any())).thenReturn(Collections.singletonList(valueSetMember));
        TTIriRef ttIriRef= new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(ttIriRef);
        String actual = entityService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", true);
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NotNullIriExpandFalse() throws SQLException {
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000552115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        TTIriRef ttIriRef= new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(ttIriRef);
        String actual = entityService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", false);
        assertNotNull(actual);
    }


    @Test
    public void getGraphData_NullIri() throws SQLException, JsonProcessingException {
        GraphDto actual = entityService.getGraphData(null);
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NotNullIri() throws JsonProcessingException, SQLException {
//        TTEntity ttEntity = new TTEntity()
//                .setIri("http://endhealth.info/im#25451000252115")
//                .set(IM.IS_A, new TTArray().add(new TTIriRef("http://endhealth.info/im#25451000252115")))
//                .set(IM.IS_CONTAINED_IN, new TTArray().add(new TTIriRef("http://endhealth.info/im#25451000252115")))
//                .set(IM.ROLE_GROUP, new TTArray().add(new TTNode()
//                        .set(RDFS.LABEL, new TTIriRef("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
//                        .set(RDFS.COMMENT, new TTIriRef("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
//                ))
//                .set(IM.PROPERTY_GROUP, new TTArray()
//                        .add(new TTNode()
//                                .set(IM.INHERITED_FROM,new TTIriRef("http://endhealth.info/im#25451000252115"))
//                                .set(SHACL.PROPERTY, new TTArray()
//                                        .add(new TTNode()
//                                                .set(SHACL.PATH, new TTIriRef("http://endhealth.info/im#25451000252115"))
//                                                .set(SHACL.MINCOUNT, new TTLiteral(1))
//                                                .set(SHACL.MAXCOUNT, new TTLiteral(10))
//                                                .set(SHACL.CLASS, new TTIriRef("http://endhealth.info/im#Class","Class"))
//                                                .set(SHACL.DATATYPE, new TTIriRef("http://endhealth.info/im#DataType","DataType"))
//                                        ))));
//        when(entityRepository.getEntityByIri(any())).thenReturn(ttEntity);
        Tpl tpl = new Tpl()
                .setDbid(123)
                .setLiteral("10")
                .setObject(new TTIriRef())
                .setParent(null)
                .setPredicate(new TTIriRef());
        when(entityTripleRepository.getTriplesRecursive(any(),anySet())).thenReturn(Collections.singletonList(tpl));
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NullParent() throws JsonProcessingException, SQLException {
//        TTEntity ttEntity = new TTEntity()
//                .setIri("http://endhealth.info/im#25451000252115");
//        when(entityRepository.getEntityByIri(any())).thenReturn(ttEntity);
        Tpl tpl = new Tpl()
                .setDbid(123)
                .setLiteral("10")
                .setObject(new TTIriRef())
                .setParent(null)
                .setPredicate(new TTIriRef());
        when(entityTripleRepository.getTriplesRecursive(any(),anySet())).thenReturn(Collections.singletonList(tpl));
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NotNullParent() throws JsonProcessingException, SQLException {
//        TTEntity ttEntity = new TTEntity()
//                .setIri("http://endhealth.info/im#25451000252115")
//                .set(IM.IS_A, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
//                .set(IM.IS_CONTAINED_IN, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"));
//        when(entityRepository.getEntityByIri(any())).thenReturn(ttEntity);
        Tpl tpl = new Tpl()
                .setDbid(123)
                .setLiteral("10")
                .setObject(new TTIriRef())
                .setParent(null)
                .setPredicate(new TTIriRef());
        when(entityTripleRepository.getTriplesRecursive(any(),anySet())).thenReturn(Collections.singletonList(tpl));
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }


    @Test
    public void getGraphData_NullInheritedFrom() throws JsonProcessingException, SQLException {
//        TTEntity ttEntity = new TTEntity()
//                .setIri("http://endhealth.info/im#25451000252115")
//                .setName("Adverse reaction to Amlodipine Besilate")
//                .setType(new TTArray())
//                .set(IM.IS_A, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
//                .set(IM.IS_CONTAINED_IN, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
//                .set(IM.PROPERTY_GROUP, new TTArray()
//                        .add(new TTNode()
//                                .set(SHACL.PROPERTY, new TTArray()
//                                        .add(new TTNode()
//                                                .set(SHACL.PATH, new TTIriRef("http://endhealth.info/im#25451000252115"))
//                                                .set(SHACL.MINCOUNT, new TTLiteral(1))
//                                                .set(SHACL.MAXCOUNT, new TTLiteral(10))
//                                                .set(SHACL.CLASS, new TTIriRef("http://endhealth.info/im#Class","Class"))
//                                                .set(SHACL.DATATYPE, new TTIriRef("http://endhealth.info/im#DataType","DataType"))
//                                        ))));
//        when(entityRepository.getEntityByIri(any())).thenReturn(ttEntity);
        Tpl tpl = new Tpl()
                .setDbid(123)
                .setLiteral("10")
                .setObject(new TTIriRef())
                .setParent(null)
                .setPredicate(new TTIriRef());
        when(entityTripleRepository.getTriplesRecursive(any(),anySet())).thenReturn(Collections.singletonList(tpl));
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

}
