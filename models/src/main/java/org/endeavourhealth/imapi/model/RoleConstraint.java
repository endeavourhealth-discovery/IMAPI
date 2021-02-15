package org.endeavourhealth.imapi.model;

import java.util.ArrayList;
import java.util.List;

public class RoleConstraint {
   List<RoleConstraint> subRole;
   private EntailmentConcept property;
   private EntailmentConcept value;

   public List<RoleConstraint> getSubRole() {
      return subRole;
   }

   public RoleConstraint setSubRole(List<RoleConstraint> subRole) {
      this.subRole = subRole;
      return this;
   }

   public RoleConstraint addSubRole(RoleConstraint subRole){
      if (this.subRole==null)
         this.subRole= new ArrayList<>();
      this.subRole.add(subRole);
      return this;
   }

   public EntailmentConcept getProperty() {
      return property;
   }

   public RoleConstraint setProperty(EntailmentConcept property) {
      this.property = property;
      return this;
   }

   public EntailmentConcept getValue() {
      return value;
   }

   public RoleConstraint setValue(EntailmentConcept value) {
      this.value = value;
      return this;
   }
}
