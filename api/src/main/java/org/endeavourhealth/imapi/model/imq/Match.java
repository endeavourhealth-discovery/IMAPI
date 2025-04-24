package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
@JsonPropertyOrder({"ifTrue","ifFalse","name", "description", "exclude", "nodeRef", "header","preface","boolMatch", "boolWhere", "iri", "typeOf", "instanceOf", "where", "match"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends IriLD implements GraphNode {

  @Getter
  private Bool bool;
  private List<Match> match;
  private boolean exclude;
  private String includeIf;
  private Element graph;
  private List<Where> where;
  private String description;
  private OrderLimit orderBy;
  private String nodeRef;
  private boolean optional;
  private FunctionClause aggregate;
  @Getter
  private List<Node> instanceOf;
  @Getter
  private Node typeOf;
  @Getter
  private String variable;
  @Getter
  private String name;
  private Path path;
  private String displayLabel;
  @Getter
  private FunctionClause function;
  @Getter
  private Entail entailment;
  private Return returx;
  @Getter
  private RuleAction ifTrue;
  @Getter
  private RuleAction ifFalse;
  private boolean hasRules;
  @Getter
  private boolean hasLinked;
  @Getter
  private boolean union;
  @Getter
  private boolean rule;
  @Getter
  private boolean hasTest;
  @Getter
  private boolean test;
  @Getter
  private String header;
  @Getter
  private String preface;


  public Match setReturx(Return returx) {
    this.returx = returx;
    return this;
  }

  @JsonGetter
  public boolean hasRules(){
    return hasRules;
  }


  public Match setHeader(String header) {
    this.header = header;
    return this;
  }

  public Match setPreface(String preface) {
    this.preface = preface;
    return this;
  }

  public Match setHasRules(boolean hasRules) {
    this.hasRules = hasRules;
    return this;
  }

  public Match setHasLinked(boolean hasLinked) {
    this.hasLinked = hasLinked;
    return this;
  }


  public Match setIsUnion(boolean union) {
    this.union = union;
    return this;
  }


  @JsonSetter
  public Match setIsRule(boolean rule) {
    this.rule = rule;
    return this;
  }

  public Match setHasTest(boolean hasTest) {
    this.hasTest = hasTest;
    return this;
  }


  @JsonSetter
  public Match setIsTest(boolean test) {
    this.test = test;
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


  public Match setBool(Bool bool) {
    this.bool = bool;
    return this;
  }


  @Override
  public Path getPath() {
    return path;
  }

  @Override
  public Match setPath(Path path) {
    this.path = path;
    return this;
  }

  @Override
  public Match path(Consumer<Path> builder) {
    this.path = new Path();
    builder.accept(this.path);
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

  public Match setInstanceOf(List<Node> instanceOf) {
    this.instanceOf = instanceOf;
    return this;
  }

  public Match addInstanceOf(Node instanceOf) {
    if (this.instanceOf == null) {
      this.instanceOf = new ArrayList<>();
    }
    this.instanceOf.add(instanceOf);
    return this;
  }

  public Match instanceOf(Consumer<Node> builder) {
    Node node = new Node();
    addInstanceOf(node);
    builder.accept(node);
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


  public OrderLimit getOrderBy() {
    return orderBy;
  }

  @JsonSetter
  public Match setOrderBy(OrderLimit orderBy) {
    this.orderBy = orderBy;
    return this;
  }


  public Match orderBy(Consumer<OrderLimit> builder) {
    OrderLimit orderBy = new OrderLimit();
    setOrderBy(orderBy);
    builder.accept(orderBy);
    return this;
  }


  public boolean isExclude() {
    return exclude;
  }

  public Match setExclude(boolean exclude) {
    this.exclude = exclude;
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


  public List<Match> getMatch() {
    return match;
  }

  @JsonSetter
  public Match setMatch(List<Match> match) {
    this.match = match;
    return this;
  }

  public Match match(Consumer<Match> builder) {
    Match match = new Match();
    addMatch(match);
    builder.accept(match);
    return this;
  }

  public Match addMatch(Match match) {
    if (this.match == null)
      this.match = new ArrayList<>();
    this.match.add(match);
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

  public String getDescription() {
    return description;
  }

  public List<Where> getWhere() {
    return where;
  }

  @JsonSetter
  public Match setWhere(List<Where> where) {
    this.where = where;
    return this;
  }

  public Match addWhere(Where prop) {
    if (this.where == null)
      this.where = new ArrayList<>();
    this.where.add(prop);
    return this;
  }

  public Match where(Consumer<Where> builder) {
    Where prop = new Where();
    addWhere(prop);
    builder.accept(prop);
    return this;
  }


}