package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class PropertyValue extends QuantificationImpl{

   private ConceptReference property;
   private ConceptReference valueType;
   private ConceptReference inverseOf;
   private Set<String> oneOf;
   private String minInclusive;
   private String minExclusive;
   private String maxInclusive;
   private String maxExclusive;
   private String pattern;
   private String valueData;
   private String individual;
   private ClassExpression expression;
   private int group;


   public ConceptReference getProperty() {
      return property;
   }

   public PropertyValue setProperty(ConceptReference property) {
      this.property = property;
      return this;
   }




   public String getValueData() {
      return valueData;
   }

   public PropertyValue setValueData(String valueData) {
      this.valueData = valueData;
      return this;
   }

   public Set<String> getOneOf() {
      return oneOf;
   }

   public PropertyValue setOneOf(Set<String> oneOf) {
      this.oneOf = oneOf;
      return this;
   }

   public PropertyValue addOneOf(String oneOf){
      if (this.oneOf==null)
         this.oneOf= new HashSet<>();
      this.oneOf.add(oneOf);
      return this;
   }

   


   public String getPattern() {
      return pattern;
   }

   public PropertyValue setPattern(String pattern) {
      this.pattern = pattern;
      return this;
   }

   public ConceptReference getValueType() {
      return valueType;
   }

   public PropertyValue setValueType(ConceptReference objectType) {
      this.valueType = objectType;
      return this;
   }

   public ConceptReference getInverseOf() {
      return inverseOf;
   }

   public PropertyValue setInverseOf(ConceptReference inverseOf) {
      this.inverseOf = inverseOf;
      return this;
   }

   public String getMinInclusive() {
      return minInclusive;
   }

   public PropertyValue setMinInclusive(String minInclusive) {
      this.minInclusive = minInclusive;
      return this;
   }

   public String getMinExclusive() {
      return minExclusive;
   }

   public PropertyValue setMinExclusive(String minExclusive) {
      this.minExclusive = minExclusive;
      return this;
   }

   public String getMaxInclusive() {
      return maxInclusive;
   }

   public PropertyValue setMaxInclusive(String maxInclusive) {
      this.maxInclusive = maxInclusive;
      return this;
   }

   public String getMaxExclusive() {
      return maxExclusive;
   }

   public PropertyValue setMaxExclusive(String maxExclusive) {
      this.maxExclusive = maxExclusive;
      return this;
   }

   public String getIndividual() {
      return individual;
   }

   public PropertyValue setIndividual(String individual) {
      this.individual = individual;
      return this;
   }

   public ClassExpression getExpression() {
      return expression;
   }

   public PropertyValue setExpression(ClassExpression expression) {
      this.expression = expression;
      return this;
   }

   public int getGroup() {
      return group;
   }

   public PropertyValue setGroup(int group) {
      this.group = group;
      return this;
   }
}
