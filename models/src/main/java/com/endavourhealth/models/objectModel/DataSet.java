package com.endavourhealth.models.objectModel;

import java.util.List;

public class DataSet {
    private String iri;
    private List<DataSetEntity> entity;

    public String getIri() {
        return iri;
    }

    public DataSet setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public List<DataSetEntity> getEntity() {
        return entity;
    }

    public DataSet setEntity(List<DataSetEntity> entity) {
        this.entity = entity;
        return this;
    }
}
