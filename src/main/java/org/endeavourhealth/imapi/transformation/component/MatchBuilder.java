package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for constructing IMQ Match objects.
 * Match objects represent conditions/predicates in queries.
 */
public class MatchBuilder {

  private final Match match;

  /**
   * Creates a new MatchBuilder.
   */
  public MatchBuilder() {
    this.match = new Match();
  }

  /**
   * Creates a MatchBuilder from an existing Match.
   *
   * @param match The match to build upon
   */
  public MatchBuilder(Match match) {
    this.match = match;
  }

  /**
   * Sets the variable that this match binds to.
   */
  public MatchBuilder withVariable(String variable) {
    match.setVariable(variable);
    return this;
  }

  /**
   * Sets the type that objects must be an instance of.
   */
  public MatchBuilder withInstanceOf(Node node) {
    match.addInstanceOf(node);
    return this;
  }

  /**
   * Sets the type that objects must be a typeOf.
   */
  public MatchBuilder withTypeOf(String typeOf) {
    match.setTypeOf(typeOf);
    return this;
  }

  /**
   * Adds a rule/constraint to this match.
   */
  public MatchBuilder addRule(Match rule) {
    match.addRule(rule);
    return this;
  }

  /**
   * Sets the bind-as parameter.
   */
  public MatchBuilder withBindAs(String bindAs) {
    match.setParameter(bindAs);
    return this;
  }

  /**
   * Builds and returns the Match object.
   */
  public Match build() {
    return match;
  }

  /**
   * Gets the underlying Match object without building.
   */
  public Match get() {
    return match;
  }
}