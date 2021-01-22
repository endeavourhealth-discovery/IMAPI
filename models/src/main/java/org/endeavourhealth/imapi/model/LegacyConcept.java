package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class LegacyConcept extends Concept{
   private List<ConceptReference> mappedFrom;

   public LegacyConcept(){
      this.setConceptType(ConceptType.LEGACY);
   }

   @JsonProperty("MappedFrom")
   public List<ConceptReference> getMappedFrom() {
      return mappedFrom;
   }

   public LegacyConcept setMappedFrom(List<ConceptReference> mappedFrom) {
      this.mappedFrom = mappedFrom;
      return this;
   }
   public LegacyConcept addMappedFrom(ConceptReference mappedFrom){
      if (this.mappedFrom==null)
         this.mappedFrom= new ArrayList<>();
      this.mappedFrom.add(mappedFrom);
      return this;
   }
}
