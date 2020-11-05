package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

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
