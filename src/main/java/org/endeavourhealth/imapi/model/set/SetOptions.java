package org.endeavourhealth.imapi.model.set;

import java.util.List;

public class SetOptions {
  private String setIri;
  private boolean includeDefinition;
  private boolean includeCore;
  private boolean includeLegacy;
  private boolean includeSubsets;
  private List<String> schemes;
  private boolean includeIM1id;
  private List<String> subsumptions;
  private String graph;

  public SetOptions() {
  }

  public SetOptions(String setIri, boolean includeDefinition, boolean includeCore, boolean includeLegacy, boolean includeSubsets, List<String> schemes,
                    List<String> subsumptions, String graph) {
    this.setIri = setIri;
    this.includeDefinition = includeDefinition;
    this.includeCore = includeCore;
    this.includeLegacy = includeLegacy;
    this.includeSubsets = includeSubsets;
    this.schemes = schemes;
    this.subsumptions = subsumptions;
    this.graph = graph;
  }

  public String getSetIri() {
    return setIri;
  }

  public List<String> getSchemes() {
    return schemes;
  }

  public List<String> getSubsumptions() {
    return subsumptions;
  }

  public String getGraph() {
    return graph;
  }

  public boolean includeDefinition() {
    return includeDefinition;
  }

  public boolean includeCore() {
    return includeCore;
  }

  public boolean includeLegacy() {
    return includeLegacy;
  }

  public boolean includeSubsets() {
    return includeSubsets;
  }

  public SetOptions setSetIri(String setIri) {
    this.setIri = setIri;
    return this;
  }

  public SetOptions setIncludeDefinition(boolean includeDefinition) {
    this.includeDefinition = includeDefinition;
    return this;
  }

  public SetOptions setIncludeCore(boolean includeCore) {
    this.includeCore = includeCore;
    return this;
  }

  public SetOptions setIncludeLegacy(boolean includeLegacy) {
    this.includeLegacy = includeLegacy;
    return this;
  }

  public SetOptions setIncludeSubsets(boolean includeSubsets) {
    this.includeSubsets = includeSubsets;
    return this;
  }

  public SetOptions setSchemes(List<String> schemes) {
    this.schemes = schemes;
    return this;
  }

  public boolean isIncludeIM1id() {
    return includeIM1id;
  }

  public SetOptions setIncludeIM1id(boolean includeIM1id) {
    this.includeIM1id = includeIM1id;
    return this;
  }

  public SetOptions setSubsumptions(List<String> subsumptions) {
    this.subsumptions = subsumptions;
    return this;
  }

  public SetOptions setGraph(String graph) {
    this.graph = graph;
    return this;
  }
}
