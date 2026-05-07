package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"description", "nodeRef", "iri", "name", "bool", "match", "property", "range", "operator", "isNull", "value", "intervalUnit", "is", "relativeTo", "anyRoleGroup"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties({"key"})
public class Where extends Element implements Assignable{

  private String description;
  private Range range;
  private boolean inverse;
  private Node typeOf;
  private List<Node> is;
  private Operator operator;
  private String value;
  private String valueLabel;
  private String subjectVariable;
  private String subjectParameter;
  private boolean not;
  private boolean anyRoleGroup;
  private boolean isNull;
  private boolean roleGroup;
  private boolean isNotNull;
  private TTIriRef units;
  private List<Where> or;
  private List<Where> and;
  private String propertyRef;
  private String shortLabel;
  private FunctionClause function;
  private TTIriRef qualifier;
  private List<Node> propertyList;
  private String propertyVariable;
  private String node;
  private List<IriLD> excludeProperty;
  private boolean exists;
  private boolean invalid;
  private boolean linked;
  private String valueTerm;
  private Compare compare;



  public String getSubjectVariable() {
    return subjectVariable;
  }

  public Where setSubjectVariable(String subjectVariable) {
    this.subjectVariable = subjectVariable;
    return this;
  }


  public boolean isLinked(){
    return linked;
  }

  public Where setLinked(boolean is){
    this.linked = true;
    return this;
  }

  public boolean isInvalid() {
    return invalid;
  }

  public Where setIsInvalid(boolean invalid) {
    this.invalid = invalid;
    return this;
  }

  public List<Node> getPropertyList() {
    return propertyList;
  }
  public String getSubjectParameter() {
    return subjectParameter;
  }
  public Where setSubjectParameter(String subject) {
    this.subjectParameter = subject;
    return this;
  }
  public Where setParameter(String parameter) {
    super.setParameter(parameter);
    return this;
  }

  @Override
  public String getDescription() {
    return description;
  }


  @Override
  public Compare getCompare() {
    return this.compare;
  }

  @Override
  public Where setCompare(Compare compare) {
    this.compare = compare;
    return this;
  }

  public Where compare(Consumer<Compare> builder) {
    this.compare = new Compare();
    builder.accept(this.compare);
    return this;
  }

  public Node getTypeOf() {
    return typeOf;
  }

  public List<Node> getIs() {
    return is;
  }

  @Override
  public Operator getOperator() {
    return operator;
  }

  public boolean isNot() {
    return not;
  }

  public boolean isAnyRoleGroup() {
    return anyRoleGroup;
  }

  public boolean isRoleGroup() {
    return roleGroup;
  }

  public boolean isNotNull() {
    return isNotNull;
  }

  public void setNotNull(boolean notNull) {
    isNotNull = notNull;
  }

  public boolean isInverse() {
    return inverse;
  }

  public List<Where> getOr() {
    return or;
  }

  public List<Where> getAnd() {
    return and;
  }

  public String getShortLabel() {
    return shortLabel;
  }


  public boolean isExists() {
    return exists;
  }

  public Where setExists(boolean exists) {
    this.exists = exists;
    return this;
  }


  public List<IriLD> getExcludeProperty() {
    return excludeProperty;
  }

  public Where setExcludeProperty(List<IriLD> properties){
    this.excludeProperty = properties;
    return this;
  }

  public Where addExcludeProperty(IriLD property){
    if(this.excludeProperty == null) this.excludeProperty = new ArrayList<>();
    this.excludeProperty.add(property);
    return this;
  }

  public Where excludeProperty(Consumer<IriLD> builder){
    IriLD property = new IriLD();
    addExcludeProperty(property);
    builder.accept(property);
    return this;
  }


  public String getNode() {
    return node;
  }

  public Where setNode(String node) {
    this.node = node;
    return this;
  }

  public Where setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public Where setPropertyVariable(String variable) {
    this.propertyVariable=variable;
    return this;
  }

  public String getPropertyVariable() {
    return propertyVariable;
  }



  public Where setPropertyList(List<Node> propertyList) {
    this.propertyList = propertyList;
    return this;
  }

  public Where addToPropertyList(Node property) {
    if (this.propertyList == null) this.propertyList = new ArrayList<>();
    this.propertyList.add(property);
    return this;
  }


  public Where setNot(boolean not) {
    this.not = not;
    return this;
  }


  public Where setShortLabel(String shortLabel) {
    this.shortLabel = shortLabel;
    return this;
  }


  public Where setRoleGroup(boolean roleGroup) {
    this.roleGroup = roleGroup;
    return this;
  }

  public Where() {
  }

  public Where(String iri) {
    super.setIri(iri);
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


  public static Where iri(String iri) {
    return new Where(iri);
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


  @Override
  public Where setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }



  public Where setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Where setIri(VocabEnum iri) {
    super.setIri(iri.toString());
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

  public Where setAnyRoleGroup(boolean anyRoleGroup) {
    this.anyRoleGroup = anyRoleGroup;
    return this;
  }


  public Where setName(String name) {
    super.setName(name);
    return this;
  }

  @Override
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

  public Where setOperator(Operator operator) {
    this.operator = operator;
    return this;
  }


  @Override
  public String getValueTerm() {
    return this.valueTerm;
  }

  @Override
  public Assignable setValueTerm(String valueTerm) {
    this.valueTerm = valueTerm;
    return this;
  }


  public String getValue() {
    return this.value;
  }

  public Where setValue(String value) {
    this.value = value;
    return this;
  }


  public Where setQualifier(TTIriRef qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  public TTIriRef getQualifier() {
    return this.qualifier;
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


  public Where setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }

  public TTIriRef getUnits() {
    return units;
  }


  public Where function(Consumer<FunctionClause> builder) {
    this.function = new FunctionClause();
    builder.accept(this.function);
    return this;
  }


}
