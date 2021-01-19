package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"conceptType","status","version","isRef","iri","name","description",
    "code","scheme","subClassOf",",equivalentTo","isA","member","expansion"})
public class ValueSet extends Concept{
   private List<ClassExpression> member;
   private List<Concept> expansion;

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

   @JsonProperty("Expansion")
   public List<Concept> getExpansion() {
      return expansion;
   }

   public ValueSet setExpansion(List<Concept> expansion) {
      this.expansion = expansion;
      return this;
   }
   public ValueSet addExpansion (Concept expansion){
      if (this.expansion==null)
         this.expansion= new ArrayList<>();
      this.expansion.add(expansion);
      return this;
   }

}
