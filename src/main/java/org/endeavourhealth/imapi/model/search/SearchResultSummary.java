package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SearchResultSummary {
  @JsonProperty(defaultValue = "")
  private String name;
  @JsonProperty(value = "iri", required = true)
  private String iri;
  @JsonProperty(defaultValue = "")
  private String code;
  @JsonProperty(defaultValue = "")
  private String description;
  @JsonProperty(required = true)
  private TTIriRef status;
  @JsonProperty(required = true)
  private TTIriRef scheme;
  @JsonProperty(required = true)
  private Set<TTIriRef> type = new HashSet<>();
  @JsonProperty(defaultValue = "0")
  private Integer usageTotal;
  @JsonProperty(defaultValue = "")
  private String bestMatch;
  private String preferredName;
  private Set<String> key;
  private Set<TTIriRef> isA = new HashSet<>();
  Set<SearchTermCode> termCode = new HashSet<>();
  Set<TTIriRef> unit;
  List<TTIriRef> qualifier;

  public SearchResultSummary addTermCode(String term, String code, TTIriRef status) {
    SearchTermCode tc = new SearchTermCode();
    tc.setTerm(term).setCode(code).setStatus(status);
    this.termCode.add(tc);
    return this;
  }

  public Set<TTIriRef> getUnit() {
    return unit;
  }

  public SearchResultSummary setUnit(Set<TTIriRef> unit) {
    this.unit = unit;
    return this;
  }

  public SearchResultSummary addIntervalUnit(TTIriRef intervalUnit) {
    if (this.unit == null) {
      this.unit = new HashSet<>();
    }
    this.unit.add(intervalUnit);
    return this;
  }

  public SearchResultSummary intervalUnit(Consumer<TTIriRef> builder) {
    TTIriRef intervalUnit = new TTIriRef();
    addIntervalUnit(intervalUnit);
    builder.accept(intervalUnit);
    return this;
  }


  public List<TTIriRef> getQualifier() {
    return qualifier;
  }

  public SearchResultSummary setQualifier(List<TTIriRef> qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  public SearchResultSummary addQualifier(TTIriRef qualifier) {
    if (this.qualifier == null) {
      this.qualifier = new ArrayList<>();
    }
    this.qualifier.add(qualifier);
    return this;
  }

  public SearchResultSummary qualifier(Consumer<TTIriRef> builder) {
    TTIriRef qualifier = new TTIriRef();
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

  public SearchResultSummary(String name, String iri, String code, String description, TTIriRef status, TTIriRef scheme, Set<TTIriRef> entityTypes, Set<TTIriRef> isDescendentOf, Integer usageTotal, String bestMatch) {
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

  public Set<TTIriRef> getIsA() {
    return isA;
  }

  public SearchResultSummary setIsA(Set<TTIriRef> isA) {
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

  public TTIriRef getStatus() {
    return status;
  }

  @JsonSetter
  public SearchResultSummary setStatus(TTIriRef status) {
    this.status = status;
    return this;
  }

  public TTIriRef getScheme() {
    return scheme;
  }

  @JsonSetter
  public SearchResultSummary setScheme(TTIriRef scheme) {
    this.scheme = scheme;
    return this;
  }

  public Set<TTIriRef> getType() {
    return type;
  }

  public SearchResultSummary setType(Set<TTIriRef> type) {
    this.type = type;
    return this;
  }

  public SearchResultSummary addType(TTIriRef entityType) {
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
