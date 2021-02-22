package org.endeavourhealth.imapi.model;

import java.util.HashSet;
import java.util.Set;

public class ConceptRole {
   private ConceptReference property;
   private ConceptReference valueType;
   private String valueData;
   private Set<ConceptRole> subrole;
   private int groupNumber;

   public int getGroupNumber() {
      return groupNumber;
   }

   public ConceptRole setGroupNumber(int groupNumber) {
      this.groupNumber = groupNumber;
      return this;
   }

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

   public Set<ConceptRole> getSubrole() {
      return subrole;
   }

   public ConceptRole setRole(Set<ConceptRole> subrole) {
      this.subrole = subrole;
      return this;
   }
   public ConceptRole addSubrol(ConceptRole subrole){
      if (this.subrole==null)
         this.subrole= new HashSet<>();
      this.subrole.add(subrole);
      return this;
   }
}
