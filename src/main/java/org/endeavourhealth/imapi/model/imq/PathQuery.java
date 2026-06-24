package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

@Getter
public class PathQuery extends TTIriRef {
  private TTIriRef source;
  private TTIriRef target;
  private Integer depth;

  @JsonSetter
  public PathQuery setSource(TTIriRef source) {
    this.source = source;
    return this;
  }

  public PathQuery setSource(String source) {
    this.source = TTIriRefExtensionsKt.iri(new TTIriRef(), ).iri(source);
    return this;
  }

  @JsonSetter
  public PathQuery setTarget(TTIriRef target) {
    this.target = target;
    return this;
  }

  public PathQuery setTarget(String target) {
    this.target = TTIriRefExtensionsKt.iri(new TTIriRef(), ).iri(target);
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
