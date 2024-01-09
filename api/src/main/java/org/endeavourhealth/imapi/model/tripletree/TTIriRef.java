package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TTIriRef implements TTValue, Serializable {
    public static TTIriRef iri(String iri) {
        return new TTIriRef(iri);
    }
    public static TTIriRef iri(String iri, String name) {
        return new TTIriRef(iri, name);
    }

    private static Pattern iriPattern = Pattern.compile("([a-z]+)?[:].*");
    private String iri;
    @JsonProperty(defaultValue = "")
    private String name;
    @JsonProperty(defaultValue = "")
    private String description;



    public String getDescription() {
        return description;
    }

    public TTIriRef setDescription(String description) {
        this.description = description;
        return this;
    }

    public TTIriRef() {
    }
    public TTIriRef(String iri) {
        setIri(iri);
    }
    public TTIriRef(String iri, String name) {
        setIri(iri);
        setName(name);
    }

    @JsonProperty(value="@id",required = true)
    @JsonAlias({"@id"})
    public String getIri() {
        return this.iri;
    }

    public TTIriRef setIri(String iri) {
        this.iri = iri;
        if (iri != null && !iri.isEmpty() && !iriPattern.matcher(iri).matches()){
            iri= IM.NAMESPACE+iri;
            if (!iriPattern.matcher(iri).matches())
                Thread.dumpStack();
        }
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
