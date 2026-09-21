package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.imq.OrderDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"nodeVariable", "id", "count", "direction"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class OrderLimit {
  private List<IriLD> partition;
  private List<OrderDirection> property;
  private int limit;
  private String description;

  public List<OrderDirection> getProperty() {
    return property;
  }

  public OrderLimit setProperty(List<OrderDirection> property) {
    this.property = property;
    return this;
  }

  public OrderLimit addProperty(OrderDirection property) {
    if (this.property == null) this.property = new ArrayList<>();
    this.property.add(property);
    return this;
  }

  public String getDescription() {
    return description;
  }

  public OrderLimit setDescription(String description) {
    this.description = description;
    return this;
  }

  public int getLimit() {
    return limit;
  }

  public OrderLimit setLimit(int limit) {
    this.limit = limit;
    return this;
  }
  public List<IriLD> getPartition() {
    return partition;
  }
  public OrderLimit setPartition(List<IriLD> partition) {
    this.partition = partition;
    return this;
  }
  public OrderLimit addPartition(IriLD partition) {
    if (this.partition == null) this.partition = new ArrayList<>();
    this.partition.add(partition);
    return this;
  }
  public OrderLimit partition(Consumer<IriLD> builder) {
    IriLD partition = new IriLD();
    addPartition(partition);
    builder.accept(partition);
    return this;
  }
}
