package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ValuePath  extends IriLD{
  String iri;
  String name;
  String nodeRef;
  Node typeOf;
  ValuePath path;

  @Override
  public String getIri() {
    return iri;
  }

  @Override
  public ValuePath setIri(String iri) {
    this.iri = iri;
    return this;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ValuePath setName(String name) {
    this.name = name;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public ValuePath setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public Node getTypeOf() {
    return typeOf;
  }

  public ValuePath setTypeOf(Node typeOf) {
    this.typeOf = typeOf;
    return this;
  }

  public ValuePath getPath() {
    return path;
  }

  public ValuePath setPath(ValuePath path) {
    this.path = path;
    return this;
  }
}
