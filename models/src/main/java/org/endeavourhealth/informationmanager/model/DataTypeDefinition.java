package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataTypeDefinition extends Axiom{
    private DataTypeRestriction dataTypeRestriction;



    @JsonProperty("DataTypeRestriction")
    public DataTypeRestriction getDataTypeRestriction() {
        return dataTypeRestriction;
    }

    public DataTypeDefinition setDataTypeRestriction(DataTypeRestriction dataTypeRestriction) {
        this.dataTypeRestriction = dataTypeRestriction;
        return this;
    }
}
