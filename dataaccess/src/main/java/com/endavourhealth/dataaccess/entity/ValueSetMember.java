package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ValueSetMember {
    @Id
    private String conceptIri;
    private String conceptName;
    private String code;
    private String schemeIri;
    private String schemeName;

    public String getConceptIri() {
        return conceptIri;
    }

    public ValueSetMember setConceptIri(String conceptIri) {
        this.conceptIri = conceptIri;
        return this;
    }

    public String getConceptName() {
        return conceptName;
    }

    public ValueSetMember setConceptName(String conceptName) {
        this.conceptName = conceptName;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ValueSetMember setCode(String code) {
        this.code = code;
        return this;
    }

    public String getSchemeIri() {
        return schemeIri;
    }

    public ValueSetMember setSchemeIri(String schemeIri) {
        this.schemeIri = schemeIri;
        return this;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public ValueSetMember setSchemeName(String schemeName) {
        this.schemeName = schemeName;
        return this;
    }
}
