package com.endavourhealth.testutils;

import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.Concept;

@Service
public class ConceptExamples {
	
	public Concept getEncounter() {
		Concept concept = new Concept(":Encounter");
		concept.setName("Encounter (record entry)");
		concept.setDescription("An encounter is an interaction between a patient and healthcare provider for the purpose of providing care, including assessment of care needs. Encounters are the mainstay of care provision and the concept covers encounters in any care domain. For example a GP consultation is an encounter and an in-patient stay is an encounter.<p>Encounters are often defined according to the publishers definition or even the nature of the IT system in use. In Discovery an encounter model is a superset of the different encounter models from the<p>Events within hospital are also considered encounters and in Discovery are subs encounters of the overall encounter. Examples of this may be admissions, discharges, ward transfer.<p>different domains and systems. The different patterns are differentiated by the use of Encounter types or archetypes.<p>For example, a spell in hospital would be considered an encounter. The admission event is also considered an encounter, subsidiary to the spell encounter, as a discharge would be.<p>Similarly an accident and emergency attendance may have a subsidiary encounter for the initial assessment.<p>The additional properties relating to encounter types (e.g.... method of admission, critical care function type) etc, are considered as non-core and are referenced via the information model concepts. Each property type has a concept and each value class is considered concept. Consequently these are not specified in this document but are available via the ontology.");

		return concept;
	}
	
	public Concept getUnknonwn() {
		Concept concept = new Concept("--Unknown--");
		concept.setName("Not known, unable to retreive from database");
		
		return concept;
	}
}
