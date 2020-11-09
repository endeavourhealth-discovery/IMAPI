package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class AnnotationPropertyRangeAxiom extends Axiom {
    private ConceptReference concept;

    @JsonProperty("Concept")
    public ConceptReference getConcept() {
        return concept;
    }

    @JsonSetter
    public AnnotationPropertyRangeAxiom setConcept(ConceptReference concept) {
        this.concept = concept;
        return this;
    }

    public AnnotationPropertyRangeAxiom setConcept(String iri) {
        this.concept = new ConceptReference(iri);
        return this;
    }

}
