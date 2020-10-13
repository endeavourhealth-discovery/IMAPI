package com.endavourhealth.concept.testutils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.endavourhealth.concept.models.Concept;

public class ConceptExamples {
	
	public static Concept getUnknonwnConcept() {
		Concept concept = new Concept("--Unknown--");
		concept.setName("Not known, unable to retreive from database");
		
		return concept;
	}
	
	public static EncounterBuilder getEncounterConceptBuilder() {
		return new EncounterBuilder();
	}
	
	public static ConceptBuilder getConceptBuilder(String iri) {
		return new ConceptBuilder(iri);
	}
	
	public static Concept getEncounterConcept() {
		return null;
	}
	
	public static Concept getConcept(String iri) {
		return new Concept(iri);
	}
	
	public static Set<Concept> getConcepts(String ... iris) {
		Set<Concept> concepts = new HashSet<Concept>();
		
		for(String iri : iris) {
			concepts.add(ConceptExamples.getConcept(iri));
		}
		
		return concepts;
	}
	
	public static class ConceptBuilder {
		
		Concept concept;
	
		private ConceptBuilder(String iri) {
			concept = new Concept(iri);
		}
		
		public ConceptBuilder withName(String name) {
			concept.setName(name);
			
			return this;
		}
		
		public ConceptBuilder withDescription(String description) {
			concept.setDescription(description);
			
			return this;
		}
		
		@SuppressWarnings("unused")
		public ConceptBuilder withMultipleInheritance() {			
			Concept two = buildParents(concept, "2");
			//concept.addParent(two);
			
			Concept five = buildParents(two, "3", "5");
			//two.addParent(five);
			
			Concept six = buildParents(two, "4", "6");
			//two.addParent(six);
			
			Concept nine = buildParents(six, "8", "9");
			//six.addParent(nine);	
			
			Concept ten = buildParents(six, "7", "10");
			//six.addParent(ten);	
			
			Concept eleven = buildParents(ten, "11");
			//ten.addParent(eleven);	
			
			Concept twelve = buildParents(ten, "12");
			//ten.addParent(twelve);				
			
			return this;
		}
		
		public ConceptBuilder withSingleInheritance() {
			buildParents(concept, "14", "15");
			
			return this;
		}
		
		public ConceptBuilder withChildren(String ... iris) {
			for(String iri : iris) {
				Concept child = newConcept(iri);
				concept.addChild(child);
			}
			
			return this;
		}
		
		public ConceptBuilder withProvisionOfCareInheritance() {
			buildParents(concept, ":ProvisionOfCare", ":HealthRecord", ":DiscoveryCommonDataModel", ":894281000252100", "owl:Thing");
			
			return this;
		}
		
		public ConceptBuilder withPatientHealthEventInheritance() {
			buildParents(concept, ":PatientHealthEvent", "HealthEvent", ":HealthRecordEntry", "RecordEntryClass", ":DiscoveryCommonDataModel", ":894281000252100", "owl:Thing");	
			
			return this;
		}
			
		public Concept build() {
			return concept;
		}
		
		private Concept buildParents(Concept child, String ... parentIris) {
			Concept root = null;
			
			Concept directParent = newConcept(parentIris[0]);
			directParent.addChild(child);
			root = directParent;
			
			if(parentIris.length >= 2) {
				root = buildParents(directParent, Arrays.copyOfRange(parentIris, 1, parentIris.length));
			}
			
			return root;
		}
		
		private Concept newConcept(String iri) {
			return new Concept(iri);
		}
	}
	
	public static class MutliInheritanceConceptBuilder {

		Concept concept;
		
		public static String IRI = "1";
		public static String NAME = "1";
		public static final String DESCRIPTION = "1";

		private MutliInheritanceConceptBuilder() {
			concept = newConcept(IRI);
			concept.setName(NAME);
			concept.setDescription(DESCRIPTION);		
		}
		
		public MutliInheritanceConceptBuilder withParents() {
			concept.addParent(getProvisionOfCareParent());
			concept.addParent(getPatientHealthEventParent());
			
			return this;
		}
		
		public MutliInheritanceConceptBuilder withChildren() {
			// TODO
			
			return this;
		}
		
		public Concept build() {
			return concept;
		}
		
		public MutliInheritanceConceptBuilder reset() {
			concept = null;
			
			return this;
		}
		
		private Concept getProvisionOfCareParent() {
			return buildParents(":ProvisionOfCare", ":HealthRecord", ":DiscoveryCommonDataModel", ":894281000252100", "owl:Thing");
		}
		
		private Concept getPatientHealthEventParent() {
			return buildParents(":PatientHealthEvent", "HealthEvent", ":HealthRecordEntry", "RecordEntryClass", ":DiscoveryCommonDataModel", ":894281000252100", "owl:Thing");	
		}
		
		private Concept newConcept(String iri) {
			return new Concept(iri);
		}
		
		private Concept buildParents(String ... parentIris) {
			Concept directParent = newConcept(parentIris[0]);
			
			Concept child = directParent;
			for(int p = 1; p < parentIris.length; p++) {
				Concept parent = newConcept(parentIris[p]);
				parent.addChild(child);
				
				child = parent;		
			}
			
			return directParent;
		}
	}
	
	
	public static class EncounterBuilder {

		Concept concept;
		
		public static String IRI = ":Encounter";
		public static String NAME = "Encounter (record entry)";
		public static final String DESCRIPTION = "An encounter is an interaction between a patient and healthcare provider for the purpose of providing care, including assessment of care needs. Encounters are the mainstay of care provision and the concept covers encounters in any care domain. For example a GP consultation is an encounter and an in-patient stay is an encounter.<p>Encounters are often defined according to the publishers definition or even the nature of the IT system in use. In Discovery an encounter model is a superset of the different encounter models from the<p>Events within hospital are also considered encounters and in Discovery are subs encounters of the overall encounter. Examples of this may be admissions, discharges, ward transfer.<p>different domains and systems. The different patterns are differentiated by the use of Encounter types or archetypes.<p>For example, a spell in hospital would be considered an encounter. The admission event is also considered an encounter, subsidiary to the spell encounter, as a discharge would be.<p>Similarly an accident and emergency attendance may have a subsidiary encounter for the initial assessment.<p>The additional properties relating to encounter types (e.g.... method of admission, critical care function type) etc, are considered as non-core and are referenced via the information model concepts. Each property type has a concept and each value class is considered concept. Consequently these are not specified in this document but are available via the ontology.";

		private EncounterBuilder() {
			concept = newConcept(IRI);
			concept.setName(NAME);
			concept.setDescription(DESCRIPTION);		
		}
		
		public EncounterBuilder withParents() {
			concept.addParent(getProvisionOfCareParent());
			concept.addParent(getPatientHealthEventParent());
			
			return this;
		}
		
		public EncounterBuilder withChildren() {
			withChildren(":AccidentAndEmergencyEncounter", ":CriticalCareEncounter", ":HospitalAdmission", ":HospitalDischarge", ":HospitalInpatientStay", ":HospitalOutpatientEncounter");
			
			return this;
		}
		
		public Concept build() {
			return concept;
		}
		
		public EncounterBuilder reset() {
			concept = null;
			
			return this;
		}
		
		private Concept getProvisionOfCareParent() {
			return buildParents(":ProvisionOfCare", ":HealthRecord", ":DiscoveryCommonDataModel", ":894281000252100", "owl:Thing");
		}
		
		private Concept getPatientHealthEventParent() {
			return buildParents(":PatientHealthEvent", "HealthEvent", ":HealthRecordEntry", "RecordEntryClass", ":DiscoveryCommonDataModel", ":894281000252100", "owl:Thing");	
		}
		
		private Concept newConcept(String iri) {
			return new Concept(iri);
		}
				
		private void withChildren(String ... iris) {
			for(String iri : iris) {
				Concept child = newConcept(iri);
				concept.addChild(child);
			}
		}
		
		private Concept buildParents(String ... parentIris) {
			Concept directParent = newConcept(parentIris[0]);
			
			Concept child = directParent;
			for(int p = 1; p < parentIris.length; p++) {
				Concept parent = newConcept(parentIris[p]);
				parent.addChild(child);
				
				child = parent;		
			}
			
			return directParent;
		}
	}
}
