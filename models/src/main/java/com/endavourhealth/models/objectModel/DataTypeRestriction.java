package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataTypeRestriction {
    private String dataType;
    private List<FacetRestriction> facetRestriction;

    @JsonProperty("DataType")
    public String getDataType() {
        return dataType;
    }

    public DataTypeRestriction setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("FacetRestriction")
    public List<FacetRestriction> getFacetRestriction() {
        return facetRestriction;
    }

    public DataTypeRestriction setFacetRestriction(List<FacetRestriction> facetRestriction) {
        this.facetRestriction = facetRestriction;
        return this;
    }

    public DataTypeRestriction addFacetRestriction(FacetRestriction facetRestriction) {
        if (this.facetRestriction == null)
            this.facetRestriction = new ArrayList<>();

        this.facetRestriction.add(facetRestriction);
        return this;
    }
}
