package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class SearchBinding {
  private TTIriRef path;
  private TTIriRef node;

  public TTIriRef getPath() {
    return path;
  }

  public SearchBinding setPath(TTIriRef path) {
    this.path = path;
    return this;
  }

  public TTIriRef getNode() {
    return node;
  }

  public SearchBinding setNode(TTIriRef node) {
    this.node = node;
    return this;
  }
}
