package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Path;

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
   * Sets the IRI for this path.
   */
  public PathBuilder withIri(String iri) {
    path.setIri(iri);
    return this;
  }

  /**
   * Sets the name for this path.
   */
  public PathBuilder withName(String name) {
    path.setName(name);
    return this;
  }

  /**
   * Sets the description for this path.
   */
  public PathBuilder withDescription(String description) {
    path.setDescription(description);
    return this;
  }

  /**
   * Adds a nested path to this path.
   */
  public PathBuilder addPath(Path nestedPath) {
    path.addPath(nestedPath);
    return this;
  }

  /**
   * Adds a nested path with IRI to this path.
   */
  public PathBuilder addPath(String pathIri) {
    Path nestedPath = new Path();
    nestedPath.setIri(pathIri);
    path.addPath(nestedPath);
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
   * Sets the typeOf constraint for this path.
   */
  public PathBuilder withTypeOf(String typeOfIri) {
    path.setTypeOf(typeOfIri);
    return this;
  }

  /**
   * Sets the typeOf constraint for this path.
   */
  public PathBuilder withTypeOf(Node typeOfNode) {
    path.setTypeOf(typeOfNode);
    return this;
  }

  /**
   * Marks this path as optional.
   */
  public PathBuilder optional() {
    path.setOptional(true);
    return this;
  }

  /**
   * Marks this path as inverse.
   */
  public PathBuilder inverse() {
    path.setInverse(true);
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