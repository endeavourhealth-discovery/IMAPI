package org.endeavourhealth.imapi.model.codegen;

public class CodeGenTemplate {
  private String header;
  private String footer;
  private String property;
  private String collectionProperty;

  public String getHeader() {
    return header;
  }

  public CodeGenTemplate setHeader(String header) {
    this.header = header;
    return this;
  }

  public String getFooter() {
    return footer;
  }

  public CodeGenTemplate setFooter(String footer) {
    this.footer = footer;
    return this;
  }

  public String getProperty() {
    return property;
  }

  public CodeGenTemplate setProperty(String property) {
    this.property = property;
    return this;
  }

  public String getCollectionProperty() {
    return collectionProperty;
  }

  public CodeGenTemplate setCollectionProperty(String collectionProperty) {
    this.collectionProperty = collectionProperty;
    return this;
  }
}
