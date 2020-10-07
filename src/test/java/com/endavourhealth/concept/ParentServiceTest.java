package com.endavourhealth.concept;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.endavourhealth.concept.models.ConceptTreeNode;
import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;

class ParentServiceTest {

	private static final Integer IS_A_CONCEPT_DB_ID = 99;
	private static final Integer NOT_IS_A_CONCEPT_DB_ID = IS_A_CONCEPT_DB_ID + 1;
	// do not use to set up test data for mocked services to use - meant for ad-hoc testing
	private static final Integer RESERVED_CONCEPT_DB_ID = NOT_IS_A_CONCEPT_DB_ID + 1;

	ParentService parentService;
	
	TestData multipleInheritanceTestData;
	TestData singleInheritanceTestData;
	TestData noConceptPropertyObjects;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setUp() {
	
		// the test data and dependencies that use that data
		
		// multi inheritance (uses concept Ids 1-12)
		multipleInheritanceTestData = setupMultiInheritanceTestData(1);
		// single inheritance (uses concept Ids 13-15)
		singleInheritanceTestData = setupSingleInheritanceWithOtherPropertiesTestData(13);
		// no CPO data (uses concept Ids 16-16)
		noConceptPropertyObjects = setupNoConceptPropertyObjects(16);

		IdentifierService identifierService = setUpIdentifierService(TestData.mergeAllConcepts(multipleInheritanceTestData.concepts, singleInheritanceTestData.concepts, noConceptPropertyObjects.concepts));
		ConceptPropertyObjectRepository conceptPropertyObjectRepository = setUpConceptPropertyObjectRepository(TestData.mergeAllConceptPropertyObject(multipleInheritanceTestData.conceptPropertyObjects, singleInheritanceTestData.conceptPropertyObjects, noConceptPropertyObjects.conceptPropertyObjects));		

		// service under test
		parentService = new ParentService(conceptPropertyObjectRepository, identifierService, initConceptEntity(IS_A_CONCEPT_DB_ID));
	}

	
	// builds a graph using concepts 1 to 12 where 1 is the leaf and 12 is one of the roots
	@SuppressWarnings("unused")
	private TestData setupMultiInheritanceTestData(int leafConceptDbId) {
		TestData testData = new TestData();	
		
		testData.leafConceptDbId = leafConceptDbId;
		testData.leafConcept = initConceptModel(leafConceptDbId);
		testData.concepts = initConceptModels(leafConceptDbId, 12);
				
		// ConceptTreeNode version
		ConceptTreeNode conceptTreeNode = new ConceptTreeNode(initConceptModel(leafConceptDbId)); // leaf
		ConceptTreeNode two = initParentConceptTreeNode(conceptTreeNode, 2); // branch
		
		ConceptTreeNode three = initParentConceptTreeNode(two, 3);
		ConceptTreeNode five = initParentConceptTreeNode(three, 5); // root
		
		ConceptTreeNode four = initParentConceptTreeNode(two, 4);
		ConceptTreeNode six = initParentConceptTreeNode(four, 6); // branch
		
		ConceptTreeNode eight = initParentConceptTreeNode(six, 8);
		ConceptTreeNode nine = initParentConceptTreeNode(eight, 9); // root
		
		ConceptTreeNode seven = initParentConceptTreeNode(six, 7);
		ConceptTreeNode ten = initParentConceptTreeNode(seven, 10); // branch
		
		ConceptTreeNode eleven = initParentConceptTreeNode(ten, 11); // root
		
		ConceptTreeNode twelve = initParentConceptTreeNode(ten, 12); // root
		testData.conceptTreeNode = conceptTreeNode;
		
		// ConceptObjectProperty version
		Map<Integer, List<ConceptPropertyObject>> conceptPropertyObjects = new HashMap<Integer, List<ConceptPropertyObject>>();		
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(leafConceptDbId, 2)); // branch
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(2, 3));	
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(3, 5)); // root
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(2, 4));	
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(4, 6)); // branch
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(6, 8));	
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(8, 9)); // root
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(6, 7));	
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(7, 10)); // branch
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(10, 11)); // root
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(10, 12)); // root
		testData.conceptPropertyObjects = conceptPropertyObjects;
		
		return testData;	
	}
	
	// builds a graph using concepts 13, 14 and 15 where 13 is the leaf, 15 is the root and 14 is not part of the inheritance hierarchy 
	@SuppressWarnings("unused")
	private TestData setupSingleInheritanceWithOtherPropertiesTestData(int leafConceptDbId) {
		TestData testData = new TestData();	
		
		testData.leafConceptDbId = leafConceptDbId;
		testData.leafConcept = initConceptModel(leafConceptDbId);
		testData.concepts = initConceptModels(leafConceptDbId, 3);
		
		// ConceptTreeNode version
		ConceptTreeNode conceptTreeNode = new ConceptTreeNode(initConceptModel(leafConceptDbId)); // leaf
		ConceptTreeNode two = initParentConceptTreeNode(conceptTreeNode, 15); // root
		testData.conceptTreeNode = conceptTreeNode;
		
		// ConceptObjectProperty version
		Map<Integer, List<ConceptPropertyObject>> conceptPropertyObjects = new HashMap<Integer, List<ConceptPropertyObject>>();	
		
		addConceptPropertyObject(conceptPropertyObjects, initIsAConceptPropertyObject(leafConceptDbId, 15)); // branch
		addConceptPropertyObject(conceptPropertyObjects, initConceptPropertyObject(13, 14, NOT_IS_A_CONCEPT_DB_ID)); // not an is-a property - not part of the inheritance hierarchy 		
		testData.conceptPropertyObjects = conceptPropertyObjects;
		
		return testData;	
	} 
	
	// builds a test set where there are no entries in the COP table for the concept
	private TestData setupNoConceptPropertyObjects(int leafConceptDbId) {
		TestData testData = new TestData();	
		
		testData.leafConceptDbId = leafConceptDbId;
		testData.leafConcept = initConceptModel(leafConceptDbId);
		testData.concepts = initConceptModels(leafConceptDbId, 1);
		
		// ConceptObjectPropertys - empty
		Map<Integer, List<ConceptPropertyObject>> conceptPropertyObjects = new HashMap<Integer, List<ConceptPropertyObject>>();		
		testData.conceptPropertyObjects = conceptPropertyObjects;
		
		return testData;	
	} 	
	
	private void addConceptPropertyObject(Map<Integer, List<ConceptPropertyObject>> copMap, ConceptPropertyObject cop) {
		Integer concept = cop.getConcept();

		List<ConceptPropertyObject> cops;
		if(copMap.containsKey(concept)) {
			cops = copMap.get(concept);
		}
		else {
			cops = new ArrayList<ConceptPropertyObject>();
			copMap.put(concept, cops);
		}
		
		cops.add(cop);		
	}
	
	private ConceptPropertyObject initIsAConceptPropertyObject(int childDbId, int parentDbId) {
		return initConceptPropertyObject(childDbId, parentDbId, IS_A_CONCEPT_DB_ID);	
	}
	
	private ConceptPropertyObject initConceptPropertyObject(int childDbId, int parentDbId, int propertyDbId) {
		ConceptPropertyObject cop = new ConceptPropertyObject();
		cop.setConcept(childDbId);
		cop.setProperty(propertyDbId);
		cop.setObject(parentDbId);
		
		return cop;		
	}	
	
	private ConceptTreeNode initParentConceptTreeNode(ConceptTreeNode tree, Integer parentIri) {			
		ConceptTreeNode parentTree = new ConceptTreeNode(initConceptModel(parentIri), tree);
	
		return parentTree;
	}
	
	private ConceptPropertyObjectRepository setUpConceptPropertyObjectRepository(Map<Integer, List<ConceptPropertyObject>> copMap) {
		ConceptPropertyObjectRepository conceptPropertyObjectRepository = Mockito.mock(ConceptPropertyObjectRepository.class);			
		
		Mockito.when(conceptPropertyObjectRepository.findByConcept(Mockito.anyInt())).thenAnswer(new Answer<List<ConceptPropertyObject>>() {

			@Override
			public List<ConceptPropertyObject> answer(InvocationOnMock invocation) throws Throwable {
				Integer dbId = (Integer) invocation.getArguments()[0];
				
				return copMap.get(dbId);
			}

		});		
		
		return conceptPropertyObjectRepository;
	}
	
	private IdentifierService setUpIdentifierService(Map<String, com.endavourhealth.concept.models.Concept> concepts) {
		IdentifierService identifierService = Mockito.mock(IdentifierService.class);
		
		// concept model keyed by IRI. Note that IRI and DBID represent the same int value

		
		Mockito.when(identifierService.getConcept(Mockito.anyInt())).thenAnswer(new Answer<com.endavourhealth.concept.models.Concept>() {

			@Override
			public com.endavourhealth.concept.models.Concept answer(InvocationOnMock invocation) throws Throwable {
				Integer dbId = (Integer) invocation.getArguments()[0];
				
				return concepts.get(dbId.toString());
			}

		});	
		
		Mockito.when(identifierService.getConcept(Mockito.anyString())).thenAnswer(new Answer<com.endavourhealth.concept.models.Concept>() {

			@Override
			public com.endavourhealth.concept.models.Concept answer(InvocationOnMock invocation) throws Throwable {
				String iri = (String) invocation.getArguments()[0];
				
				return concepts.get(iri.toString());
			}

		});	
		
		Mockito.when(identifierService.getDbId(Mockito.any(com.endavourhealth.concept.models.Concept.class))).thenAnswer(new Answer<Integer>() {

			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				com.endavourhealth.concept.models.Concept concept = (com.endavourhealth.concept.models.Concept) invocation.getArguments()[0];
				
				return Integer.parseInt(concept.getIri());
			}

		});	
		
		return identifierService;
	}
	
	// concept model keyed by IRI. Note that IRI and DBID represent the same int value
	private Map<String, com.endavourhealth.concept.models.Concept> initConceptModels(int startId, int conceptCount) {
		Map<String, com.endavourhealth.concept.models.Concept> concepts = new HashMap<String, com.endavourhealth.concept.models.Concept>();
		
		for(int c = 0; c < conceptCount; c++) {
			Integer iri = c + startId;			
			concepts.put(iri.toString(), initConceptModel(iri));
		}
		
		return concepts;
	}
	
	private com.endavourhealth.concept.models.Concept initConceptModel(Integer iri) {
		return new com.endavourhealth.concept.models.Concept(iri.toString());
	}	
	
	private Concept initConceptEntity(Integer dbid) {
		Concept entity = new Concept();
		entity.setDbid(dbid);
		entity.setIri(dbid.toString());
		
		return entity;
	}
		
	@Test
	void testAddParentsWithMultipleInheritance() {
		assertNull(multipleInheritanceTestData.leafConcept.getTree());
		
		parentService.addParents(multipleInheritanceTestData.leafConcept, multipleInheritanceTestData.leafConceptDbId);
		
		assertNotNull(multipleInheritanceTestData.leafConcept.getTree());
		assertTrue(multipleInheritanceTestData.leafConcept.getTree().deepEquals(multipleInheritanceTestData.conceptTreeNode));
	}
	
	@Test
	// tests that the tree building ignores COP that are not of type "Is a" ie not part of the inheritance hierarchy 
	void testAddParentsWithSingleInheritanceWithOtherProperties() {
		assertNull(singleInheritanceTestData.leafConcept.getTree());
		
		parentService.addParents(singleInheritanceTestData.leafConcept, singleInheritanceTestData.leafConceptDbId);
		
		assertNotNull(singleInheritanceTestData.leafConcept.getTree());
		assertTrue(singleInheritanceTestData.leafConcept.getTree().deepEquals(singleInheritanceTestData.conceptTreeNode));
	}
	
	@Test
	// tests that the tree building ignores COP that are not of type "Is a" ie not part of the inheritance hierarchy 
	void testAddParentsWithNoConceptPropertyObjects() {
		assertNull(noConceptPropertyObjects.leafConcept.getTree());
		
		parentService.addParents(noConceptPropertyObjects.leafConcept, singleInheritanceTestData.leafConceptDbId);
		
		assertNull(singleInheritanceTestData.leafConcept.getTree());
	}		
	
	@Test
	void testAddParentsWithNullConcept() {
		parentService.addParents(null, RESERVED_CONCEPT_DB_ID);
	}

	@Test
	void testAddParentsWithNullConceptDbId() {
		parentService.addParents(initConceptModel(RESERVED_CONCEPT_DB_ID), null);
	}
	
	private static class TestData {
		Integer leafConceptDbId;
		com.endavourhealth.concept.models.Concept leafConcept;
		Map<String, com.endavourhealth.concept.models.Concept> concepts;
		ConceptTreeNode conceptTreeNode;
		Map<Integer, List<ConceptPropertyObject>> conceptPropertyObjects;
		
		@SuppressWarnings("unchecked")
		private static Map<String, com.endavourhealth.concept.models.Concept> mergeAllConcepts(Map<String, com.endavourhealth.concept.models.Concept> ... conceptMaps) {
			Map<String, com.endavourhealth.concept.models.Concept> merged = new HashMap<String, com.endavourhealth.concept.models.Concept>();
			
			for(Map<String, com.endavourhealth.concept.models.Concept> conceptMap : conceptMaps) {
				merged.putAll(conceptMap);
			}
			
			return merged;
		}
		
		@SuppressWarnings("unchecked")
		private static Map<Integer, List<ConceptPropertyObject>> mergeAllConceptPropertyObject(Map<Integer, List<ConceptPropertyObject>> ... conceptPropertyObjectsMaps) {
			Map<Integer, List<ConceptPropertyObject>> merged = new HashMap<Integer, List<ConceptPropertyObject>>();
			
			for(Map<Integer, List<ConceptPropertyObject>> conceptPropertyObjectsMap : conceptPropertyObjectsMaps) {
				merged.putAll(conceptPropertyObjectsMap);
			}
			
			return merged;
		}
	}
	
	
}