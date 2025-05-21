package org.endeavourhealth.imapi.model.dto;


import lombok.Getter;

@Getter
public class SimpleMap {

  String name;
  String code;
  String scheme;
  String iri;

  public SimpleMap() {
  }

  public SimpleMap(String iri, String name, String code, String scheme) {
    this.name = name;
    this.code = code;
    this.scheme = scheme;
    this.iri = iri;
  }

  public SimpleMap setName(String name) {
    this.name = name;
    return this;
  }

  public SimpleMap setCode(String code) {
    this.code = code;
    return this;
  }

  public SimpleMap setScheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  public SimpleMap setIri(String iri) {
    this.iri = iri;
    return this;
  }
}
