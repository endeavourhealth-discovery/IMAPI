package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnotationPropertyRangeAxiom extends Axiom {
    private String iri;

    @JsonProperty("Iri")
    public String getIri() {
        return iri;
    }

    public AnnotationPropertyRangeAxiom setIri(String iri) {
        this.iri = iri;
        return this;
    }
}
