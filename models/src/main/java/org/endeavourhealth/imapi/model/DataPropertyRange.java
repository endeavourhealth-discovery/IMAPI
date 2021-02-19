package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","property","dataType"})
public class DataPropertyRange extends Axiom{
    private ConceptReference dataType;



    public ConceptReference getDataType() {
        return dataType;
    }

    public DataPropertyRange setDataType(ConceptReference dataType) {
        this.dataType = dataType;
        return this;
    }

}
