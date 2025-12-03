package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"ifTrue", "ifFalse", "name", "description", "nodeRef", "header", "typeOf", "is", "path", "and", "or", "not", "where", "return", "then", ""})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends IriLD implements HasPaths {
  private Element graph;
  private Where where;
  private String description;
  private String nodeRef;
  private boolean optional;
  private FunctionClause aggregate;
  private Node typeOf;
  private String variable;
  String parameter;
  private String name;
  private List<Path> path;
  private FunctionClause function;
  private Entail entailment;
  private Return returx;
  private RuleAction ifTrue;
  private RuleAction ifFalse;
  private boolean baseRule;
  private boolean union;
  private Integer ruleNumber;
  private boolean inverse;
  private List<Match> not;
  private List<Match> or;
  private List<Match> and;
  private List<Match> rule;
  private String libraryItem;
  private boolean invalid;
  private List<Node> is;
  private List<GroupBy> groupBy;
  private String keepAs;
  private OrderLimit orderBy;
  private String asDescription;

  public Node getTypeOf() {
    return typeOf;
  }

  public String getVariable() {
    return variable;
  }

  public Where getWhere() {
    return where;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public String getParameter() {
    return parameter;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<Path> getPath() {
    return path;
  }

  public FunctionClause getFunction() {
    return function;
  }

  public Entail getEntailment() {
    return entailment;
  }

  public Return getReturx() {
    return returx;
  }

  public RuleAction getIfTrue() {
    return ifTrue;
  }

  public RuleAction getIfFalse() {
    return ifFalse;
  }

  public boolean isBaseRule() {
    return baseRule;
  }

  public boolean isUnion() {
    return union;
  }

  public void setUnion(boolean union) {
    this.union = union;
  }

  public Integer getRuleNumber() {
    return ruleNumber;
  }

  public void setRuleNumber(Integer ruleNumber) {
    this.ruleNumber = ruleNumber;
  }

  public boolean isInverse() {
    return inverse;
  }

  public List<Match> getNot() {
    return not;
  }

  public List<Match> getOr() {
    return or;
  }

  public List<Match> getAnd() {
    return and;
  }

  public List<Match> getRule() {
    return rule;
  }

  public String getLibraryItem() {
    return libraryItem;
  }

  public boolean isInvalid() {
    return invalid;
  }

  public void setInvalid(boolean invalid) {
    this.invalid = invalid;
  }

  public List<Node> getIs() {
    return is;
  }

  public String getKeepAs() {
    return keepAs;
  }

  public String getAsDescription() {
    return asDescription;
  }

  public Match setAsDescription(String asDescription) {
    this.asDescription = asDescription;
    return this;
  }

  public OrderLimit getOrderBy() {
    return orderBy;
  }

  public Match setOrderBy(OrderLimit orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public Match orderBy(Consumer<OrderLimit> builder) {
    this.orderBy = new OrderLimit();
    builder.accept(this.orderBy);
    return this;
  }

  public Match setKeepAs(String keepAs) {
    this.keepAs = keepAs;
    return this;
  }


  public List<GroupBy> getGroupBy() {
    return groupBy;
  }

  public Match setGroupBy(List<GroupBy> groupBy) {
    this.groupBy = groupBy;
    return this;
  }

  public Match addGroupBy(GroupBy group) {
    if (this.groupBy == null)
      this.groupBy = new ArrayList<>();
    this.groupBy.add(group);
    return this;
  }

  public Match groupBy(Consumer<GroupBy> builder) {
    GroupBy group = new GroupBy();
    addGroupBy(group);
    builder.accept(group);
    return this;
  }


  @JsonSetter
  public Match setIs(List<Node> is) {
    this.is = is;
    return this;
  }


  public Match setLibraryItem(String libraryItem) {
    this.libraryItem = libraryItem;
    return this;
  }

  public Match setWhere(Where where) {
    this.where = where;
    return this;
  }

  public Match where(Consumer<Where> builder) {
    if (this.where == null) {
      Where where = new Where();
      setWhere(where);
      builder.accept(where);
    } else builder.accept(null);
    return this;
  }

  public Match setRule(List<Match> rule) {
    this.rule = rule;
    return this;
  }

  public Match addRule(Match rule) {
    if (this.rule == null) {
      this.rule = new ArrayList<>();
    }
    this.rule.add(rule);
    return this;
  }


  public Match setOr(List<Match> ors) {
    this.or = ors;
    return this;
  }

  public Match addOr(Match or) {
    if (this.or == null) {
      this.or = new ArrayList<>();
    }
    this.or.add(or);
    return this;
  }

  public Match or(Consumer<Match> builder) {
    Match or = new Match();
    addOr(or);
    builder.accept(or);
    return this;
  }

  public Match setAnd(List<Match> and) {
    this.and = and;
    return this;
  }

  public Match addAnd(Match and) {
    if (this.and == null) {
      this.and = new ArrayList<>();
    }
    this.and.add(and);
    return this;
  }

  public Match and(Consumer<Match> builder) {
    Match and = new Match();
    addAnd(and);
    builder.accept(and);
    return this;
  }

  public Match setNot(List<Match> not) {
    this.not = not;
    return this;
  }

  public Match addNot(Match not) {
    if (this.not == null) {
      this.not = new ArrayList<>();
    }
    this.not.add(not);
    return this;
  }

  public Match not(Consumer<Match> builder) {
    Match not = new Match();
    addNot(not);
    builder.accept(not);
    return this;
  }


  public Match setBaseRule(boolean baseRule) {
    this.baseRule = baseRule;
    return this;
  }

  public Match setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }

  public Match setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }


  public Match setReturx(Return returx) {
    this.returx = returx;
    return this;
  }


  public Match setIsUnion(boolean union) {
    this.union = union;
    return this;
  }


  public Match setIfTrue(RuleAction ifTrue) {
    this.ifTrue = ifTrue;
    return this;
  }

  public Match setIfFalse(RuleAction ifFalse) {
    this.ifFalse = ifFalse;
    return this;
  }


  @JsonGetter
  public Return getReturn() {
    return returx;
  }

  @JsonSetter
  public Match setReturn(Return returx) {
    this.returx = returx;
    return this;
  }

  public Match return_(Consumer<Return> builder) {
    this.returx = new Return();
    builder.accept(this.returx);
    return this;
  }

  public Match setEntailment(Entail entailment) {
    this.entailment = entailment;
    return this;
  }

  public Match setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public Match function(Consumer<FunctionClause> builder) {
    FunctionClause function = new FunctionClause();
    this.function = function;
    builder.accept(function);
    return this;
  }


  public Match setPath(List<Path> path) {
    this.path = path;
    return this;
  }

  public Match addPath(Path path) {
    if (this.path == null) {
      this.path = new ArrayList<>();
    }
    this.path.add(path);
    return this;
  }

  public Match path(Consumer<Path> builder) {
    Path path = new Path();
    this.addPath(path);
    builder.accept(path);
    return this;
  }


  @Override
  public Match setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Match setVariable(String variable) {
    this.variable = variable;
    return this;
  }

  public Match addIs(Node is) {
    if (this.is == null) {
      this.is = new ArrayList<>();
    }
    this.is.add(is);
    return this;
  }


  @JsonIgnore
  public Match is(Consumer<Node> builder) {
    Node is = new Node();
    addIs(is);
    builder.accept(is);
    return this;
  }


  @JsonSetter
  public Match setTypeOf(Node typeOf) {
    this.typeOf = typeOf;
    return this;
  }

  public FunctionClause getAggregate() {
    return aggregate;
  }

  public Match setAggregate(FunctionClause aggregate) {
    this.aggregate = aggregate;
    return this;
  }

  public Match aggregate(Consumer<FunctionClause> builder) {
    FunctionClause function = new FunctionClause();
    this.aggregate = function;
    builder.accept(function);
    return this;
  }

  public boolean isOptional() {
    return optional;
  }

  public Match setOptional(boolean optional) {
    this.optional = optional;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public Match setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }


  public Element getGraph() {
    return graph;
  }

  public Match setGraph(Element graph) {
    this.graph = graph;
    return this;
  }


  public Match setTypeOf(String type) {
    this.typeOf = new Node().setIri(type);
    return this;
  }


  public Match setName(String name) {
    this.name = name;
    return this;
  }


  public Match setDescription(String description) {
    this.description = description;
    return this;
  }


}