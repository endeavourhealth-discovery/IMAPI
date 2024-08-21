package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class Contact {
    private Identifier[] telecom;

    @JsonProperty("telecom")
    public Identifier[] getTelecom() { return telecom; }
    @JsonProperty("telecom")
    public void setTelecom(Identifier[] value) { this.telecom = value; }
}
