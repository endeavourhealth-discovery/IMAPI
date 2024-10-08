package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class FHIRConcept {
    private String code;
    private String display;
    private String definition;
    private FHIRConcept[] concept;

    @JsonProperty("code")
    public String getCode() { return code; }
    @JsonProperty("code")
    public void setCode(String value) { this.code = value; }

    @JsonProperty("display")
    public String getDisplay() { return display; }
    @JsonProperty("display")
    public void setDisplay(String value) { this.display = value; }

    @JsonProperty("definition")
    public String getDefinition() { return definition; }
    @JsonProperty("definition")
    public void setDefinition(String value) { this.definition = value; }

    @JsonProperty("concept")
    public FHIRConcept[] getConcept() { return concept; }
    @JsonProperty("concept")
    public void setConcept(FHIRConcept[] value) { this.concept = value; }
}
