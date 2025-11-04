package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Path;
import org.endeavourhealth.imapi.model.imq.Node;

/**
 * Fluent builder for constructing IMQuery Path objects.
 * Manages navigation through entity relationships.
 */
public class PathBuilder {
  private final Path path;

  public PathBuilder() {
    this.path = new Path();
  }

  public PathBuilder(String iri) {
    this.path = new Path();
    Node node = new Node();
    node.setIri(iri);
    path.setTypeOf(node);
  }

  public PathBuilder typeOf(String iri) {
    Node node = new Node();
    node.setIri(iri);
    path.setTypeOf(node);
    return this;
  }

  public PathBuilder typeOf(Node node) {
    path.setTypeOf(node);
    return this;
  }

  public PathBuilder iri(String iri) {
    path.setIri(iri);
    return this;
  }

  public PathBuilder target(String targetIri) {
    // Set target node reference
    return this;
  }

  public Path build() {
    return path;
  }
}