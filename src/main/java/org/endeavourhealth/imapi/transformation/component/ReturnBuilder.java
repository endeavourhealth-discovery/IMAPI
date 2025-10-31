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
   * Adds a property with variable to the return clause.
   */
  public ReturnBuilder addProperty(String variable) {
    ReturnProperty property = new ReturnProperty();
    property.setVariable(variable);
    returnClause.addProperty(property);
    return this;
  }

  /**
   * Adds a property with variable and IRI.
   */
  public ReturnBuilder addProperty(String variable, String iri) {
    ReturnProperty property = new ReturnProperty();
    property.setVariable(variable);
    property.setIri(iri);
    returnClause.addProperty(property);
    return this;
  }

  /**
   * Sets distinct flag for return values.
   */
  public ReturnBuilder withDistinct(boolean distinct) {
    returnClause.setDistinct(distinct);
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