package com.endavourhealth.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.endavourhealth.concept.models.Concept;
import com.endavourhealth.concept.models.ConceptTreeNode;
import com.endavourhealth.concept.models.TreeNode;
import com.endavourhealth.concept.testutils.ConceptExamples;
import com.endavourhealth.concept.testutils.ConceptTreeNodeExamples;
import com.endavourhealth.datamodel.DataModelService;
import com.endavourhealth.datamodel.models.DataModelDetail;
import com.endavourhealth.testutils.EndeavourMySqlContainer;
import com.endavourhealth.testutils.EndeavourMySqlContainerInitializer;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test-containers")
@ContextConfiguration(initializers = { DataModelServiceIntegrationTest.Initializer.class })
class DataModelServiceIntegrationTest {

	@Autowired
	DataModelService dataModelService;

//	
//	@Autowired
//	ConceptTreeNodeExamples conceptTreeNodeExamples;
//	
//	@Autowired
//	ConceptExamples conceptExamples;
	
	private Concept knownConceptWithNoParentsAndNoChildren;
	private Concept knownConceptWithParentsAndNoChildren;
	private Concept unknownConceptWithNoParentsAndNoChildren;
	private Concept knownConceptWithNoParentsAndChildren;
	
	private Concept knownConcept;
	private Concept unknownConcept;

	@ClassRule
	public static EndeavourMySqlContainer mysqlContainer = new EndeavourMySqlContainer()
																.withInitSqlScripts("/initConceptDb.sql");

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			EndeavourMySqlContainerInitializer mySqlInitializer = new EndeavourMySqlContainerInitializer(mysqlContainer);
			mySqlInitializer.initialize(configurableApplicationContext);
		}
	}
	
	@BeforeEach
	public void setUp() {
		knownConcept = ConceptExamples.getEncounterConceptBuilder().build();
		unknownConcept = ConceptExamples.getUnknonwnConcept();

		
		
		knownConceptWithNoParentsAndNoChildren = ConceptExamples.getEncounterConceptBuilder().build();
		knownConceptWithParentsAndNoChildren = ConceptExamples.getEncounterConceptBuilder().withParents().build();
		knownConceptWithNoParentsAndChildren = ConceptExamples.getEncounterConceptBuilder().withChildren().build();
		unknownConceptWithNoParentsAndNoChildren = ConceptExamples.getUnknonwnConcept();
	}

//	@Test
//	void testGetConceptWithKnownIri() {
//		Concept concept = conceptService.getConceptSummary(ConceptExamples.EncounterBuilder.IRI);
//		
//		assertEquals(ConceptExamples.EncounterBuilder.IRI, concept.getIri());
//		assertEquals(ConceptExamples.EncounterBuilder.NAME, concept.getName());
//		assertEquals(ConceptExamples.EncounterBuilder.DESCRIPTION, concept.getDescription());
//	}
//	
//	@Test
//	void testGetConceptWithUnknownIri() {
//		assertNull(conceptService.getConceptSummary(unknownConceptWithNoParentsAndNoChildren.getIri()));
//	}
	
	@Test
	void testDataModelDetail() {
		DataModelDetail dataModel = dataModelService.getDataModelDetail(knownConcept.getIri());
		
		assertNotNull(dataModel);
		
		// TODO - check parents, children etc

	}
	
//	@Test
//	void testAddChildren() {
//		assertTrue(knownConceptWithNoParentsAndNoChildren.getParents().isEmpty());
//		
//		dataModelService.addChildren(knownConceptWithNoParentsAndNoChildren);
//		
//		assertFalse((knownConceptWithNoParentsAndNoChildren.getChildren().isEmpty()));
//		assertTrue(knownConceptWithNoParentsAndNoChildren.deepEquals(knownConceptWithNoParentsAndChildren));
//	}	
}