package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Document {
    private Ontology informationModel;



    @JsonProperty("Ontology")
    public Ontology getInformationModel() {
        return informationModel;
    }

    public Document setInformationModel(Ontology informationModel) {
        this.informationModel = informationModel;
        return this;
    }

}
