package com.endavourhealth.models.objectModel;

public class Filter {
    private String description;
    private Operator operator;

    private Function function;
    private String fromPath;

    private String valueConcept;
    private String valueSet;
    private String comparisonOperator;
    private String exactValue;
    private String valueFrom;
    private String valueTo;

    private Boolean includeNull;

    public String getDescription() {
        return description;
    }

    public Filter setDescription(String description) {
        this.description = description;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public Filter setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public Function getFunction() {
        return function;
    }

    public Filter setFunction(Function function) {
        this.function = function;
        return this;
    }

    public String getFromPath() {
        return fromPath;
    }

    public Filter setFromPath(String fromPath) {
        this.fromPath = fromPath;
        return this;
    }

    public String getValueConcept() {
        return valueConcept;
    }

    public Filter setValueConcept(String valueConcept) {
        this.valueConcept = valueConcept;
        return this;
    }

    public String getValueSet() {
        return valueSet;
    }

    public Filter setValueSet(String valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    public Filter setComparisonOperator(String comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
        return this;
    }

    public String getExactValue() {
        return exactValue;
    }

    public Filter setExactValue(String exactValue) {
        this.exactValue = exactValue;
        return this;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public Filter setValueFrom(String valueFrom) {
        this.valueFrom = valueFrom;
        return this;
    }

    public String getValueTo() {
        return valueTo;
    }

    public Filter setValueTo(String valueTo) {
        this.valueTo = valueTo;
        return this;
    }

    public Boolean getIncludeNull() {
        return includeNull;
    }

    public Filter setIncludeNull(Boolean includeNull) {
        this.includeNull = includeNull;
        return this;
    }
}
