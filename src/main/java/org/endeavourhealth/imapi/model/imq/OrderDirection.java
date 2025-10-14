package org.endeavourhealth.imapi.model.imq;

public class OrderDirection extends RelativeTo {
  private Order direction;
  private FunctionClause function;

  public FunctionClause getFunction() {
    return function;
  }
  public OrderDirection setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public  OrderDirection setNodeRef(String nodeRef){
    super.setNodeRef(nodeRef);
    return this;
  }

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
