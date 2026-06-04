package org.endeavourhealth.imapi.model.requests;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.interfacemanager.model.DatabaseOption;
import org.endeavourhealth.interfacemanager.model.NamespaceVocab;
import org.endeavourhealth.interfacemanager.model.TextSearchStyle;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@JsonPropertyOrder({"context", "textSearch", "argument", "query", "pathQuery", "update"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
public class QueryRequest implements ContextMap {
  private String name;
  private Page page;
  private Map<String, String> context;
  private String textSearch;
  private Set<ArgumentExtended> argument;
  @JsonProperty(required = true)
  private Query query;
  private PathQuery pathQuery;
  private Update update;
  @Getter
  private String queryStringDefinition;
  private String askIri;
  private List<Map<Long, String>> timings = new ArrayList<>();
  private Set<TTIriRefExtended> cohort;
  @Setter
  private boolean includeNames;
  @Setter
  private TextSearchStyle textSearchStyle;
  @Getter
  private DatabaseOption language;

  public QueryRequest() {
  }

  public QueryRequest setCohort(Set<TTIriRefExtended> cohort) {
    this.cohort = cohort;
    return this;
  }

  public QueryRequest addToCohort(TTIriRefExtended cohort) {
    if (this.cohort == null) {
      this.cohort = new HashSet<>();
    }
    this.cohort.add(cohort);
    return this;
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

  public Update getUpdate() {
    return update;
  }

  public QueryRequest setUpdate(Update update) {
    this.update = update;
    return this;
  }

  public QueryRequest setPathQuery(PathQuery pathQuery) {
    this.pathQuery = pathQuery;
    return this;
  }

  public QueryRequest setName(String name) {
    this.name = name;
    return this;
  }

  @JsonSetter
  public QueryRequest setArgument(Set<ArgumentExtended> argument) {
    this.argument = argument;
    return this;
  }

  public QueryRequest addArgument(ArgumentExtended argument) {
    if (this.argument == null)
      this.argument = new HashSet<>();
    this.argument.add(argument);
    return this;
  }

  public QueryRequest argument(Consumer<ArgumentExtended> builder) {
    ArgumentExtended argument = new ArgumentExtended();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }

  public QueryRequest addArgument(String parameter, Object value) {
    ArgumentExtended argument = new ArgumentExtended();
    argument.setParameter(parameter);
    if (value instanceof String)
      argument.setValueData((String) value);
    else if (value instanceof TTIriRefExtended)
      argument.setValueIri((TTIriRefExtended) value);
    else
      throw new IllegalArgumentException("Using add argument this way must include a string value or TTIref value");
    addArgument(argument);
    return this;
  }

  public Object getArgumentDataValue(String parameter) {
    if (this.argument == null)
      return null;
    else {
      for (ArgumentExtended arg : this.argument) {
        if (arg.getParameter().equals(parameter))
          return arg.getValueData();
      }
    }
    return null;

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
  public Map<String, String> getContext() {
    return this.context;
  }

  @JsonIgnore
  public QueryRequest setContext(TTContext context) {
    this.context = new HashMap<>();
    for (TTPrefix prefix : context.getPrefixes()) {
      this.context.put(prefix.getPrefix(), prefix.getIri());
    }
    return this;
  }

  @Override
  @JsonSetter
  public ContextMap setContext(Map<String, String> prefixMap) {
    this.context = prefixMap;
    return this;
  }

  public QueryRequest setDefaultPrefixMap() {
    this.context = new HashMap<>();
    context.put(NamespaceVocab.IM.toString(), "im");
    context.put(NamespaceVocab.SNOMED.toString(), "sn");
    context.put(NamespaceVocab.OWL.toString(), "owl");
    context.put(NamespaceVocab.RDF.toString(), "rdf");
    context.put(NamespaceVocab.RDFS.toString(), "rdfs");
    context.put(NamespaceVocab.XSD.toString(), "xsd");
    context.put(NamespaceVocab.SHACL.toString(), "sh");
    return this;
  }

  public QueryRequest setAskIri(String askIri) {
    this.askIri = askIri;
    return this;
  }

  public QueryRequest setLanguage(DatabaseOption language) {
    this.language = language;
    return this;
  }

  @Override
  public int hashCode() {
    resolveArgs();
    StringBuilder hs = new StringBuilder();
    for (ArgumentExtended arg : argument) {
      String argumentString = arg.getHashString();
      hs.append(argumentString);
    }
    if (null != query.getIri()) hs.append(query.getIri());
    return Objects.hash(hs.toString()); // add update datetime
  }

  public QueryRequest setQueryStringDefinition(String queryStringDefinition) {
    this.queryStringDefinition = queryStringDefinition;
    return this;
  }

  public void resolveArgs() {
    if (this.argument == null) this.argument = new HashSet<>();
    List<String> defaultDates = List.of("$searchDate", "$achievementDate");
    for (String date : defaultDates) {
      boolean hasDate = this.argument.stream()
        .anyMatch(arg -> date.equals(arg.getParameter()));
      if (!hasDate)
        this.argument.add(new ArgumentExtended().parameter(date).valueData(LocalDate.now().toString()));
    }
  }
}
