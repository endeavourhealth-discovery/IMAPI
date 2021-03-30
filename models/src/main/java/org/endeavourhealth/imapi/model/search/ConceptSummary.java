package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class ConceptSummary {
    private String name;
    private String iri;
    private String code;
    private String description;
    private TTIriRef status;
    private TTIriRef scheme;
    private TTIriRef conceptType;
    private List<TTIriRef> isDescendentOf = new ArrayList<>();
    private Integer weighting;

    public String getName() {
        return name;
    }

    public ConceptSummary setName(String name) {
        this.name = name;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public ConceptSummary setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ConceptSummary setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ConceptSummary setDescription(String description) {
        this.description = description;
        return this;
    }

    public TTIriRef getStatus() {
        return status;
    }

    public ConceptSummary setStatus(TTIriRef status) {
        this.status = status;
        return this;
    }

    public TTIriRef getScheme() {
        return scheme;
    }

    public ConceptSummary setScheme(TTIriRef scheme) {
        this.scheme = scheme;
        return this;
    }

    public TTIriRef getConceptType() {
        return conceptType;
    }

    public ConceptSummary setConceptType(TTIriRef conceptType) {
        this.conceptType = conceptType;
        return this;
    }

    public List<TTIriRef> getIsDescendentOf() {
        return isDescendentOf;
    }

    public ConceptSummary setIsDescendentOf(List<TTIriRef> isDescendentOf) {
        this.isDescendentOf = isDescendentOf;
        return this;
    }

    public Integer getWeighting() {
        return weighting;
    }

    public ConceptSummary setWeighting(Integer weighting) {
        this.weighting = weighting;
        return this;
    }
}
