package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Argument {

  private String parameter;
  private String valueData;
  private String valueVariable;
  private TTIriRef valueIri;
  @Getter
  private Set<TTIriRef> valueIriList;
  private Set<String> valueDataList;
  private Path valuePath;
  private String valueNodeRef;

  public String getValueNodeRef() {
    return valueNodeRef;
  }

  public Argument setValueNodeRef(String valueNodeRef) {
    this.valueNodeRef = valueNodeRef;
    return this;
  }


  public Argument setValueIriList(Set<TTIriRef> valueIriList) {
    this.valueIriList = valueIriList;
    return this;
  }


  public Path getValuePath() {
    return valuePath;
  }

  public Argument setValuePath(Path valuePath) {
    this.valuePath = valuePath;
    return this;
  }

  public Set<String> getValueDataList() {
    return valueDataList;
  }

  public Argument setValueDataList(Set<String> valueDataList) {
    this.valueDataList = valueDataList;
    return this;
  }

  public Argument addToValueDataList(String value) {
    if (this.valueDataList == null)
      this.valueDataList = new HashSet<>();
    this.valueDataList.add(value);
    return this;
  }

  public Argument addToValueIriList(TTIriRef value) {
    if (this.valueIriList == null)
      this.valueIriList = new HashSet<>();
    this.valueIriList.add(value);
    return this;
  }

  public TTIriRef getValueIri() {
    return valueIri;
  }

  @JsonSetter
  public Argument setValueIri(TTIriRef valueIri) {
    this.valueIri = valueIri;
    return this;
  }

  public String getValueVariable() {
    return valueVariable;
  }

  public Argument setValueVariable(String valueVariable) {
    this.valueVariable = valueVariable;
    return this;
  }

  public String getParameter() {
    return parameter;
  }

  public Argument setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }

  public String getValueData() {
    return valueData;
  }

  public Argument setValueData(String valueData) {
    this.valueData = valueData;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameter, valueData, valueVariable, valueIri != null ? valueIri.getIri() : null, valueIriList, valueDataList, valuePath);
  }

}