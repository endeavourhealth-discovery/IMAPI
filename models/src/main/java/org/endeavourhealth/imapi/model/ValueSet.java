package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ValueSet extends Concept{
   private List<ClassExpression> member;
   private List<Concept> memberExpansion;

   public ValueSet(){
      this.setConceptType(ConceptType.VALUESET);
   }
   @JsonProperty("Member")
   public List<ClassExpression> getMember() {
      return member;
   }

   public ValueSet setMember(List<ClassExpression> member) {
      this.member = member;
      return this;
   }
   public ValueSet addMember (ClassExpression expression){
      if (this.member==null)
         this.member= new ArrayList<>();
      member.add(expression);
      return this;
   }

   @JsonProperty("MemberExpansion")
   public List<Concept> getMemberExpansion() {
      return memberExpansion;
   }

   public ValueSet setMemberExpansion(List<Concept> memberExpansion) {
      this.memberExpansion = memberExpansion;
      return this;
   }
   public ValueSet addMemberExpansion(Concept expansion){
      if (this.memberExpansion==null)
         this.memberExpansion= new ArrayList<>();
      this.memberExpansion.add(expansion);
      return this;
   }
}
