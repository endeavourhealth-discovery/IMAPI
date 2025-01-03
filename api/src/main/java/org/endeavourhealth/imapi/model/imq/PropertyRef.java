package org.endeavourhealth.imapi.model.imq;

public class PropertyRef extends Node {
  private String valueVariable;
  private String propertyRef;

  public String getPropertyRef() {
    return propertyRef;
  }

  public PropertyRef setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  public PropertyRef setIri(String iri){
    super.setIri(iri);
    return this;
  }

  public PropertyRef setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }

  public String getValueVariable() {
    return valueVariable;
  }

  public PropertyRef setValueVariable(String valueVariable) {
    this.valueVariable = valueVariable;
    return this;
  }


  public PropertyRef setInverse(boolean inverse) {
    super.setInverse(inverse);
    return this;
  }

  public PropertyRef setParameter(String parameter) {
    super.setParameter(parameter);
    return this;
  }


  public PropertyRef setVariable(String variable) {
    super.setVariable(variable);
    return this;
  }

}
