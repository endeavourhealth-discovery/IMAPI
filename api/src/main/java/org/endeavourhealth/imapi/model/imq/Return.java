package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"nodeRef", "function", "property", "groupBy", "as"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Return {
  private String nodeRef;
  private List<ReturnProperty> property;
  private FunctionClause function;
  private String as;



  public String getAs() {
    return as;
  }

  @JsonSetter
  public Return setAs(String as) {
    this.as = as;
    return this;
  }

  public Return as(String as) {
    this.as = as;
    return this;
  }

  public FunctionClause getFunction() {
    return function;
  }

  public Return setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public Return function(Consumer<FunctionClause> builder) {
    builder.accept(setFunction(new FunctionClause()).getFunction());
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public Return setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }


  public List<ReturnProperty> getProperty() {
    return property;
  }

  public Return setProperty(List<ReturnProperty> property) {
    this.property = property;
    return this;
  }

  public Return addProperty(ReturnProperty property) {
    if (this.property == null)
      this.property = new ArrayList<>();
    this.property.add(property);
    return this;
  }

  public Return property(Consumer<ReturnProperty> builder) {
    ReturnProperty property = new ReturnProperty();
    addProperty(property);
    builder.accept(property);
    return this;

  }


}
