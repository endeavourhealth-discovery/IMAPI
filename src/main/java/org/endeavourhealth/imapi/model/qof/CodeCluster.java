package org.endeavourhealth.imapi.model.qof;

public class CodeCluster {
  private String code;
  private String description;
  private String SNOMEDCT;

  public String getCode() {
    return code;
  }

  public CodeCluster setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public CodeCluster setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getSNOMEDCT() {
    return SNOMEDCT;
  }

  public CodeCluster setSNOMEDCT(String SNOMEDCT) {
    this.SNOMEDCT = SNOMEDCT;
    return this;
  }
}
