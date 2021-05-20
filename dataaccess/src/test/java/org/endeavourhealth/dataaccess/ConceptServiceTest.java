package org.endeavourhealth.dataaccess;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.graph.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.http.HttpEntity;
import org.endeavourhealth.dataaccess.entity.*;
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

    @Test
    public void getConcept_NullConcept(){
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTConcept actual = conceptService.getConcept("http://endhealth.info/im#25451000252115") ;

        assertNull(actual);

    }
    @Test
    public void getConcept_NullJson(){
        Concept concept = new Concept().setJson(null);
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        TTConcept actual = conceptService.getConcept("http://endhealth.info/im#25451000252115") ;

        assertNull(actual);

    }

    @Test
    public void getConcept_NullName(){
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        Concept unnamedParent = new Concept().setIri("http://snomed.info/sct#62014003").setName("Adverse reaction caused by drug (disorder)");
        when(conceptRepository.findAllByIriIn( anySet()))
                .thenReturn(Collections.singletonList(unnamedParent));
        String name = "Adverse reaction caused by drug (disorder)";
        TTConcept actual = conceptService.getConcept("http://endhealth.info/im#25451000252115") ;

        assertEquals(name, actual.getAsArray(IM.IS_A).get(0).asIriRef().getName());

    }

    @Test
    public void getConceptReference_NullIri(){
        TTIriRef actual = conceptService.getConceptReference(null);

        assertNull(actual);

    }

    @Test
    public void getConceptReference_NullConcept(){
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTIriRef actual = conceptService.getConceptReference("http://endhealth.info/im#25451000252115");

        assertNull(actual);

    }

    @Test
    public void getConceptReference_NotNullConcept(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        TTIriRef actual = conceptService.getConceptReference("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NullIri(){
        List<ConceptReferenceNode> actual = conceptService
                .getImmediateChildren(null, 1, 10, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NotNullIriAndInactiveTrue(){
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept().setIri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri( any(),any())).thenReturn(tplList);
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        List<ConceptReferenceNode> actual = conceptService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 10, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NotNullIriAndInactiveFalse(){
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setStatus(new Concept().setIri("http://endhealth.info/im#Active")))
                .setObject(new Concept().setIri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri(any(),any())).thenReturn(tplList);
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        List<ConceptReferenceNode> actual = conceptService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 10, true,false);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NullIri(){
        List<ConceptReferenceNode> actual = conceptService
                .getImmediateParents(null, 1, 10, true,true);

        assertNotNull(actual);
    }

    @Test
    public void getImmediateParents_NotNullIriAndInactiveTrue(){
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept().setIri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateParentsByIri( any(),any())).thenReturn(tplList);
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        List<ConceptReferenceNode> actual = conceptService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 10, true,true);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NotNullIriAndInactiveFalse(){
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setStatus(new Concept().setIri("http://endhealth.info/im#Active"))));
        when(conceptTripleRepository.findImmediateParentsByIri( any(),any())).thenReturn(tplList);
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
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
    public void usages_NullIri(){
        List<TTIriRef> actual = conceptService.usages(null);

        assertNotNull(actual);
    }

    @Test
    public void usages_NotNullIriAndNullScheme(){
        Tpl tpl = new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(null));
        when(conceptTripleRepository.findDistinctByObject_IriAndPredicate_IriNot( any(), any())).thenReturn(Collections.singleton(tpl));

        List<TTIriRef> actual = conceptService.usages("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void usages_NotNullIriAndNotNullScheme(){

        Tpl tpl = new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#25451000252115")
                                .setName("Adverse reaction to Amlodipine Besilate")));
        when(conceptTripleRepository.findDistinctByObject_IriAndPredicate_IriNot( any(), any())).thenReturn(Collections.singleton(tpl));

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
    public void getValueSetMembers_NullIri(){
        ExportValueSet actual = conceptService.getValueSetMembers(null, true);

        assertNull(actual);

    }

    @Test
    public void getValueSetMembers_ExpandTrue(){

        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));
        ValueSetMember valueSetMember = new ValueSetMember()
                .setConceptIri("http://endhealth.info/im#25451000252115")
                .setConceptName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setSchemeIri("http://endhealth.info/im#891071000252105")
                .setSchemeName("Discovery code");

        when(valueSetRepository.expandMember(any())).thenReturn(Collections.singletonList(valueSetMember));
        ExportValueSet actual = conceptService.getValueSetMembers("http://endhealth.info/im#25451000252115", true);

        assertNotNull(actual);

    }

    @Test
    public void getValueSetMembers_ExpandFalse(){

        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));
        ExportValueSet actual = conceptService.getValueSetMembers("http://endhealth.info/im#25451000252115", false);

        assertNotNull(actual);

    }

    @Test
    public void isValuesetMember_NullIriAndMember(){
        ValueSetMembership actual = conceptService.isValuesetMember(null, null);

        assertNull(actual);
    }

    @Test
    public void isValuesetMember_NotNullIriAndHasMember(){
        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));

        ValueSetMember valueSetMember1 = new ValueSetMember()
                .setConceptIri(IM.HAS_MEMBER.getIri());
        ValueSetMember valueSetMember2 = new ValueSetMember()
                .setConceptIri(IM.NOT_MEMBER.getIri());
        when(valueSetRepository.expandMember(tpl1.getObject().getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(tpl2.getObject().getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = conceptService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.HAS_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    public void isValuesetMember_NotNullIriAndNotMember(){
        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));

        ValueSetMember valueSetMember1 = new ValueSetMember()
                .setConceptIri(IM.HAS_MEMBER.getIri());
        ValueSetMember valueSetMember2 = new ValueSetMember()
                .setConceptIri(IM.NOT_MEMBER.getIri());
        when(valueSetRepository.expandMember(tpl1.getObject().getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(valueSetRepository.expandMember(tpl2.getObject().getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = conceptService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.NOT_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    public void getCoreMappedFromLegacy_NullIri(){
        List<TTIriRef> actual = conceptService.getCoreMappedFromLegacy(null);

        assertNotNull(actual);
    }

    @Test
    public void getCoreMappedFromLegacy_NotNullIri(){
        Tpl tpl = new Tpl()
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(Collections.singleton(tpl));

        List<TTIriRef> actual = conceptService.getCoreMappedFromLegacy("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void getLegacyMappedToCore_NullIri(){
        List<TTIriRef> actual = conceptService.getLegacyMappedToCore(null);

        assertNotNull(actual);
    }

    @Test
    public void getLegacyMappedToCore_NotNullIri(){
        Tpl tpl = new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));

        when(conceptTripleRepository.findAllByObject_Iri_AndPredicate_Iri(any(),any())).thenReturn(Collections.singleton(tpl));

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
    public void download_NullIri(){
        HttpEntity actual = conceptService.download(null, "excel", true, true, true ,true,
                true, true);

        assertNull(actual);

    }

    @Test
    public void download_EmptyIri(){
        HttpEntity actual = conceptService.download("", "excel", true, true, true ,true,
                true, true);

        assertNull(actual);

    }

    @Test
    public void download_NullFormat(){
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", null, true,
                true, true ,true, true, true);

        assertNull(actual);

    }

    @Test
    public void download_EmptyFormat(){
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", "", true,
                true, true ,true, true, true);

        assertNull(actual);

    }

    @Test
    public void download_AllSelectionsTrueExcelFormat(){

        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept().setIri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri( any(),any())).thenReturn(tplList);
        when(conceptTripleRepository.findImmediateParentsByIri( any(),any())).thenReturn(tplList);
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", "excel", true,
                true, true ,true, true, true);

        assertNotNull(actual);

    }

    @Test
    public void download_AllSelectionsTrueJsonFormat(){

        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept().setIri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri( any(),any())).thenReturn(tplList);
        when(conceptTripleRepository.findImmediateParentsByIri( any(),any())).thenReturn(tplList);
        Concept concept = new Concept().setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}");
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));
        HttpEntity actual = conceptService.download("http://endhealth.info/im#25451000252115", "json", true,
                true, true ,true, true, true);

        assertNotNull(actual);

    }

    @Test
    public void getAllProperties_NullIri(){
        List<PropertyValue> actual = conceptService.getAllProperties((String) null);
        assertNotNull(actual);
    }

    @Test
    public void getAllProperties_NotNullIri() throws JsonProcessingException {
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
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        List<PropertyValue> actual = conceptService.getAllProperties("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getAllProperties_NullInheritedFrom() throws JsonProcessingException {
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
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        List<PropertyValue> actual = conceptService.getAllProperties("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getRoles_NullIri(){
        List<PropertyValue> actual = conceptService.getRoles(null);
        assertNotNull(actual);
    }

    @Test
    public void getRoles_NotNullIri() throws JsonProcessingException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.ROLE_GROUP, new TTArray()
                        .add(new TTNode()
                                .setPredicateMap(new HashMap<>())));
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        List<PropertyValue> actual = conceptService.getRoles("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NullIri(){
        String actual = conceptService.valueSetMembersCSV(null, true);
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NotNullIriExpandTrue(){
        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000552115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));
        ValueSetMember valueSetMember = new ValueSetMember()
                .setConceptIri("http://endhealth.info/im#25451000652115")
                .setConceptName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setSchemeIri("http://endhealth.info/im#891071000252105")
                .setSchemeName("Discovery code");

        when(valueSetRepository.expandMember(any())).thenReturn(Collections.singletonList(valueSetMember));
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        String actual = conceptService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", true);
        assertNotNull(actual);
    }

    @Test
    public void valueSetMembersCSV_NotNullIriExpandFalse(){
        Tpl tpl1 = new Tpl()
                .setPredicate(new Concept().setIri(IM.HAS_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));
        Tpl tpl2 = new Tpl()
                .setPredicate(new Concept().setIri(IM.NOT_MEMBER.getIri()))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000552115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#891071000252105")
                                .setName("Discovery code")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.HAS_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl1));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(tpl2));
//        ValueSetMember valueSetMember = new ValueSetMember()
//                .setConceptIri("http://endhealth.info/im#25451000652115")
//                .setConceptName("Adverse reaction to Amlodipine Besilate")
//                .setCode("25451000252115")
//                .setSchemeIri("http://endhealth.info/im#891071000252105")
//                .setSchemeName("Discovery code");
//        when(valueSetRepository.expandMember(any())).thenReturn(Collections.singletonList(valueSetMember));
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115").setName("Adverse reaction to Amlodipine Besilate");
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        String actual = conceptService.valueSetMembersCSV("http://endhealth.info/im#25451000252115", false);
        assertNotNull(actual);
    }


    @Test
    public void getGraphData_NullIri(){
        GraphDto actual = conceptService.getGraphData(null);
        assertNull(actual);
    }

    @Test
    public void getGraphData_NotNullIri() throws JsonProcessingException {
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
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NullParent() throws JsonProcessingException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115");
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getGraphData_NotNullParent() throws JsonProcessingException {
        TTConcept ttConcept = new TTConcept()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(IM.IS_A, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
                .set(IM.IS_CONTAINED_IN, new TTIriRef().setIri("http://endhealth.info/im#25451000252115"));
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }


    @Test
    public void getGraphData_NullInheritedFrom() throws JsonProcessingException {
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
        String json = new ObjectMapper().writeValueAsString(ttConcept);
        Concept concept = new Concept().setJson(json);
        when(conceptRepository.findByIri(any())).thenReturn(concept);
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setStatus(new Concept().setIri("http://endhealth.info/im#Active")))
                .setObject(new Concept().setIri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.findImmediateChildrenByIri(any(),any())).thenReturn(tplList);
        GraphDto actual = conceptService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }











}
