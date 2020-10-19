package com.endavourhealth.concept;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.entity.testutils.ConceptTctExamples;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.services.concept.ChildService;
import com.endavourhealth.services.concept.ConceptConverter;
import com.endavourhealth.services.concept.ParentService;
import com.endavourhealth.services.concept.models.Concept;

class ChildServiceTest {


	ParentService parentService;
	ChildService childService;
	
	Pair<Concept, Concept> hasChildrenTestData;
	Pair<Concept, Concept> hasNoChildrenTestData;

	Map<String, com.endavourhealth.services.concept.models.Concept> allConcepts;
	Map<Integer, List<ConceptTct>> allConceptTcts;
	
	int unknownConceptDbId = 37;
	
	@BeforeEach
	public void setUp() {
	
		// service under test's dependencies
		allConceptTcts = new HashMap<Integer, List<ConceptTct>>();
		ConceptTctRepository conceptTctRepository = setupConceptTctRepository();
				
		// service under test
		childService = new ChildService();
		childService.conceptTctRepository = conceptTctRepository;
		childService.conceptConverter = new ConceptConverter();
		//childService.identifierService = identifierService;
			
		// one child (uses concept Ids 1-2)
		hasChildrenTestData = setupHasChildrenTestData(1);

		// no children (uses concept Ids 3)
		hasNoChildrenTestData = setupHasNoChildrenTestData(3);
	}
	
	private void addConceptTcts(Set<ConceptTct> conceptTcts) {
		for(ConceptTct conceptTct : conceptTcts) {
			Integer target = conceptTct.getTarget().getDbid();
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
		Set<ConceptTct> conceptTcts = ConceptTctExamples.getConceptTctBuilder(leafConceptDbId).withChild(childIri, ConceptTct.DIRECT_RELATION_LEVEL).build();
		addConceptTcts(conceptTcts);

		return testData;	
	}
	
	private Pair<Concept, Concept> setupHasNoChildrenTestData(Integer leafConceptDbId) {
		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		Concept expectedConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
		
		Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 

		return testData;	
	}
	
	private ConceptTctRepository setupConceptTctRepository() {
		ConceptTctRepository conceptPropertyObjectRepository = Mockito.mock(ConceptTctRepository.class);			
		
		Mockito.when(conceptPropertyObjectRepository.findByTargetDbidAndLevel(Mockito.anyInt(), Mockito.anyInt())).thenAnswer(new Answer<List<ConceptTct>>() {

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