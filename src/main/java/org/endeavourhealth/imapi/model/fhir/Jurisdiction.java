package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Jurisdiction {
    private Coding[] coding;

    @JsonProperty("coding")
    public Coding[] getCoding() { return coding; }
    @JsonProperty("coding")
    public void setCoding(Coding[] value) { this.coding = value; }
}
