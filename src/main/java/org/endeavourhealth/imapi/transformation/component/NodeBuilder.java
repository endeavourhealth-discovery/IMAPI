package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Node;

/**
 * Fluent builder for constructing IMQuery Node objects.
 * Represents entities and references in IMQuery.
 */
public class NodeBuilder {
  private final Node node;

  public NodeBuilder() {
    this.node = new Node();
  }

  public NodeBuilder(String iri) {
    this.node = new Node();
    this.node.setIri(iri);
  }

  public NodeBuilder iri(String iri) {
    node.setIri(iri);
    return this;
  }

  public NodeBuilder name(String name) {
    node.setName(name);
    return this;
  }

  public NodeBuilder description(String description) {
    node.setDescription(description);
    return this;
  }

  public Node build() {
    return node;
  }
}