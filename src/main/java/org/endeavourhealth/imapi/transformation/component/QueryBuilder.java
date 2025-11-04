package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for constructing IMQuery Query objects.
 * Simplifies programmatic query construction during transformation.
 */
public class QueryBuilder {
  private final Query query;

  public QueryBuilder() {
    this.query = new Query();
  }

  public QueryBuilder(String iri, String name) {
    this.query = new Query();
    this.query.setIri(iri);
    this.query.setName(name);
  }

  public QueryBuilder iri(String iri) {
    query.setIri(iri);
    return this;
  }

  public QueryBuilder name(String name) {
    query.setName(name);
    return this;
  }

  public QueryBuilder description(String description) {
    query.setDescription(description);
    return this;
  }

  public QueryBuilder typeOf(String iriRef) {
    Node typeOf = new Node();
    typeOf.setIri(iriRef);
    query.setTypeOf(typeOf);
    return this;
  }

  public QueryBuilder isCohort(String cohortIri) {
    TTIriRef cohort = new TTIriRef();
    cohort.setIri(cohortIri);
    query.setIsCohort(cohort);
    return this;
  }

  public QueryBuilder addRule(Match rule) {
    query.addRule(rule);
    return this;
  }

  public QueryBuilder rule(List<Match> rules) {
    query.setRule(rules);
    return this;
  }

  public QueryBuilder addWhere(Match where) {
    query.addAnd(where);
    return this;
  }

  public QueryBuilder returnProperty(String property) {
    Return returnSpec = query.getReturn();
    if (returnSpec == null) {
      returnSpec = new Return();
      query.setReturn(returnSpec);
    }
    // Add return property
    return this;
  }

  public QueryBuilder variable(String variable) {
    query.setVariable(variable);
    return this;
  }

  public QueryBuilder activeOnly(boolean activeOnly) {
    query.setActiveOnly(activeOnly);
    return this;
  }

  public QueryBuilder parameter(String parameter) {
    query.setParameter(parameter);
    return this;
  }

  public QueryBuilder bindAs(String bindAs) {
    query.setBindAs(bindAs);
    return this;
  }

  public QueryBuilder addAndCondition(Match condition) {
    query.addAnd(condition);
    return this;
  }

  public QueryBuilder addOrCondition(Match condition) {
    query.addOr(condition);
    return this;
  }

  public QueryBuilder addNotCondition(Match condition) {
    query.addNot(condition);
    return this;
  }

  public Query build() {
    // Set default typeOf if not specified
    if (query.getTypeOf() == null) {
      Node defaultType = new Node();
      defaultType.setIri("http://endhealth.info/im#Patient");
      query.setTypeOf(defaultType);
    }
    return query;
  }

  public Query buildWithoutValidation() {
    return query;
  }
}