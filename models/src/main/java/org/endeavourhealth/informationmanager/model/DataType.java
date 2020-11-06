package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class DataType extends Concept {
    private Set<DataTypeDefinition> dataTypeDefinition;

    @JsonProperty("DataTypeDefinition")
    public Set<DataTypeDefinition> getDataTypeDefinition() {
        return dataTypeDefinition;
    }

    public DataType setDataTypeDefinition(Set<DataTypeDefinition> dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;
        return this;
    }

    public DataType addDataTypeDefinition(DataTypeDefinition dataTypeDefinition) {
        if (this.dataTypeDefinition == null)
            this.dataTypeDefinition = new HashSet<>();

        this.dataTypeDefinition.add(dataTypeDefinition);
        return this;
    }
}
