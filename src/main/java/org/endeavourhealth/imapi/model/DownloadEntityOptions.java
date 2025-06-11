package org.endeavourhealth.imapi.model;

public class DownloadEntityOptions {
  private String entityIri;
  private String format;
  private boolean includeHasSubtypes;
  private boolean includeInferred;
  private boolean includeProperties;
  private boolean includeMembers;
  private boolean expandMembers;
  private boolean expandSubsets;
  private boolean includeTerms;
  private boolean includeIsChildOf;
  private boolean includeHasChildren;
  private boolean includeInactive;

  public DownloadEntityOptions() {
    this.includeHasChildren = false;
    this.includeInactive = false;
    this.includeInferred = false;
    this.includeMembers = false;
    this.includeIsChildOf = false;
    this.includeProperties = false;
    this.includeHasSubtypes = false;
    this.expandMembers = false;
    this.expandSubsets = false;
    this.includeTerms = false;
  }

  public String getEntityIri() {
    return entityIri;
  }

  public DownloadEntityOptions setEntityIri(String entityIri) {
    this.entityIri = entityIri;
    return this;
  }

  public String getFormat() {
    return format;
  }

  public DownloadEntityOptions setFormat(String format) {
    this.format = format;
    return this;
  }

  public boolean includeHasSubtypes() {
    return includeHasSubtypes;
  }

  public DownloadEntityOptions setIncludeHasSubtypes(boolean includeSubtypes) {
    this.includeHasSubtypes = includeSubtypes;
    return this;
  }

  public boolean includeInferred() {
    return includeInferred;
  }

  public DownloadEntityOptions setIncludeInferred(boolean includeInferred) {
    this.includeInferred = includeInferred;
    return this;
  }

  public boolean includeProperties() {
    return includeProperties;
  }

  public DownloadEntityOptions setIncludeProperties(boolean includeProperties) {
    this.includeProperties = includeProperties;
    return this;
  }

  public boolean includeMembers() {
    return includeMembers;
  }

  public DownloadEntityOptions setIncludeMembers(boolean includeMembers) {
    this.includeMembers = includeMembers;
    return this;
  }

  public boolean expandMembers() {
    return expandMembers;
  }

  public DownloadEntityOptions setExpandMembers(boolean expandMembers) {
    this.expandMembers = expandMembers;
    return this;
  }

  public boolean expandSubsets() {
    return expandSubsets;
  }

  public DownloadEntityOptions setExpandSubsets(boolean expandSubsets) {
    this.expandSubsets = expandSubsets;
    return this;
  }

  public boolean includeTerms() {
    return includeTerms;
  }

  public DownloadEntityOptions setIncludeTerms(boolean includeTerms) {
    this.includeTerms = includeTerms;
    return this;
  }

  public boolean includeIsChildOf() {
    return includeIsChildOf;
  }

  public DownloadEntityOptions setIncludeIsChildOf(boolean includeIsChildOf) {
    this.includeIsChildOf = includeIsChildOf;
    return this;
  }

  public boolean includeHasChildren() {
    return includeHasChildren;
  }

  public DownloadEntityOptions setIncludeHasChildren(boolean includeHasChildren) {
    this.includeHasChildren = includeHasChildren;
    return this;
  }

  public boolean includeInactive() {
    return includeInactive;
  }

  public DownloadEntityOptions setIncludeInactive(boolean includeInactive) {
    this.includeInactive = includeInactive;
    return this;
  }
}
