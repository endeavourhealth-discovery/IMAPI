package com.endavourhealth.dataaccess.entity.testutils;

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
			conceptTcts.add(newConceptTct(conceptDbId, targetDbId, level));
			
			return this;
		}
		
		public ConceptTctBuilder withChild(Integer sourceDbId, Integer level) {
			conceptTcts.add(newConceptTct(sourceDbId, conceptDbId, level));
			
			return this;
		}
		
		public Set<ConceptTct> build() {
			return conceptTcts;
		}
		
		private ConceptTct newConceptTct(Integer sourceConceptDbId, Integer targetConceptDbId, Integer level) {
			ConceptTct conceptTct = new ConceptTct();
			conceptTct.setDbid(nextConceptTctDbId++);
			conceptTct.setLevel(level);
			conceptTct.setProperty(ConceptExamples.IS_A_CONCEPT);
			conceptTct.setSource(ConceptExamples.getConcept(sourceConceptDbId));
			conceptTct.setTarget(ConceptExamples.getConcept(targetConceptDbId));
			
			return conceptTct;
		}
	}
}
