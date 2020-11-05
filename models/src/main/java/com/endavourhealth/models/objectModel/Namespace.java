package com.endavourhealth.models.objectModel;

public class Namespace {
    private String prefix;
    private String iri;
    private String version;

    public String getPrefix() {
        return prefix;
    }

    public Namespace setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Namespace setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Namespace setVersion(String version) {
        this.version = version;
        return this;
    }
}
