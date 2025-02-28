package org.endeavourhealth.imapi.model.imq;

import java.util.function.Consumer;

public class Path extends Element{
  private Node node;
  private boolean inverse;
  public boolean isInverse() {
    return inverse;
  }

  public Node getNode() {
    return node;
  }

  public Path setNode(Node node) {
    this.node = node;
    return this;
  }

  public Path node(Consumer<Node> builder){
    this.node = new Node();
    builder.accept(this.node);
    return this;
  }

  public Path setInverse(boolean inverse) {
    this.inverse= inverse;
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
