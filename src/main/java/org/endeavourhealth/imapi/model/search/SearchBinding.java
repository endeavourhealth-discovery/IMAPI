package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

public class SearchBinding {
  private TTIriRefExtended path;
  private TTIriRefExtended node;

  public TTIriRefExtended getPath() {
    return path;
  }

  public SearchBinding setPath(TTIriRefExtended path) {
    this.path = path;
    return this;
  }

  public TTIriRefExtended getNode() {
    return node;
  }

  public SearchBinding setNode(TTIriRefExtended node) {
    this.node = node;
    return this;
  }
}
