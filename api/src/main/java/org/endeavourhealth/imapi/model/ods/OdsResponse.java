package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OdsResponse {
    private Organisation organisation;

    @JsonProperty("Organisation")
    public Organisation getOrganisation() {
        return organisation;
    }

    public OdsResponse setOrganisation(Organisation organisation) {
        this.organisation = organisation;
        return this;
    }
}
