package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Path extends Element implements HasPaths {
  private boolean inverse;
  private boolean optional;
  private List<Path> path;
  private String pathVariable;
  private Node typeOf;
  private TTIriRef qualifier;
  private String node;

  public String getNode() {
    return node;
  }

  public Path setNode(String node) {
    this.node=node;
    return this;
  }

  public Path setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }

  public Path setPathVariable(String variable) {
    this.pathVariable = variable;
    return this;
  }

  public String getPathVariable() {
    return pathVariable;
  }


  public Path setIri(VocabEnum iri){
    super.setIri(iri.toString());
    return this;
  }



  public Path setTypeOf(VocabEnum iri){
    this.setTypeOf(iri.toString());
    return this;
  }

  public TTIriRef getQualifier() {
    return qualifier;
  }

  public Path setQualifier(TTIriRef qualifier) {
    this.qualifier = qualifier;
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
    this.addPath(p);
    path.accept(p);
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
  @JsonSetter
  public Path setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  @Override
  @JsonSetter
  public Path setName(String name) {
    super.setName(name);
    return this;
  }

  public boolean isOptional() {
    return optional;
  }

  public boolean isInverse() {
    return inverse;
  }
}
