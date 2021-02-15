package org.endeavourhealth.imapi.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionConstraint {
   private List<ExpressionConstraint> union;
   private List<ExpressionConstraint> intersection;
   private EntailmentConcept concept;
   private RoleConstraint role;

   public List<ExpressionConstraint> getUnion() {
      return union;
   }

   public ExpressionConstraint setUnion(List<ExpressionConstraint> union) {
      this.union = union;
      return this;
   }

   public ExpressionConstraint addUnion(ExpressionConstraint union){
      if (this.union!=null)
         this.union= new ArrayList<>();
      this.union.add(union);
      return this;
   }
   public ExpressionConstraint addIntersection(ExpressionConstraint intersection){
      if (this.intersection!=null)
         this.intersection= new ArrayList<>();
      this.intersection.add(intersection);
      return this;
   }

   public List<ExpressionConstraint> getIntersection() {
      return intersection;
   }

   public ExpressionConstraint setIntersection(List<ExpressionConstraint> intersection) {
      this.intersection = intersection;
      return this;
   }

   public EntailmentConcept getConcept() {
      return concept;
   }

   public ExpressionConstraint setConcept(EntailmentConcept concept) {
      this.concept = concept;
      return this;
   }

   public RoleConstraint getRole() {
      return role;
   }

   public ExpressionConstraint setRole(RoleConstraint role) {
      this.role = role;
      return this;
   }
}
