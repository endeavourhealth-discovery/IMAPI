package org.endeavourhealth.imapi.model.tripletree;

public class TTPrefix {
  String iri;
  String prefix;
  String name;

  public TTPrefix() {
  }

  public TTPrefix(String iri, String prefix) {
    this.iri = iri;
    this.prefix = prefix;
  }

  public TTPrefix(String iri, String prefix, String name) {
    this.iri = iri;
    this.prefix = prefix;
    this.name = name;
  }

  public String getIri() {
    return iri;
  }

  public TTPrefix setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getPrefix() {
    return prefix;
  }

  public TTPrefix setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public String getName() {
    return name;
  }

  public TTPrefix setName(String name) {
    this.name = name;
    return this;
  }
}
