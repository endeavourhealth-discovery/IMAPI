package org.endeavourhealth.imapi.mapping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MappingInstruction {

    private String propertyType; // [reference, constant, template]
    private String property;
    private String valueType; // [reference, constant, template]
    private String value;

    public MappingInstruction() {
    }

    public MappingInstruction(String propertyType, String property, String valueType, String value) {
        this.propertyType = propertyType;
        this.property = property;
        this.valueType = valueType;
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public MappingInstruction setValueType(String valueType) {
        this.valueType = valueType;
        return this;
    }

    public String getValue() {
        return value;
    }

    public MappingInstruction setValue(String value) {
        this.value = value;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public MappingInstruction setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public MappingInstruction setPropertyType(String propertyType) {
        this.propertyType = propertyType;
        return this;
    }

}
