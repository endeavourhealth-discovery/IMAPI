package org.endeavourhealth.logic.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.graph.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.http.HttpEntity;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
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
public class ConceptServiceTest {
    @InjectMocks
    ConceptService conceptService;

    @Mock
    ConceptRepository conceptRepository;

    @Mock
    ConceptTripleRepository conceptTripleRepository;

    @Mock
    ConceptTctRepository conceptTctRepository;

    @Mock
    ConceptSearchRepository conceptSearchRepository;

    @Mock
    ValueSetRepository valueSetRepository;

    @Mock
    TermCodeRepository termCodeRepository;

    @Mock
    ConceptTypeRepository conceptTypeRepository;

    @Test
    public void getConcept_NullIri() throws SQLException, JsonProcessingException {
        TTConcept actual = conceptService.getConcept(null) ;
        assertNull(actual);

    }
    @Test
    public void getConcept_NotNullIri() throws SQLException, JsonProcessingException {
        TTConcept concept = new TTConcept();
        when(conceptRepository.getConceptByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        TTConcept actual = conceptService.getConcept("http://endhealth.info/im#25451000252115") ;

        assertNotNull(actual);

    }

    @Test
    public void getConcept_NullNameNullConcept() throws SQLException, JsonProcessingException {
        when(conceptRepository.getConceptByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTConcept actual = conceptService.getConcept("http://endhealth.info/im#25451000252115") ;
        assertNull(actual);

    }

    @Test
    public void getConcept_NullName() throws SQLException, JsonProcessingException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setType(new TTArray())
                .set(IM.IS_A, new TTIriRef().setIri("http://snomed.info/sct#62014003").setName("Adverse reaction caused by drug (disorder)"));

        when(conceptRepository.getConceptByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttConcept);
        TTIriRef unnamedParent = new TTIriRef().setIri("http://snomed.info/sct#62014003").setName("Adverse reaction caused by drug (disorder)");
        when(conceptRepository.findAllByIriIn( anySet()))
                .thenReturn(Collections.singletonList(unnamedParent));
        String name = "Adverse reaction caused by drug (disorder)";
        TTConcept actual = conceptService.getConcept("http://endhealth.info/im#25451000252115") ;

        assertEquals(name,actual.get(IM.IS_A).asIriRef().getName());

    }

    @Test
    public void getConceptReference_NullIri() throws SQLException {
        TTIriRef actual = conceptService.getConceptReference(null);

        assertNull(actual);

    }

    @Test
    public void getConceptReference_NullConcept() throws SQLException {
        when(conceptRepository.getConceptReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTIriRef actual = conceptService.getConceptReference("http://endhealth.info/im#25451000252115");

        assertNull(actual);

    }

    @Test
    public void getConceptReference_NotNullConcept() throws SQLException, JsonProcessingException {
        TTIriRef ttIriRef = new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("http://endhealth.info/im#25451000252115");
        when(conceptRepository.getConceptReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttIriRef);
        TTIriRef actual = conceptService.getConceptReference("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NullIri() throws SQLException {
        List<ConceptReferenceNode> actual = conceptService
                .getImmediateChildren(null, 1, 10, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NotNullIriAndInactiveTrue() throws SQLException {

        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(
                        new ConceptReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new ConceptReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(conceptTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0, 20,true))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(conceptTypeRepository.getConceptTypes(any())).thenReturn(ttArray);
        List<ConceptReferenceNode> actual = conceptService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 20, true,true);
        assertNotNull(actual);
    }

    @Test
    public void getImmediateChildren_NotNullIriAndInactiveFalse() throws SQLException {
        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(
                        new ConceptReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new ConceptReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(conceptTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                                0, 20,false))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(conceptTypeRepository.getConceptTypes(any())).thenReturn(ttArray);
        List<ConceptReferenceNode> actual = conceptService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 20, true,false);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NullIri() throws SQLException {
        List<ConceptReferenceNode> actual = conceptService
                .getImmediateParents(null, 1, 10, true,true);

        assertNotNull(actual);
    }

    @Test
    public void getImmediateParents_NotNullIriAndInactiveTrue() throws SQLException {

        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(
                        new ConceptReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new ConceptReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(conceptTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115",
                0, 20,true))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(conceptTypeRepository.getConceptTypes(any())).thenReturn(ttArray);
        List<ConceptReferenceNode> actual = conceptService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 20, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NotNullIriAndInactiveFalse() throws SQLException {
        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115",
                0,10,false))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(conceptTypeRepository.getConceptTypes(any())).thenReturn(ttArray);
        List<ConceptReferenceNode> actual = conceptService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 10, true,false);

        assertNotNull(actual);

    }

    @Test
    public void isWhichType_NullIri() throws SQLException {
        List<TTIriRef> actual = conceptService
                .isWhichType(null, Arrays.asList("A","B"));

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_EmptyCandidates() throws SQLException {
        List<TTIriRef> actual = conceptService
                .isWhichType("http://endhealth.info/im#25451000252115", Collections.emptyList());

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_NullCandidates() throws SQLException {
        List<TTIriRef> actual = conceptService
                .isWhichType("http://endhealth.info/im#25451000252115", null);

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_NullIriAndCandidates() throws SQLException {
        List<TTIriRef> actual = conceptService
                .isWhichType(null, null);

        assertNotNull(actual);
    }

    @Test
    public void isWhichType_NotNullIriAndCandidates() throws SQLException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://www.w3.org/2002/07/owl#Class")
                .setName("Class");

        when(conceptTctRepository.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(any(),any(),any()))
                .thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = conceptService
                .isWhichType("http://endhealth.info/im#25451000252115",
                        Collections.singletonList("http://endhealth.info/im#25451000252115"));

        assertNotNull(actual);
    }

    @Test
    public void usages_NullIri() throws SQLException {
        List<TTIriRef> actual = conceptService.usages(null);

        assertNotNull(actual);
    }

    @Test
    public void usages_NotNullIri() throws SQLException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        when(conceptTripleRepository.findDistinctByObject_IriAndPredicate_IriNot( any(), any())).thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = conceptService.usages("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullRequest() throws Exception {
        List<ConceptSummary> actual = conceptService.advancedSearch(null);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullTermFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest().setTermFilter(null);

        List<ConceptSummary> actual = conceptService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullSchemeFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(null);

        ConceptSummary conceptSearch = new ConceptSummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
                .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(conceptSearchRepository.advancedSearch(any()))
                .thenReturn(Collections.singletonList(conceptSearch));

        List<ConceptSummary> actual = conceptService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NotNullSchemeFilter() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        ConceptSummary conceptSearch = new ConceptSummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(conceptSearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(conceptSearch));

        List<ConceptSummary> actual = conceptService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NotNullMarkIfDescendentOf() throws Exception {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setMarkIfDescendentOf(Arrays.asList(":DiscoveryCommonDataModel", ":SemanticConcept", ":VSET_ValueSet"))
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
                        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        ConceptSummary conceptSearch = new ConceptSummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(conceptSearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(conceptSearch));

        List<ConceptSummary> actual = conceptService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void getAncestorDefinitions_NullIri() throws SQLException {
        List<TTConcept> actual = conceptService.getAncestorDefinitions(null);

        assertNotNull(actual);

    }

       @Test
    public void getAncestorDefinitions_NullConcept() throws SQLException, JsonProcessingException {
        when(conceptTctRepository.findByDescendant_Iri_AndType_Iri_OrderByLevel(any(),any())).thenReturn(null);
        List<TTConcept> actual = conceptService.getAncestorDefinitions("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);

    }


    @Test
    public void getAncestorDefinitions_NotEqualIri() throws SQLException, JsonProcessingException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115");

        when(conceptTctRepository.findByDescendant_Iri_AndType_Iri_OrderByLevel(any(),any())).thenReturn(Collections.singletonList(ttConcept));
        List<TTConcept> actual = conceptService.getAncestorDefinitions("http://endhealth.info/im#25451000552115");

        assertNotNull(actual);

    }

    @Test
    public void getAncestorDefinitions_EqualIri() throws SQLException, JsonProcessingException {
        TTConcept ttConcept = new TTConcept()
                                .setIri("http://endhealth.info/im#25451000552115");

        when(conceptTctRepository.findByDescendant_Iri_AndType_Iri_OrderByLevel(any(),any())).thenReturn(Collections.singletonList(ttConcept));

        List<TTConcept> actual = conceptService.getAncestorDefinitions("http://endhealth.info/im#25451000552115");

        assertNotNull(actual);

    }

    @Test
    public void getValueSetMembers_NullIri() throws SQLException {
        ExportValueSet actual = conceptService.getValueSetMembers(null, true);

        assertNull(actual);

    }

    @Test
    public void getValueSetMembers_ExpandTrue() throws SQLException {
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000652115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(valueSetRepository.expandMember(any())).thenReturn(Collections.singletonList(valueSetMember));
        ExportValueSet actual = conceptService.getValueSetMembers("http://endhealth.info/im#25451000252115", true);

        assertNotNull(actual);

    }

    @Test
    public void getValueSetMembers_ExpandFalse() throws SQLException {

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        ExportValueSet actual = conceptService.getValueSetMembers("http://endhealth.info/im#25451000252115", false);

        assertNotNull(actual);

    }

    @Test
    public void isValuesetMember_NullIriAndMember() throws SQLException {
        ValueSetMembership actual = conceptService.isValuesetMember(null, null);

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
        when(conceptTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(conceptTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri(IM.HAS_MEMBER.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri(IM.NOT_MEMBER.getIri()));
        when(valueSetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = conceptService.isValuesetMember("http://endhealth.info/im#25451000252115",
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
        when(conceptTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(conceptTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri(IM.HAS_MEMBER.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri(IM.NOT_MEMBER.getIri()));
        when(valueSetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = conceptService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.NOT_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    public void getCoreMappedFromLegacy_NullIri() throws SQLException {
        List<TTIriRef> actual = conceptService.getCoreMappedFromLegacy(null);

        assertNotNull(actual);
    }

    @Test
    public void getCoreMappedFromLegacy_NotNullIri() throws SQLException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        when(conceptTripleRepository.getCoreMappedFromLegacyBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = conceptService.getCoreMappedFromLegacy("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void getLegacyMappedToCore_NullIri() throws SQLException {
        List<TTIriRef> actual = conceptService.getLegacyMappedToCore(null);

        assertNotNull(actual);
    }

    @Test
    public void getLegacyMappedToCore_NotNullIri() throws SQLException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");


        when(conceptTripleRepository.findAllByObject_Iri_AndPredicate_Iri(any(),any())).thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = conceptService.getLegacyMappedToCore("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void getConceptTermCodes_NullIri() throws SQLException {
        List<org.endeavourhealth.imapi.model.TermCode> actual = conceptService.getConceptTermCodes(null);
        assertNotNull(actual);
    }

    @Test
    public void getConceptTermCodes_NotNullIri() throws SQLException {
        org.endeavourhealth.imapi.model.TermCode termCode = new org.endeavourhealth.imapi.model.TermCode()
                .setCode("24951000252112")
                .setTerm("Adverse reaction to Testogel")
                .setScheme(new TTIriRef()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"))
                .setConcept_term_code("32231000252116");
        when(termCodeRepository.findAllByConcept_Iri(any())).thenReturn(Collections.singletonList(termCode));
        List<org.endeavourhealth.imapi.model.TermCode> actual = conceptService.getConceptTermCodes("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void download_NullIri() throws SQLException, JsonProcessingException {
        HttpEntity actual = conceptService.download(null, "excel", true, true, true ,true,
                true, true);

        assertNull(actual);

    }

    @Test
    public void download_EmptyIri() throws SQLException, JsonProcessingException {
        HttpEntity actual = conceptService.download("", "excel", true, true, true ,true,
                true, true);

        assertNull(actual);

    }

    @Test
    public void download_NullFormat() throws SQLException, JsonProcessingException {
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", null, true,
                true, true ,true, true, true);

        assertNull(actual);

    }

    @Test
    public void download_EmptyFormat() throws SQLException, JsonProcessingException {
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", "", true,
                true, true ,true, true, true);

        assertNull(actual);

    }

    @Test
    public void download_AllSelectionsTrueExcelFormat() throws SQLException, JsonProcessingException {
        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0,20,true))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        when(conceptTripleRepository.findImmediateParentsByIri( "http://endhealth.info/im#25451000252115",
                0,20,true))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(conceptRepository.getConceptByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttConcept);
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", "excel", true,
                true, true ,true, true, true);

        assertNotNull(actual);

    }

    @Test
    public void download_AllSelectionsTrueJsonFormat() throws SQLException, JsonProcessingException {

        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0,20,true))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        when(conceptTripleRepository.findImmediateParentsByIri( "http://endhealth.info/im#25451000252115",
                0,20,true))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(conceptRepository.getConceptByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttConcept);
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", "json", true,
                true, true ,true, true, true);

        assertNotNull(actual);

    }

    @Test
    public void getAllProperties_NullIri() throws SQLException, JsonProcessingException {
        List<PropertyValue> actual = conceptService.getAllProperties((String) null);
        assertNotNull(actual);
    }

    @Test
    public void getAllProperties_NotNullIri() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.PROPERTY_GROUP, new TTArray()
                        .add(new TTNode()
                                .set(IM.INHERITED_FROM, new TTIriRef("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                                .set(SHACL.PROPERTY, new TTArray()
                                        .add(new TTNode()
                                                .set(SHACL.PATH, new TTIriRef("http://endhealth.info/im#administrativeGender","Gender"))
                                                .set(SHACL.MINCOUNT, new TTLiteral(1))
                                                .set(SHACL.MAXCOUNT, new TTLiteral(10))
                                                .set(SHACL.CLASS, new TTIriRef("http://endhealth.info/im#Class","Class"))
                                                .set(SHACL.DATATYPE, new TTIriRef("http://endhealth.info/im#DataType","DataType"))
                                        ))));
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        List<PropertyValue> actual = conceptService.getAllProperties("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getAllProperties_NullInheritedFrom() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.PROPERTY_GROUP, new TTArray()
                        .add(new TTNode()
                                .set(SHACL.PROPERTY, new TTArray()
                                        .add(new TTNode()
                                                .set(SHACL.PATH, new TTIriRef("http://endhealth.info/im#administrativeGender","Gender"))
                                                .set(SHACL.MINCOUNT, new TTLiteral(1))
                                                .set(SHACL.MAXCOUNT, new TTLiteral(10))
                                                .set(SHACL.CLASS, new TTIriRef("http://endhealth.info/im#Class","Class"))
                                                .set(SHACL.DATATYPE, new TTIriRef("http://endhealth.info/im#DataType","DataType"))
                                        ))));
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        List<PropertyValue> actual = conceptService.getAllProperties("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getRoles_NullIri() throws SQLException, JsonProcessingException {
        List<PropertyValue> actual = conceptService.getRoles(null);
        assertNotNull(actual);
    }

    @Test
    public void getRoles_NotNullIri() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.ROLE_GROUP, new TTArray()
                        .add(new TTNode()
                                .setPredicateMap(new HashMap<>())));
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        List<PropertyValue> actual = conceptService.getRoles("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NullIri() throws SQLException {
        String actual = conceptService.valueSetMembersCSV(null, true);
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NotNullIriExpandTrue() throws SQLException {
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000552115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000652115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(valueSetRepository.expandMember(any())).thenReturn(Collections.singletonList(valueSetMember));
        TTIriRef ttIriRef= new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(conceptRepository.getConceptReferenceByIri(any())).thenReturn(ttIriRef);
        String actual = conceptService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", true);
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NotNullIriExpandFalse() throws SQLException {
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setConcept(iri("http://endhealth.info/im#25451000552115","Adverse reaction to Amlodipine Besilate"))
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105","Discovery code"));

        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember1));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(valueSetMember2));
        TTIriRef ttIriRef= new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(conceptRepository.getConceptReferenceByIri(any())).thenReturn(ttIriRef);
        String actual = conceptService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", false);
        assertNotNull(actual);
    }


    @Test
    public void getGraphData_NullIri() throws SQLException, JsonProcessingException {
        GraphDto actual = conceptService.getGraphData(null);
        assertNull(actual);
    }

    @Test
    public void getGraphData_NotNullIri() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.IS_A, new TTArray().add(new TTIriRef("http://endhealth.info/im#25451000252115")))
                .set(IM.IS_CONTAINED_IN, new TTArray().add(new TTIriRef("http://endhealth.info/im#25451000252115")))
                .set(IM.ROLE_GROUP, new TTArray().add(new TTNode()
                        .set(RDFS.LABEL, new TTIriRef("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                        .set(RDFS.COMMENT, new TTIriRef("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"))
                ))
                .set(IM.PROPERTY_GROUP, new TTArray()
                        .add(new TTNode()
                                .set(IM.INHERITED_FROM,new TTIriRef("http://endhealth.info/im#25451000252115"))
                                .set(SHACL.PROPERTY, new TTArray()
                                        .add(new TTNode()
                                                .set(SHACL.PATH, new TTIriRef("http://endhealth.info/im#25451000252115"))
                                                .set(SHACL.MINCOUNT, new TTLiteral(1))
                                                .set(SHACL.MAXCOUNT, new TTLiteral(10))
                                                .set(SHACL.CLASS, new TTIriRef("http://endhealth.info/im#Class","Class"))
                                                .set(SHACL.DATATYPE, new TTIriRef("http://endhealth.info/im#DataType","DataType"))
                                        ))));
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NullParent() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NotNullParent() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.IS_A, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
                .set(IM.IS_CONTAINED_IN, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"));
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }


    @Test
    public void getGraphData_NullInheritedFrom() throws JsonProcessingException, SQLException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setType(new TTArray())
                .set(IM.IS_A, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
                .set(IM.IS_CONTAINED_IN, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
                .set(IM.PROPERTY_GROUP, new TTArray()
                        .add(new TTNode()
                                .set(SHACL.PROPERTY, new TTArray()
                                        .add(new TTNode()
                                                .set(SHACL.PATH, new TTIriRef("http://endhealth.info/im#25451000252115"))
                                                .set(SHACL.MINCOUNT, new TTLiteral(1))
                                                .set(SHACL.MAXCOUNT, new TTLiteral(10))
                                                .set(SHACL.CLASS, new TTIriRef("http://endhealth.info/im#Class","Class"))
                                                .set(SHACL.DATATYPE, new TTIriRef("http://endhealth.info/im#DataType","DataType"))
                                        ))));
        when(conceptRepository.getConceptByIri(any())).thenReturn(ttConcept);
        TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(conceptTypeRepository.getConceptTypes(any())).thenReturn(ttArray);
        ConceptReferenceNode conceptReferenceNode = new ConceptReferenceNode()
                .setChildren(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new ConceptReferenceNode("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",
                0,20,false))
                .thenReturn(Collections.singletonList(conceptReferenceNode));
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

}
