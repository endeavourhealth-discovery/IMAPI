package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class DataProperty extends Concept {
    private Set<PropertyAxiom> subDataPropertyOf;
    private Set<DataRangeAxiom> dataPropertyRange;
    private Set<ClassAxiom> propertyDomain;
    private Axiom isFunctional;

    @JsonProperty("IsFunctional")
    public Axiom getIsFunctional() {
        return isFunctional;
    }

    public void setIsFunctional(Axiom isFunctional) {
        this.isFunctional = isFunctional;
    }



    @JsonProperty("SubDataPropertyOf")
    public Set<PropertyAxiom> getSubDataPropertyOf() {
        return subDataPropertyOf;
    }

    public DataProperty setSubDataPropertyOf(Set<PropertyAxiom> subDataPropertyOf) {
        this.subDataPropertyOf = subDataPropertyOf;
        return this;
    }
    public DataProperty addSubDataPropertyOf(PropertyAxiom prop) {
        if (this.subDataPropertyOf == null)
            this.subDataPropertyOf = new HashSet<>();
        this.subDataPropertyOf.add(prop);

        return this;
    }


    @JsonProperty("DataPropertyRange")
    public Set<DataRangeAxiom> getDataPropertyRange() {
        return dataPropertyRange;
    }

    public DataProperty setDataPropertyRange(Set<DataRangeAxiom> dataPropertyRange) {
        this.dataPropertyRange = dataPropertyRange;
        return this;
    }
    public DataProperty addDataPropertyRange(DataRangeAxiom range) {
        if (this.dataPropertyRange == null)
            this.dataPropertyRange = new HashSet<>();
        this.dataPropertyRange.add(range);
        return this;
    }

    @JsonProperty("PropertyDomain")
    public Set<ClassAxiom> getPropertyDomain() {
        return propertyDomain;
    }

    public DataProperty setPropertyDomain(Set<ClassAxiom> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }
    public DataProperty addPropertyDomain(ClassAxiom domain) {
        if (this.propertyDomain == null)
            this.propertyDomain = new HashSet<>();
        this.propertyDomain.add(domain);

        return this;
    }
}
