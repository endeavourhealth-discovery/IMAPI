package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptStatus;
import org.endeavourhealth.imapi.model.ConceptType;

import java.util.ArrayList;
import java.util.List;

public class ConceptSummary {
    private String name;
    private String iri;
    private String code;
    private String description;
    private ConceptStatus status;
    private ConceptReference scheme;
    private ConceptType conceptType;
    private List<ConceptReference> isDescendentOf = new ArrayList<>();
    private Integer weighting;

    public String getDescription() {
        return description;
    }

    public ConceptStatus getStatus() {
        return status;
    }

    public ConceptSummary setDescription(String description) {
        this.description = description;
        return this;
    }

    public ConceptSummary setStatus(ConceptStatus status) {
        this.status = status;
        return this;
    }

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

    public ConceptReference getScheme() {
        return scheme;
    }

    public ConceptSummary setScheme(ConceptReference scheme) {
        this.scheme = scheme;
        return this;
    }

    public ConceptType getConceptType() {
        return conceptType;
    }

    public ConceptSummary setConceptType(ConceptType conceptType) {
        this.conceptType = conceptType;
        return this;
    }

    public List<ConceptReference> getIsDescendentOf() {
        return isDescendentOf;
    }

    public ConceptSummary setIsDescendentOf(List<ConceptReference> isDescendentOf) {
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
