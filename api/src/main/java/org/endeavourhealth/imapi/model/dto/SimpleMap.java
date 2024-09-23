package org.endeavourhealth.imapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleMap {

  String name;
  String code;
  String scheme;
  @JsonProperty("@id")
  String iri;

  public SimpleMap() {
  }

  public SimpleMap(String iri, String name, String code, String scheme) {
    this.name = name;
    this.code = code;
    this.scheme = scheme;
    this.iri = iri;
  }

  public String getName() {
    return name;
  }

  public SimpleMap setName(String name) {
    this.name = name;
    return this;
  }

  public String getCode() {
    return code;
  }

  public SimpleMap setCode(String code) {
    this.code = code;
    return this;
  }

  public String getScheme() {
    return scheme;
  }

  public SimpleMap setScheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public SimpleMap setIri(String iri) {
    this.iri = iri;
    return this;
  }
}
