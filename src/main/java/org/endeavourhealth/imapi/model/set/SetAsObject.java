package org.endeavourhealth.imapi.model.set;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.Set;

public class SetAsObject {
  private String iri;
  private String name;
  private TTArray included;
  private Set<TTIriRefExtended> subsets;

  public SetAsObject(String iri, String name, TTArray included, Set<TTIriRefExtended> subsets) {
    this.iri = iri;
    this.name = name;
    this.included = included;
    this.subsets = subsets;
  }

  public SetAsObject() {
  }

  public TTArray getIncluded() {
    return included;
  }

  public SetAsObject setIncluded(TTArray included) {
    this.included = included;
    return this;
  }

  public Set<TTIriRefExtended> getSubsets() {
    return subsets;
  }

  public SetAsObject setSubsets(Set<TTIriRefExtended> subsets) {
    this.subsets = subsets;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public void setIri(String iri) {
    this.iri = iri;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
