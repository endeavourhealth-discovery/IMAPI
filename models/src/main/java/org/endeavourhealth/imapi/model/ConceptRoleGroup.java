package org.endeavourhealth.imapi.model;

import java.util.ArrayList;
import java.util.List;

public class ConceptRoleGroup {
   private List<ConceptRole> role;
   private int groupNumber;

   public List<ConceptRole> getRole() {
      return role;
   }

   public ConceptRoleGroup setRole(List<ConceptRole> role) {
      this.role = role;
      return this;
   }

   public ConceptRoleGroup addRole (ConceptRole role){
      if (this.role==null)
         this.role= new ArrayList<>();
      this.role.add(role);
      return this;
   }

   public int getGroupNumber() {
      return groupNumber;
   }

   public ConceptRoleGroup setGroupNumber(int groupNumber) {
      this.groupNumber = groupNumber;
      return this;
   }
}
