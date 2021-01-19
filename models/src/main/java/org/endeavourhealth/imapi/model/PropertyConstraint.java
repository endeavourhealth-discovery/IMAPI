package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@JsonPropertyOrder({"Property","min","max","ValueClass","DataType","Data","OneOf",
    "minInclusive","minExclusive","maxInclusive","maxExclusive","pattern"})
public class PropertyConstraint {
   private ConceptReference property;
   private Integer min;
   private Integer max;
   private ConceptReference valueClass;
   private ConceptReference dataType;
   private Set<String> oneOf;
   private String minInclusive;
   private String minExclusive;
   private String maxInclusive;
   private String maxExclusive;
   private String pattern;

   @JsonProperty("Property")
   public ConceptReference getProperty() {
      return property;
   }

   public PropertyConstraint setProperty(ConceptReference property) {
      this.property = property;
      return this;
   }

   public Integer getMin() {
      return min;
   }

   public PropertyConstraint setMin(Integer min) {
      this.min = min;
      return this;
   }

   public Integer getMax() {
      return max;
   }

   public PropertyConstraint setMax(Integer max) {
      this.max = max;
      return this;
   }

   @JsonProperty("ValueClass")
   public ConceptReference getValueClass() {
      return valueClass;
   }

   public PropertyConstraint setValueClass(ConceptReference valueClass) {
      this.valueClass = valueClass;
      return this;
   }

   @JsonProperty("DataType")
   public ConceptReference getDataType() {
      return dataType;
   }

   public PropertyConstraint setDataType(ConceptReference dataType) {
      this.dataType = dataType;
      return this;
   }



   @JsonProperty("OneOf")
   public Set<String> getOneOf() {
      return oneOf;
   }

   public PropertyConstraint setOneOf(Set<String> oneOf) {
      this.oneOf = oneOf;
      return this;
   }
   public PropertyConstraint addOneOf(String oneOf){
      if (this.oneOf==null)
         this.oneOf= new HashSet<>();
      this.oneOf.add(oneOf);
      return this;
   }

   public String getMinInclusive() {
      return minInclusive;
   }

   public PropertyConstraint setMinInclusive(String minInclusive) {
      this.minInclusive = minInclusive;
      return this;
   }

   public String getMinExclusive() {
      return minExclusive;
   }

   public PropertyConstraint setMinExclusive(String minExclusive) {
      this.minExclusive = minExclusive;
      return this;
   }

   public String getMaxInclusive() {
      return maxInclusive;
   }

   public PropertyConstraint setMaxInclusive(String maxInclusive) {
      this.maxInclusive = maxInclusive;
      return this;
   }

   public String getMaxExclusive() {
      return maxExclusive;
   }

   public PropertyConstraint setMaxExclusive(String maxExclusive) {
      this.maxExclusive = maxExclusive;
      return this;
   }

   public String getPattern() {
      return pattern;
   }

   public PropertyConstraint setPattern(String pattern) {
      this.pattern = pattern;
      return this;
   }
}
