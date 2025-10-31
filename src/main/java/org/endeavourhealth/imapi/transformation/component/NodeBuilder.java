package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Prefix;
import org.endeavourhealth.imapi.model.imq.Property;

/**
 * Fluent builder for constructing IMQ Node objects.
 * Node objects represent entities or concepts in the data model.
 */
public class NodeBuilder {

  private final Node node;

  /**
   * Creates a new NodeBuilder.
   */
  public NodeBuilder() {
    this.node = new Node();
  }

  /**
   * Creates a NodeBuilder from an existing Node.
   *
   * @param node The node to build upon
   */
  public NodeBuilder(Node node) {
    this.node = node;
  }

  /**
   * Sets the IRI (Internationalized Resource Identifier) for this node.
   */
  public NodeBuilder withIri(String iri) {
    node.setIri(iri);
    return this;
  }

  /**
   * Sets the name for this node.
   */
  public NodeBuilder withName(String name) {
    node.setName(name);
    return this;
  }

  /**
   * Sets the description for this node.
   */
  public NodeBuilder withDescription(String description) {
    node.setDescription(description);
    return this;
  }

  /**
   * Sets the type of this node.
   */
  public NodeBuilder withType(String type) {
    node.setType(type);
    return this;
  }

  /**
   * Adds a property to this node.
   */
  public NodeBuilder addProperty(Property property) {
    node.addProperty(property);
    return this;
  }

  /**
   * Adds a property with IRI to this node.
   */
  public NodeBuilder addProperty(String propertyIri) {
    Property property = new Property();
    property.setIri(propertyIri);
    node.addProperty(property);
    return this;
  }

  /**
   * Builds and returns the Node object.
   */
  public Node build() {
    return node;
  }

  /**
   * Gets the underlying Node object without building.
   */
  public Node get() {
    return node;
  }
}