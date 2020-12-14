package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"conceptType","id","status","version","iri","name","description",
    "code","scheme","annotations","subClassOf",",equivalentTo","DisjointWith",
    "subDataPropertyOf","dataPropertyRange","propertyDomain","isFunctional"})

public class DataProperty extends Concept {
    private Set<PropertyAxiom> subDataPropertyOf;
    private Set<DataPropertyRange> dataPropertyRange;
    private Set<ClassExpression> propertyDomain;
    private Axiom isFunctional;

    public DataProperty(){
        super();
        this.setConceptType(ConceptType.DATAPROPERTY);
    }

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
    public Set<DataPropertyRange> getDataPropertyRange() {
        return dataPropertyRange;
    }

    public DataProperty setDataPropertyRange(Set<DataPropertyRange> dataPropertyRange) {
        this.dataPropertyRange = dataPropertyRange;
        return this;
    }
    public DataProperty addDataPropertyRange(DataPropertyRange range) {
        if (this.dataPropertyRange == null)
            this.dataPropertyRange = new HashSet<>();
        this.dataPropertyRange.add(range);
        return this;
    }

    @JsonProperty("PropertyDomain")
    public Set<ClassExpression> getPropertyDomain() {
        return propertyDomain;
    }

    public DataProperty setPropertyDomain(Set<ClassExpression> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }
    public DataProperty addPropertyDomain(ClassExpression domain) {
        if (this.propertyDomain == null)
            this.propertyDomain = new HashSet<>();
        this.propertyDomain.add(domain);

        return this;
    }
}
