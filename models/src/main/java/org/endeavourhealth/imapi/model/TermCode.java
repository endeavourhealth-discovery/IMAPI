package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class TermCode {
    private String name;
    private String code;
    private String scheme;
    private String entityTermCode;

    public String getName() {
        return name;
    }

    public TermCode setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TermCode setCode(String code) {
        this.code = code;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public TermCode setScheme(String scheme) {
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
