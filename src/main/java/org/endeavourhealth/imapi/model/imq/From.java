package org.endeavourhealth.imapi.model.imq;

public class From{
  private Node typeOf;
  private String alias;

  public Node getTypeOf() {
    return typeOf;
  }
  public From setTypeOf(Node typeOf) {
    this.typeOf = typeOf;
    return this;
  }
  public String getAlias() {
    return alias;
  }
  public From setAlias(String alias) {
    this.alias = alias;
    return this;
  }

}
