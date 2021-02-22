package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyAxiom extends Axiom {
    private ConceptReference property;


    public ConceptReference getProperty() {
        return property;
    }

    public PropertyAxiom setProperty(ConceptReference property) {
        this.property = property;
        return this;
    }


}
