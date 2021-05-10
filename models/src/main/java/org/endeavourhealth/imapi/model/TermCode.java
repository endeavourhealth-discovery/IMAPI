package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class TermCode {
    private String term;
    private String code;
    private TTIriRef scheme;
    private String concept_term_code;

    public String getTerm() {
        return term;
    }

    public TermCode setTerm(String term) {
        this.term = term;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TermCode setCode(String code) {
        this.code = code;
        return this;
    }

    public TTIriRef getScheme() {
        return scheme;
    }

    public TermCode setScheme(TTIriRef scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getConcept_term_code() {
        return concept_term_code;
    }

    public TermCode setConcept_term_code(String concept_term_code) {
        this.concept_term_code = concept_term_code;
        return this;
    }
}
