package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"id","property","dataType","exactValue","oneOf","dataTypeRestriction"})
public class DataRangeAxiom extends Axiom implements DataRange{
    private ConceptReference dataType;
    private DataTypeRestriction dataTypeRestriction;
    private Set<ConceptReference> oneOf;
    private String exactValue;


    @JsonProperty("ExactValue")
    public String getExactValue() {
        return exactValue;
    }

    public DataRangeAxiom setExactValue(String value) {
        this.exactValue = value;
        return this;
    }

    @JsonProperty("OneOf")
    public Set<ConceptReference> getOneOf() {
        return oneOf;
    }

    public DataRangeAxiom setOneOf(Set<ConceptReference> oneOf) {
        this.oneOf = oneOf;
        return this;
    }
    public DataRangeAxiom addOneOf(ConceptReference value) {
        if (this.oneOf == null)
            this.oneOf = new HashSet<>();
        this.oneOf.add(value);
        return this;
    }
    public DataRangeAxiom addOneOf(String value) {
        if (this.oneOf == null)
            this.oneOf = new HashSet<>();
        this.oneOf.add(new ConceptReference(value));
        return this;
    }

    @JsonProperty("DataType")
    public ConceptReference getDataType() {
        return dataType;
    }

    @JsonSetter
    public DataRangeAxiom setDataType(ConceptReference dataType) {
        this.dataType = dataType;
        return this;
    }
    public DataRangeAxiom setDataType(String dataType) {
        this.dataType = new ConceptReference(dataType);
        return this;
    }

    @JsonProperty("DataTypeRestriction")
    public DataTypeRestriction getDataTypeRestriction() {
        return dataTypeRestriction;
    }

    public DataRangeAxiom setDataTypeRestriction(DataTypeRestriction dataTypeRestriction) {
        this.dataTypeRestriction = dataTypeRestriction;
        return this;
    }
}
