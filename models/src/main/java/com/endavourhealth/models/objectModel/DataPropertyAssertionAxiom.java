package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataPropertyAssertionAxiom extends Axiom{
    private String property;
    private String dataType;
    private String value;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @JsonProperty("DataType")
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
