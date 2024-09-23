package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class Contact {
    private Identifier[] telecom;
    private String name;

  public String getName() {
    return name;
  }

  public Contact setName(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("telecom")
    public Identifier[] getTelecom() { return telecom; }
    @JsonProperty("telecom")
    public void setTelecom(Identifier[] value) { this.telecom = value; }
}
