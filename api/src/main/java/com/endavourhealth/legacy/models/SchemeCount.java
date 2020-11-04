package com.endavourhealth.legacy.models;

public class SchemeCount {
    private String name;
    private String iri;
    private Integer count;

    public String getName() {
        return name;
    }

    public SchemeCount setName(String name) {
        this.name = name;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public SchemeCount setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public SchemeCount setCount(Integer count) {
        this.count = count;
        return this;
    }
}
