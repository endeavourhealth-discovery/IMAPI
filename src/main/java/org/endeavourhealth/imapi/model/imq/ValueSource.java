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
  private String keepRef;

  public String getKeepRef() {
    return keepRef;
  }

  public ValueSource setKeepRef(String keepRef) {
    this.keepRef = keepRef;
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
