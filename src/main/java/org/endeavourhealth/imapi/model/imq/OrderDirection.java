package org.endeavourhealth.imapi.model.imq;

public class OrderDirection extends IriLD{
  private Order direction;
  private FunctionClause function;
  private String nodeRef;
  private String variable;

  public String getVariable() {
    return variable;
  }

  public OrderDirection setVariable(String variable) {
    this.variable = variable;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public OrderDirection setNodeRef(String nodeRef){
    this.nodeRef = nodeRef;
    return this;
  }

  public FunctionClause getFunction() {
    return function;
  }
  public OrderDirection setFunction(FunctionClause function) {
    this.function = function;
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
