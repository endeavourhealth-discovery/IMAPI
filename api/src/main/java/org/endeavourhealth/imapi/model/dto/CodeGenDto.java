package org.endeavourhealth.imapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class CodeGenDto {
  private String name;
  private String extension;
  private String collectionWrapper;
  private Map<String, String> datatypeMap = new HashMap<>();
  private String template;

  public CodeGenDto() {
  }

  public String getName() {
    return name;
  }

  public CodeGenDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getExtension() {
    return extension;
  }

  public CodeGenDto setExtension(String extension) {
    this.extension = extension;
    return this;
  }

  public String getCollectionWrapper() {
    return collectionWrapper;
  }

  public CodeGenDto setCollectionWrapper(String collectionWrapper) {
    this.collectionWrapper = collectionWrapper;
    return this;
  }

  public Map<String, String> getDatatypeMap() {
    return datatypeMap;
  }

  public CodeGenDto setDatatypeMap(Map<String, String> datatypeMap) {
    this.datatypeMap = datatypeMap;
    return this;
  }

  @JsonIgnore
  public String getDataType(String datatype) {
    if (datatypeMap == null)
      return null;

    return datatypeMap.get(datatype);
  }

  public String getTemplate() {
    return template;
  }

  public CodeGenDto setTemplate(String template) {
    this.template = template;
    return this;
  }

  @JsonIgnore
  public boolean hasCollectionWrapper() {
    return collectionWrapper != null && !collectionWrapper.isEmpty();
  }
}
