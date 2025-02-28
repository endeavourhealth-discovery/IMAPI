package org.endeavourhealth.imapi.model.imq;

public class OrderDirection extends RelativeTo {
  private Order direction;

  public Order getDirection() {
    return direction;
  }

  public OrderDirection setDirection(Order direction) {
    this.direction = direction;
    return this;
  }

  public OrderDirection setIri(String iri) {
    super.setIri(iri);
    return this;
  }
}
