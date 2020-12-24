package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Set;

@JsonPropertyOrder({"property","inverseOf","quantificationType","min","max","dataType","valueData"})
public class DataPropertyValue extends QuantificationImpl{

   private ConceptReference property;
   private ConceptReference dataType;
   private String valueData;
   private Set<String> oneOf;
   private String minOperator;
   private String minValue;
   private String maxOperator;
   private String maxValue;

   private String pattern;


   @JsonProperty("Property")
   public ConceptReference getProperty() {
      return property;
   }

   public DataPropertyValue setProperty(ConceptReference property) {
      this.property = property;
      return this;
   }

   @JsonProperty("DataType")
   public ConceptReference getDataType() {
      return dataType;
   }

   public DataPropertyValue setDataType(ConceptReference dataType) {
      this.dataType = dataType;
      return this;
   }

   @JsonIgnore
   public DataPropertyValue setDataType(String dataType) {
      this.dataType = new ConceptReference(dataType);
      return this;
   }


   @JsonProperty("ValueData")
   public String getValueData() {
      return valueData;
   }

   public DataPropertyValue setValueData(String valueData) {
      this.valueData = valueData;
      return this;
   }

   public Set<String> getOneOf() {
      return oneOf;
   }

   public DataPropertyValue setOneOf(Set<String> oneOf) {
      this.oneOf = oneOf;
      return this;
   }

   public String getMinOperator() {
      return minOperator;
   }

   public DataPropertyValue setMinOperator(String minOperator) {
      this.minOperator = minOperator;
      return this;
   }

   public String getMinValue() {
      return minValue;
   }

   public DataPropertyValue setMinValue(String minValue) {
      this.minValue = minValue;
      return this;
   }

   public String getMaxOperator() {
      return maxOperator;
   }

   public DataPropertyValue setMaxOperator(String maxOperator) {
      this.maxOperator = maxOperator;
      return this;
   }

   public String getMaxValue() {
      return maxValue;
   }

   public DataPropertyValue setMaxValue(String maxValue) {
      this.maxValue = maxValue;
      return this;
   }

   public String getPattern() {
      return pattern;
   }

   public DataPropertyValue setPattern(String pattern) {
      this.pattern = pattern;
      return this;
   }

}
