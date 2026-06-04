package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.utility.ObjectToOpenApiMap;
import org.endeavourhealth.interfacemanager.model.Argument;
import org.endeavourhealth.interfacemanager.model.Path;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ArgumentExtended extends Argument {

  public ArgumentExtended qualifier(TTIriRefExtended qualifier) {
    this.setQualifier(qualifier);
    return this;
  }

  public ArgumentExtended valueVariable(String valueVariable) {
    this.setValueVariable(valueVariable);
    return this;
  }

  public ArgumentExtended valueObject(Object valueObject) {
    Map<String, Object> map = ObjectToOpenApiMap.convert(valueObject);
    this.setValueObject(map);
    return this;
  }

  public ArgumentExtended valueNodeRef(String valueNodeRef) {
    this.setValueNodeRef(valueNodeRef);
    return this;
  }

  public ArgumentExtended valueIriList(Set<TTIriRefExtended> valueIriList) {
    this.setValueIriList(valueIriList.stream().map(i -> (TTIriRef) i).toList());
    return this;
  }

  public ArgumentExtended valuePath(Path valuePath) {
    this.setValuePath(valuePath);
    return this;
  }

  public ArgumentExtended valueDataList(Set<String> valueDataList) {
    this.setValueDataList(valueDataList.stream().toList());
    return this;
  }

  public ArgumentExtended addToValueDataList(String value) {
    if (this.getValueDataList() == null)
      this.setValueDataList(new ArrayList<>() {
      });
    this.addValueDataListItem(value);
    return this;
  }

  public ArgumentExtended addToValueIriList(TTIriRefExtended value) {
    if (this.getValueIriList() == null)
      this.setValueIriList(new ArrayList<>());
    this.addValueIriListItem(value);
    return this;
  }

  @JsonSetter
  public ArgumentExtended valueIri(TTIriRefExtended valueIri) {
    this.setValueIri(valueIri);
    return this;
  }

  public ArgumentExtended valueParameter(String valueParameter) {
    this.setValueParameter(valueParameter);
    return this;
  }

  public ArgumentExtended parameter(String parameter) {
    this.setParameter(parameter);
    return this;
  }

  public ArgumentExtended valueData(String valueData) {
    this.setValueData(valueData);
    return this;
  }

  public ArgumentExtended dataType(TTIriRefExtended dataType) {
    this.setDataType(dataType);
    return this;
  }

  @Override
  public int hashCode() {

    return Objects.hash(getHashString());
  }

  @JsonIgnore
  public String getHashString() {
    StringBuilder hs = new StringBuilder();
    if (null != this.getParameter()) hs.append(this.getParameter());
    if (null != this.getValueData()) hs.append(this.getValueData());
    if (null != this.getValueParameter()) hs.append(this.getValueParameter());
    if (null != this.getValueIri()) hs.append(this.getValueIri().getIri());
    if (null != this.getValueDataList()) {
      List<String> sorted = this.getValueDataList().stream().sorted().toList();
      for (String s : sorted) hs.append(s);
    }
    if (null != this.getValuePath()) hs.append(this.getValuePath().getIri());
    if (null != this.getValueNodeRef()) hs.append(this.getValueNodeRef());
    if (null != this.getDataType()) hs.append(this.getDataType().getIri());
    if (null != this.getValueObject()) hs.append(this.getValueObject());
    if (null != this.getValueVariable()) hs.append(this.getValueVariable());
    return hs.toString();
  }

}