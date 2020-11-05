package com.endavourhealth.models.objectModel;

import java.util.List;

public class DataSetAttribute {
    private String iri;
    private String fromPath;
    private List<Criterion> filter;
    private String assignedValue;

    public String getIri() {
        return iri;
    }

    public DataSetAttribute setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getFromPath() {
        return fromPath;
    }

    public DataSetAttribute setFromPath(String fromPath) {
        this.fromPath = fromPath;
        return this;
    }

    public List<Criterion> getFilter() {
        return filter;
    }

    public DataSetAttribute setFilter(List<Criterion> filter) {
        this.filter = filter;
        return this;
    }

    public String getAssignedValue() {
        return assignedValue;
    }

    public DataSetAttribute setAssignedValue(String assignedValue) {
        this.assignedValue = assignedValue;
        return this;
    }
}
