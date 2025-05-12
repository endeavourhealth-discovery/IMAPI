package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
@JsonPropertyOrder({"ifTrue","ifFalse","name", "description", "nodeRef", "header","typeOf", "instanceOf","and","or","not","where"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends IriLD{
  private Element graph;
  @Getter
  private Where where;
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
  String parameter;
  @Getter
  private String name;
  @Getter
  private List<Path> path;
  @Getter
  private FunctionClause function;
  @Getter
  private Entail entailment;
  private Return returx;
  @Getter
  private RuleAction ifTrue;
  @Getter
  private RuleAction ifFalse;
  @Getter
  private boolean baseRule;
  @Getter
  private boolean hasLinked;
  @Getter
  private boolean union;
  @Getter
  @Setter
  private Integer ruleNumber;
  @Getter
  private boolean inverse;
  @Getter
  private Match then;
  @Getter
  private List<Match> not;
  @Getter
  private List<Match> or;
  @Getter
  private List<Match> and;
  @Getter
  private List<Match> rule;

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


  public Match setOr(List<Match> or) {
    this.or = or;
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
    this.and= and;
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


  public Match setThen(Match then) {
    this.then = then;
    return this;
  }

  public Match then(Consumer<Match> builder) {
    Match then = new Match();
    setThen(then);
    builder.accept(then);
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




  public Match setHasLinked(boolean hasLinked) {
    this.hasLinked = hasLinked;
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

  public String getDescription() {
    return description;
  }




}