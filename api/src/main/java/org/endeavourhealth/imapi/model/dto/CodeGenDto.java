package org.endeavourhealth.imapi.model.dto;

public class CodeGenDto {
  private String name;
  private String extension;
  private String collectionWrapper;
  private String datatypeMap;
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

  public String getDatatypeMap() {
    return datatypeMap;
  }

  public CodeGenDto setDatatypeMap(String datatypeMap) {
    this.datatypeMap = datatypeMap;
    return this;
  }

  public String getTemplate() {
    return template;
  }

  public CodeGenDto setTemplate(String template) {
    this.template = template;
    return this;
  }

}
