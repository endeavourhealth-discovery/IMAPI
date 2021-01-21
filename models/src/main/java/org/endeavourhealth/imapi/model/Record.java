package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Record extends Concept{
   private List<PropertyConstraint> property;

   public Record(){
      this.setConceptType(ConceptType.RECORD);
   }
   @JsonProperty("Property")
   public List<PropertyConstraint> getProperty() {
      return property;
   }

   public Record setProperty(List<PropertyConstraint> property) {
      this.property = property;
      return this;
   }
   public Record addProperty(PropertyConstraint property){
      if (this.property==null)
         this.property= new ArrayList<>();
      this.property.add(property);
      return this;
   }
}
