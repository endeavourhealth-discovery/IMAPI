package com.endavourhealth.legacy.models;

public class RelatedConcept {
    private Integer minCardinality;
    private Integer maxCardinality;
    private Concept relationship;
    private Concept concept;

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public RelatedConcept setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public RelatedConcept setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Concept getRelationship() {
        return relationship;
    }

    public RelatedConcept setRelationship(Concept relationship) {
        this.relationship = relationship;
        return this;
    }

    public Concept getConcept() {
        return concept;
    }

    public RelatedConcept setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }
}
