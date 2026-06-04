package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SearchResultSummary {
  @JsonProperty()
  private String name;
  @JsonProperty(value = "iri", required = true)
  private String iri;
  @JsonProperty()
  private String code;
  @JsonProperty()
  private String description;
  @JsonProperty(required = true)
  private TTIriRefExtended status;
  @JsonProperty(required = true)
  private TTIriRefExtended scheme;
  @JsonProperty(required = true)
  private Set<TTIriRefExtended> type = new HashSet<>();
  @JsonProperty(defaultValue = "0")
  private Integer usageTotal;
  @JsonProperty()
  private String bestMatch;
  private String preferredName;
  private Set<String> key;
  private Set<TTIriRefExtended> isA = new HashSet<>();
  Set<SearchTermCode> termCode = new HashSet<>();
  Set<TTIriRefExtended> unit;
  List<TTIriRefExtended> qualifier;

  public SearchResultSummary addTermCode(String term, String code, TTIriRefExtended status) {
    SearchTermCode tc = new SearchTermCode();
    tc.setTerm(term).setCode(code).setStatus(status);
    this.termCode.add(tc);
    return this;
  }

  public Set<TTIriRefExtended> getUnit() {
    return unit;
  }

  public SearchResultSummary setUnit(Set<TTIriRefExtended> unit) {
    this.unit = unit;
    return this;
  }

  public SearchResultSummary addIntervalUnit(TTIriRefExtended intervalUnit) {
    if (this.unit == null) {
      this.unit = new HashSet<>();
    }
    this.unit.add(intervalUnit);
    return this;
  }

  public SearchResultSummary intervalUnit(Consumer<TTIriRefExtended> builder) {
    TTIriRefExtended intervalUnit = new TTIriRefExtended();
    addIntervalUnit(intervalUnit);
    builder.accept(intervalUnit);
    return this;
  }


  public List<TTIriRefExtended> getQualifier() {
    return qualifier;
  }

  public SearchResultSummary setQualifier(List<TTIriRefExtended> qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  public SearchResultSummary addQualifier(TTIriRefExtended qualifier) {
    if (this.qualifier == null) {
      this.qualifier = new ArrayList<>();
    }
    this.qualifier.add(qualifier);
    return this;
  }

  public SearchResultSummary qualifier(Consumer<TTIriRefExtended> builder) {
    TTIriRefExtended qualifier = new TTIriRefExtended();
    addQualifier(qualifier);
    builder.accept(qualifier);
    return this;
  }


  public String getPreferredName() {
    return preferredName;
  }

  public SearchResultSummary setPreferredName(String preferredName) {
    this.preferredName = preferredName;
    return this;
  }

  public SearchResultSummary(String name, String iri, String code, String description, TTIriRefExtended status, TTIriRefExtended scheme, Set<TTIriRefExtended> entityTypes, Set<TTIriRefExtended> isDescendentOf, Integer usageTotal, String bestMatch) {
    this.name = name;
    this.iri = iri;
    this.code = code;
    this.description = description;
    this.status = status;
    this.scheme = scheme;
    this.type = entityTypes;
    this.isA = isDescendentOf;
    this.usageTotal = usageTotal;
    this.bestMatch = bestMatch;
  }

  public SearchResultSummary() {
  }

  public Set<TTIriRefExtended> getIsA() {
    return isA;
  }

  public SearchResultSummary setIsA(Set<TTIriRefExtended> isA) {
    this.isA = isA;
    return this;
  }

  public String getName() {
    return name;
  }

  public SearchResultSummary setName(String name) {
    this.name = name;
    return this;
  }

  @JsonSetter("name")
  public SearchResultSummary setNameFromJson(String name) {
    this.name = name;
    if (this.bestMatch == null)
      this.bestMatch = name;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public SearchResultSummary setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getCode() {
    return code;
  }

  public SearchResultSummary setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public SearchResultSummary setDescription(String description) {
    this.description = description;
    return this;
  }

  public TTIriRefExtended getStatus() {
    return status;
  }

  @JsonSetter
  public SearchResultSummary setStatus(TTIriRefExtended status) {
    this.status = status;
    return this;
  }

  public TTIriRefExtended getScheme() {
    return scheme;
  }

  @JsonSetter
  public SearchResultSummary setScheme(TTIriRefExtended scheme) {
    this.scheme = scheme;
    return this;
  }

  public Set<TTIriRefExtended> getType() {
    return type;
  }

  public SearchResultSummary setType(Set<TTIriRefExtended> type) {
    this.type = type;
    return this;
  }

  public SearchResultSummary addType(TTIriRefExtended entityType) {
    if (this.type == null)
      this.type = new HashSet<>();
    this.type.add(entityType);
    return this;
  }

  public Integer getUsageTotal() {
    return usageTotal;
  }

  public SearchResultSummary setUsageTotal(Integer usageTotal) {
    this.usageTotal = usageTotal;
    return this;
  }

  public String getBestMatch() {
    return bestMatch;
  }

  public SearchResultSummary setBestMatch(String bestMatch) {
    this.bestMatch = bestMatch;
    return this;
  }

  public Set<SearchTermCode> getTermCode() {
    return termCode;
  }

  public SearchResultSummary setTermCode(Set<SearchTermCode> searchTermCodes) {
    this.termCode = searchTermCodes;
    return this;
  }

  public Set<String> getKey() {
    return key;
  }

  public SearchResultSummary setKey(Set<String> key) {
    this.key = key;
    return this;
  }
}
