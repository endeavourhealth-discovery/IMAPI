package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Argument {

  private String parameter;
  private String valueData;
  private String valueParameter;
  private TTIriRef valueIri;
  @Getter
  private Set<TTIriRef> valueIriList;
  private Set<String> valueDataList;
  private Path valuePath;
  private String valueNodeRef;
  private TTIriRef dataType;
  @Getter
  private Object valueObject;
  @Getter
  private String valueVariable;
  @Getter
  private TTIriRef qualifier;


  public Argument setQualifier(TTIriRef qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  public Argument setValueVariable(String valueVariable) {
    this.valueVariable = valueVariable;
    return this;
  }

  public Argument setValueObject(Object valueObject) {
    this.valueObject = valueObject;
    return this;
  }



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

  public String getValueParameter() {
    return valueParameter;
  }

  public Argument setValueParameter(String valueParameter) {
    this.valueParameter = valueParameter;
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

  public TTIriRef getDataType() {
    return dataType;
  }

  public Argument setDataType(TTIriRef dataType) {
    this.dataType = dataType;
    return this;
  }

  @Override
  public int hashCode() {

    return Objects.hash(getHashString());
  }

  @JsonIgnore
  public String getHashString() {
    StringBuilder hs = new StringBuilder();
    if (null != parameter) hs.append(parameter);
    if (null != valueData) hs.append(valueData);
    if (null != valueParameter) hs.append(valueParameter);
    if (null != valueIri) hs.append(valueIri.getIri());
    if (null != valueDataList) {
      List<String> sorted = valueDataList.stream().sorted().toList();
      for (String s : sorted) hs.append(s);
    }
    if (null != valuePath) hs.append(valuePath.getIri());
    if (null != valueNodeRef) hs.append(valueNodeRef);
    if (null != dataType) hs.append(dataType.getIri());
    if (null != valueObject) hs.append(valueObject);
    if (null != valueVariable) hs.append(valueVariable);
    return hs.toString();
  }

}