package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"description", "nodeVariable", "iri", "name", "bool", "match", "property", "range", "operator", "isNull", "value", "intervalUnit", "instanceOf", "relativeTo", "anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties({"key"})
public class Where extends Element implements Assignable,BoolGroup<Where> {
  @Getter
  private String description;
  private Range range;
  @Getter
  private Node typeOf;
  @Getter
  private List<Node> is;
  @Getter
  private List<Node> notIs;
  private List<Where> not;
  private Operator operator;
  private String value;
  private String valueLabel;
  private boolean anyRoleGroup;
  private boolean isNull;
  private RelativeTo relativeTo;
  private boolean isNotNull;
  private FunctionClause function;
  private TTIriRef unit;
  private String valueParameter;
  private String valueVariable;
  @Getter
  private boolean inverse;
  @Getter
  private List<Where> or;
  @Getter
  private List<Where> and;



  public Where() {
  }

  public Where(String iri) {
    super.setIri(iri);
  }

  public Where setNotIs(List<Node> notIs) {
    this.notIs = notIs;
    return this;
  }

  public Where addNotIs(Node notIs) {
    if (this.notIs == null) {
      this.notIs = new ArrayList<>();
    }
    this.notIs.add(notIs);
    return this;
  }

  public Where setAnd(List<Where> and) {
    this.and = and;
    return this;
  }


  public Where addAnd(Where and) {
    if (this.and == null) {
      this.and = new ArrayList<>();
    }
    this.and.add(and);
    return this;
  }
  public Where and(Consumer<Where> builder) {
    Where and = new Where();
    addAnd(and);
    builder.accept(and);
    return this;
  }


  @Override
  public List<Where> getNot() {
    return this.not;
  }

  public Where setOr(List<Where> or) {
    this.or = or;
    return this;
  }

  public Where addOr(Where or) {
    if (this.or == null) {
      this.or = new ArrayList<>();
    }
    this.or.add(or);
    return this;
  }

  public Where or(Consumer<Where> builder) {
    Where or = new Where();
    addOr(or);
    builder.accept(or);
    return this;
  }


  @JsonSetter
  public Where setTypeOf(Node typeOf) {
    this.typeOf = typeOf;
    return this;
  }


  public Where setTypeOf(String type) {
    this.typeOf = new Node().setIri(type);
    return this;
  }

  public Where setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }



  public Where setNot(List<Where> not) {
    this.not = not;
    return this;
  }

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


  public String getValueVariable() {
    return valueVariable;
  }

  public Where setValueVariable(String valueVariable) {
    this.valueVariable= valueVariable;
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




  public Where setName(String name) {
    super.setName(name);
    return this;
  }

  public Where setDescription(String description) {
    this.description = description;
    return this;
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

  public RelativeTo getRelativeTo() {
    return this.relativeTo;
  }

  public Where setRelativeTo(RelativeTo relativeTo) {
    this.relativeTo = relativeTo;
    return this;
  }

  public Where relativeTo(Consumer<RelativeTo> builder) {
    builder.accept(setRelativeTo(new RelativeTo()).getRelativeTo());
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
  public Where setUnit(TTIriRef intervalUnit) {
    this.unit = intervalUnit;
    return this;
  }

  public TTIriRef getUnit() {
    return this.unit;
  }

}
