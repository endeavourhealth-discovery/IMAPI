package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class PropertyAxiom extends Axiom {
    private ConceptReference property;

    @JsonProperty("Property")
    public ConceptReference getProperty() {
        return property;
    }

    @JsonSetter
    public PropertyAxiom setProperty(ConceptReference property) {
        this.property = property;
        return this;
    }

    public PropertyAxiom setProperty(String property) {
        this.property = new ConceptReference(property);
        return this;
    }

}
