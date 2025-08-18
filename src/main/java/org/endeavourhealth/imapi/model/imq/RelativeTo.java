package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonFilter;

public class RelativeTo extends Node {
  private String valueVariable;
  private String propertyRef;
  private String targetLabel;

  public String getTargetLabel() {
    return targetLabel;
  }
  public RelativeTo setTargetLabel(String targetLabel) {
    this.targetLabel = targetLabel;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public RelativeTo setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  public RelativeTo setIri(String iri){
    super.setIri(iri);
    return this;
  }

  public RelativeTo setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }

  public String getValueVariable() {
    return valueVariable;
  }

  public RelativeTo setValueVariable(String valueVariable) {
    this.valueVariable = valueVariable;
    return this;
  }


  public RelativeTo setInverse(boolean inverse) {
    super.setInverse(inverse);
    return this;
  }

  public RelativeTo setParameter(String parameter) {
    super.setParameter(parameter);
    return this;
  }


  public RelativeTo setVariable(String variable) {
    super.setVariable(variable);
    return this;
  }

}
