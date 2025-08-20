package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;

public class RelativeTo extends IriLD {
  private String valueVariable;
  private String propertyRef;
  private String targetLabel;
  @Getter
  private String nodeRef;
  @Getter
  private String parameter;



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
