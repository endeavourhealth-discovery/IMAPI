package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;


public class Meta {
    private String lastUpdated;
    private String[] profile;

    @JsonProperty("lastUpdated")
    public String getLastUpdated() { return lastUpdated; }
    @JsonProperty("lastUpdated")
    public void setLastUpdated(String value) { this.lastUpdated = value; }

    @JsonProperty("profile")
    public String[] getProfile() { return profile; }
    @JsonProperty("profile")
    public void setProfile(String[] value) { this.profile = value; }
}
