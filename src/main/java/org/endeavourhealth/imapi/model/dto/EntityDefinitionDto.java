package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class EntityDefinitionDto {

  private String iri;
  private String name;
  private String description;
  private String status;
  private List<TTIriRef> types;
  private List<TTIriRef> isa;
  private List<TTIriRef> subtypes;

  public String getIri() {
    return iri;
  }

  public EntityDefinitionDto setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public EntityDefinitionDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public EntityDefinitionDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public EntityDefinitionDto setStatus(String status) {
    this.status = status;
    return this;
  }

  public List<TTIriRef> getTypes() {
    return types;
  }

  public EntityDefinitionDto setTypes(List<TTIriRef> types) {
    this.types = types;
    return this;
  }

  public List<TTIriRef> getIsa() {
    return isa;
  }

  public EntityDefinitionDto setIsa(List<TTIriRef> isa) {
    this.isa = isa;
    return this;
  }

  public List<TTIriRef> getSubtypes() {
    return subtypes;
  }

  public EntityDefinitionDto setSubtypes(List<TTIriRef> subtypes) {
    this.subtypes = subtypes;
    return this;
  }
}
