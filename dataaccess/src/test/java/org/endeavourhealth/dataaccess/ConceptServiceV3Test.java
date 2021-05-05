package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.entity.*;
import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConceptServiceV3Test {
    @InjectMocks
    ConceptServiceV3 conceptServiceV3;

    @Mock
    ConceptRepository conceptRepository;

    @Mock
    ConceptTripleRepository conceptTripleRepository;

    @Mock
    ConceptTctRepository conceptTctRepository;

    @Mock
    ConceptSearchRepository conceptSearchRepository;

    @Mock
    ConceptTypeRepository conceptTypeRepository;

    @Mock
    ValueSetRepository valueSetRepository;

    @Mock
    ConceptTermRepository conceptTermRepository;

    @Test
    public void getConcept_NullConcept(){
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTConcept actual = conceptServiceV3.getConcept("http://endhealth.info/im#25451000252115") ;

        assertNull(actual);

    }
    @Test
    public void getConcept_NullJson(){
        Concept concept = new Concept().setJson(null);
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        TTConcept actual = conceptServiceV3.getConcept("http://endhealth.info/im#25451000252115") ;

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
        TTConcept actual = conceptServiceV3.getConcept("http://endhealth.info/im#25451000252115") ;

        assertEquals(name, actual.getAsArray(IM.IS_A).get(0).asIriRef().getName());

    }

    @Test
    public void getConceptReference_NullIri(){
        TTIriRef actual = conceptServiceV3.getConceptReference(null);

        assertNull(actual);

    }

    @Test
    public void getConceptReference_NullConcept(){
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTIriRef actual = conceptServiceV3.getConceptReference("http://endhealth.info/im#25451000252115");

        assertNull(actual);

    }

    @Test
    public void getConceptReference_NotNullConcept(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByIri("http://endhealth.info/im#25451000252115")).thenReturn(concept);
        TTIriRef actual = conceptServiceV3.getConceptReference("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    public void getImmediateChildren_NullIri(){
        List<ConceptReferenceNode> actual = conceptServiceV3
                .getImmediateChildren(null, 1, 10, true,true);

        assertNull(actual);

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
        List<ConceptReferenceNode> actual = conceptServiceV3.getImmediateChildren
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
        List<ConceptReferenceNode> actual = conceptServiceV3.getImmediateChildren
                ("http://endhealth.info/im#25451000252115", 1, 10, true,false);

        assertNotNull(actual);

    }

    @Test
    public void getImmediateParents_NullIri(){
        List<ConceptReferenceNode> actual = conceptServiceV3
                .getImmediateParents(null, 1, 10, true,true);

        assertNull(actual);
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
        List<ConceptReferenceNode> actual = conceptServiceV3.getImmediateParents
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
        List<ConceptReferenceNode> actual = conceptServiceV3.getImmediateParents
                ("http://endhealth.info/im#25451000252115", 1, 10, true,false);

        assertNotNull(actual);

    }

    @Test
    public void isWhichType_NullIri(){
        List<TTIriRef> actual = conceptServiceV3
                .isWhichType(null, Arrays.asList("A","B"));

        assertNull(actual);
    }

    @Test
    public void isWhichType_EmptyCandidates(){
        List<TTIriRef> actual = conceptServiceV3
                .isWhichType("http://endhealth.info/im#25451000252115", Collections.emptyList());

        assertNull(actual);
    }

    @Test
    public void isWhichType_NullCandidates(){
        List<TTIriRef> actual = conceptServiceV3
                .isWhichType("http://endhealth.info/im#25451000252115", null);

        assertNull(actual);
    }

    @Test
    public void isWhichType_NullIriAndCandidates(){
        List<TTIriRef> actual = conceptServiceV3
                .isWhichType(null, null);

        assertNull(actual);
    }

    @Test
    public void isWhichType_NotNullIriAndCandidates(){
        Tct tct = new Tct()
                .setAncestor(new Concept()
                    .setIri("http://www.w3.org/2002/07/owl#Class")
                    .setName("Class"));

        when(conceptTctRepository.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(any(),any(),any()))
                .thenReturn(Collections.singleton(tct));

        List<TTIriRef> actual = conceptServiceV3
                .isWhichType("http://endhealth.info/im#25451000252115",
                        Collections.singletonList("http://endhealth.info/im#25451000252115"));

        assertNotNull(actual);
    }

    @Test
    public void usages_NullIri(){
        List<ConceptSummary> actual = conceptServiceV3.usages(null);

        assertNull(actual);
    }

    @Test
    public void usages_NotNullIriAndNullScheme(){
        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"))
                .setDescendant(new Concept().setIri("http://endhealth.info/im#25451000252115"));
        when(conceptTctRepository.findByAncestor_Iri_AndType_Iri( any(),any())).thenReturn(Collections.singleton(tct));

        Tpl tpl = new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(null));
        when(conceptTripleRepository.findAllByObject_Iri( any())).thenReturn(Collections.singleton(tpl));

        List<ConceptSummary> actual = conceptServiceV3.usages("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void usages_NotNullIriAndNotNullScheme(){
        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"))
                .setDescendant(new Concept().setIri("http://www.w3.org/2002/07/owl#Class"));
        when(conceptTctRepository.findByAncestor_Iri_AndType_Iri( any(),any())).thenReturn(Collections.singleton(tct));

        Tpl tpl = new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate")
                        .setCode("25451000252115")
                        .setScheme(new Concept()
                                .setIri("http://endhealth.info/im#25451000252115")
                                .setName("Adverse reaction to Amlodipine Besilate")));
        when(conceptTripleRepository.findAllByObject_Iri( any())).thenReturn(Collections.singleton(tpl));

        List<ConceptSummary> actual = conceptServiceV3.usages("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NullRequest(){
        List<ConceptSummary> actual = conceptServiceV3.advancedSearch(null);

        assertNull(actual);
    }

    @Test
    public void advancedSearch_NullTermFilter(){
        SearchRequest searchRequest = new SearchRequest().setTermFilter(null);

        List<ConceptSummary> actual = conceptServiceV3.advancedSearch(searchRequest);

        assertNull(actual);
    }

    @Test
    public void advancedSearch_NullSchemeFilter(){
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(null);

        ConceptSearch conceptSearch = new ConceptSearch().setConcept(new Concept()
                .setDbid(12608)
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(new Concept().setIri("http://endhealth.info/im#891071000252105").setName("Discovery code"))
                .setStatus(new Concept().setIri("http://endhealth.info/im#Active").setName("Active")));
        when(conceptSearchRepository.findLegacyByTerm(any(),any(),any(),any()))
                .thenReturn(Collections.singletonList(conceptSearch));
        ConceptType conceptType = new ConceptType()
                .setType(new Concept().setIri("http://www.w3.org/2002/07/owl#Class").setName("Class"));
        when(conceptTypeRepository.findAllByConcept_Dbid(any())).thenReturn(Collections.singletonList(conceptType));

        List<ConceptSummary> actual = conceptServiceV3.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NotNullSchemeFilter(){
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        ConceptSearch conceptSearch = new ConceptSearch().setConcept(new Concept()
                .setDbid(12608)
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(new Concept().setIri("http://endhealth.info/im#891071000252105").setName("Discovery code"))
                .setStatus(new Concept().setIri("http://endhealth.info/im#Active").setName("Active")));
        when(conceptSearchRepository.findLegacySchemesByTerm(any(),any(),any(),any(),any()))
                .thenReturn(Collections.singletonList(conceptSearch));
        ConceptType conceptType = new ConceptType()
                .setType(new Concept().setIri("http://www.w3.org/2002/07/owl#Class").setName("Class"));
        when(conceptTypeRepository.findAllByConcept_Dbid(any())).thenReturn(Collections.singletonList(conceptType));

        List<ConceptSummary> actual = conceptServiceV3.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void advancedSearch_NotNullMarkIfDescendentOf(){
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setMarkIfDescendentOf(Arrays.asList(":DiscoveryCommonDataModel", ":SemanticConcept", ":VSET_ValueSet"))
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
                        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        ConceptSearch conceptSearch = new ConceptSearch().setConcept(new Concept()
                .setDbid(12608)
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(new Concept().setIri("http://endhealth.info/im#891071000252105").setName("Discovery code"))
                .setStatus(new Concept().setIri("http://endhealth.info/im#Active").setName("Active")));
        when(conceptSearchRepository.findLegacySchemesByTerm(any(),any(),any(),any(),any()))
                .thenReturn(Collections.singletonList(conceptSearch));
        ConceptType conceptType = new ConceptType()
                .setType(new Concept().setIri("http://www.w3.org/2002/07/owl#Class").setName("Class"));
        when(conceptTypeRepository.findAllByConcept_Dbid(any())).thenReturn(Collections.singletonList(conceptType));
        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://www.w3.org/2002/07/owl#Class")
                        .setName("Class"));
        when(conceptTctRepository.findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(any(),any(),any()))
                .thenReturn(Collections.singleton(tct));

        List<ConceptSummary> actual = conceptServiceV3.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    public void getAncestorDefinitions_NullIri(){
        List<TTConcept> actual = conceptServiceV3.getAncestorDefinitions(null);

        assertNull(actual);

    }

    @Test
    public void getAncestorDefinitions_NotNullIri(){
        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}"));

        when(conceptTctRepository.findByDescendant_Iri_AndType_OrderByLevel(any(),any())).thenReturn(Collections.singleton(tct));

        List<TTConcept> actual = conceptServiceV3.getAncestorDefinitions("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    public void getAncestorDefinitions_NotEqualIri(){
        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setJson("{\"@id\": \"http://endhealth.info/im#25451000252115\", \"http://endhealth.info/im#isA\": [{\"@id\": \"http://snomed.info/sct#62014003\"}], \"http://endhealth.info/im#code\": \"25451000252115\", \"http://endhealth.info/im#scheme\": {\"@id\": \"http://endhealth.info/im#891071000252105\"}, \"http://endhealth.info/im#status\": {\"@id\": \"http://endhealth.info/im#Active\"}, \"http://endhealth.info/im#roleGroup\": [{\"http://endhealth.info/im#role\": [{\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}], \"http://endhealth.info/im#groupNumber\": {\"@type\": \"http://www.w3.org/2001/XMLSchema#integer\", \"@value\": 0}}], \"http://www.w3.org/2000/01/rdf-schema#label\": \"Adverse reaction to Amlodipine Besilate\", \"http://www.w3.org/2002/07/owl#equivalentClass\": [{\"http://www.w3.org/2002/07/owl#intersectionOf\": [{\"@id\": \"http://snomed.info/sct#62014003\"}, {\"http://www.w3.org/2002/07/owl#onProperty\": {\"@id\": \"http://snomed.info/sct#246075003\"}, \"http://www.w3.org/2002/07/owl#someValuesFrom\": {\"@id\": \"http://snomed.info/sct#384976003\"}, \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": {\"@id\": \"http://www.w3.org/2002/07/owl#Restriction\"}}]}], \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\": [{\"@id\": \"http://www.w3.org/2002/07/owl#Class\"}]}"));

        when(conceptTctRepository.findByDescendant_Iri_AndType_OrderByLevel(any(),any())).thenReturn(Collections.singleton(tct));

        List<TTConcept> actual = conceptServiceV3.getAncestorDefinitions("http://endhealth.info/im#25451000552115");

        assertNotNull(actual);

    }

    @Test
    public void getAncestorDefinitions_NullJson(){
        Tct tct = new Tct()
                .setAncestor(new Concept()
                                .setIri("http://endhealth.info/im#25451000252115")
                                .setJson(null));

        when(conceptTctRepository.findByDescendant_Iri_AndType_OrderByLevel(any(),any())).thenReturn(Collections.singleton((tct)));

        List<TTConcept> actual = conceptServiceV3.getAncestorDefinitions("http://endhealth.info/im#25451000552115");

        assertNull(actual);

    }

    @Test
    public void getValueSetMembers_NullIri(){
        ExportValueSet actual = conceptServiceV3.getValueSetMembers(null, true);

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
        ExportValueSet actual = conceptServiceV3.getValueSetMembers("http://endhealth.info/im#25451000252115", true);

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

        ExportValueSet actual = conceptServiceV3.getValueSetMembers("http://endhealth.info/im#25451000252115", false);

        assertNotNull(actual);

    }

    @Test
    public void isValuesetMember_NullIriAndMember(){
        ValueSetMembership actual = conceptServiceV3.isValuesetMember(null, null);

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

        ValueSetMembership actual = conceptServiceV3.isValuesetMember("http://endhealth.info/im#25451000252115",
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

        ValueSetMembership actual = conceptServiceV3.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.NOT_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    public void getCoreMappedFromLegacy_NullIri(){
        List<TTIriRef> actual = conceptServiceV3.getCoreMappedFromLegacy(null);

        assertNull(actual);
    }

    @Test
    public void getCoreMappedFromLegacy_NotNullIri(){
        Tpl tpl = new Tpl()
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(Collections.singleton(tpl));

        List<TTIriRef> actual = conceptServiceV3.getCoreMappedFromLegacy("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void getLegacyMappedToCore_NullIri(){
        List<TTIriRef> actual = conceptServiceV3.getLegacyMappedToCore(null);

        assertNull(actual);
    }

    @Test
    public void getLegacyMappedToCore_NotNullIri(){
        Tpl tpl = new Tpl()
                .setSubject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));

        when(conceptTripleRepository.findAllByObject_Iri_AndPredicate_Iri(any(),any())).thenReturn(Collections.singleton(tpl));

        List<TTIriRef> actual = conceptServiceV3.getLegacyMappedToCore("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);
    }

    @Test
    public void getSynonyms_NullIri(){
        List<String> actual = conceptServiceV3.getSynonyms(null);
        assertNull(actual);
    }

    @Test
    public void getSynonyms_NotNullIri(){
        String term = "Adverse reaction to Amlodipine Besilate";

        when(conceptTermRepository.getSynonyms(any())).thenReturn(Collections.singletonList(term));

        List<String> actual = conceptServiceV3.getSynonyms("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }


}