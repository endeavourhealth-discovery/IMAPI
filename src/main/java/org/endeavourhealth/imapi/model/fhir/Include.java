package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Include {
    private String system;

    @JsonProperty("system")
    public String getSystem() { return system; }
    @JsonProperty("system")
    public void setSystem(String value) { this.system = value; }
}
