package org.endeavourhealth.imapi.model.imq;

public class GroupBy extends IriLD{
  private String nodeRef;
  private String valueRef;
  private String propertyRef;

  public String getNodeRef() {
    return nodeRef;
  }

  public GroupBy setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public String getValueRef() {
    return valueRef;
  }

  public GroupBy setValueRef(String valueRef) {
    this.valueRef = valueRef;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public GroupBy setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }
}
