package org.endeavourhealth.imapi.filer;

public class TTImportConfig {
  private String folder = ".";
  private String importType = null;
  private boolean secure = false;
  private boolean skiptct = false;
  private boolean skipsearch = false;
  private boolean skipdelete = false;
  private boolean skiplucene = false;
  private boolean skipBulk = false;
  private String resourceFolder;

  public String getResourceFolder() {
    return resourceFolder;
  }

  public TTImportConfig setResourceFolder(String resourceFolder) {
    this.resourceFolder = resourceFolder;
    return this;
  }

  public boolean isSkipBulk() {
    return skipBulk;
  }

  public TTImportConfig setSkipBulk(boolean skipBulk) {
    this.skipBulk = skipBulk;
    return this;
  }

  public boolean isSkiplucene() {
    return skiplucene;
  }

  public TTImportConfig setSkiplucene(boolean skiplucene) {
    this.skiplucene = skiplucene;
    return this;
  }

  public String getFolder() {
    return folder;
  }

  public TTImportConfig setFolder(String folder) {
    this.folder = folder;
    return this;
  }

  public String getImportType() {
    return importType;
  }

  public TTImportConfig setImportType(String importType) {
    this.importType = importType;
    return this;
  }

  public boolean isSecure() {
    return secure;
  }

  public TTImportConfig setSecure(boolean secure) {
    this.secure = secure;
    return this;
  }

  public boolean isSkiptct() {
    return skiptct;
  }

  public TTImportConfig setSkiptct(boolean skiptct) {
    this.skiptct = skiptct;
    return this;
  }

  public boolean isSkipsearch() {
    return skipsearch;
  }

  public TTImportConfig setSkipsearch(boolean skipsearch) {
    this.skipsearch = skipsearch;
    return this;
  }

  public boolean isSkipdelete() {
    return skipdelete;
  }

  public TTImportConfig setSkipdelete(boolean skipdelete) {
    this.skipdelete = skipdelete;
    return this;
  }
}
