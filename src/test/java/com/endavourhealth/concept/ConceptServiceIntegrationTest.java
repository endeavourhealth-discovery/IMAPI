package com.endavourhealth.concept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import com.endavourhealth.testutils.ConceptExamples;
import com.endavourhealth.testutils.ConceptTreeNodeExamples;
import com.endavourhealth.testutils.EndeavourMySqlContainer;
import com.endavourhealth.testutils.EndeavourMySqlContainerInitializer;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test-containers")
@ContextConfiguration(initializers = { ConceptServiceIntegrationTest.Initializer.class })
class ConceptServiceIntegrationTest {

	@Autowired
	ConceptService conceptService;
//	
//	@Autowired
//	ConceptTreeNodeExamples conceptTreeNodeExamples;
//	
//	@Autowired
//	ConceptExamples conceptExamples;
	
	private Concept knownConceptWithNoParentsAndNoChildren;
	private Concept knownConceptWithParentsAndNoChildren;
	private Concept unknownConceptWithNoParentsAndNoChildren;
	
	// TODO
	private Concept knownConceptWithNoParentsAndChildren;
	private Concept knownConceptWithParentsAndChildren;

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
		knownConceptWithNoParentsAndNoChildren = ConceptExamples.getEncounterConceptBuilder().build();
		knownConceptWithParentsAndNoChildren = ConceptExamples.getEncounterConceptBuilder().withParents().build();
		knownConceptWithNoParentsAndChildren = ConceptExamples.getEncounterConceptBuilder().withChildren().build();
		unknownConceptWithNoParentsAndNoChildren = ConceptExamples.getUnknonwnConcept();
	}

	@Test
	void testGetConceptWithKnownIri() {
		Concept concept = conceptService.getConcept(ConceptExamples.EncounterBuilder.IRI);
		
		assertEquals(ConceptExamples.EncounterBuilder.IRI, concept.getIri());
		assertEquals(ConceptExamples.EncounterBuilder.NAME, concept.getName());
		assertEquals(ConceptExamples.EncounterBuilder.DESCRIPTION, concept.getDescription());
	}
	
	@Test
	void testGetConceptWithUnknownIri() {
		assertNull(conceptService.getConcept(unknownConceptWithNoParentsAndNoChildren.getIri()));
	}
	
	@Test
	void testAddParents() {
		assertTrue(knownConceptWithNoParentsAndNoChildren.getParents().isEmpty());
		
		conceptService.addParents(knownConceptWithNoParentsAndNoChildren);
		
		assertFalse((knownConceptWithNoParentsAndNoChildren.getParents().isEmpty()));
		assertTrue(knownConceptWithNoParentsAndNoChildren.deepEquals(knownConceptWithParentsAndNoChildren));
	}
	
	@Test
	void testAddChildren() {
		assertTrue(knownConceptWithNoParentsAndNoChildren.getParents().isEmpty());
		
		conceptService.addChildren(knownConceptWithNoParentsAndNoChildren);
		
		assertFalse((knownConceptWithNoParentsAndNoChildren.getChildren().isEmpty()));
		assertTrue(knownConceptWithNoParentsAndNoChildren.deepEquals(knownConceptWithNoParentsAndChildren));
	}	
}