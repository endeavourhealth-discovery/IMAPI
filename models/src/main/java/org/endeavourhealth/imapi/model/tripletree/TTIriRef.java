package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TTIriRef implements TTValue, Serializable {
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

    @JsonProperty("@id")
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
    @JsonIgnore
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
