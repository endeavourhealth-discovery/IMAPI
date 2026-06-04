package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.List;

public class EntityDefinitionDto {

  private String iri;
  private String name;
  private String description;
  private String status;
  private List<TTIriRefExtended> types;
  private List<TTIriRefExtended> isa;
  private List<TTIriRefExtended> subtypes;

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

  public List<TTIriRefExtended> getTypes() {
    return types;
  }

  public EntityDefinitionDto setTypes(List<TTIriRefExtended> types) {
    this.types = types;
    return this;
  }

  public List<TTIriRefExtended> getIsa() {
    return isa;
  }

  public EntityDefinitionDto setIsa(List<TTIriRefExtended> isa) {
    this.isa = isa;
    return this;
  }

  public List<TTIriRefExtended> getSubtypes() {
    return subtypes;
  }

  public EntityDefinitionDto setSubtypes(List<TTIriRefExtended> subtypes) {
    this.subtypes = subtypes;
    return this;
  }
}
