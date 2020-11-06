package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacetRestriction {
    private String facet;
    private String constrainingFacet;

    @JsonProperty("Facet")
    public String getFacet() {
        return facet;
    }

    public FacetRestriction setFacet(String facet) {
        this.facet = facet;
        return this;
    }

    @JsonProperty("ConstrainingFacet")
    public String getConstrainingFacet() {
        return constrainingFacet;
    }

    public FacetRestriction setConstrainingFacet(String constrainingFacet) {
        this.constrainingFacet = constrainingFacet;
        return this;
    }
}
