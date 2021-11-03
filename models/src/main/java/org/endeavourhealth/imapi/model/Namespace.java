package org.endeavourhealth.imapi.model;

public class Namespace {
    private String iri;
    private String prefix;
    private String name;

    public Namespace(String iri, String prefix, String name) {
        this.iri = iri;
        this.prefix = prefix;
        this.name = name;
    }

    public String getIri() {
        return iri;
    }

    public Namespace setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Namespace setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getName() {
        return name;
    }

    public Namespace setName(String name) {
        this.name = name;
        return this;
    }
}
