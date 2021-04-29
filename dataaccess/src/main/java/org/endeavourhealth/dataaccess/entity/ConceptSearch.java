package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;

@Entity
public class ConceptSearch {

    @Id
    private Integer dbid;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="concept_dbid", referencedColumnName = "dbid", nullable = true)
    private Concept concept;
    private String term;
    private Integer weighting;

    public Integer getDbid() {
        return dbid;
    }

    public ConceptSearch setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getConcept() {
        return concept;
    }

    public ConceptSearch setConcept(Concept concept_dbid) {
        this.concept = concept_dbid;
        return this;
    }

    public String getTerm() {
        return term;
    }

    public ConceptSearch setTerm(String term) {
        this.term = term;
        return this;
    }

    public Integer getWeighting() {
        return weighting;
    }

    public ConceptSearch setWeighting(Integer weighting) {
        this.weighting = weighting;
        return this;
    }
}
