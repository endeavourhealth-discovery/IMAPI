package com.endavourhealth.concept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.endavourhealth.testutils.EndeavourMySqlContainer;
import com.endavourhealth.testutils.EndeavourMySqlContainerInitializer;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test-containers")
@ContextConfiguration(initializers = { ConceptServiceIntegrationTest.Initializer.class })
class ConceptServiceIntegrationTest {

	@Autowired
	ConceptService conceptService;
	
	private String knownIri;
	private String unknownIri;
	
	private Concept knownConcept;

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
		knownIri = ":Encounter";
		unknownIri = ":-Unknown-";
		
		knownConcept = new Concept(knownIri);
		knownConcept.setName("Encounter (record entry)");
		knownConcept.setDescription("An encounter is an interaction between a patient and healthcare provider for the purpose of providing care, including assessment of care needs. Encounters are the mainstay of care provision and the concept covers encounters in any care domain. For example a GP consultation is an encounter and an in-patient stay is an encounter.<p>Encounters are often defined according to the publishers definition or even the nature of the IT system in use. In Discovery an encounter model is a superset of the different encounter models from the<p>Events within hospital are also considered encounters and in Discovery are subs encounters of the overall encounter. Examples of this may be admissions, discharges, ward transfer.<p>different domains and systems. The different patterns are differentiated by the use of Encounter types or archetypes.<p>For example, a spell in hospital would be considered an encounter. The admission event is also considered an encounter, subsidiary to the spell encounter, as a discharge would be.<p>Similarly an accident and emergency attendance may have a subsidiary encounter for the initial assessment.<p>The additional properties relating to encounter types (e.g.... method of admission, critical care function type) etc, are considered as non-core and are referenced via the information model concepts. Each property type has a concept and each value class is considered concept. Consequently these are not specified in this document but are available via the ontology.");
	}

	@Test
	void testGetConceptWithKnownIri() {
		Concept concept = conceptService.getConcept(knownIri);
		assertEquals(knownConcept.getIri(), concept.getIri());
		assertEquals(knownConcept.getName(), concept.getName());
		assertEquals(knownConcept.getDescription(), concept.getDescription());
	}
	
	@Test
	void testGetConceptWithUnknownIri() {
		assertNull(conceptService.getConcept(unknownIri));
	}
	
	// TODO - test the parent stuff
}