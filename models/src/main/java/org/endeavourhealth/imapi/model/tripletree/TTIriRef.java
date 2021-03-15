package org.endeavourhealth.imapi.model.tripletree;

import java.util.Objects;

public class TTIriRef extends TTValue {
    public static TTIriRef iri(String iri) {
        return new TTIriRef(iri);
    }

    public static TTIriRef iri(String iri, String name) {
        return new TTIriRef(iri, name);
    }

    private String iri;
    private String name;

    public TTIriRef() {
    }
    public TTIriRef(String iri) {
        this.iri = iri;
    }
    public TTIriRef(String iri, String name) {
        this.iri = iri;
        this.name = name;
    }

    public String getIri() {
        return this.iri;
    }

    public TTIriRef setIri(String iri) {
        this.iri = iri;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTIriRef ttIriRef = (TTIriRef) o;
        return iri.equals(ttIriRef.iri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri);
    }
}
