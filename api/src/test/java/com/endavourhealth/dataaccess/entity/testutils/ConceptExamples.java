package com.endavourhealth.dataaccess.entity.testutils;

import com.endavourhealth.dataaccess.entity.Concept;

public class ConceptExamples {
	public static final Integer IS_A_CONCEPT_DB_ID = 99;
	public static final Integer HAS_MEMBER_CONCEPT_DB_ID = IS_A_CONCEPT_DB_ID + 1;
	
	public static final Concept IS_A_CONCEPT = getConcept(IS_A_CONCEPT_DB_ID, ":IsA");
	public static final Concept HAS_MEMBER_CONCEPT = getConcept(HAS_MEMBER_CONCEPT_DB_ID, ":HasA");
	
	public static Concept getConcept(Integer dbId, String iri) {
		Concept concept = new Concept();
		
		concept.setIri(iri);
		concept.setDbid(dbId);
		
		return concept;
	}
	
	public static Concept getConcept(Integer dbId) {
		Concept concept = new Concept();
		
		concept.setIri(dbId.toString());
		concept.setDbid(dbId);
		
		return concept;
	}
}
