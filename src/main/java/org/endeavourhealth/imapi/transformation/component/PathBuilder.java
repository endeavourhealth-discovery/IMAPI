package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Path;
import org.endeavourhealth.imapi.model.imq.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for constructing IMQ Path objects.
 * Path objects represent navigation paths through the data model.
 */
public class PathBuilder {

  private final Path path;

  /**
   * Creates a new PathBuilder.
   */
  public PathBuilder() {
    this.path = new Path();
  }

  /**
   * Creates a PathBuilder from an existing Path.
   *
   * @param path The path to build upon
   */
  public PathBuilder(Path path) {
    this.path = path;
  }

  /**
   * Sets the starting node.
   */
  public PathBuilder withStartNode(Node node) {
    path.setStartNode(node);
    return this;
  }

  /**
   * Adds a property to the path.
   */
  public PathBuilder addProperty(Property property) {
    path.addNode(property);
    return this;
  }

  /**
   * Adds a property with IRI to the path.
   */
  public PathBuilder addProperty(String propertyIri) {
    Property property = new Property();
    property.setIri(propertyIri);
    path.addNode(property);
    return this;
  }

  /**
   * Sets the variable binding for this path.
   */
  public PathBuilder withVariable(String variable) {
    path.setVariable(variable);
    return this;
  }

  /**
   * Builds and returns the Path object.
   */
  public Path build() {
    return path;
  }

  /**
   * Gets the underlying Path object without building.
   */
  public Path get() {
    return path;
  }
}