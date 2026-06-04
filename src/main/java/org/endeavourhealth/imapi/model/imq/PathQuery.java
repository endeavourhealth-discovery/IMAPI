package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

@Getter
public class PathQuery extends TTIriRefExtended {
  private TTIriRefExtended source;
  private TTIriRefExtended target;
  private Integer depth;

  @JsonSetter
  public PathQuery setSource(TTIriRefExtended source) {
    this.source = source;
    return this;
  }

  public PathQuery setSource(String source) {
    this.source = new TTIriRefExtended().iri(source);
    return this;
  }

  @JsonSetter
  public PathQuery setTarget(TTIriRefExtended target) {
    this.target = target;
    return this;
  }

  public PathQuery setTarget(String target) {
    this.target = new TTIriRefExtended().iri(target);
    return this;
  }

  public PathQuery setDepth(Integer depth) {
    this.depth = depth;
    return this;
  }

  @Override
  public PathQuery iri(String iri) {
    super.setIri(iri);
    return this;
  }

  @Override
  public PathQuery name(String name) {
    super.setName(name);
    return this;
  }
}
