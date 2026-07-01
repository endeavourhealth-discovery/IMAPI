package org.endeavourhealth.imapi.model.imq;

public class Expression {

  private String value;
  private String nodeRef;
  private String iri;
  private String propertyRef;

  public String getValue() {
    return value;
  }

  public Expression setValue(String value) {
    this.value = value;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public Expression setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public Expression setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public Expression setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }
}
