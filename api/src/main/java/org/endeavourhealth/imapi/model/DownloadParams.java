package org.endeavourhealth.imapi.model;

public class DownloadParams {
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

  public DownloadParams() {
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

  public boolean includeHasSubtypes() {
    return includeHasSubtypes;
  }

  public DownloadParams setIncludeHasSubtypes(boolean includeSubtypes) {
    this.includeHasSubtypes = includeSubtypes;
    return this;
  }

  public boolean includeInferred() {
    return includeInferred;
  }

  public DownloadParams setIncludeInferred(boolean includeInferred) {
    this.includeInferred = includeInferred;
    return this;
  }

  public boolean includeProperties() {
    return includeProperties;
  }

  public DownloadParams setIncludeProperties(boolean includeProperties) {
    this.includeProperties = includeProperties;
    return this;
  }

  public boolean includeMembers() {
    return includeMembers;
  }

  public DownloadParams setIncludeMembers(boolean includeMembers) {
    this.includeMembers = includeMembers;
    return this;
  }

  public boolean expandMembers() {
    return expandMembers;
  }

  public DownloadParams setExpandMembers(boolean expandMembers) {
    this.expandMembers = expandMembers;
    return this;
  }

  public boolean expandSubsets() {
    return expandSubsets;
  }

  public DownloadParams setExpandSubsets(boolean expandSubsets) {
    this.expandSubsets = expandSubsets;
    return this;
  }

  public boolean includeTerms() {
    return includeTerms;
  }

  public DownloadParams setIncludeTerms(boolean includeTerms) {
    this.includeTerms = includeTerms;
    return this;
  }

  public boolean includeIsChildOf() {
    return includeIsChildOf;
  }

  public DownloadParams setIncludeIsChildOf(boolean includeIsChildOf) {
    this.includeIsChildOf = includeIsChildOf;
    return this;
  }

  public boolean includeHasChildren() {
    return includeHasChildren;
  }

  public DownloadParams setIncludeHasChildren(boolean includeHasChildren) {
    this.includeHasChildren = includeHasChildren;
    return this;
  }

  public boolean includeInactive() {
    return includeInactive;
  }

  public DownloadParams setIncludeInactive(boolean includeInactive) {
    this.includeInactive = includeInactive;
    return this;
  }
}
