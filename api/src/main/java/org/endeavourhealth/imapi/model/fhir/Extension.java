package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

public class Extension  {
    private String url;
    private String valueCode;
    private Integer valueInteger;

  public String getUrl() {
    return url;
  }

  public Integer getValueInteger() {
    return valueInteger;
  }

  public Extension setValueInteger(Integer valueInteger) {
    this.valueInteger = valueInteger;
    return this;
  }

  public Extension setUrl(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
    public String getURL() { return url; }
    @JsonProperty("url")
    public void setURL(String value) { this.url = value; }

    @JsonProperty("valueCode")
    public String getValueCode() { return valueCode; }
    @JsonProperty("valueCode")
    public void setValueCode(String value) { this.valueCode = value; }
}
