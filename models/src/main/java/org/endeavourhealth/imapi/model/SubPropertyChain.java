package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
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
    public SubPropertyChain addProperty(ConceptReference property) {
        if (this.property == null)
            this.property = new HashSet<>();
        this.property.add(property);
        return this;
    }

}
