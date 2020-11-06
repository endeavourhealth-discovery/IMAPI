package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyAxiom extends Axiom {
    private ConceptReference property;

    @JsonProperty("Property")
    public ConceptReference getProperty() {
        return property;
    }

    public PropertyAxiom setProperty(ConceptReference property) {
        this.property = property;
        return this;
    }

    public PropertyAxiom setProperty(String property) {
        this.property = new ConceptReference(property);
        return this;
    }

}
