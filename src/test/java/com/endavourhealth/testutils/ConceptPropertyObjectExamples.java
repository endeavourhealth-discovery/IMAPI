package com.endavourhealth.testutils;

import java.util.HashSet;
import java.util.Set;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;

public class ConceptPropertyObjectExamples {

	public static final Integer IS_A_CONCEPT_DB_ID = 99;
	public static final Integer HAS_MEMBER_CONCEPT_DB_ID = IS_A_CONCEPT_DB_ID + 1;
	
	public static ConceptPropertyObjectsBuilder getConceptPropertyObjectsBuilder() {
		return new ConceptPropertyObjectsBuilder();
	}

	public static class ConceptPropertyObjectsBuilder {

		Set<ConceptPropertyObject> objects;

		private ConceptPropertyObjectsBuilder() {
			objects = new HashSet<ConceptPropertyObject>();
		}
		
		public ConceptPropertyObjectsBuilder addMultiInheritanceConceptPropertyObjects() {

			// addConceptPropertyObject(conceptPropertyObjects,
			// initIsAConceptPropertyObject(leafConceptDbId, 2)); // branch
			objects.add(initIsAConceptPropertyObject(1, 2));
			
			objects.add(initIsAConceptPropertyObject(2, 3));
			objects.add(initIsAConceptPropertyObject(3, 5)); // root

			objects.add(initIsAConceptPropertyObject(2, 4));
			objects.add(initIsAConceptPropertyObject(4, 6)); // branch

			objects.add(initIsAConceptPropertyObject(6, 8));
			objects.add(initIsAConceptPropertyObject(8, 9)); // root

			objects.add(initIsAConceptPropertyObject(6, 7));
			objects.add(initIsAConceptPropertyObject(7, 10)); // branch

			objects.add(initIsAConceptPropertyObject(10, 11)); // root

			objects.add(initIsAConceptPropertyObject(10, 12)); // root
			
			return this;

		}
		
		public ConceptPropertyObjectsBuilder addSingleInheritanceConceptPropertyObjects() {
			//objects.add(initIsAConceptPropertyObject(1, 13));
			objects.add(initIsAConceptPropertyObject(13, 14));
			objects.add(initIsAConceptPropertyObject(14, 15));
			
			return this;
		}
		
		public ConceptPropertyObjectsBuilder addHasMemberConceptPropertyObject() {
			objects.add(initConceptPropertyObject(13, 16, HAS_MEMBER_CONCEPT_DB_ID));
			
			return this;
		}		
		
		public Set<ConceptPropertyObject> build() {
			return objects;
		}

		private ConceptPropertyObject initIsAConceptPropertyObject(int childDbId, int parentDbId) {
			return initConceptPropertyObject(childDbId, parentDbId, IS_A_CONCEPT_DB_ID);
		}

		private ConceptPropertyObject initConceptPropertyObject(int childDbId, int parentDbId, int propertyDbId) {
			ConceptPropertyObject cop = new ConceptPropertyObject();
			cop.setConcept(childDbId);
			cop.setProperty(propertyDbId);
			cop.setObject(parentDbId);

			return cop;
		}
	}

}
