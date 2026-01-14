package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"node", "variable", "iri", "name", "function", "as"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Return {
  @Getter
  private String iri;
  @Getter
  private String nodeRef;
  @Getter
  private String name;
  @Getter
  private String propertyRef;
  @Getter
  private String value;
  @Getter
  private boolean inverse;
  @Getter
  private FunctionClause function;
  @Getter
  private String unit;
  @Getter
  private String as;
  private List<Return> returx;
  private TTIriRef dataType;
  @JsonProperty("case")
  private Case case_;
  @Getter
  private String description;



  public Return setName(String name) {
    this.name = name;
    return this;
  }


  @JsonProperty("case")
  public Case getCase() {
    return case_;
  }

  @JsonProperty("case")
  public Return setCase(Case case_) {
    this.case_ = case_;
    return this;
  }

  public Return case_(Consumer<Case> builder) {
    builder.accept(this.setCase(new Case()).getCase());
    return this;
  }

  public TTIriRef getDataType() {
    return dataType;
  }

  public Return setDataType(TTIriRef dataType) {
    this.dataType = dataType;
    return this;
  }

  public Return setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }


  @JsonProperty("return")
  public List<Return> getReturn() {
    return returx;
  }

  public Return setReturn(List<Return> returns) {
    this.returx = returns;
    return this;
  }
  public Return addReturn(Return return_) {
    if (this.returx == null)
      this.returx = new ArrayList<>();
    this.returx.add(return_);
    return this;
  }

  public Return return_(Consumer<Return> builder) {
    Return return_ = new Return();
    addReturn(return_);
    builder.accept(return_);
    return this;
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

  public Return setUnit(String unit) {
    this.unit = unit;
    return this;
  }

  public Return setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public Return function(Consumer<FunctionClause> builder) {
    builder.accept(setFunction(new FunctionClause()).getFunction());
    return this;
  }

  public Return setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }


  public Return setValue(String value) {
    this.value = value;
    return this;
  }

  public Return setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public Return setIri(VocabEnum iri) {
    this.iri = iri.toString();
    return this;
  }

  public Return setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }


  public Return setDescription(String description) {
    this.description = description;
    return this;
  }
}
