package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.Entity;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@JsonPropertyOrder({"iri", "label", "type", "comment", "status", "scheme", "isContainedIn", "subClassOf"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class EntityExtended extends Entity {

  public EntityExtended iri(String iri) {
    this.setIri(iri);
    return this;
  }

  public EntityExtended type(Set<TTIriRef> type) {
    this.setType(type.stream().toList());
    return this;
  }

  public Set<TTIriRef> getTypeAsSet() {
    if (this.getType() == null) {
      return new HashSet<>();
    }
    return new HashSet<>(this.getType());
  }

  public EntityExtended addType(TTIriRef newType) {
    if (null != this.getType()) {
      this.getType().add(newType);
    } else {
      this.setType(new ArrayList<>());
      this.getType().add(newType);
    }
    return this;
  }

  public EntityExtended name(String name) {
    this.setName(name);
    return this;
  }

  public EntityExtended description(String description) {
    this.setDescription(description);
    return this;
  }

  public EntityExtended isContainedIn(Set<TTEntity> isContainedIn) {
    this.setIsContainedIn(isContainedIn.stream().toList());
    return this;
  }

  public EntityExtended addIsContainedIn(TTEntity folder) {
    if (this.getIsContainedIn() == null)
      this.setIsContainedIn(new ArrayList<>());
    this.getIsContainedIn().add(folder);
    return this;
  }

  @JsonSetter
  public EntityExtended status(TTIriRefExtended status) {
    this.setStatus(status);
    return this;
  }

  @JsonSetter
  public EntityExtended scheme(TTIriRefExtended scheme) {
    this.setScheme(scheme);
    return this;
  }
}
