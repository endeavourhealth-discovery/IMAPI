package com.endavourhealth.concept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.util.Pair;

import com.endavourhealth.concept.models.TreeNode;
import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.testutils.ConceptExamples;
import com.endavourhealth.dataaccess.entity.testutils.ConceptPropertyObjectExamples;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;

class TreeServiceTest {
	
	TreeService treeService;
	FromConceptConverter<Concept> fromConceptConverter;
	
	Pair<Concept, Concept> multipleInheritanceTestData;
	Pair<Concept, Concept> singleInheritanceTestData;
	Pair<Concept, Concept> noConceptPropertyObjects;
	
	Concept multiInheritanceConcept;
	Set<TreeNode<Concept>> multiInheritanceConceptParents;
	
	Map<Integer, List<ConceptPropertyObject>> allCops;

	@BeforeEach
	public void setUp() {
	
		// service under test's dependencies
		allCops = new HashMap<Integer, List<ConceptPropertyObject>>();
		ConceptPropertyObjectRepository conceptPropertyObjectRepository = setUpConceptPropertyObjectRepository();	
		ConceptTctRepository conceptTctRepository;
		ConceptRepository conceptRepository;
		
		// service under test
		treeService = new TreeService();
		treeService.conceptPropertyObjectRepository = conceptPropertyObjectRepository;
		treeService.conceptTctRepository = conceptTctRepository;
		treeService.conceptRepository = conceptRepository;

		fromConceptConverter = new FromConceptConverter<Concept>() {
			
			@Override
			public Concept convert(Concept concept) {
				return concept;
			}
		};
		
		// the test data and dependencies that use that data
		
		// multi inheritance (uses concept Ids 1-12)
		multipleInheritanceTestData = setupMultiInheritanceTestData(1);
		// single inheritance (uses concept Ids 13-15)
		singleInheritanceTestData = setupSingleInheritanceWithOtherPropertiesTestData(13);
		// no CPO data (uses concept Ids 16-16)
		noConceptPropertyObjects = setupNoConceptPropertyObjects(16);
	}
	
	public void doIt() {
		Set<TreeNode<Concept>> parents = treeService.getParents(multiInheritanceConcept, fromConceptConverter);
		
		
		assertTrue(multiInheritanceConceptParents, parents);
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
	
	// builds a graph using concepts 1 to 12 where 1 is the leaf and 12 is one of the roots
	private void setupMultiInheritanceTestData(Integer leafConceptDbId) {
//		Concept actualConcept = ConceptExamples.getConcept(leafConceptDbId.toString());
//		Concept expectedConcept = ConceptExamples.getConceptBuilder(leafConceptDbId.toString()).withMultipleInheritance().build();
		
		//Pair<Concept, Concept> testData = Pair.of(actualConcept, expectedConcept); 
		
		// data for repos
		Concept leaf = ConceptExamples.getConcept(leafConceptDbId);
		
		Set<ConceptPropertyObject> conceptPropertyObjects = ConceptPropertyObjectExamples.getConceptPropertyObjectsBuilder().addMultiInheritanceConceptPropertyObjects(leaf).build();
		//addConceptObjectProperties(conceptPropertyObjects);
	
	}	

}
