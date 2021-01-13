package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.ConceptReference;

public class ValueSetMember {
    private ConceptReference concept;
    private String code;
    private ConceptReference scheme;

    public ConceptReference getConcept() {
        return concept;
    }

    public ValueSetMember setConcept(ConceptReference concept) {
        this.concept = concept;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ValueSetMember setCode(String code) {
        this.code = code;
        return this;
    }

    public ConceptReference getScheme() {
        return scheme;
    }

    public ValueSetMember setScheme(ConceptReference scheme) {
        this.scheme = scheme;
        return this;
    }
}
