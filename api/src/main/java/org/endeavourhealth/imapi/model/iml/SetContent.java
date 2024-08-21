package org.endeavourhealth.imapi.model.iml;

import java.util.Set;


public class SetContent {
  private String name;
  private String description;
  private String status;
  private int version;
  private String setDefinition;
  private Set<String> subsets;
  private Set<Concept> concepts;

  public String getName() {
    return name;
  }

  public SetContent setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public SetContent setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public SetContent setStatus(String status) {
    this.status = status;
    return this;
  }

  public int getVersion() {
    return version;
  }

  public SetContent setVersion(int version) {
    this.version = version;
    return this;
  }

  public String getSetDefinition() {
    return setDefinition;
  }

  public SetContent setSetDefinition(String setDefinition) {
    this.setDefinition = setDefinition;
    return this;
  }

  public Set<String> getSubsets() {
    return subsets;
  }

  public SetContent setSubsets(Set<String> subsets) {
    this.subsets = subsets;
    return this;
  }

  public Set<Concept> getConcepts() {
    return concepts;
  }

  public SetContent setConcepts(Set<Concept> concepts) {
    this.concepts = concepts;
    return this;
  }
}
