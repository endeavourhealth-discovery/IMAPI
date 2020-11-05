package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataType extends Concept {
    private List<DataTypeDefinition> dataTypeDefinition;

    @JsonProperty("DataTypeDefinition")
    public List<DataTypeDefinition> getDataTypeDefinition() {
        return dataTypeDefinition;
    }

    public DataType setDataTypeDefinition(List<DataTypeDefinition> dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;
        return this;
    }

    public DataType addDataTypeDefinition(DataTypeDefinition dataTypeDefinition) {
        if (this.dataTypeDefinition == null)
            this.dataTypeDefinition = new ArrayList<>();

        this.dataTypeDefinition.add(dataTypeDefinition);
        return this;
    }
}
