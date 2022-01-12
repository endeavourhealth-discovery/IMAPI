package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.io.Serializable;

public class ValueSetMember implements Serializable {
    private TTIriRef entity;
    private String code;
    private TTIriRef scheme;
    private String label;
    private MemberType type;
    private TTIriRef directParent;

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

    public String getLabel() {
        return label;
    }

    public ValueSetMember setLabel(String label) {
        this.label = label;
        return this;
    }

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }

    public TTIriRef getDirectParent() {
        return directParent;
    }

    public void setDirectParent(TTIriRef directParent) {
        this.directParent = directParent;
    }
}
