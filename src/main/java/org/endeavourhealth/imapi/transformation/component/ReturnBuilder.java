package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Return;
import org.endeavourhealth.imapi.model.imq.ReturnProperty;

/**
 * Fluent builder for constructing IMQuery RETURN specifications.
 * Manages what properties should be returned from a query.
 */
public class ReturnBuilder {
  private final Return returnSpec;

  public ReturnBuilder() {
    this.returnSpec = new Return();
  }

  public ReturnBuilder as(String alias) {
    returnSpec.setAs(alias);
    return this;
  }

  public ReturnBuilder asDescription(String description) {
    returnSpec.setAsDescription(description);
    return this;
  }

  public ReturnBuilder addProperty(String propertyIri) {
    ReturnProperty prop = new ReturnProperty();
    prop.setIri(propertyIri);
    returnSpec.addProperty(prop);
    return this;
  }

  public ReturnBuilder addProperty(ReturnProperty property) {
    returnSpec.addProperty(property);
    return this;
  }

  public ReturnBuilder nodeRef(String nodeRef) {
    returnSpec.setNodeRef(nodeRef);
    return this;
  }

  public Return build() {
    return returnSpec;
  }
}