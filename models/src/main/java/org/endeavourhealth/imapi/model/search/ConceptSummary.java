package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptType;

import java.util.ArrayList;
import java.util.List;

public class ConceptSummary {
    private String name;
    private String iri;
    private String code;
    private ConceptReference scheme;
    private ConceptType conceptType;
    private List<ConceptReference> types = new ArrayList<>();
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

    public List<ConceptReference> getTypes() {
        return types;
    }

    public ConceptSummary setTypes(List<ConceptReference> types) {
        this.types = types;
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
