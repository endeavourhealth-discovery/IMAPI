package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class RelativeTo extends IriLD {
  private String valueVariable;
  private String propertyRef;
  private String targetLabel;
  @Getter
  private String parameterName;
  @Getter
  private String nodeRef;
  @Getter
  private String parameter;
  private TTIriRef qualifier;

public TTIriRef getQualifier() {
  return qualifier;
}
public RelativeTo setQualifier(TTIriRef qualifier) {
  this.qualifier = qualifier;
  return this;
}

public RelativeTo setParameterName(String name) {
  this.parameterName = name;
  return this;
}

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
    this.nodeRef=nodeRef;
    return this;
  }

  public String getValueVariable() {
    return valueVariable;
  }

  public RelativeTo setValueVariable(String valueVariable) {
    this.valueVariable = valueVariable;
    return this;
  }


  public RelativeTo setParameter(String parameter) {
    this.parameter= parameter;
    return this;
  }



}
