package com.endavourhealth.models.objectModel;

public class Criterion {
    private String iri;
    private String description;
    private String fromEntity;
    private String operator;
    private Filter filter;
    private Restriction restriction;

    public String getIri() {
        return iri;
    }

    public Criterion setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Criterion setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getFromEntity() {
        return fromEntity;
    }

    public Criterion setFromEntity(String fromEntity) {
        this.fromEntity = fromEntity;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public Criterion setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public Criterion setFilter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public Criterion setRestriction(Restriction restriction) {
        this.restriction = restriction;
        return this;
    }
}
