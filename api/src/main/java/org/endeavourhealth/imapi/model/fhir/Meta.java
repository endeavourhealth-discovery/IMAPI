package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;

public class Meta {
    private OffsetDateTime lastUpdated;
    private String[] profile;

    @JsonProperty("lastUpdated")
    public OffsetDateTime getLastUpdated() { return lastUpdated; }
    @JsonProperty("lastUpdated")
    public void setLastUpdated(OffsetDateTime value) { this.lastUpdated = value; }

    @JsonProperty("profile")
    public String[] getProfile() { return profile; }
    @JsonProperty("profile")
    public void setProfile(String[] value) { this.profile = value; }
}
