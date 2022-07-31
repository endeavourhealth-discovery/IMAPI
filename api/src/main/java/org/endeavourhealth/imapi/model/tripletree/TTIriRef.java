package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(TTIriRef.class);

    private String iri;
    private String name;

    public TTIriRef() {
    }
    public TTIriRef(String iri) {
        setIri(iri);
    }
    public TTIriRef(String iri, String name) {
        setIri(iri);
        setName(name);
    }

    @JsonProperty("@id")
    public String getIri() {
        return this.iri;
    }

    public TTIriRef setIri(String iri) {
        this.iri = iri;
        if (iri != null && !iri.isEmpty() && !iri.matches("[a-z]+[:].*")){
            LOG.error("Invalid IRI [{}]", iri);
            Thread.dumpStack();
        }
        return this;
    }

    @JsonProperty("@id")
    public TTIriRef setId(String iri){
        this.iri=iri;
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
        if (!(o instanceof TTIriRef)) return false;
        TTIriRef ttIriRef = (TTIriRef) o;
        return iri.equals(ttIriRef.iri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri);
    }
}
