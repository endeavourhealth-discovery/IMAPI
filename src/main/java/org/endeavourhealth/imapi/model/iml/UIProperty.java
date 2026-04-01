package org.endeavourhealth.imapi.model.iml;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class UIProperty {
  private String iri;
  private String name;
  private String propertyType;
  private String valueType;
  private String valueTypeName;
  private int maxCount;
  private int number;
  private String intervalUnitIri;
  private List<TTIriRef> intervalUnitOptions;
  private String unitIri;
  private List<TTIriRef> unitOptions;
  private String operatorIri;
  private List<String> operatorOptions;
  private List<TTIriRef> qualifierOptions;
  private Integer setMemberCount;

  public UIProperty() {
  }

  public String getIri() {
    return iri;
  }

  public UIProperty setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public UIProperty setName(String name) {
    this.name = name;
    return this;
  }

  public String getPropertyType() {
    return propertyType;
  }

  public UIProperty setPropertyType(String propertyType) {
    this.propertyType = propertyType;
    return this;
  }

  public String getValueType() {
    return valueType;
  }

  public UIProperty setValueType(String valueType) {
    this.valueType = valueType;
    return this;
  }

  public int getMaxCount() {
    return maxCount;
  }

  public UIProperty setMaxCount(int maxCount) {
    this.maxCount = maxCount;
    return this;
  }

  public int getNumber() {
    return number;
  }

  public UIProperty setNumber(int number) {
    this.number = number;
    return this;
  }

  public String getIntervalUnitIri() {
    return intervalUnitIri;
  }

  public UIProperty setIntervalUnitIri(String intervalUnitIri) {
    this.intervalUnitIri = intervalUnitIri;
    return this;
  }

  public List<TTIriRef> getIntervalUnitOptions() {
    return intervalUnitOptions;
  }

  public UIProperty setIntervalUnitOptions(List<TTIriRef> intervalUnitOptions) {
    this.intervalUnitOptions = intervalUnitOptions;
    return this;
  }

  public String getUnitIri() {
    return unitIri;
  }

  public UIProperty setUnitIri(String unitIri) {
    this.unitIri = unitIri;
    return this;
  }

  public List<TTIriRef> getUnitOptions() {
    return unitOptions;
  }

  public UIProperty setUnitOptions(List<TTIriRef> unitOptions) {
    this.unitOptions = unitOptions;
    return this;
  }

  public String getOperatorIri() {
    return operatorIri;
  }

  public UIProperty setOperatorIri(String operatorIri) {
    this.operatorIri = operatorIri;
    return this;
  }

  public List<String> getOperatorOptions() {
    return operatorOptions;
  }

  public UIProperty setOperatorOptions(List<String> operatorOptions) {
    this.operatorOptions = operatorOptions;
    return this;
  }

  public List<TTIriRef> getQualifierOptions() {
    return qualifierOptions;
  }

  public UIProperty setQualifierOptions(List<TTIriRef> qualifierOptions) {
    this.qualifierOptions = qualifierOptions;
    return this;
  }

  public Integer getSetMemberCount() {
    return setMemberCount;
  }

  public UIProperty setSetMemberCount(Integer setMemberCount) {
    this.setMemberCount = setMemberCount;
    return this;
  }

  public String getValueTypeName() {
    return this.valueTypeName;
  }

  public UIProperty setValueTypeName(String valueTypeName) {
    this.valueTypeName = valueTypeName;
    return this;
  }

  public UIProperty addQualifierOption(String iri, String name) {
    if (this.qualifierOptions == null) {
      this.qualifierOptions = new ArrayList<>();
    }
    this.qualifierOptions.add(new TTIriRef(iri, name));
    return this;
  }
}