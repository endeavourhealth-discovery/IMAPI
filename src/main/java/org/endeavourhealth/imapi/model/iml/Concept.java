package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Concept extends Entity {

  private String code;
  private String im1Id;
  private Set<TTIriRef> subClassOf;
  private Set<Concept> matchedFrom;
  private Integer usage;
  private String codeId;
  private String alternativeCode;
  private Set<TTIriRef> subsumedBy;

  public Concept setSubsumedBy(Set<TTIriRef> subsumedBy) {
    this.subsumedBy = subsumedBy;
    return this;
  }

  public Concept addSubsumedBy(TTIriRef subsumedBy) {
    if (this.subsumedBy == null)
      this.subsumedBy = new HashSet<>();
    this.subsumedBy.add(subsumedBy);
    return this;
  }

  public Concept setAlternativeCode(String alternativeCode) {
    this.alternativeCode = alternativeCode;
    return this;
  }

  public Concept setCodeId(String codeId) {
    this.codeId = codeId;
    return this;
  }

  public Concept setSubClassOf(Set<TTIriRef> subClassOf) {
    this.subClassOf = subClassOf;
    return this;
  }

  public Concept addSubClassOf(TTIriRef superClass) {
    if (this.subClassOf == null)
      this.subClassOf = new HashSet<>();
    this.subClassOf.add(superClass);
    return this;
  }

  public Concept setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Concept setUsage(Integer usage) {
    this.usage = usage;
    return this;
  }


  public Concept setName(String name) {
    super.setName(name);
    return this;
  }

  public Concept setDescription(String description) {
    super.setDescription(description);
    return this;
  }

  public Concept setCode(String code) {
    this.code = code;
    return this;
  }


  public Concept setScheme(TTIriRef scheme) {
    super.setScheme(scheme);
    return this;
  }

  public Concept setMatchedFrom(Set<Concept> matchedFrom) {
    this.matchedFrom = matchedFrom;
    return this;
  }

  public Concept addMatchedFrom(Concept legacy) {
    if (this.matchedFrom == null)
      this.matchedFrom = new HashSet<>();
    this.matchedFrom.add(legacy);
    return this;
  }

  public void setIm1Id(String im1Id) {
    this.im1Id = im1Id;
  }
}
