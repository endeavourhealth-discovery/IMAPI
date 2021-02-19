package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class DataTypeDefinition extends Axiom{
    private ConceptReference dataType;
    private Set<String> oneOf;
    private String minOperator;
    private String minValue;
    private String maxOperator;
    private String maxValue;
    private String pattern;




    public Set<String> getOneOf() {
        return oneOf;
    }

    public DataTypeDefinition setOneOf(Set<String> oneOf) {
        this.oneOf = oneOf;
        return this;
    }
    public DataTypeDefinition addOneOf(String value) {
        if (this.oneOf == null)
            this.oneOf = new HashSet<>();
        this.oneOf.add(value);
        return this;
    }

    public ConceptReference getDataType() {
        return dataType;
    }

    public DataTypeDefinition setDataType(ConceptReference dataType) {
        this.dataType = dataType;
        return this;
    }


    public DataTypeDefinition setMinOperator(String minOperator) {
        this.minOperator= minOperator;
        return this;
    }

    @JsonProperty("minOperator")
    public String getMinOperator() {
        return this.minOperator;
    }

    public DataTypeDefinition setMaxOperator(String maxOperator) {
        this.maxOperator= maxOperator;
        return this;
    }

    @JsonProperty("maxOperator")
    public String getMaxOperator() {
        return this.maxOperator;
    }

    public DataTypeDefinition setPattern(String pattern) {
        this.pattern=pattern;
        return this;
    }

    @JsonProperty("pattern")
    public String getPattern() {
        return this.pattern;
    }

    @JsonProperty("minValue")
    public String getMinValue() {
        return minValue;
    }

    public DataTypeDefinition setMinValue(String minValue) {
        this.minValue= minValue;
        return this;
    }


    @JsonProperty("maxValue")
    public String getMaxValue() {
        return this.maxValue;
    }

    public DataTypeDefinition setMaxValue(String maxValue) {
        this.maxValue= maxValue;
        return this;
    }
}
