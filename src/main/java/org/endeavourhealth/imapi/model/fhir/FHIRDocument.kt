package org.endeavourhealth.imapi.model.fhir

class FHIRDocument {
  var valueSets: MutableList<ValueSet> = mutableListOf()
    private set
  var codeSystems: MutableList<CodeSystem> = mutableListOf()
    private set

  fun setValueSets(valueSets: MutableList<ValueSet>): FHIRDocument {
    this.valueSets = valueSets
    return this
  }

  fun valueSets(builder: (ValueSet) -> Unit): FHIRDocument {
    val vs = ValueSet().apply(builder)
    this.valueSets.add(vs)
    return this
  }


  fun setCodeSystems(codeSystems: MutableList<CodeSystem>): FHIRDocument {
    this.codeSystems = codeSystems
    return this
  }

  fun codeSystems(builder: (CodeSystem) -> Unit): FHIRDocument {
    val cs = CodeSystem().apply(builder)
    this.codeSystems.add(cs)
    return this
  }
}
