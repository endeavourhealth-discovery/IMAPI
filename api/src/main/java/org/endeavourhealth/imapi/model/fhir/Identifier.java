package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class Identifier {
    private String system;
    private String value;

    @JsonProperty("system")
    public String getSystem() { return system; }
    @JsonProperty("system")
    public void setSystem(String value) { this.system = value; }

    @JsonProperty("value")
    public String getValue() { return value; }
    @JsonProperty("value")
    public void setValue(String value) { this.value = value; }
}
