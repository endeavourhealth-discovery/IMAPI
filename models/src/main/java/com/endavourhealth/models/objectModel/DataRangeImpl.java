package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataRangeImpl implements DataRange {
    private String dataType;
    private DataTypeRestriction dataTypeRestriction;
    private List<String> oneOf;
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
    public List<String> getOneOf() {
        return oneOf;
    }

    public DataRange setOneOf(List<String> oneOf) {
        this.oneOf = oneOf;
        return this;
    }
    public DataRange addOneOf(String value) {
        if (this.oneOf == null)
            this.oneOf = new ArrayList<>();
        this.oneOf.add(value);
        return this;
    }

    @JsonProperty("DataType")
    public String getDataType() {
        return dataType;
    }

    public DataRange setDataType(String dataType) {
        this.dataType = dataType;
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
