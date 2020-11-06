package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class DataTypeRestriction {
    private ConceptReference dataType;
    private Set<FacetRestriction> facetRestriction;

    @JsonProperty("DataType")
    public ConceptReference getDataType() {
        return dataType;
    }

    public DataTypeRestriction setDataType(ConceptReference dataType) {
        this.dataType = dataType;
        return this;
    }
    public DataTypeRestriction setDataType(String dataType) {
        this.dataType = new ConceptReference(dataType);
        return this;
    }

    @JsonProperty("FacetRestriction")
    public Set<FacetRestriction> getFacetRestriction() {
        return facetRestriction;
    }

    public DataTypeRestriction setFacetRestriction(Set<FacetRestriction> facetRestriction) {
        this.facetRestriction = facetRestriction;
        return this;
    }

    public DataTypeRestriction addFacetRestriction(FacetRestriction facetRestriction) {
        if (this.facetRestriction == null)
            this.facetRestriction = new HashSet<>();

        this.facetRestriction.add(facetRestriction);
        return this;
    }
}
