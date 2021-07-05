package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.Serializable;

public class ValueSetMember implements Serializable {
    private TTIriRef entity;
    private String code;
    private TTIriRef scheme;

    public TTIriRef getEntity() {
        return entity;
    }

    public ValueSetMember setEntity(TTIriRef entity) {
        this.entity = entity;
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
