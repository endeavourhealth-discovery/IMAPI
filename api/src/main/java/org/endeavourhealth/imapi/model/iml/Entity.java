package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"iri", "label", "type", "comment", "status", "scheme", "isContainedIn", "subClassOf"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Entity {
  private String iri;
  private Set<TTIriRef> entityType;
  private String name;
  private String description;
  private Set<TTEntity> isContainedIn;
  private TTIriRef status;
  private TTIriRef scheme;

  @JsonProperty("@id")
  public String getIri() {
    return iri;
  }

  public Entity setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public Set<TTIriRef> getEntityType() {
    return entityType;
  }

  public Entity setEntityType(Set<TTIriRef> entityType) {
    this.entityType = entityType;
    return this;
  }

  public Entity addType(TTIriRef newType) {
    if (null != entityType) {
      entityType.add(newType);
    } else {
      entityType = new HashSet<>();
      entityType.add(newType);
    }
    return this;
  }

  public String getName() {
    return name;
  }

  public Entity setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Entity setDescription(String description) {
    this.description = description;
    return this;
  }

  public Set<TTEntity> getIsContainedIn() {
    return isContainedIn;
  }

  public Entity setIsContainedIn(Set<TTEntity> isContainedIn) {
    this.isContainedIn = isContainedIn;
    return this;
  }

  public Entity addIsContainedIn(TTEntity folder) {
    if (this.isContainedIn == null)
      this.isContainedIn = new HashSet<>();
    this.isContainedIn.add(folder);
    return this;
  }

  public TTIriRef getStatus() {
    return status;
  }

  @JsonSetter
  public Entity setStatus(TTIriRef status) {
    this.status = status;
    return this;
  }

  public TTIriRef getScheme() {
    return scheme;
  }

  @JsonSetter
  public Entity setScheme(TTIriRef scheme) {
    this.scheme = scheme;
    return this;
  }
}
