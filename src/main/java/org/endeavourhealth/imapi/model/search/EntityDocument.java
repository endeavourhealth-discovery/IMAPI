package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EntityDocument {
  Integer id;
  String iri;
  String name;
  Integer length;
  String preferredName;
  String code;
  String alternativeCode;
  TTIriRef scheme;
  Set<TTIriRef> entityType = new HashSet<>();
  TTIriRef status;
  Set<SearchTermCode> termCode = new HashSet<>();
  Integer usageTotal;
  String match;
  Set<TTIriRef> isA = new HashSet<>();
  Set<TTIriRef> memberOf = new HashSet<>();
  Integer subsumptionCount;
  Set<String> binding;
  private List<TTIriRef> isDescendentOf = new ArrayList<>();

  public Set<String> getBinding() {
    return binding;
  }

  public EntityDocument setBinding(Set<String> binding) {
    this.binding = binding;
    return this;
  }

  public EntityDocument addBinding(String path, String node) {
    if (this.binding == null) {
      this.binding = new HashSet<>();
    }
    this.binding.add(path + " " + node);
    return this;
  }

  public String getAlternativeCode() {
    return alternativeCode;
  }

  public EntityDocument setAlternativeCode(String alternativeCode) {
    this.alternativeCode = alternativeCode;
    return this;
  }

  public Integer getSubsumptionCount() {
    return subsumptionCount;
  }

  public EntityDocument setSubsumptionCount(Integer subsumptionCount) {
    this.subsumptionCount = subsumptionCount;
    return this;
  }

  public Integer getLength() {
    return length;
  }

  public EntityDocument setLength(Integer length) {
    this.length = length;
    return this;
  }

  public String getPreferredName() {
    return preferredName;
  }

  public EntityDocument setPreferredName(String preferredName) {
    this.preferredName = preferredName;
    return this;
  }

  public Set<TTIriRef> getIsA() {
    return isA;
  }

  public EntityDocument setIsA(Set<TTIriRef> isA) {
    this.isA = isA;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public EntityDocument setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public EntityDocument setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public EntityDocument setName(String name) {
    this.name = name;
    return this;
  }

  public String getCode() {
    return code;
  }

  public EntityDocument setCode(String code) {
    this.code = code;
    return this;
  }

  public TTIriRef getScheme() {
    return scheme;
  }

  @JsonSetter
  public EntityDocument setScheme(TTIriRef scheme) {
    this.scheme = scheme;
    return this;
  }

  public TTIriRef getStatus() {
    return status;
  }

  @JsonSetter
  public EntityDocument setStatus(TTIriRef status) {
    this.status = status;
    return this;
  }

  public EntityDocument addType(TTIriRef type) {
    this.entityType.add(type);
    return this;
  }

  public Integer getUsageTotal() {
    return usageTotal;
  }

  public EntityDocument setUsageTotal(Integer usageTotal) {
    this.usageTotal = usageTotal;
    return this;
  }


  public List<TTIriRef> getIsDescendentOf() {
    return isDescendentOf;
  }

  public EntityDocument setIsDescendentOf(List<TTIriRef> isDescendentOf) {
    this.isDescendentOf = isDescendentOf;
    return this;
  }

  public Set<TTIriRef> getEntityType() {
    return entityType;
  }

  public EntityDocument setEntityType(Set<TTIriRef> entityType) {
    this.entityType = entityType;
    return this;
  }


  public Set<SearchTermCode> getTermCode() {
    return termCode;
  }

  public EntityDocument setTermCode(Set<SearchTermCode> searchTermCode) {
    this.termCode = searchTermCode;
    return this;
  }

  public EntityDocument addTermCode(String term, String code, TTIriRef status) {

    SearchTermCode tc = new SearchTermCode();
    tc.setTerm(term).setCode(code).setStatus(status);
    this.termCode.add(tc);
    return this;
  }


  public Set<TTIriRef> getMemberOf() {
    return memberOf;
  }

  public EntityDocument setMemberOf(Set<TTIriRef> memberOf) {
    this.memberOf = memberOf;
    return this;
  }


  public String getMatch() {
    return match;
  }

  public EntityDocument setMatch(String match) {
    this.match = match;
    return this;
  }

}
