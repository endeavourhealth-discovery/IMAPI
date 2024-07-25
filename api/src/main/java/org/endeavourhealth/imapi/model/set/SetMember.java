package org.endeavourhealth.imapi.model.set;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.Serializable;

public class SetMember implements Serializable {
  private TTIriRef entity;
  private String code;
  private TTIriRef scheme;
  private String label;
  private MemberType type;
  private TTIriRef directParent;

  public TTIriRef getEntity() {
    return entity;
  }

  @JsonSetter
  public SetMember setEntity(TTIriRef entity) {
    this.entity = entity;
    return this;
  }

  public String getCode() {
    return code;
  }

  public SetMember setCode(String code) {
    this.code = code;
    return this;
  }

  public TTIriRef getScheme() {
    return scheme;
  }

  @JsonSetter
  public SetMember setScheme(TTIriRef scheme) {
    this.scheme = scheme;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public SetMember setLabel(String label) {
    this.label = label;
    return this;
  }

  public MemberType getType() {
    return type;
  }

  public void setType(MemberType type) {
    this.type = type;
  }

  public TTIriRef getDirectParent() {
    return directParent;
  }

  @JsonSetter
  public void setDirectParent(TTIriRef directParent) {
    this.directParent = directParent;
  }
}
