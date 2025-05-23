package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

@Getter
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

  public Entity setIri(String iri) {
    this.iri = iri;
    return this;
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

  public Entity setName(String name) {
    this.name = name;
    return this;
  }

  public Entity setDescription(String description) {
    this.description = description;
    return this;
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

  @JsonSetter
  public Entity setStatus(TTIriRef status) {
    this.status = status;
    return this;
  }

  @JsonSetter
  public Entity setScheme(TTIriRef scheme) {
    this.scheme = scheme;
    return this;
  }
}
