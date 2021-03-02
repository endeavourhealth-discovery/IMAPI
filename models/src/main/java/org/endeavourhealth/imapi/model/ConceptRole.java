package org.endeavourhealth.imapi.model;

import java.util.HashSet;
import java.util.Set;

public class ConceptRole {
   private ConceptReference property;
   private ConceptReference valueType;
   private String valueData;

   public ConceptReference getProperty() {
      return property;
   }

   public ConceptRole setProperty(ConceptReference property) {
      this.property = property;
      return this;
   }

   public ConceptReference getValueType() {
      return valueType;
   }

   public ConceptRole setValueType(ConceptReference valueType) {
      this.valueType = valueType;
      return this;
   }

   public String getValueData() {
      return valueData;
   }

   public ConceptRole setValueData(String valueData) {
      this.valueData = valueData;
      return this;
   }

}
