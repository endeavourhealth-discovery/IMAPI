package org.endeavourhealth.imapi.model.ods;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgGeoLocation {
    private OrgLocation location;

    @JsonProperty("Location")
    public OrgLocation getLocation() {
        return location;
    }

    public OrgGeoLocation setLocation(OrgLocation location) {
        this.location = location;
        return this;
    }
}
