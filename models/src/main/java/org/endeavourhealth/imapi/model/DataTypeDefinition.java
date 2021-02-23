package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class DataTypeDefinition extends Axiom{
    private ConceptReference dataType;
    private Set<String> oneOf;
    private String minInclusive;
    private String minExclusive;
    private String maxInclusive;
    private String maxExclusive;
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




    public DataTypeDefinition setPattern(String pattern) {
        this.pattern=pattern;
        return this;
    }

    @JsonProperty("pattern")
    public String getPattern() {
        return this.pattern;
    }

    public String getMinInclusive() {
        return minInclusive;
    }

    public DataTypeDefinition setMinInclusive(String minInclusive) {
        this.minInclusive = minInclusive;
        return this;
    }

    public String getMinExclusive() {
        return minExclusive;
    }

    public DataTypeDefinition setMinExclusive(String minExclusive) {
        this.minExclusive = minExclusive;
        return this;
    }

    public String getMaxInclusive() {
        return maxInclusive;
    }

    public DataTypeDefinition setMaxInclusive(String maxInclusive) {
        this.maxInclusive = maxInclusive;
        return this;
    }

    public String getMaxExclusive() {
        return maxExclusive;
    }

    public DataTypeDefinition setMaxExclusive(String maxExclusive) {
        this.maxExclusive = maxExclusive;
        return this;
    }
}
