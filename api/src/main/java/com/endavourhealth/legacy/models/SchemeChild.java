package com.endavourhealth.legacy.models;

public class SchemeChild {
    private String iri;
    private String name;
    private String code;

    public String getIri() {
        return iri;
    }

    public SchemeChild setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public SchemeChild setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SchemeChild setCode(String code) {
        this.code = code;
        return this;
    }
}
