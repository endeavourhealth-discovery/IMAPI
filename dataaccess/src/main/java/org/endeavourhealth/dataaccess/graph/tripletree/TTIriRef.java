package org.endeavourhealth.dataaccess.graph.tripletree;

import org.eclipse.rdf4j.model.IRI;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class TTIriRef extends TTValue {
    private IRI iri;
    private String name;

    public TTIriRef() {
    }
    public TTIriRef(IRI iri) {
        this.iri = iri;
    }
    public TTIriRef(IRI iri, String name) {
        this.iri = iri;
        this.name = name;
    }

    public String getIri() {
        return this.iri.stringValue();
    }

    public TTIriRef setIri(String iri) {
        this.iri = iri(iri);
        return this;
    }

    public String getName() {
        return name;
    }

    public TTIriRef setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public TTIriRef asIriRef() {
        return this;
    }

    @Override
    public boolean isIriRef() {
        return true;
    }
}
