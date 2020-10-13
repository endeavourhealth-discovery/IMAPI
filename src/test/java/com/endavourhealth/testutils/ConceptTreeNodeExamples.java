package com.endavourhealth.testutils;

import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.Concept;
import com.endavourhealth.concept.models.ConceptTreeNode;

@Service
public class ConceptTreeNodeExamples {

	
//	public ConceptTreeNode getEncounterTree() {
//		
//		
//		ConceptTreeNode two = initParentConceptTreeNode(one, ":PatientHealthEvent"); 
//		ConceptTreeNode three = initParentConceptTreeNode(two, ":HealthEvent"); 
//		ConceptTreeNode four = initParentConceptTreeNode(three, ":HealthRecordEntry");
//		ConceptTreeNode five = initParentConceptTreeNode(four, ":RecordEntryClass");
//		ConceptTreeNode six = initParentConceptTreeNode(five, ":DiscoveryCommonDataModel");
//		ConceptTreeNode seven = initParentConceptTreeNode(six, ":894281000252100"); // root
//		initParentConceptTreeNode(seven, "owl:Thing");
//		
//		ConceptTreeNode nine = initParentConceptTreeNode(one, ":ProvisionOfCare"); 
//		ConceptTreeNode ten = initParentConceptTreeNode(nine, ":HealthRecord"); 
//		ConceptTreeNode eleven = initParentConceptTreeNode(ten, ":DiscoveryCommonDataModel"); 
//		ConceptTreeNode twelve = initParentConceptTreeNode(eleven, ":894281000252100"); // root
//		initParentConceptTreeNode(twelve, "owl:Thing");
//		
//		return one;
//	}
	
	public ConceptTreeNode getProvisionOfCareTree() {
		ConceptTreeNode one = new ConceptTreeNode(new Concept(":ProvisionOfCare")); // leaf
		
		ConceptTreeNode two = initParentConceptTreeNode(one, ":HealthRecord"); 
		ConceptTreeNode three = initParentConceptTreeNode(two, ":DiscoveryCommonDataModel"); 
		ConceptTreeNode four = initParentConceptTreeNode(three, ":894281000252100"); // root
		initParentConceptTreeNode(four, "owl:Thing");	
		
		return one;
	}
	
	public ConceptTreeNode getPatientHealthEventTree() {
		ConceptTreeNode one = new ConceptTreeNode(new Concept(":PatientHealthEvent")); // leaf

		ConceptTreeNode two = initParentConceptTreeNode(one, ":HealthEvent"); 
		ConceptTreeNode three = initParentConceptTreeNode(two, ":HealthRecordEntry");
		ConceptTreeNode four = initParentConceptTreeNode(three, ":RecordEntryClass");
		ConceptTreeNode five = initParentConceptTreeNode(four, ":DiscoveryCommonDataModel");
		ConceptTreeNode six = initParentConceptTreeNode(five, ":894281000252100"); // root
		initParentConceptTreeNode(six, "owl:Thing");
		
		return one;
	}
		
	private ConceptTreeNode initParentConceptTreeNode(ConceptTreeNode tree, String parentIri) {			
		ConceptTreeNode parentTree = new ConceptTreeNode(new Concept(parentIri));
		parentTree.addChild(tree);
	
		return parentTree;
	}
}
