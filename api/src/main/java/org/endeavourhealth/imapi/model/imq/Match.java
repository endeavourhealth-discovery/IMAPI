package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"name", "description", "exclude", "nodeRef", "boolMatch", "boolWhere", "iri", "typeOf", "instanceOf", "where", "match"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends IriLD {

  private Bool boolMatch;
  private Bool boolWhere;
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
  private List<Node> instanceOf;
  private Node typeOf;
  private String variable;
  private String name;
  private Match then;
  private List<IriLD> path;
  private String displayLabel;
  private boolean hasInlineSet;

  public boolean isHasInlineSet() {
    return hasInlineSet;
  }

  public Match setHasInlineSet(boolean hasInlineSet) {
    this.hasInlineSet = hasInlineSet;
    return this;
  }


  public String getIncludeIf() {
    return includeIf;
  }

  public Match setIncludeIf(String includeIf) {
    this.includeIf = includeIf;
    return this;
  }

  public String getDisplayLabel() {
    return displayLabel;
  }

  public Match setDisplayLabel(String displayLabel) {
    this.displayLabel = displayLabel;
    return this;
  }

  public Bool getBoolMatch() {
    return boolMatch;
  }

  public Match setBoolMatch(Bool boolMatch) {
    this.boolMatch = boolMatch;
    return this;
  }

  public Bool getBoolWhere() {
    return boolWhere;
  }

  public Match setBoolWhere(Bool boolWhere) {
    this.boolWhere = boolWhere;
    return this;
  }

  public Match getThen() {
    return then;
  }

  public Match setThen(Match then) {
    this.then = then;
    return this;
  }

  public List<IriLD> getPath() {
    return path;
  }

  public Match setPath(List<IriLD> path) {
    this.path = path;
    return this;
  }

  public Match addPath(IriLD path) {
    if (this.path == null)
      this.path = new ArrayList();
    this.path.add(path);
    return this;
  }

  public Match path(Consumer<IriLD> builder) {
    IriLD path = new IriLD();
    addPath(path);
    builder.accept(path);
    return this;
  }

  public Match then(Consumer<Match> builder) {
    Match match = new Match();
    setThen(match);
    builder.accept(match);
    return this;
  }

  @Override
  public Match setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public String getVariable() {
    return variable;
  }

  public Match setVariable(String variable) {
    this.variable = variable;
    return this;
  }

  public String getName() {
    return name;
  }

  public List<Node> getInstanceOf() {
    return instanceOf;
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

  public Node getTypeOf() {
    return typeOf;
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
