package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class DataPropertyAssertionAxiom extends Axiom{
    private ConceptReference property;
    private ConceptReference dataType;
    private String value;

    @JsonProperty("Property")
    public ConceptReference getProperty() {
        return property;
    }

    @JsonSetter
    public void setProperty(ConceptReference property) {
        this.property = property;
    }
    public void setProperty(String property) {
        this.property = new ConceptReference(property);
    }

    @JsonProperty("DataType")
    public ConceptReference getDataType() {
        return dataType;
    }

    @JsonSetter
    public void setDataType(ConceptReference dataType) {
        this.dataType = dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = new ConceptReference(dataType);
    }

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
