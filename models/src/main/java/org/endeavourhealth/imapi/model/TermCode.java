package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class TermCode {
    private String term;
    private String code;
    private TTIriRef scheme;
    private String entityTermCode;

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

    public String getEntityTermCode() {
        return entityTermCode;
    }

    public TermCode setEntityTermCode(String entityTermCode) {
        this.entityTermCode = entityTermCode;
        return this;
    }
}
