package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;


@JsonPropertyOrder({"nodeVariable", "id", "count", "direction"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class OrderLimit {
  private OrderDirection property;
  private int limit;
  private String description;
  public OrderDirection getProperty() {
    return property;
  }

  public OrderLimit setProperty(OrderDirection property) {
    this.property = property;
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


}
