package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for constructing IMQuery Match objects.
 * Used for building rule conditions and WHERE clauses.
 */
public class MatchBuilder {
  private final Match match;

  public MatchBuilder() {
    this.match = new Match();
  }

  public MatchBuilder(RuleAction ifTrue, RuleAction ifFalse) {
    this.match = new Match();
    this.match.setIfTrue(ifTrue);
    this.match.setIfFalse(ifFalse);
  }

  public MatchBuilder ifTrue(RuleAction action) {
    match.setIfTrue(action);
    return this;
  }

  public MatchBuilder ifFalse(RuleAction action) {
    match.setIfFalse(action);
    return this;
  }

  public MatchBuilder ifTrue(String action) {
    return ifTrue(RuleAction.valueOf(action.toUpperCase()));
  }

  public MatchBuilder ifFalse(String action) {
    return ifFalse(RuleAction.valueOf(action.toUpperCase()));
  }

  public MatchBuilder name(String name) {
    match.setName(name);
    return this;
  }

  public MatchBuilder description(String description) {
    match.setDescription(description);
    return this;
  }

  public MatchBuilder typeOf(String iriRef) {
    Node typeOf = new Node();
    typeOf.setIri(iriRef);
    match.setTypeOf(typeOf);
    return this;
  }

  public MatchBuilder ruleNumber(int ruleNumber) {
    match.setRuleNumber(ruleNumber);
    return this;
  }

  public MatchBuilder baseRule(boolean baseRule) {
    match.setBaseRule(baseRule);
    return this;
  }

  public MatchBuilder addWhere(Where where) {
    match.setWhere(where);
    return this;
  }

  public MatchBuilder addAnd(Match andCondition) {
    match.addAnd(andCondition);
    return this;
  }

  public MatchBuilder addOr(Match orCondition) {
    match.addOr(orCondition);
    return this;
  }

  public MatchBuilder addNot(Match notCondition) {
    match.addNot(notCondition);
    return this;
  }

  public MatchBuilder isCohort(String cohortIri) {
    TTIriRef cohort = new TTIriRef();
    cohort.setIri(cohortIri);
    match.setIsCohort(cohort);
    return this;
  }

  public MatchBuilder variable(String variable) {
    match.setVariable(variable);
    return this;
  }

  public MatchBuilder parameter(String parameter) {
    match.setParameter(parameter);
    return this;
  }

  public Match build() {
    return match;
  }
}