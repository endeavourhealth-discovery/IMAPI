package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;

import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.function.Consumer;

@JsonPropertyOrder({"context", "textSearch", "argument", "referenceDate", "query", "pathQuery", "update"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class QueryRequest implements ContextMap {

  private String name;
  private Page page;
  private Map<String, String> context;
  private String textSearch;
  private List<Argument> argument;
  @JsonProperty(required = true)
  private Query query;
  private PathQuery pathQuery;
  private Update update;
  private String referenceDate;
  private String askIri;
  private List<Map<Long, String>> timings = new ArrayList<>();
  private List<TTIriRef> cohort;

  public List<TTIriRef> getCohort() {
    return cohort;
  }

  public QueryRequest setCohort(List<TTIriRef> cohort) {
    this.cohort = cohort;
    return this;
  }

  public QueryRequest addToCohort(TTIriRef cohort) {
    if (this.cohort == null) {
      this.cohort = new ArrayList<>();
    }
    this.cohort.add(cohort);
    return this;
  }


  public List<Map<Long, String>> getTimings() {
    return timings;
  }

  public QueryRequest setTimings(List<Map<Long, String>> timings) {
    this.timings = timings;
    return this;
  }

  public QueryRequest addTiming(String positiion) {
    long now = new Date().getTime();
    Map<Long, String> timingMap = new HashMap<>();
    timingMap.put(now, positiion);
    timings.add(timingMap);
    return this;
  }


  @JsonIgnore
  public TTContext getAsContext() {
    if (this.context == null)
      return null;
    TTContext ttContext = new TTContext();
    for (Map.Entry<String, String> prefix : this.context.entrySet()) {
      ttContext.add(prefix.getValue(), prefix.getKey());
    }
    return ttContext;
  }


  @JsonIgnore
  public QueryRequest setContext(TTContext context) {
    if (context == null)
      this.context = null;
    this.context = new HashMap<>();
    for (TTPrefix prefix : context.getPrefixes()) {
      this.context.put(prefix.getPrefix(), prefix.getIri());
    }
    return this;
  }


  public Update getUpdate() {
    return update;
  }

  public QueryRequest setUpdate(Update update) {
    this.update = update;
    return this;
  }

  public PathQuery getPathQuery() {
    return pathQuery;
  }

  public QueryRequest setPathQuery(PathQuery pathQuery) {
    this.pathQuery = pathQuery;
    return this;
  }

  public String getName() {
    return name;
  }

  public QueryRequest setName(String name) {
    this.name = name;
    return this;
  }

  public List<Argument> getArgument() {
    return argument;
  }

  @JsonSetter
  public QueryRequest setArgument(List<Argument> argument) {
    this.argument = argument;
    return this;
  }


  public QueryRequest addArgument(Argument argument) {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    this.argument.add(argument);
    return this;
  }

  public QueryRequest argument(Consumer<Argument> builder) {
    Argument argument = new Argument();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }

  public QueryRequest addArgument(String parameter, Object value) {
    Argument argument = new Argument();
    argument.setParameter(parameter);
    if (value instanceof String)
      argument.setValueData((String) value);
    else if (value instanceof TTIriRef)
      argument.setValueIri((TTIriRef) value);
    else
      throw new IllegalArgumentException("Using add argument this way must include a string value or TTIref value");
    addArgument(argument);
    return this;
  }

  public Object getArgumentDataValue(String parameter) {
    if (this.argument == null)
      return null;
    else {
      for (Argument arg : this.argument) {
        if (arg.getParameter().equals(parameter))
          return arg.getValueData();
      }
    }
    return null;

  }


  public String getReferenceDate() {
    return referenceDate;
  }

  public QueryRequest setReferenceDate(String referenceDate) {
    this.referenceDate = referenceDate;
    return this;
  }

  public Page getPage() {
    return page;
  }

  @JsonSetter
  public QueryRequest setPage(Page page) {
    this.page = page;
    return this;
  }

  public QueryRequest page(Consumer<Page> builder) {
    Page page = new Page();
    this.page = page;
    builder.accept(page);
    return this;
  }

  public String getTextSearch() {
    return textSearch;
  }

  public QueryRequest setTextSearch(String textSearch) {
    this.textSearch = textSearch;
    return this;
  }


  public Query getQuery() {
    return query;
  }

  @JsonSetter
  public QueryRequest setQuery(Query query) {
    this.query = query;
    return this;
  }

  public QueryRequest query(Consumer<Query> builder) {
    this.query = new Query();
    builder.accept(this.query);
    return this;
  }


  @Override
  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return this.context;
  }

  @Override
  @JsonSetter
  public ContextMap setContext(Map<String, String> prefixMap) {
    this.context = prefixMap;
    return this;
  }

  public QueryRequest setDefaultPrefixMap() {
    this.context = new HashMap<>();
    context.put(IM.NAMESPACE, "im");
    context.put(SNOMED.NAMESPACE, "sn");
    context.put(OWL.NAMESPACE, "owl");
    context.put(RDF.NAMESPACE, "rdf");
    context.put(RDFS.NAMESPACE, "rdfs");
    context.put(XSD.NAMESPACE, "xsd");
    context.put(SHACL.NAMESPACE, "sh");
    return this;
  }

  public String getAskIri() {
    return askIri;
  }

  public QueryRequest setAskIri(String askIri) {
    this.askIri = askIri;
    return this;
  }
}
