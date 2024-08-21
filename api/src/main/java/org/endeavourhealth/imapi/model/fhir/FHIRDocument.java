package org.endeavourhealth.imapi.model.fhir;

import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FHIRDocument {
  private String graph;
  private List<ValueSet> valueSets;
  private List<CodeSystem> codeSystems;

  public List<ValueSet> getValueSets() {
    return valueSets;
  }

  public FHIRDocument setValueSets(List<ValueSet> valueSets) {
    this.valueSets = valueSets;
    return this;
  }
  public FHIRDocument addValueSets (ValueSet valueSets){
      if (this.valueSets == null) {
        this.valueSets = new ArrayList<>();
      }
      this.valueSets.add(valueSets);
      return this;
    }
    public FHIRDocument valueSets (Consumer< ValueSet > builder) {
      ValueSet valueSets = new ValueSet();
      addValueSets(valueSets);
      builder.accept(valueSets);
      return this;
    }


  public List<CodeSystem> getCodeSystems() {
    return codeSystems;
  }

  public FHIRDocument setCodeSystems(List<CodeSystem> codeSystems) {
    this.codeSystems = codeSystems;
    return this;
  }
  public FHIRDocument addCodeSystems (CodeSystem codeSystems){
      if (this.codeSystems == null) {
        this.codeSystems = new ArrayList<>();
      }
      this.codeSystems.add(codeSystems);
      return this;
    }
  public FHIRDocument codeSystems(Consumer <CodeSystem> builder) {
      CodeSystem codeSystems = new CodeSystem();
      addCodeSystems(codeSystems);
      builder.accept(codeSystems);
      return this;
    }


  public String getGraph() {
    return graph;
  }

  public FHIRDocument setGraph(String graph) {
    this.graph = graph;
    return this;
  }


}
