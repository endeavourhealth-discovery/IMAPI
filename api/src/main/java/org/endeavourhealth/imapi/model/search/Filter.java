package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Filter {
  private String field;
  private List<TTIriRef> iriValue;
  private List<Filter> and;
  private boolean not;
  private List<String> textValue;
  private boolean startsWithTerm;

  public boolean isStartsWithTerm() {
    return startsWithTerm;
  }

  public Filter setStartsWithTerm(boolean startsWithTerm) {
    this.startsWithTerm = startsWithTerm;
    return this;
  }

  public List<String> getTextValue() {
    return textValue;
  }

  public Filter setTextValue(List<String> textValue) {
    this.textValue = textValue;
    return this;
  }

  public Filter addTextValue(String text) {
    if (this.textValue == null)
      this.textValue = new ArrayList<>();
    this.textValue.add(text);
    return this;
  }

  public String getField() {
    return field;
  }

  public Filter setField(String field) {
    this.field = field;
    return this;
  }


  public boolean isNot() {
    return not;
  }

  public Filter setNot(boolean not) {
    this.not = not;
    return this;
  }

  public List<Filter> getAnd() {
    return and;
  }

  public Filter setAnd(List<Filter> and) {
    this.and = and;
    return this;
  }

  public Filter addAnd(Filter and) {
    if (this.and == null) {
      this.and = new ArrayList<>();
    }
    this.and.add(and);
    return this;
  }

  public Filter and(Consumer<Filter> builder) {
    Filter order = new Filter();
    addAnd(order);
    builder.accept(order);
    return this;
  }

  public List<TTIriRef> getIriValue() {
    return iriValue;
  }

  public Filter setIriValue(List<TTIriRef> iriValue) {
    this.iriValue = iriValue;
    return this;
  }

  public Filter addIriValue(TTIriRef iriValue) {
    if (this.iriValue == null) {
      this.iriValue = new ArrayList<>();
    }
    this.iriValue.add(iriValue);
    return this;
  }


}
