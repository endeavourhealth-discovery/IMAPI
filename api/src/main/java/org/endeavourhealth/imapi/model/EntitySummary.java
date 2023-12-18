package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import java.util.Objects;

public class EntitySummary {
    private String iri;
    private String name;
    private String code;
    private TTIriRef scheme;

    public String getIri() {
        return iri;
    }

    public EntitySummary setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public EntitySummary setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public EntitySummary setCode(String code) {
        this.code = code;
        return this;
    }

    public TTIriRef getScheme() {
        return scheme;
    }

    @JsonSetter
    public EntitySummary setScheme(TTIriRef scheme) {
        this.scheme = scheme;
        return this;
    }
    public EntitySummary setScheme(Vocabulary scheme) {
        return setScheme(scheme.asTTIriRef());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntitySummary other = (EntitySummary) o;
        return iri.equals(other.iri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri);
    }
}
