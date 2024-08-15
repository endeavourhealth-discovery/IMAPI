package org.endeavourhealth.imapi.logic.codegen;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataModel {

  private String iri;
  private String name;
  private String comment;

  private List<DataModelProperty> properties = new ArrayList<>();

  public String getIri() {
    return iri;
  }

  public DataModel setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public DataModel setName(String name) {
    this.name = name;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public DataModel setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public List<DataModelProperty> getProperties() {
    return properties;
  }

  public DataModel setProperties(List<DataModelProperty> properties) {
    this.properties = properties;
    return this;
  }

  public Set<String> getPropertyNames() {
    Set<String> propertyNames = new HashSet<>();
    for (DataModelProperty p : properties)
      propertyNames.add(p.getName());
    return propertyNames;
  }

  public DataModel addProperty(DataModelProperty property) {
    if (!this.getPropertyNames().contains(property.getName()))
      this.properties.add(property);
    return this;
  }
}
