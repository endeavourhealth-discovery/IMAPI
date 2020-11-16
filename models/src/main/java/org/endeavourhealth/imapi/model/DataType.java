package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"conceptType","id","status","version","iri","name","description",
    "code","scheme","annotations","dataTypeDefinition"})
public class DataType extends Concept {
    private DataTypeDefinition dataTypeDefinition;

    public DataType(){
        this.setConceptType(ConceptType.DATATYPE);
    }

    @JsonProperty("DataTypeDefinition")
    public DataTypeDefinition getDataTypeDefinition() {
        return dataTypeDefinition;
    }

    public DataType setDataTypeDefinition(DataTypeDefinition dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;
        return this;
    }

}
