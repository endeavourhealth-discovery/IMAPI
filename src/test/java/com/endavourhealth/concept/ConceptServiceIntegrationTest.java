package com.endavourhealth.concept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
	
	@Autowired
	ConceptTreeNodeExamples conceptTreeNodeExamples;
	
	@Autowired
	ConceptExamples conceptExamples;
	
	private Concept knownConcept;
	private Concept unknownConcept;
	
	private ConceptTreeNode knownConceptTree;

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
		knownConcept = conceptExamples.getEncounter();
		unknownConcept = conceptExamples.getUnknonwn();
		
		knownConceptTree = conceptTreeNodeExamples.getEncounterTree();
	}

	@Test
	void testGetConceptWithKnownIri() {
		Concept concept = conceptService.getConcept(knownConcept.getIri());
		assertEquals(knownConcept.getIri(), concept.getIri());
		assertEquals(knownConcept.getName(), concept.getName());
		assertEquals(knownConcept.getDescription(), concept.getDescription());
	}
	
	@Test
	void testGetConceptWithUnknownIri() {
		assertNull(conceptService.getConcept(unknownConcept.getIri()));
	}
	
	@Test
	void testAddParents() {
		assertNull(knownConcept.getTree());
		
		conceptService.addParents(knownConcept);
		
		assertNotNull(knownConcept.getTree());
		assertTrue(knownConceptTree.deepEquals(knownConcept.getTree()));
	}
}