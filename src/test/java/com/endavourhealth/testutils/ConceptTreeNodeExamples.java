package com.endavourhealth.testutils;

import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.Concept;
import com.endavourhealth.concept.models.ConceptTreeNode;

@Service
public class ConceptTreeNodeExamples {

	
	public ConceptTreeNode getEncounterTree() {
		ConceptTreeNode one = new ConceptTreeNode(new Concept(":Encounter")); // leaf
		
		ConceptTreeNode two = initParentConceptTreeNode(one, ":PatientHealthEvent"); 
		ConceptTreeNode three = initParentConceptTreeNode(two, ":HealthEvent"); 
		ConceptTreeNode four = initParentConceptTreeNode(three, ":HealthRecordEntry");
		ConceptTreeNode five = initParentConceptTreeNode(four, ":RecordEntryClass");
		ConceptTreeNode six = initParentConceptTreeNode(five, ":DiscoveryCommonDataModel");
		ConceptTreeNode seven = initParentConceptTreeNode(six, ":894281000252100"); // root
		initParentConceptTreeNode(seven, "owl:Thing");
		
		ConceptTreeNode nine = initParentConceptTreeNode(one, ":ProvisionOfCare"); 
		ConceptTreeNode ten = initParentConceptTreeNode(nine, ":HealthRecord"); 
		ConceptTreeNode eleven = initParentConceptTreeNode(ten, ":DiscoveryCommonDataModel"); 
		ConceptTreeNode twelve = initParentConceptTreeNode(eleven, ":894281000252100"); // root
		initParentConceptTreeNode(twelve, "owl:Thing");
		
		return one;
	}
		
	private ConceptTreeNode initParentConceptTreeNode(ConceptTreeNode tree, String parentIri) {			
		ConceptTreeNode parentTree = new ConceptTreeNode(new Concept(parentIri), tree);
	
		return parentTree;
	}
}
