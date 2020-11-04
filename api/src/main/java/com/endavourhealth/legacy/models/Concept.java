package com.endavourhealth.legacy.models;

public class Concept {
    private String iri;
    private String name;
    private String description;

    public Concept() {}

    public Concept(String iri, String name) {
        this.iri = iri;
        this.name = name;
    }

    public String getIri() {
        return iri;
    }

    public Concept setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public Concept setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }
}
