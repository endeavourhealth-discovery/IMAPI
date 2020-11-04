package com.endavourhealth.concept;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.util.Pair;

import com.endavourhealth.concept.testutils.ConceptExamples;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.testutils.ConceptPropertyObjectExamples;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.services.concept.ConceptConverter;
import com.endavourhealth.services.concept.ParentService;
import com.endavourhealth.services.concept.models.Concept;

class ParentServiceTest {

	ParentService parentService;
	
	Pair<Concept, Concept> multipleInheritanceTestData;
	Pair<Concept, Concept> singleInheritanceTestData;
	Pair<Concept, Concept> noConceptPropertyObjects;
	
	Map<Integer, List<ConceptPropertyObject>> allCops;

	@BeforeEach
	public void setUp() {
	
		// service under test's dependencies
		allCops = new HashMap<Integer, List<ConceptPropertyObject>>();
		ConceptPropertyObjectRepository conceptPropertyObjectRepository = setUpConceptPropertyObjectRepository();	
		
		// service under test
		parentService = new ParentService();
//		parentService.conceptPropertyObjectRepository = conceptPropertyObjectRepository;
		parentService.conceptConverter = new ConceptConverter();
		parentService.isAConcept = com.endavourhealth.dataaccess.entity.testutils.ConceptExamples.IS_A_CONCEPT;
		
		// the test data and dependencies that use that data
		
		// multi inheritance (uses concept Ids 1-12)
		multipleInheritanceTestData = setupMultiInheritanceTestData(1);
		// single inheritance (uses concept Ids 13-15)
		singleInheritanceTestData = setupSingleInheritanceWithOtherPropertiesTestData(13);
		// no CPO data (uses concept Ids 16-16)
		noConceptPropertyObjects = setupNoConceptPropertyObjects(16);
	}
	


	private void addConceptObjectProperties(Set<ConceptPropertyObject> conceptPropertyObjects) {
		for(ConceptPropertyObject conceptPropertyObject : conceptPropertyObjects) {
		
			Integer concept = conceptPropertyObject.getConcept().getDbid();
	
			List<ConceptPropertyObject> cops;
			if(allCops.containsKey(concept)) {
				cops = allCops.get(concept);
			}
			else {
				cops = new ArrayList<ConceptPropertyObject>();
				allCops.put(concept, cops);
			}
			
			cops.add(conceptPropertyObject);	
		}
	}
	
	// builds a graph using concepts 1 to 12 where 1 is the leaf and 12 is one of the roots
	private Pair<Concept, Concept> setupMultiInheritanceTestData(Integer leafConceptDbId) {
		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		Concept expectedConcept = ConceptExamples.getConceptBuilder(leafConceptDbId.toString()).withMultipleInheritance().build();
		
		Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 
		
		// data for repos		
		Set<ConceptPropertyObject> conceptPropertyObjects = ConceptPropertyObjectExamples.getConceptPropertyObjectsBuilder().addMultiInheritanceConceptPropertyObjects().build();
		addConceptObjectProperties(conceptPropertyObjects);
	
		return testData;	
	}
	
	// builds a graph using concepts 13, 14 and 15 where 13 is the leaf, 15 is the root and 14 is not part of the inheritance hierarchy 
	private Pair<Concept, Concept> setupSingleInheritanceWithOtherPropertiesTestData(Integer leafConceptDbId) {
		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		Concept expectedConcept = ConceptExamples.getConceptBuilder(leafConceptDbId.toString()).withSingleInheritance().build();
		
		Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 
		
		// data for repos
		Set<ConceptPropertyObject> conceptPropertyObjects = ConceptPropertyObjectExamples.getConceptPropertyObjectsBuilder()
																	.addSingleInheritanceConceptPropertyObjects()
																	.addHasMemberConceptPropertyObject()
																	.build();
		addConceptObjectProperties(conceptPropertyObjects);
	
		return testData;			

	} 
	
	// builds a test set where there are no entries in the COP table for the concept
	private Pair<Concept, Concept> setupNoConceptPropertyObjects(Integer leafConceptDbId) {
		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		Concept expectedConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		
		Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 
		
		// data for repos	
		Set<ConceptPropertyObject> conceptPropertyObjects = new HashSet<ConceptPropertyObject>();
		addConceptObjectProperties(conceptPropertyObjects);
	
		return testData;
	} 	
	
	private ConceptPropertyObjectRepository setUpConceptPropertyObjectRepository() {
		ConceptPropertyObjectRepository conceptPropertyObjectRepository = Mockito.mock(ConceptPropertyObjectRepository.class);			
		
		Mockito.when(conceptPropertyObjectRepository.findByConceptDbid(Mockito.anyInt())).thenAnswer(new Answer<List<ConceptPropertyObject>>() {

			@Override
			public List<ConceptPropertyObject> answer(InvocationOnMock invocation) throws Throwable {
				Integer dbId = (Integer) invocation.getArguments()[0];
				
				return allCops.get(dbId);
			}

		});		
		
		return conceptPropertyObjectRepository;
	}
		
	@Test
	void testAddParentsWithMultipleInheritance() {
		Concept actualConcept = multipleInheritanceTestData.getFirst();
		assertFalse(actualConcept.hasParents());
		
		parentService.addParents(actualConcept, Integer.parseInt(actualConcept.getIri()));
		
		assertTrue(actualConcept.hasParents());
		assertTrue(actualConcept.deepEquals(multipleInheritanceTestData.getSecond()));
	}
	
	@Test
	// tests that the tree building ignores COP that are not of type "Is a" ie not part of the inheritance hierarchy 
	void testAddParentsWithSingleInheritanceWithOtherProperties() {
		Concept actualConcept = singleInheritanceTestData.getFirst();
		assertFalse(actualConcept.hasParents());
		
		parentService.addParents(actualConcept, Integer.parseInt(actualConcept.getIri()));
		
		assertTrue(actualConcept.hasParents());
		assertTrue(actualConcept.deepEquals(singleInheritanceTestData.getSecond()));
	}
	
	@Test
	// tests that the tree building ignores COP that are not of type "Is a" ie not part of the inheritance hierarchy 
	void testAddParentsWithNoConceptPropertyObjects() {
		Concept actualConcept = noConceptPropertyObjects.getFirst();
		assertFalse(actualConcept.hasParents());
		
		parentService.addParents(actualConcept, Integer.parseInt(actualConcept.getIri()));
		
		assertFalse(actualConcept.hasParents());
		assertTrue(actualConcept.deepEquals(noConceptPropertyObjects.getSecond()));
	}		
	
	@Test
	void testAddParentsWithNullConcept() {
		parentService.addParents(null, Integer.parseInt(noConceptPropertyObjects.getFirst().getIri()));
	}

	@Test
	void testAddParentsWithNullConceptDbId() {
		parentService.addParents(noConceptPropertyObjects.getFirst(), null);
	}
	
	@Test
	void testAddParentsWithNullConceptAndNullConceptDbId() {
		parentService.addParents(null, null);
	}

	@Test
	void testAddParentsWithNoParents() {
		// TODO
	}

}