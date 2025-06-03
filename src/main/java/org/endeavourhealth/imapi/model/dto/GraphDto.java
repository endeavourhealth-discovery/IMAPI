package org.endeavourhealth.imapi.model.dto;

import java.util.ArrayList;
import java.util.List;

public class GraphDto {

  public enum GraphType {
    NONE,
    WRAPPER,
    PROPERTIES,
    SUBTYPE,
    ISA
  }

  String key;
  GraphType type;
  String name;
  String iri;
  String propertyType;
  String valueTypeIri;
  String valueTypeName;
  String inheritedFromIri;
  String inheritedFromName;
  String min;
  String max;
  List<GraphDto> children;
  List<GraphDto> leafNodes;

  public GraphDto() {
    this.children = new ArrayList<>();
    this.leafNodes = new ArrayList<>();
  }

  public GraphDto(String iri, String name, String valueTypeIri, String valueTypeName, String inheritedFromIri,
                  String inheritedFromName) {
    this.name = name;
    this.iri = iri;
    this.valueTypeIri = valueTypeIri;
    this.valueTypeName = valueTypeName;
    this.inheritedFromIri = inheritedFromIri;
    this.inheritedFromName = inheritedFromName;
  }

  public GraphDto(String iri, String name, String valueTypeIri, String valueTypeName) {
    this.name = name;
    this.iri = iri;
    this.valueTypeIri = valueTypeIri;
    this.valueTypeName = valueTypeName;
  }

  public List<GraphDto> getLeafNodes() {
    return leafNodes;
  }

  public void setLeafNodes(List<GraphDto> leafNodes) {
    this.leafNodes = leafNodes;
  }

  public GraphType getType() {
    return type;
  }

  public GraphDto setType(GraphType type) {
    this.type = type;
    return this;
  }

  public String getKey() {
    return key;
  }

  public GraphDto setKey(String key) {
    this.key = key;
    return this;
  }

  public String getMin() {
    return min;
  }

  public GraphDto setMin(String min) {
    this.min = min;
    return this;
  }

  public String getMax() {
    return min;
  }

  public GraphDto setMax(String max) {
    this.max = max;
    return this;
  }

  public String getValueTypeIri() {
    return valueTypeIri;
  }

  public GraphDto setValueTypeIri(String valueTypeIri) {
    this.valueTypeIri = valueTypeIri;
    return this;
  }

  public String getValueTypeName() {
    return valueTypeName;
  }

  public GraphDto setValueTypeName(String valueTypeName) {
    this.valueTypeName = valueTypeName;
    return this;
  }

  public String getName() {
    return name;
  }

  public GraphDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public GraphDto setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getPropertyType() {
    return propertyType;
  }

  public GraphDto setPropertyType(String propertyType) {
    this.propertyType = propertyType;
    return this;
  }

  public String getInheritedFromIri() {
    return inheritedFromIri;
  }

  public GraphDto setInheritedFromIri(String inheritedFromIri) {
    this.inheritedFromIri = inheritedFromIri;
    return this;
  }

  public String getInheritedFromName() {
    return inheritedFromName;
  }

  public GraphDto setInheritedFromName(String inheritedFromName) {
    this.inheritedFromName = inheritedFromName;
    return this;
  }

  public List<GraphDto> getChildren() {
    return children;
  }

  public GraphDto setChildren(List<GraphDto> children) {
    this.children = children;
    return this;
  }
}
