package org.endeavourhealth.imapi.model;

import java.util.HashSet;
import java.util.Set;

/**
 * An existential role i.e. an objectSomevaluesFrom expression meanng at least one of
 * Used in clinical concept definition and has no cardinality constraints resulting in a more direct association with the concept
 * Role groups are roles with Roles<p>
 *    When filed as a graph it can be represented as the predicate objet of a triple with the concept as the subject
 * </p>
 */
public class Relationship {
   private ConceptReference property;
   private ClassExpression value;
   private Set<Relationship> role;
   private int group;


   public ConceptReference getProperty() {
      return property;
   }

   public Relationship setProperty(ConceptReference property) {
      this.property = property;
      return this;
   }

   public ClassExpression getValue() {
      return value;
   }

   public Relationship setValue(ClassExpression value) {
      this.value = value;
      return this;
   }

   public Set<Relationship> getRole() {
      return role;
   }

   public Relationship setRole(Set<Relationship> role) {
      this.role = role;
      return this;
   }

   public Relationship addRole(Relationship role){
      if (this.role==null)
         this.role= new HashSet<>();
      this.role.add(role);
      return this;

   }

   public int getGroup() {
      return group;
   }

   public Relationship setGroup(int group) {
      this.group = group;
      return this;
   }
}
