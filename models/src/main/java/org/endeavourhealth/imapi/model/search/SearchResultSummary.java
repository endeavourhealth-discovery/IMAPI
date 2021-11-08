package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class SearchResultSummary {
    private String name;
    private String iri;
    private String code;
    private String description;
    private TTIriRef status;
    private TTIriRef scheme;
    private TTArray entityType;
    private List<TTIriRef> isDescendentOf = new ArrayList<>();
    private Integer weighting;
    private String match;

    public String getName() {
        return name;
    }

    public SearchResultSummary setName(String name) {
        this.name = name;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public SearchResultSummary setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SearchResultSummary setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SearchResultSummary setDescription(String description) {
        this.description = description;
        return this;
    }

    public TTIriRef getStatus() {
        return status;
    }

    public SearchResultSummary setStatus(TTIriRef status) {
        this.status = status;
        return this;
    }

    public TTIriRef getScheme() {
        return scheme;
    }

    public SearchResultSummary setScheme(TTIriRef scheme) {
        this.scheme = scheme;
        return this;
    }

    public TTArray getEntityType() {
        return entityType;
    }

    public SearchResultSummary setEntityType(TTArray entityType) {
        this.entityType = entityType;
        return this;
    }

    public List<TTIriRef> getIsDescendentOf() {
        return isDescendentOf;
    }

    public SearchResultSummary setIsDescendentOf(List<TTIriRef> isDescendentOf) {
        this.isDescendentOf = isDescendentOf;
        return this;
    }

    public Integer getWeighting() {
        return weighting;
    }

    public SearchResultSummary setWeighting(Integer weighting) {
        this.weighting = weighting;
        return this;
    }

    public String getMatch() {
        return match;
    }

    public SearchResultSummary setMatch(String match) {
        this.match = match;
        return this;
    }
}
