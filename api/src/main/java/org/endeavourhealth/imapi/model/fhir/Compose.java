package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class Compose {
    private Include[] include;

    @JsonProperty("include")
    public Include[] getInclude() { return include; }
    @JsonProperty("include")
    public void setInclude(Include[] value) { this.include = value; }
}
