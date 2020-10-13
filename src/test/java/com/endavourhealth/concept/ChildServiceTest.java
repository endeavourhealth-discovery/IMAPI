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

import com.endavourhealth.concept.models.Concept;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.testutils.ConceptExamples;
import com.endavourhealth.testutils.ConceptTctExamples;

class ChildServiceTest {


	ParentService parentService;
	ChildService childService;
	
	Pair<Concept, Concept> hasChildrenTestData;
	Pair<Concept, Concept> hasNoChildrenTestData;

	Map<String, com.endavourhealth.concept.models.Concept> allConcepts;
	Map<Integer, List<ConceptTct>> allConceptTcts;
	
	int unknownConceptDbId = 37;
	
	@BeforeEach
	public void setUp() {
	
		// service under test's dependencies
		allConcepts = new HashMap<String, com.endavourhealth.concept.models.Concept>();
		allConceptTcts = new HashMap<Integer, List<ConceptTct>>();
		
		IdentifierService identifierService = setUpIdentifierService();
		ConceptTctRepository conceptTctRepository = setupConceptTctRepository();
				
		// service under test
		childService = new ChildService();
		childService.conceptTctRepository = conceptTctRepository;
		childService.identifierService = identifierService;
			
		// one child (uses concept Ids 1-2)
		hasChildrenTestData = setupHasChildrenTestData(1);

		// no children (uses concept Ids 3)
		hasNoChildrenTestData = setupHasNoChildrenTestData(3);
	}
	
	private void addConcepts(Set<Concept> concepts) {
		for(Concept concept : concepts) {
			allConcepts.put(concept.getIri(), concept);
		}
	}

	
	private void addConceptTcts(Set<ConceptTct> conceptTcts) {
		for(ConceptTct conceptTct : conceptTcts) {
			Integer target = conceptTct.getTarget();
			List<ConceptTct> targetConceptTcts;
			
			if(allConceptTcts.containsKey(target)) {
				targetConceptTcts = allConceptTcts.get(target);
			}
			else {
				targetConceptTcts = new ArrayList<ConceptTct>();
				allConceptTcts.put(target, targetConceptTcts);
			}
			
			targetConceptTcts.add(conceptTct);
		}
		
	}
	
	// builds a graph using concepts 1 to 12 where 1 is the leaf and 12 is one of the roots
	private Pair<Concept, Concept> setupHasChildrenTestData(Integer leafConceptDbId) {
		Integer childIri = leafConceptDbId + 1;
		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		Concept expectedConcept = ConceptExamples.getConceptBuilder(leafConceptDbId.toString()).withChildren(childIri.toString()).build();
		
		Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 
		
		// data for repos
		Set<Concept> concepts = new HashSet<Concept>();
		concepts.add(actualConcept);
		concepts.addAll(expectedConcept.getChildConcepts());		
		addConcepts(concepts);
		
		Set<ConceptTct> conceptTcts = ConceptTctExamples.getConceptTctBuilder(leafConceptDbId).withChild(childIri, ConceptTct.DIRECT_RELATION_LEVEL).build();
		addConceptTcts(conceptTcts);

		return testData;	
	}
	
	private Pair<Concept, Concept> setupHasNoChildrenTestData(Integer leafConceptDbId) {
		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		Concept expectedConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		
		Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 

		// data for repos
		Set<Concept> concepts = new HashSet<Concept>();
		concepts.add(actualConcept);
		addConcepts(concepts);
	
		return testData;	
	}
	
	private ConceptTctRepository setupConceptTctRepository() {
		ConceptTctRepository conceptPropertyObjectRepository = Mockito.mock(ConceptTctRepository.class);			
		
		Mockito.when(conceptPropertyObjectRepository.findByTargetAndLevel(Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<List<ConceptTct>>() {

			@Override
			public List<ConceptTct> answer(InvocationOnMock invocation) throws Throwable {
				Integer dbId = (Integer) invocation.getArguments()[0];

				List<ConceptTct> conceptTcts = allConceptTcts.get(dbId);
				if(conceptTcts == null) {
					conceptTcts = new ArrayList<ConceptTct>();
				}
				
				return conceptTcts;
			}

		});		
		
		return conceptPropertyObjectRepository;	
	}

	private IdentifierService setUpIdentifierService() {
		IdentifierService identifierService = Mockito.mock(IdentifierService.class);
		
		// concept model keyed by IRI. Note that IRI and DBID represent the same int value
		Mockito.when(identifierService.getConcept(Mockito.anyInt())).thenAnswer(new Answer<com.endavourhealth.concept.models.Concept>() {

			@Override
			public com.endavourhealth.concept.models.Concept answer(InvocationOnMock invocation) throws Throwable {
				Integer dbId = (Integer) invocation.getArguments()[0];
				
				return allConcepts.get(dbId.toString());
			}

		});	
		
		Mockito.when(identifierService.getConcept(Mockito.anyString())).thenAnswer(new Answer<com.endavourhealth.concept.models.Concept>() {

			@Override
			public com.endavourhealth.concept.models.Concept answer(InvocationOnMock invocation) throws Throwable {
				String iri = (String) invocation.getArguments()[0];
				
				return allConcepts.get(iri.toString());
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
	
	@Test
	void testAddChildrenWithNoChildren() {
		Concept actualConcept = hasChildrenTestData.getFirst();		
		assertFalse(actualConcept.hasChildren());
		
		childService.addDirectChildren(actualConcept, Integer.parseInt(actualConcept.getIri()));
		
		assertTrue(actualConcept.hasChildren());
		assertTrue(actualConcept.deepEquals(hasChildrenTestData.getSecond()));
	}
	
	@Test
	void testAddChildrenWithChildren() {
		Concept actualConcept = hasChildrenTestData.getFirst();		
		assertFalse(actualConcept.hasChildren());
		
		childService.addDirectChildren(actualConcept, Integer.parseInt(actualConcept.getIri()));
		
		assertTrue(actualConcept.hasChildren());
		assertTrue(actualConcept.deepEquals(hasChildrenTestData.getSecond()));
	}
	
	// TODO -->
	
	@Test
	void testAddChildrenWithNullConcept() {
		childService.addDirectChildren(null, Integer.parseInt(hasChildrenTestData.getFirst().getIri()));
	}

	@Test
	void testAddChildrenWithNullConceptDbId() {
		childService.addDirectChildren(hasChildrenTestData.getFirst(), null);
	}
	
	@Test
	void testAddChildrenWithNullConceptAndNullConceptDbId() {
		childService.addDirectChildren(null, null);
	}	
	
	@Test
	void testAddChildrenWithUnknownConceptDbId() {
		Concept actualConcept = hasChildrenTestData.getFirst();
		assertFalse(actualConcept.hasChildren());
		
		childService.addDirectChildren(actualConcept, unknownConceptDbId);
		
		assertFalse(actualConcept.hasChildren());
	}		
}