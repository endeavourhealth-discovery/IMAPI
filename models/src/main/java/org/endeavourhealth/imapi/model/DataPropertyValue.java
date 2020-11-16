package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"property","inverseOf","quantificationType","min","max","dataType","valueData"})
public class DataPropertyValue extends QuantificationImpl{

   private ConceptReference property;
   private ConceptReference dataType;
   private String valueData;


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

}
