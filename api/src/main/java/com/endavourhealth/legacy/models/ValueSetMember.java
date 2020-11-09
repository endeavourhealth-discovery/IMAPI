package com.endavourhealth.legacy.models;

import com.fasterxml.jackson.databind.JsonNode;

public class ValueSetMember {
    private String iri;
    private String name;
    private String code;
    private String operatorName;

    public String getIri() {
        return iri;
    }

    public ValueSetMember setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public ValueSetMember setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ValueSetMember setCode(String code) {
        this.code = code;
        return this;
    }

	public String getOperatorName() {
		return operatorName;
	}

	public ValueSetMember setOperatorName(String operatorName) {
		this.operatorName = operatorName;
		return this;
	}
}
