package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;


public class ValueSource {
  private String parameter;
  private String iri;
  private String name;
  private String nodeRef;
  private ValuePath path;
  private String propertyRef;

  public String getPropertyRef() {
    return propertyRef;
  }

  public ValueSource setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  public ValuePath getPath() {
    return path;
  }

  public ValueSource setPath(ValuePath path) {
    this.path = path;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public ValueSource setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public ValueSource setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public String getName() {
    return name;
  }

  public ValueSource setName(String name) {
    this.name = name;
    return this;
  }



  public String getParameter() {
    return parameter;
  }

  public ValueSource setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }



}
