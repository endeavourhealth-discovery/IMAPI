package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultSummary {
    private String name;
    private String iri;
    private String code;
    private String description;
    private TTIriRef status;
    private TTIriRef scheme;
    private Set<TTIriRef> entityType= new HashSet<>();
    private Set<TTIriRef> isDescendentOf = new HashSet<>();
    private Integer weighting;
    private String match;
    private Set<String> key;
    Set<SearchTermCode> termCode = new HashSet<>();


    public SearchResultSummary(String name, String iri, String code, String description, TTIriRef status, TTIriRef scheme, Set<TTIriRef> entityTypes, Set<TTIriRef> isDescendentOf, Integer weighting, String match) {
        this.name = name;
        this.iri = iri;
        this.code = code;
        this.description = description;
        this.status = status;
        this.scheme = scheme;
        this.entityType = entityTypes;
        this.isDescendentOf = isDescendentOf;
        this.weighting = weighting;
        this.match = match;
    }

    public SearchResultSummary() {
    }

    public String getName() {
        return name;
    }

    public SearchResultSummary setName(String name) {
        this.name = name;
        return this;
    }
    @JsonSetter("name")
    public SearchResultSummary setNameFromJson(String name) {
        this.name = name;
        if (this.match==null)
            this.match=name;
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

    public Set<TTIriRef> getEntityType() {
        return entityType;
    }

    public SearchResultSummary setEntityType(Set<TTIriRef> entityType) {
        this.entityType = entityType;
        return this;
    }

    public Set<TTIriRef> getIsDescendentOf() {
        return isDescendentOf;
    }

    public SearchResultSummary setIsDescendentOf(Set<TTIriRef> isDescendentOf) {
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

    public Set<SearchTermCode> getTermCode() {
        return termCode;
    }

    public SearchResultSummary setTermCode(Set<SearchTermCode> searchTermCodes) {
        this.termCode = searchTermCodes;
        return this;
    }

    public Set<String> getKey() {
        return key;
    }

    public SearchResultSummary setKey(Set<String> key) {
        this.key = key;
        return this;
    }
}
