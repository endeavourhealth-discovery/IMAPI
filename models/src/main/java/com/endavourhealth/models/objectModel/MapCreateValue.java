package com.endavourhealth.models.objectModel;

public class MapCreateValue {
    private String valueFunction;
    private String dependentField;
    private String dependentProperty;

    public String getValueFunction() {
        return valueFunction;
    }

    public MapCreateValue setValueFunction(String valueFunction) {
        this.valueFunction = valueFunction;
        return this;
    }

    public String getDependentField() {
        return dependentField;
    }

    public MapCreateValue setDependentField(String dependentField) {
        this.dependentField = dependentField;
        return this;
    }

    public String getDependentProperty() {
        return dependentProperty;
    }

    public MapCreateValue setDependentProperty(String dependentProperty) {
        this.dependentProperty = dependentProperty;
        return this;
    }
}
