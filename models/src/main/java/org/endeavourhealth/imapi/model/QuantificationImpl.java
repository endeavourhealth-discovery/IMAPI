package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"property","inverseOf","quantificationType,","min","max"})
public class QuantificationImpl implements Quantification {
   private Integer min;
   private Integer max;
   private QuantificationType quantificationType;

   public Integer getMin() {
      return min;
   }

   public Quantification setMin(Integer min) {
      this.min = min;
      return this;
   }

   public Integer getMax() {
      return max;
   }

   public Quantification setMax(Integer max){
      this.max=max;
      return this;
   }

   public QuantificationType getQuantificationType() {
      if (this.min==null && this.max==null)
         return QuantificationType.ONLY;
      else
      if (this.min!=null)
         if (this.max!=null)
            if (this.max.equals(this.min))
               return QuantificationType.EXACT;
            else
               return QuantificationType.RANGE;
         else if (this.min==1)
            return QuantificationType.SOME;
         else
            return QuantificationType.MIN;
      else
         return QuantificationType.MAX;
   }

   @Override
   public Quantification setQuantification(QuantificationType qtype) {
      this.quantificationType= qtype;
      if (qtype==QuantificationType.SOME){
         this.min=1;
         this.max=null;
      } else if (qtype==QuantificationType.ONLY){
         this.min=null;
         this.max=null;
      }
      return this;
   }
   @Override
   public Quantification setQuantification(QuantificationType qtype, int card) {
      this.quantificationType= qtype;
      if (qtype==QuantificationType.MIN){
         this.min=1;
         this.max=null;
         return this;
      } else if (qtype==QuantificationType.MAX){
         this.min=null;
         this.max=card;
         return this;
      } else
         this.min=card;
      return this;
   }


   @Override
   public Quantification setQuantification(QuantificationType qtype, int min, int max) {
      this.quantificationType= qtype;
      this.min=min;
      this.max=max;
      return this;
   }
}
