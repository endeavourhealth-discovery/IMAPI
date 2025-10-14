package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri", "name", "property","pattern","intervalUnit","qualifier"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyRange extends TTIriRef{
  @Getter
  private TTIriRef type;
  @Getter
  private String pattern;
  @Getter
  private TTIriRef intervalUnit;
  @Getter
  private List<PropertyRange> qualifier;
  @Getter
  private TTIriRef units;
  @Getter
  private TTIriRef operator;
  @Getter
  private boolean isRelativeValue;

  public PropertyRange setRelativeValue(boolean relativeValue) {
    isRelativeValue = relativeValue;
    return this;
  }

  public PropertyRange setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }

  public PropertyRange setOperator(TTIriRef operator) {
    this.operator = operator;
    return this;
  }

  public PropertyRange setType(TTIriRef type) {
    this.type = type;
    return this;
  }

  public PropertyRange setQualifier(List<PropertyRange> qualifier) {
    this.qualifier = qualifier;
    return this;
  }
  public PropertyRange addQualifier (PropertyRange qualifier){
      if (this.qualifier == null) {
        this.qualifier = new ArrayList<>();
      }
      this.qualifier.add(qualifier);
      return this;
    }

    public PropertyRange qualifier (Consumer <PropertyRange> builder) {
      PropertyRange qualifier = new PropertyRange();
      addQualifier(qualifier);
      builder.accept(qualifier);
      return this;
    }


  @Override
  public PropertyRange setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  @Override
  public PropertyRange setName(String name) {
    super.setName(name);
    return this;
  }



  public PropertyRange setPattern(String pattern) {
    this.pattern = pattern;
    return this;
  }


  public PropertyRange setIntervalUnit(TTIriRef intervalUnit) {
    this.intervalUnit = intervalUnit;
    return this;
  }



}
