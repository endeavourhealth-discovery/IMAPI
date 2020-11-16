package org.endeavourhealth.imapi.model;

public class ConceptReference {
    private String iri;
    private String name;

    public ConceptReference() {}

    public ConceptReference(String iri) {
        this.iri = iri;
    }

    public ConceptReference(String iri, String name) {
        this.iri = iri;
        this.name = name;
    }

    public String getIri() {
        return iri;
    }

    public ConceptReference setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConceptReference setName(String name) {
        this.name = name;
        return this;
    }
}
