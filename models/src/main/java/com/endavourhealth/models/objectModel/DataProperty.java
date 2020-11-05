package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataProperty extends Concept {
    private List<PropertyAxiom> subDataPropertyOf;
    private List<PropertyRangeAxiom> dataPropertyRange;
    private List<ClassAxiom> propertyDomain;
    private Axiom isFunctional;

    @JsonProperty("IsFunctional")
    public Axiom getIsFunctional() {
        return isFunctional;
    }

    public void setIsFunctional(Axiom isFunctional) {
        this.isFunctional = isFunctional;
    }



    @JsonProperty("SubDataPropertyOf")
    public List<PropertyAxiom> getSubDataPropertyOf() {
        return subDataPropertyOf;
    }

    public DataProperty setSubDataPropertyOf(List<PropertyAxiom> subDataPropertyOf) {
        this.subDataPropertyOf = subDataPropertyOf;
        return this;
    }
    public DataProperty addSubDataPropertyOf(PropertyAxiom prop) {
        if (this.subDataPropertyOf == null)
            this.subDataPropertyOf = new ArrayList<>();
        this.subDataPropertyOf.add(prop);

        return this;
    }


    @JsonProperty("DataPropertyRange")
    public List<PropertyRangeAxiom> getDataPropertyRange() {
        return dataPropertyRange;
    }

    public DataProperty setDataPropertyRange(List<PropertyRangeAxiom> dataPropertyRange) {
        this.dataPropertyRange = dataPropertyRange;
        return this;
    }
    public DataProperty addDataPropertyRange(PropertyRangeAxiom range) {
        if (this.dataPropertyRange == null)
            this.dataPropertyRange = new ArrayList<>();
        this.dataPropertyRange.add(range);
        return this;
    }

    @JsonProperty("PropertyDomain")
    public List<ClassAxiom> getPropertyDomain() {
        return propertyDomain;
    }

    public DataProperty setPropertyDomain(List<ClassAxiom> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }
    public DataProperty addPropertyDomain(ClassAxiom domain) {
        if (this.propertyDomain == null)
            this.propertyDomain = new ArrayList<>();
        this.propertyDomain.add(domain);

        return this;
    }
}
