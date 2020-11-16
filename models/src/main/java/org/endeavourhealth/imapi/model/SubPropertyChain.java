package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class SubPropertyChain extends Axiom{
    private Set<ConceptReference> property;

    @JsonProperty("Property")
    public Set<ConceptReference> getProperty() {
        return property;
    }

    public SubPropertyChain setProperty(Set<ConceptReference> property) {
        this.property = property;
        return this;
    }
}
