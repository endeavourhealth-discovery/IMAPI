package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ValueSet {
    private String iri;
    private List<ClassExpression> member;

    public String getIri() {
        return iri;
    }

    public ValueSet setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("Member")
    public List<ClassExpression> getMember() {
        return member;
    }

    public ValueSet setMember(List<ClassExpression> member) {
        this.member = member;
        return this;
    }
}
