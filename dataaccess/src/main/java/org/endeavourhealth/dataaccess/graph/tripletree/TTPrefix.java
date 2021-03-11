package org.endeavourhealth.dataaccess.graph.tripletree;

public class TTPrefix {
    String iri;
    String prefix;

    public TTPrefix() {}

    public TTPrefix(String iri, String prefix) {
        this.iri = iri;
        this.prefix = prefix;
    }

    public String getIri() {
        return iri;
    }

    public TTPrefix setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public TTPrefix setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }
}
