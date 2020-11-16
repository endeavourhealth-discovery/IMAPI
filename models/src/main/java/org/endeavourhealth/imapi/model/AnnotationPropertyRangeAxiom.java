package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnotationPropertyRangeAxiom extends Axiom {
    private ConceptReference concept;

    public AnnotationPropertyRangeAxiom setIri(String iri) {
        this.concept = new ConceptReference(iri);
        return this;
    }

    @JsonProperty("Concept")
    public ConceptReference getConcept() {
        return concept;
    }

    public AnnotationPropertyRangeAxiom setConcept(ConceptReference concept) {
        this.concept = concept;
        return this;
    }
}
