package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.ECLSyntaxError;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"prefix", "iri", "name", "description", "activeOnly", "typeOf", "instanceOf", "and", "or", "not", "return", "construct", "query", "groupBy", "orderBy"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Query extends Match {
  private Prefixes prefixes;
  private String description;
  @Getter
  private boolean activeOnly;
  private List<Query> dataSet;
  private List<GroupBy> groupBy;
  private String iri;
  private String name;
  private boolean imQuery;
  private JsonNode parentResult;
  @Getter
  private TTIriRef persistentIri;


  public Query setRule(List<Match> rule) {
    super.setRule(rule);
    return this;
  }

  public Query addRule(Match rule) {
    super.addRule(rule);
    return this;
  }

  public Query setAnd(List<Match> and) {
    super.setAnd(and);
    return this;
  }

  public Query addAnd(Match and) {
    super.addAnd(and);
    return this;
  }

  public Query and(Consumer<Match> builder) {
    Match match = new Match();
    addAnd(match);
    builder.accept(match);
    return this;
  }

  public Query setNot(List<Match> not) {
    super.setNot(not);
    return this;
  }

  public Query addNot(Match not) {
    super.addNot(not);
    return this;
  }

  public Query not(Consumer<Match> builder) {
    Match match = new Match();
    addNot(match);
    builder.accept(match);
    return this;
  }

  public Query addInstanceOf(Node instanceOf) {
    super.addInstanceOf(instanceOf);
    return this;
  }

  public Query instanceOf(Consumer<Node> builder) {
    Node node = new Node();
    super.addInstanceOf(node);
    builder.accept(node);
    return this;
  }

  public Query setInstanceOf(List<Node> instanceOf) {
    super.setInstanceOf(instanceOf);
    return this;
  }

  public Query setOr(List<Match> or) {
    super.setOr(or);
    return this;
  }

  public Query addOr(Match or) {
    super.addOr(or);
    return this;
  }

  public Query or(Consumer<Match> builder) {
    Match match = new Match();
    addOr(match);
    builder.accept(match);
    return this;
  }

  public Query dataset(Consumer<Query> builder) {
    Query query = new Query();
    addDataSet(query);
    builder.accept(query);
    return this;
  }


  public Query setPath(List<Path> path) {
    super.setPath(path);
    return this;
  }

  public Query addPath(Path path) {
    super.addPath(path);
    return this;
  }

  public Query path(Consumer<Path> builder) {
    Path path = new Path();
    addPath(path);
    builder.accept(path);
    return this;
  }

  public Query setWhere(Where where) {
    super.setWhere(where);
    return this;
  }

  public Query where(Consumer<Where> builder) {
    super.where(builder);
    return this;
  }


  public Query setPersistentIri(TTIriRef persistentIri) {
    this.persistentIri = persistentIri;
    return this;
  }


  public Query function(Consumer<FunctionClause> builder) {
    FunctionClause function = new FunctionClause();
    super.setFunction(function);
    builder.accept(function);
    return this;
  }

  public JsonNode getParentResult() {
    return parentResult;
  }

  public Query setParentResult(JsonNode parentResult) {
    this.parentResult = parentResult;
    return this;
  }

  public boolean isImQuery() {
    return imQuery;
  }

  public Query setImQuery(boolean imQuery) {
    this.imQuery = imQuery;
    return this;
  }

  @Override
  public Query setVariable(String variable) {
    super.setVariable(variable);
    return this;
  }

  public Query setBaseRule(boolean baseRule) {
    super.setBaseRule(baseRule);
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


  public Query setTypeOf(String type) {
    super.setTypeOf(type);
    return this;
  }


  public Query setReturn(Return returx) {
    super.setReturn(returx);
    return this;
  }


  public Query return_(Consumer<Return> builder) {
    super.return_(builder);
    return this;
  }


  public String getDescription() {
    return description;
  }

  public Query setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public Query setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public Query setName(String name) {
    this.name = name;
    return this;
  }


  public Query setOrderBy(OrderLimit orderBy) {
    super.setOrderBy(orderBy);
    return this;
  }

  public Query orderBy(Consumer<OrderLimit> builder) {
    super.orderBy(builder);
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
    if (this.groupBy == null)
      this.groupBy = new ArrayList<>();
    this.groupBy.add(group);
    return this;
  }

  public Query groupBy(Consumer<GroupBy> builder) {
    GroupBy group = new GroupBy();
    addGroupBy(group);
    builder.accept(group);
    return this;
  }

  public List<Query> getDataSet() {
    return dataSet;
  }

  @JsonSetter
  public Query setDataSet(List<Query> dataSet) {
    this.dataSet = dataSet;
    return this;
  }

  public Query addDataSet(Query query) {
    if (this.dataSet == null)
      this.dataSet = new ArrayList<>();
    this.dataSet.add(query);
    return this;
  }

  public Query dataSet(Consumer<Query> builder) {
    Query query = new Query();
    addDataSet(query);
    builder.accept(query);
    return this;
  }


  public Query setActiveOnly(boolean activeOnly) {
    this.activeOnly = activeOnly;
    return this;
  }


}
