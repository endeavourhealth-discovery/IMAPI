package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Text {
    private String status;
    private String div;

    @JsonProperty("status")
    public String getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(String value) { this.status = value; }

    @JsonProperty("div")
    public String getDiv() { return div; }
    @JsonProperty("div")
    public void setDiv(String value) { this.div = value; }
}
