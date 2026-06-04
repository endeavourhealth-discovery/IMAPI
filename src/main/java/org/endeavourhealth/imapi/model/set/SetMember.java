package org.endeavourhealth.imapi.model.set;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.io.Serializable;

public class SetMember implements Serializable {
  private TTIriRefExtended entity;
  private String code;
  private TTIriRefExtended scheme;
  private String label;
  private MemberType type;
  private TTIriRefExtended directParent;

  public TTIriRefExtended getEntity() {
    return entity;
  }

  @JsonSetter
  public SetMember setEntity(TTIriRefExtended entity) {
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

  public TTIriRefExtended getScheme() {
    return scheme;
  }

  @JsonSetter
  public SetMember setScheme(TTIriRefExtended scheme) {
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

  public TTIriRefExtended getDirectParent() {
    return directParent;
  }

  @JsonSetter
  public void setDirectParent(TTIriRefExtended directParent) {
    this.directParent = directParent;
  }
}
