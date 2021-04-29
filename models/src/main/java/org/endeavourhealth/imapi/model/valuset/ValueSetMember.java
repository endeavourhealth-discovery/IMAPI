package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ValueSetMember {
    private TTIriRef concept;
    private String code;
    private TTIriRef scheme;

    public TTIriRef getConcept() {
        return concept;
    }

    public ValueSetMember setConcept(TTIriRef concept) {
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

    public TTIriRef getScheme() {
        return scheme;
    }

    public ValueSetMember setScheme(TTIriRef scheme) {
        this.scheme = scheme;
        return this;
    }
}
