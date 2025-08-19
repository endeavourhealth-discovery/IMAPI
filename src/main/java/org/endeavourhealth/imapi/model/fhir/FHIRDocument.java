package org.endeavourhealth.imapi.model.fhir;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FHIRDocument {
  private List<ValueSet> valueSets;
  private List<CodeSystem> codeSystems;

  public List<ValueSet> getValueSets() {
    return valueSets;
  }

  public FHIRDocument setValueSets(List<ValueSet> valueSets) {
    this.valueSets = valueSets;
    return this;
  }

  public FHIRDocument addValueSets(ValueSet valueSets) {
    if (this.valueSets == null) {
      this.valueSets = new ArrayList<>();
    }
    this.valueSets.add(valueSets);
    return this;
  }

  public FHIRDocument valueSets(Consumer<ValueSet> builder) {
    ValueSet vs = new ValueSet();
    addValueSets(vs);
    builder.accept(vs);
    return this;
  }


  public List<CodeSystem> getCodeSystems() {
    return codeSystems;
  }

  public FHIRDocument setCodeSystems(List<CodeSystem> codeSystems) {
    this.codeSystems = codeSystems;
    return this;
  }

  public FHIRDocument addCodeSystems(CodeSystem codeSystems) {
    if (this.codeSystems == null) {
      this.codeSystems = new ArrayList<>();
    }
    this.codeSystems.add(codeSystems);
    return this;
  }

  public FHIRDocument codeSystems(Consumer<CodeSystem> builder) {
    CodeSystem cs = new CodeSystem();
    addCodeSystems(cs);
    builder.accept(cs);
    return this;
  }
}
