package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class DataRangeImpl implements DataRange {
    private ConceptReference dataType;
    private DataTypeRestriction dataTypeRestriction;
    private Set<ConceptReference> oneOf;
    private String exactValue;


    @JsonProperty("ExactValue")
    public String getExactValue() {
        return exactValue;
    }

    public DataRange setExactValue(String value) {
        this.exactValue = value;
        return this;
    }

    @JsonProperty("OneOf")
    public Set<ConceptReference> getOneOf() {
        return oneOf;
    }

    public DataRange setOneOf(Set<ConceptReference> oneOf) {
        this.oneOf = oneOf;
        return this;
    }
    public DataRange addOneOf(ConceptReference value) {
        if (this.oneOf == null)
            this.oneOf = new HashSet<>();
        this.oneOf.add(value);
        return this;
    }
    public DataRange addOneOf(String value) {
        if (this.oneOf == null)
            this.oneOf = new HashSet<>();
        this.oneOf.add(new ConceptReference(value));
        return this;
    }
    @JsonProperty("DataType")
    public ConceptReference getDataType() {
        return dataType;
    }

    public DataRange setDataType(ConceptReference dataType) {
        this.dataType = dataType;
        return this;
    }

    public DataRange setDataType(String dataType) {
        this.dataType = new ConceptReference(dataType);
        return this;
    }

    @JsonProperty("DataTypeRestriction")
    public DataTypeRestriction getDataTypeRestriction() {
        return dataTypeRestriction;
    }

    public DataRange setDataTypeRestriction(DataTypeRestriction dataTypeRestriction) {
        this.dataTypeRestriction = dataTypeRestriction;
        return this;
    }
}
