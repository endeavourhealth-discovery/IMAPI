package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Return;
import org.endeavourhealth.imapi.model.imq.ReturnProperty;

/**
 * Fluent builder for constructing IMQ Return objects.
 * Return objects specify what data should be returned from a query.
 */
public class ReturnBuilder {

  private final Return returnClause;

  /**
   * Creates a new ReturnBuilder.
   */
  public ReturnBuilder() {
    this.returnClause = new Return();
  }

  /**
   * Creates a ReturnBuilder from an existing Return.
   *
   * @param returnClause The return clause to build upon
   */
  public ReturnBuilder(Return returnClause) {
    this.returnClause = returnClause;
  }

  /**
   * Adds a property to the return clause.
   */
  public ReturnBuilder addProperty(ReturnProperty property) {
    returnClause.addProperty(property);
    return this;
  }

  /**
   * Adds a property with IRI to the return clause.
   */
  public ReturnBuilder addProperty(String iri) {
    ReturnProperty property = new ReturnProperty();
    property.setIri(iri);
    returnClause.addProperty(property);
    return this;
  }

  /**
   * Adds a property with IRI and name.
   */
  public ReturnBuilder addProperty(String iri, String name) {
    ReturnProperty property = new ReturnProperty();
    property.setIri(iri);
    property.setName(name);
    returnClause.addProperty(property);
    return this;
  }

  /**
   * Sets the as-clause for renaming the return.
   */
  public ReturnBuilder as(String asName) {
    returnClause.setAs(asName);
    return this;
  }

  /**
   * Sets the node reference.
   */
  public ReturnBuilder withNodeRef(String nodeRef) {
    returnClause.setNodeRef(nodeRef);
    return this;
  }

  /**
   * Builds and returns the Return object.
   */
  public Return build() {
    return returnClause;
  }

  /**
   * Gets the underlying Return object without building.
   */
  public Return get() {
    return returnClause;
  }
}