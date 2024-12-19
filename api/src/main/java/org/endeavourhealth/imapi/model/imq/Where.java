package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"description", "nodeVariable", "iri", "name", "bool", "match", "property", "range", "operator", "isNull", "value", "unit", "instanceOf", "relativeTo", "anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties({"key"})
public class Where extends PropertyRef implements Assignable {
  private String description;
  private Range range;
  private List<Node> is;
  private Match match;
  private Bool boolWhere;
  private List<Where> where;
  private Operator operator;
  private String value;
  private String valueLabel;
  private boolean anyRoleGroup;
  private boolean isNull;
  private PropertyRef relativeTo;
  private boolean isNotNull;
  private FunctionClause function;
  private TTIriRef unit;

  private String valueParameter;

  public String getValueParameter() {
    return valueParameter;
  }

  public Where setValueParameter(String valueParameter) {
    this.valueParameter = valueParameter;
    return this;
  }


  public FunctionClause getFunction() {
    return function;
  }

  public Where setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public Where() {
  }

  public Where(String iri) {
    super.setIri(iri);
  }

  public static Where iri(String iri) {
    return new Where(iri);
  }


  public Where setQualifier(String qualifier) {
    super.setQualifier(qualifier);
    return this;
  }

  @Override
  public String getValueLabel() {
    return this.valueLabel;
  }


  public Bool getBoolWhere() {
    return boolWhere;
  }

  public Where setBoolWhere(Bool boolWhere) {
    this.boolWhere = boolWhere;
    return this;
  }

  public boolean getIsNotNull() {
    return isNotNull;
  }

  public Where setIsNotNull(boolean notNull) {
    isNotNull = notNull;
    return this;
  }

  public boolean getIsNull() {
    return isNull;
  }

  public Where setIsNull(boolean aNull) {
    isNull = aNull;
    return this;
  }

  public List<Where> getWhere() {
    return where;
  }

  @JsonSetter
  public Where setWhere(List<Where> where) {
    this.where = where;
    return this;
  }

  public Where addWhere(Where prop) {
    if (this.where == null) this.where = new ArrayList<>();
    this.where.add(prop);
    return this;
  }

  public Where where(Consumer<Where> builder) {
    Where prop = new Where();
    addWhere(prop);
    builder.accept(prop);
    return this;
  }

  public Where setValueVariable(String valueVariable) {
    super.setValueVariable(valueVariable);
    return this;
  }

  @JsonProperty("@id")
  public String getId() {
    return super.getIri();
  }

  @Override
  public Where setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }


  public Where setVariable(String variable) {
    super.setVariable(variable);
    return this;
  }

  public Where setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Where setAncestorsOf(boolean entailment) {
    super.setAncestorsOf(entailment);
    return this;
  }


  public Where setValueLabel(String valueLabel) {
    this.valueLabel = valueLabel;
    return this;
  }

  public boolean isAnyRoleGroup() {
    return anyRoleGroup;
  }

  public Where setAnyRoleGroup(boolean anyRoleGroup) {
    this.anyRoleGroup = anyRoleGroup;
    return this;
  }


  public Match getMatch() {
    return match;
  }

  @JsonSetter
  public Where setMatch(Match match) {
    this.match = match;
    return this;
  }


  public Where match(Consumer<Match> builder) {
    builder.accept(setMatch(new Match()).getMatch());
    return this;
  }


  public Where setName(String name) {
    super.setName(name);
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Where setDescription(String description) {
    this.description = description;
    return this;
  }

  public List<Node> getIs() {
    return is;
  }

  @JsonSetter
  public Where setIs(List<Node> isList) {
    this.is = isList;
    return this;
  }


  public Where addIs(Node isItem) {
    if (this.is == null) this.is = new ArrayList<>();
    this.is.add(isItem);
    return this;
  }

  public Where is(Consumer<Node> builder) {
    Node isItem = new Node();
    addIs(isItem);
    builder.accept(isItem);
    return this;
  }

  public Where setInverse(boolean inverse) {
    super.setInverse(inverse);
    return this;
  }

  public Where addIs(String isIri) {
    if (this.is == null) this.is = new ArrayList<>();
    this.is.add(new Node().setIri(isIri));
    return this;
  }

  public Where setDescendantsOrSelfOf(boolean entailment) {
    super.setDescendantsOrSelfOf(entailment);
    return this;
  }

  public Operator getOperator() {
    return this.operator;
  }

  public Where setOperator(Operator operator) {
    this.operator = operator;
    return this;
  }

  public PropertyRef getRelativeTo() {
    return this.relativeTo;
  }

  public Where setRelativeTo(PropertyRef relativeTo) {
    this.relativeTo = relativeTo;
    return this;
  }

  public Where relativeTo(Consumer<PropertyRef> builder) {
    builder.accept(setRelativeTo(new PropertyRef()).getRelativeTo());
    return this;
  }

  public String getValue() {
    return this.value;
  }

  public Where setValue(String value) {
    this.value = value;
    return this;
  }


  public Range getRange() {
    return range;
  }

  public Where setRange(Range range) {
    this.range = range;
    return this;
  }

  public Where range(Consumer<Range> builder) {
    this.range = new Range();
    builder.accept(this.range);
    return this;
  }

  @Override
  public Where setUnit(TTIriRef unit) {
    this.unit = unit;
    return this;
  }

  public TTIriRef getUnit() {
    return this.unit;
  }

}
