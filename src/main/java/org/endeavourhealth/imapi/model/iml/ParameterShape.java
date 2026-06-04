package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParameterShape {
  private String label;
  private TTIriRefExtended type;
  private List<TTIriRefExtended> parameterSubType;

  public List<TTIriRefExtended> getParameterSubType() {
    return parameterSubType;
  }

  public ParameterShape setParameterSubType(List<TTIriRefExtended> parameterSubType) {
    this.parameterSubType = parameterSubType;
    return this;
  }
  public ParameterShape addParameterSubType (TTIriRefExtended parameterSubType){
      if (this.parameterSubType == null) {
        this.parameterSubType = new ArrayList<>();
      }
      this.parameterSubType.add(parameterSubType);
      return this;
    }
  public ParameterShape parameterSubType (Consumer<TTIriRefExtended> builder) {
      TTIriRefExtended parameterSubType = new TTIriRefExtended();
      addParameterSubType(parameterSubType);
      builder.accept(parameterSubType);
      return this;
    }


  public TTIriRefExtended getType() {
    return type;
  }

  public ParameterShape setType(TTIriRefExtended type) {
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
