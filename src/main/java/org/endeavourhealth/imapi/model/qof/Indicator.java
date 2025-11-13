package org.endeavourhealth.imapi.model.qof;

import java.util.ArrayList;
import java.util.List;

public class Indicator {
    private String name;
    private String description;
    private String base;
    private List<Rule> denominator = new ArrayList<>();
    private List<Rule> numerator = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Indicator setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Indicator setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBase() {
        return base;
    }

    public Indicator setBase(String base) {
        this.base = base;
        return this;
    }

    public List<Rule> getDenominator() {
        return denominator;
    }

    public Indicator setDenominator(List<Rule> denominator) {
        this.denominator = denominator;
        return this;
    }

    public Indicator addDenominator(Rule rule) {
        this.denominator.add(rule);
        return this;
    }

  public List<Rule> getNumerator() {
    return numerator;
  }

  public Indicator setNumerator(List<Rule> numerator) {
    this.numerator = numerator;
    return this;
  }

  public Indicator addNumerator(Rule rule) {
    this.numerator.add(rule);
    return this;
  }
}
