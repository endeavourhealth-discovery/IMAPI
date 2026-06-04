package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.EntityDocument;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EntityDocumentExtended extends EntityDocument {

  public Set<String> getBindingAsSet() {
    if (null == this.getBinding()) return new HashSet<>();
    return new HashSet<>(this.getBinding());
  }

  public EntityDocumentExtended binding(Set<String> binding) {
    this.setBinding(binding.stream().toList());
    return this;
  }

  public EntityDocumentExtended addBinding(String path, String node) {
    this.getBindingAsSet().add(path + " " + node);
    return this;
  }

  public EntityDocumentExtended alternativeCode(String alternativeCode) {
    this.setAlternativeCode(alternativeCode);
    return this;
  }

  public EntityDocumentExtended subsumptionCount(Integer subsumptionCount) {
    this.setSubsumptionCount(subsumptionCount);
    return this;
  }

  public EntityDocumentExtended length(Integer length) {
    this.setLength(length);
    return this;
  }

  public EntityDocumentExtended preferredName(String preferredName) {
    this.setPreferredName(preferredName);
    return this;
  }

  public Set<TTIriRefExtended> getIsAAsSet() {
    if (null == this.getIsA()) return new HashSet<>();
    List<TTIriRefExtended> result = this.getIsA().stream().map(a -> (TTIriRefExtended) a).toList();
    return new HashSet<>(result);
  }

  public EntityDocumentExtended isA(Set<TTIriRefExtended> isA) {
    this.setIsA(isA.stream().map(a -> (TTIriRef) a).toList());
    return this;
  }

  public EntityDocumentExtended id(Integer id) {
    this.setId(id);
    return this;
  }

  public EntityDocumentExtended iri(String iri) {
    this.setIri(iri);
    return this;
  }

  public EntityDocumentExtended name(String name) {
    this.setName(name);
    return this;
  }

  public EntityDocumentExtended code(String code) {
    this.setCode(code);
    return this;
  }

  @JsonSetter
  public EntityDocumentExtended scheme(TTIriRefExtended scheme) {
    this.setScheme(scheme);
    return this;
  }

  @JsonSetter
  public EntityDocumentExtended status(TTIriRefExtended status) {
    this.setStatus(status);
    return this;
  }

  public EntityDocumentExtended addType(TTIriRefExtended type) {
    this.getTypeAsSet().add(type);
    return this;
  }

  public EntityDocumentExtended usageTotal(Integer usageTotal) {
    this.setUsageTotal(usageTotal);
    return this;
  }

  public EntityDocumentExtended isDescendentOf(List<TTIriRef> isDescendentOf) {
    this.setIsDescendentOf(isDescendentOf);
    return this;
  }

  public Set<TTIriRefExtended> getTypeAsSet() {
    if (null == this.getType()) return new HashSet<>();
    return new HashSet<>(this.getType().stream().map(t -> (TTIriRefExtended) t).toList());
  }

  public EntityDocumentExtended type(Set<TTIriRef> type) {
    this.setType(type.stream().toList());
    return this;
  }


  public Set<SearchTermCode> getTermCodeAsSet() {
    return new HashSet<>(this.getTermCode());
  }

  public EntityDocumentExtended termCode(Set<SearchTermCode> searchTermCode) {
    this.setTermCode(searchTermCode.stream().toList());
    return this;
  }

  public EntityDocumentExtended addTermCode(String term, String code, TTIriRefExtended status, String keyTerm) {
    SearchTermCode tc = new SearchTermCode();
    tc.setTerm(term).setCode(code).setStatus(status);
    if (term != null)
      tc.setLength(term.length());
    if (keyTerm == null)
      keyTerm = term;
    if (keyTerm != null) {
      keyTerm = keyTerm.replaceAll("[ '()\\-_./,]", "").toLowerCase();
      keyTerm = keyTerm.substring(0, Math.min(keyTerm.length(), 30));
      tc.setKeyTerm(keyTerm);
    }
    this.getTermCodeAsSet().add(tc);
    return this;
  }

  public EntityDocumentExtended memberOf(Set<TTIriRefExtended> memberOf) {
    this.setMemberOf(memberOf.stream().map(m -> (TTIriRef) m).toList());
    return this;
  }

  public EntityDocumentExtended match(String match) {
    this.setMatch(match);
    return this;
  }

}
