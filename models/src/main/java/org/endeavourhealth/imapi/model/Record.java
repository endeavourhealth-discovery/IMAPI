package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Record extends Concept{
   private ConceptReference targetClass;


   public Record(){
      this.setConceptType(ConceptType.RECORD);
   }

   public ConceptReference getTargetClass() {
      return targetClass;
   }

   public Record setTargetClass(ConceptReference targetClass) {
      this.targetClass = targetClass;
      return this;
   }
}
