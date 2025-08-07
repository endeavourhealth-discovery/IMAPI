package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Path extends Element implements HasPaths{
  @Getter
  private boolean inverse;
  @Getter
  private boolean optional;
  private List<Path> path;
  private Node typeOf;

  public Path setVariable(String variable) {
    super.setVariable(variable);
    return this;
  }

  public Node getTypeOf() {
    return typeOf;
  }

  @JsonSetter
  public Path setTypeOf(Node typeOf) {
    this.typeOf = typeOf;
    return this;
  }

  public Path setTypeOf(String iri) {
    setTypeOf(new Node().setIri(iri));
    return this;
  }

  public List<Path> getPath() {
    return path;
  }


  @JsonSetter("path")
  public Path setPath(List<Path> path) {
    this.path = path;
    return this;
  }

  public Path addPath(Path path) {
    if (this.path == null)
      this.path = new ArrayList<>();
    this.path.add(path);
    return this;
  }

  public Path path(Consumer<Path> path) {
    Path p = new Path();
    path.accept(p);
    this.path.add(p);
    return this;
  }

  public Path setOptional(boolean optional) {
    this.optional = optional;
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
