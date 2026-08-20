package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({
  "notExists",
  "from",
  "and",
  "ifTrue",
  "ifFalse",
  "name",
  "description",
  "nodeRef",
  "header",
  "typeOf",
  "is",
  "path",
  "and",
  "or",
  "not",
  "where",
  "return",
  "then",
  ""
})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query implements HasPaths, Returnable {

  private String as;
  private String name;
  private String description;
  private Node graph;
  private Where where;
  private String iri;
  private List<From> from;
  private boolean optional;
  private Node typeOf;
  private String parameter;

  private List<Path> path;
  private FunctionClause function;
  private Entail entailment;
  private List<Return> returx;
  private RuleAction ifTrue;
  private RuleAction ifFalse;
  private boolean baseRule;
  private Integer ruleNumber;
  private boolean inverse;
  private boolean activeOnly;
  private List<Query> or;
  private List<Query> and;
  private List<Query> rule;
  private List<Query> each;
  private String libraryItem;
  private boolean invalid;
  private Node is;
  private List<GroupBy> groupBy;
  private String node;
  private OrderLimit orderBy;
  private String asDescription;
  private boolean notExists;
  private String errorMessage;
  private boolean draft;
  private Query then;
  private Having having;
  private Prefixes prefixes;
  private String imQuery;
  private JsonNode parentResult;
  private List<Query> columnGroup;
  private IMQType queryType;
  private String uuid;

public List<Query> getEach() {
  return each;
}

public Query setEach(List<Query> each) {
  this.each = each;
  return this;
}
public Query addEach(Query query) {
  if (this.each == null) this.each = new ArrayList<>();
  this.each.add(query);
  return this;
}
public Query each(Consumer<Query> builder) {
  Query query = new Query();
  addEach(query);
  builder.accept(query);
  return this;
}

  public String getDescription() {
    return description;
  }
  public Query setDescription(String description) {
    this.description = description;
    return this;
  }
  public Query setAs(String as){
    this.as = as;
    return this;
  }
  public String getAs(){
    return as;
  }
  public String getUuid() {
    return uuid;
  }
  public Query setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public IMQType getQueryType() {
    return queryType;
  }
  public Query setQueryType(IMQType queryType) {
    this.queryType = queryType;
    return this;
  }

  public List<Query> getColumnGroup() {
    return columnGroup;
  }

  public Query setColumnGroup(List<Query> columnGroup) {
    this.columnGroup = columnGroup;
    return this;
  }

  public Query addColumnGroup(Query columnGroup) {
    if (this.columnGroup == null) this.columnGroup = new ArrayList<>();
    this.columnGroup.add(columnGroup);
    return this;
  }

  public String getIri() {
    return iri;
  }


  public JsonNode getParentResult() {
    return parentResult;
  }
  public Query setParentResult(JsonNode parentResult) {
    this.parentResult = parentResult;
    return this;
  }

  public String getImQuery() {
    return imQuery;
  }
  public Query setImQuery(String imQuery) {
    this.imQuery = imQuery;
    return this;
  }

  public Prefixes getPrefixes() {
    return prefixes;
  }
  public Query setPrefixes(Prefixes prefixes) {
    this.prefixes = prefixes;
    return this;
  }
  public Query addPrefix(String prefix, String namespace) {
    Prefix newPrefix = new Prefix().setPrefix(prefix).setNamespace(namespace);
    if (this.prefixes == null) {
      this.prefixes = new Prefixes();
    }
    prefixes.add(newPrefix);
    return this;
  }



  public Having getHaving() {
    return having;
  }

  public Query setHaving(Having having) {
    this.having = having;
    return this;
  }

  public Query having(Consumer<Having> builder) {
    Having having = new Having();
    setHaving(having);
    return this;
  }

  public Query getThen() {
    return then;
  }

  public Query setThen(Query then) {
    this.then = then;
    return this;
  }

  public Query then(Consumer<Query> builder) {
    this.then = new Query();
    builder.accept(this.then);
    return this;
  }

  public boolean isDraft() {
    return draft;
  }

  public Query setDraft(boolean draft) {
    this.draft = draft;
    return this;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Query setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  public boolean isNotExists() {
    return notExists;
  }

  public Query setNotExists(boolean notExists) {
    this.notExists = notExists;
    return this;
  }

  public Query rule(Consumer<Query> builder) {
    Query rule = new Query();
    addRule(rule);
    builder.accept(rule);
    return this;
  }

  @JsonGetter
  public boolean notExists() {
    return notExists;
  }

  public Node getTypeOf() {
    return typeOf;
  }

  @JsonSetter
  public Query setTypeOf(Node typeOf) {
    this.typeOf = typeOf;
    return this;
  }

  public Query setTypeOf(String type) {
    this.typeOf = new Node().setIri(type);
    return this;
  }

  public Where getWhere() {
    return where;
  }

  public Query setWhere(Where where) {
    this.where = where;
    return this;
  }

  public String getName() {
    return name;
  }

  public Query setName(String name) {
    this.name = name;
    return this;
  }

  public String getParameter() {
    return parameter;
  }

  public Query setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }


  @Override
  public List<Path> getPath() {
    return path;
  }

  public Query setPath(List<Path> path) {
    this.path = path;
    return this;
  }

  public FunctionClause getFunction() {
    return function;
  }

  public Query setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public Entail getEntailment() {
    return entailment;
  }

  public Query setEntailment(Entail entailment) {
    this.entailment = entailment;
    return this;
  }

  public RuleAction getIfTrue() {
    return ifTrue;
  }

  public Query setIfTrue(RuleAction ifTrue) {
    this.ifTrue = ifTrue;
    return this;
  }

  public RuleAction getIfFalse() {
    return ifFalse;
  }

  public Query setIfFalse(RuleAction ifFalse) {
    this.ifFalse = ifFalse;
    return this;
  }

  public boolean isBaseRule() {
    return baseRule;
  }

  public Query setBaseRule(boolean baseRule) {
    this.baseRule = baseRule;
    return this;
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

  public Query setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }

  public List<Query> getOr() {
    return or;
  }

  public Query setOr(List<Query> ors) {
    this.or = ors;
    return this;
  }

  public List<Query> getAnd() {
    return and;
  }

  public Query setAnd(List<Query> and) {
    this.and = and;
    return this;
  }

  public List<Query> getRule() {
    return rule;
  }

  public Query setRule(List<Query> rule) {
    this.rule = rule;
    return this;
  }

  public String getLibraryItem() {
    return libraryItem;
  }

  public Query setLibraryItem(String libraryItem) {
    this.libraryItem = libraryItem;
    return this;
  }

  public boolean isInvalid() {
    return invalid;
  }

  public void setInvalid(boolean invalid) {
    this.invalid = invalid;
  }

  public Node getIs() {
    return is;
  }

  @JsonSetter
  public Query setIs(Node is) {
    this.is = is;
    return this;
  }

  public String getAsDescription() {
    return asDescription;
  }

  public Query setAsDescription(String asDescription) {
    this.asDescription = asDescription;
    return this;
  }

  public OrderLimit getOrderBy() {
    return orderBy;
  }

  public Query setOrderBy(OrderLimit orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public Query orderBy(Consumer<OrderLimit> builder) {
    this.orderBy = new OrderLimit();
    builder.accept(this.orderBy);
    return this;
  }

  public List<GroupBy> getGroupBy() {
    return groupBy;
  }

  public Query setGroupBy(List<GroupBy> groupBy) {
    this.groupBy = groupBy;
    return this;
  }

  public Query addGroupBy(GroupBy group) {
    if (this.groupBy == null) this.groupBy = new ArrayList<>();
    this.groupBy.add(group);
    return this;
  }

  public Query groupBy(Consumer<GroupBy> builder) {
    GroupBy group = new GroupBy();
    addGroupBy(group);
    builder.accept(group);
    return this;
  }

  public Query where(Consumer<Where> builder) {
    if (this.where == null) {
      Where where = new Where();
      setWhere(where);
      builder.accept(where);
    } else builder.accept(null);
    return this;
  }

  public Query addRule(Query rule) {
    if (this.rule == null) {
      this.rule = new ArrayList<>();
    }
    this.rule.add(rule);
    return this;
  }

  public Query addOr(Query or) {
    if (this.or == null) {
      this.or = new ArrayList<>();
    }
    this.or.add(or);
    return this;
  }

  public Query or(Consumer<Query> builder) {
    Query or = new Query();
    addOr(or);
    builder.accept(or);
    return this;
  }

  public Query addAnd(Query and) {
    if (this.and == null) {
      this.and = new ArrayList<>();
    }
    this.and.add(and);
    return this;
  }

  public Query and(Consumer<Query> builder) {
    Query and = new Query();
    addAnd(and);
    builder.accept(and);
    return this;
  }

  @JsonGetter
  public List<Return> getReturn() {
    return returx;
  }

  @JsonSetter
  public Query setReturn(List<Return> returns) {
    this.returx = returns;
    return this;
  }

  public Query addReturn(Return returnx) {
    if (this.returx == null) {
      this.returx = new ArrayList<>();
    }
    this.returx.add(returnx);
    return this;
  }

  public Query return_(Consumer<Return> builder) {
    Return returx = new Return();
    addReturn(returx);
    builder.accept(returx);
    return this;
  }

  public Query function(Consumer<FunctionClause> builder) {
    FunctionClause function = new FunctionClause();
    this.function = function;
    builder.accept(function);
    return this;
  }

  public Query addPath(Path path) {
    if (this.path == null) {
      this.path = new ArrayList<>();
    }
    this.path.add(path);
    return this;
  }

  public Query path(Consumer<Path> builder) {
    Path path = new Path();
    this.addPath(path);
    builder.accept(path);
    return this;
  }

  @Override
  public Query setIri(String iri) {
    this.iri= iri;
    return this;
  }

  @JsonIgnore
  public Query is(Consumer<Node> builder) {
    this.is = new Node();
    builder.accept(is);
    return this;
  }

  public boolean isOptional() {
    return optional;
  }

  public Query setOptional(boolean optional) {
    this.optional = optional;
    return this;
  }

  public List<From> getFrom() {
    return from;
  }
  public Query setFrom(List<From> from) {
    this.from = from;
    return this;
  }
  public Query addFrom(From from) {
    if (this.from == null) this.from = new ArrayList<>();
    this.from.add(from);
    return this;
  }
  public Query from(Consumer<From> builder) {
    From from = new From();
    addFrom(from);
    builder.accept(from);
    return this;
  }

  public Node getGraph() {
    return graph;
  }

  public Query setGraph(Node graph) {
    this.graph = graph;
    return this;
  }

  public String getNode() {
    return node;
  }

  public Query setNode(String node) {
    this.node = node;
    return this;
  }

  public boolean isActiveOnly() {
    return activeOnly;
  }

  public Query setActiveOnly(boolean activeOnly) {
    this.activeOnly = activeOnly;
    return this;
  }
}
