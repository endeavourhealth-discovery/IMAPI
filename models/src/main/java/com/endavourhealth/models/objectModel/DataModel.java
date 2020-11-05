package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private String iri;
    private List<String> entity;

    public String getIri() {
        return iri;
    }

    public DataModel setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("Entity")
    public List<String> getEntity() {
        return entity;
    }

    public DataModel setEntity(List<String> entity) {
        this.entity = entity;
        return this;
    }

    public DataModel addEntity(String entity) {
        if (this.entity == null)
            this.entity = new ArrayList<>();

        this.entity.add(entity);
        return this;
    }
}
