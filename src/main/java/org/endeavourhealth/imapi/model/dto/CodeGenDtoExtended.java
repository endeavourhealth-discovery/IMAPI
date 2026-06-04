package org.endeavourhealth.imapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.interfacemanager.model.CodeGenDto;

import java.util.Map;

public class CodeGenDtoExtended extends CodeGenDto {

  public CodeGenDtoExtended() {
  }

  public CodeGenDtoExtended name(String name) {
    this.setName(name);
    return this;
  }

  public CodeGenDtoExtended extension(String extension) {
    this.setExtension(extension);
    return this;
  }

  public CodeGenDtoExtended collectionWrapper(String collectionWrapper) {
    this.setCollectionWrapper(collectionWrapper);
    return this;
  }

  public CodeGenDtoExtended datatypeMap(Map<String, String> datatypeMap) {
    this.setDatatypeMap(datatypeMap);
    return this;
  }

  @JsonIgnore
  public String getDataType(String datatype) {
    if (this.getDatatypeMap() == null)
      return null;

    return this.getDatatypeMap().get(datatype);
  }

  public CodeGenDtoExtended template(String template) {
    this.setTemplate(template);
    return this;
  }

  public CodeGenDtoExtended complexTypes(Boolean complexTypes) {
    this.setComplexTypes(complexTypes);
    return this;
  }

  @JsonIgnore
  public boolean hasCollectionWrapper() {
    return this.getCollectionWrapper() != null && !this.getCollectionWrapper().isEmpty();
  }
}
