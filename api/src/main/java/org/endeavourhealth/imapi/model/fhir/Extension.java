package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class Extension {
    private String url;
    private String valueCode;

    @JsonProperty("url")
    public String getURL() { return url; }
    @JsonProperty("url")
    public void setURL(String value) { this.url = value; }

    @JsonProperty("valueCode")
    public String getValueCode() { return valueCode; }
    @JsonProperty("valueCode")
    public void setValueCode(String value) { this.valueCode = value; }
}
