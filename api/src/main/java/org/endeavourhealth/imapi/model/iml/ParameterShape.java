package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParameterShape {
  private String label;
  private TTIriRef type;
  private List<TTIriRef> parameterSubType;

  public List<TTIriRef> getParameterSubType() {
    return parameterSubType;
  }

  public ParameterShape setParameterSubType(List<TTIriRef> parameterSubType) {
    this.parameterSubType = parameterSubType;
    return this;
  }
  public ParameterShape addParameterSubType (TTIriRef parameterSubType){
      if (this.parameterSubType == null) {
        this.parameterSubType = new ArrayList<>();
      }
      this.parameterSubType.add(parameterSubType);
      return this;
    }
  public ParameterShape parameterSubType (Consumer< TTIriRef > builder) {
      TTIriRef parameterSubType = new TTIriRef();
      addParameterSubType(parameterSubType);
      builder.accept(parameterSubType);
      return this;
    }


  public TTIriRef getType() {
    return type;
  }

  public ParameterShape setType(TTIriRef type) {
    this.type = type;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public ParameterShape setLabel(String label) {
    this.label = label;
    return this;
  }
}
