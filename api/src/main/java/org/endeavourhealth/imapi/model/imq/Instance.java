package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Instance {
  private TTIriRef include;
  private TTIriRef exclude;
  private boolean ancestorsOf;
  private boolean ancestorsOrSelfOf;
  private boolean descendantsOrSelfOf;
  private boolean descendantsOf;

  public TTIriRef getInclude() {
    return include;
  }

  public Instance setInclude(TTIriRef include) {
    this.include = include;
    return this;
  }

  public TTIriRef getExclude() {
    return exclude;
  }

  public Instance setExclude(TTIriRef exclude) {
    this.exclude = exclude;
    return this;
  }

  public boolean isAncestorsOf() {
    return ancestorsOf;
  }

  public Instance setAncestorsOf(boolean ancestorsOf) {
    this.ancestorsOf = ancestorsOf;
    return this;
  }

  public boolean isAncestorsOrSelfOf() {
    return ancestorsOrSelfOf;
  }

  public Instance setAncestorsOrSelfOf(boolean ancestorsOrSelfOf) {
    this.ancestorsOrSelfOf = ancestorsOrSelfOf;
    return this;
  }

  public boolean isDescendantsOrSelfOf() {
    return descendantsOrSelfOf;
  }

  public Instance setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
    this.descendantsOrSelfOf = descendantsOrSelfOf;
    return this;
  }

  public boolean isDescendantsOf() {
    return descendantsOf;
  }

  public Instance setDescendantsOf(boolean descendantsOf) {
    this.descendantsOf = descendantsOf;
    return this;
  }
}
