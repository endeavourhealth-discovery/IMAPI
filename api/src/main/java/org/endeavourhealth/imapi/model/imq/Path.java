package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.util.function.Consumer;

public class Path extends Element {
  @Getter
  private boolean inverse;
  @Getter
  private Where where;

  @JsonSetter
  public Path setWhere(Where where) {
    this.where = where;
    return this;
  }

  public Path where(Consumer<Where> where) {
    this.where = new Where();
    where.accept(this.where);
    return this;
  }


  public Path setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }

  @Override
  public Path setDescription(String description) {
    super.setDescription(description);
    return this;
  }

  @Override
  public Path setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  @Override
  public Path setName(String name) {
    super.setName(name);
    return this;
  }
}
