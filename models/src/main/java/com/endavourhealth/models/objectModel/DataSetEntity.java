package com.endavourhealth.models.objectModel;

import java.util.List;

public class DataSetEntity {
    private String iri;
    private List<Criterion> filter;
    private List<DataSetAttribute> attribute;

    public String getIri() {
        return iri;
    }

    public DataSetEntity setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public List<Criterion> getFilter() {
        return filter;
    }

    public DataSetEntity setFilter(List<Criterion> filter) {
        this.filter = filter;
        return this;
    }

    public List<DataSetAttribute> getAttribute() {
        return attribute;
    }

    public DataSetEntity setAttribute(List<DataSetAttribute> attribute) {
        this.attribute = attribute;
        return this;
    }
}
