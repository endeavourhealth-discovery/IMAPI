package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity

public class SearchResult  {

    @EmbeddedId
    private SearchResultId id;
    private String iri;
    private String name;
  //  private String type;
    private String code;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="scheme", referencedColumnName = "iri", nullable = true)
    private Concept scheme;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="status", referencedColumnName = "iri", nullable = true)
    private Concept status;
    private String description;

    public SearchResultId getId() {
        return id;
    }

    public SearchResult setId(SearchResultId id) {
        this.id = id;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public SearchResult setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public SearchResult setName(String name) {
        this.name = name;
        return this;
    }

//    public String getType() {
//        return type;
//    }
//
//    public SearchResult setType(String type) {
//        this.type = type;
//        return this;
//    }

    public String getCode() {
        return code;
    }

    public SearchResult setCode(String code) {
        this.code = code;
        return this;
    }

    public Concept getScheme() {
        return scheme;
    }

    public SearchResult setScheme(Concept scheme) {
        this.scheme = scheme;
        return this;
    }

    public Concept getStatus() {
        return status;
    }

    public SearchResult setStatus(Concept status) {
        this.status = status;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SearchResult setDescription(String description) {
        this.description = description;
        return this;
    }
}
