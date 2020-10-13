package com.endavourhealth.testutils;

import java.util.HashSet;
import java.util.Set;

import com.endavourhealth.dataaccess.entity.ConceptTct;

public class ConceptTctExamples {

	public static final Integer IS_A_CONCEPT_DB_ID = 99;
	
	public static ConceptTctBuilder getConceptTctBuilder(Integer sourceDbId) {
		return new ConceptTctBuilder(sourceDbId);
	}
	
	public static class ConceptTctBuilder {
		Integer conceptDbId;
		Set<ConceptTct> conceptTcts;
		Integer nextConceptTctDbId;

		private ConceptTctBuilder(Integer conceptDbId) {
			this.conceptDbId = conceptDbId;
			this.conceptTcts = new HashSet<ConceptTct>();
			this.nextConceptTctDbId = 0;
		}
		
		public ConceptTctBuilder withParent(Integer targetDbId, Integer level) {
			ConceptTct conceptTct = new ConceptTct(nextConceptTctDbId++, conceptDbId, IS_A_CONCEPT_DB_ID, level, targetDbId);
			conceptTcts.add(conceptTct);
			
			return this;
		}
		
		public ConceptTctBuilder withChild(Integer sourceDbId, Integer level) {
			ConceptTct conceptTct = new ConceptTct(nextConceptTctDbId++, sourceDbId, IS_A_CONCEPT_DB_ID, level, conceptDbId);
			conceptTcts.add(conceptTct);
			
			return this;
		}
		
		public Set<ConceptTct> build() {
			return conceptTcts;
		}
		
	}
}
