package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"conceptType","id","status","version","iri","name","description",
    "code","scheme","annotations","subClassOf",",equivalentTo","DisjointWith",
    "subObjectPropertyOf","inversePropertyOf","objectPropertyRange","propertyDomain",
    "subPropertyChain","isFunctional","isSymmetric","isTransitive","isReflexive"})
public class ObjectProperty extends Concept {
    private Set<PropertyAxiom> subObjectPropertyOf;
    private PropertyAxiom inversePropertyOf;
    private Set<ClassExpression> objectPropertyRange;
    private Set<ClassExpression> propertyDomain;
    private Set<SubPropertyChain> subPropertyChain;
    private Axiom isFunctional;
    private Axiom isSymmetric;
    private Axiom isTransitive;
    private Axiom isReflexive;

    public ObjectProperty(){
        this.setConceptType(ConceptType.OBJECTPROPERTY);
    }

    @JsonProperty("IsFunctional")
    public Axiom getIsFunctional() {
        return isFunctional;
    }

    public void setIsFunctional(Axiom isFunctional) {
        this.isFunctional = isFunctional;
    }

    @JsonProperty("IsSymmetric")
    public Axiom getIsSymmetric() {
        return isSymmetric;
    }

    public void setIsSymmetric(Axiom isSymmetric) {
        this.isSymmetric = isSymmetric;
    }
    @JsonProperty("IsTransitive")
    public Axiom getIsTransitive() {
        return isTransitive;
    }

    public void setIsTransitive(Axiom isTransitive) {
        this.isTransitive = isTransitive;
    }

    @JsonProperty("IsReflexive")
    public Axiom getIsReflexive() {
        return isReflexive;
    }

    public void setIsReflexive(Axiom isReflexive) {
        this.isReflexive = isReflexive;
    }

    @JsonProperty("SubObjectPropertyOf")
    public Set<PropertyAxiom> getSubObjectPropertyOf() {
        return subObjectPropertyOf;
    }

    public ObjectProperty setSubObjectPropertyOf(Set<PropertyAxiom> subObjectPropertyOf) {
        this.subObjectPropertyOf = subObjectPropertyOf;
        return this;
    }

    public ObjectProperty addSubObjectPropertyOf(PropertyAxiom subObjectPropertyOf) {
        if (this.subObjectPropertyOf == null)
            this.subObjectPropertyOf = new HashSet<>();
        this.subObjectPropertyOf.add(subObjectPropertyOf);

        return this;
    }

    @JsonProperty("InversePropertyOf")
    public PropertyAxiom getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ObjectProperty setInversePropertyOf(PropertyAxiom inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    @JsonProperty("ObjectPropertyRange")
    public Set<ClassExpression> getObjectPropertyRange() {
        return objectPropertyRange;
    }

    public ObjectProperty setObjectPropertyRange(Set<ClassExpression> propertyRange) {
        this.objectPropertyRange = propertyRange;
        return this;
    }
    public ObjectProperty addObjectPropertyRange(ClassExpression range) {
        if (this.objectPropertyRange == null)
            this.objectPropertyRange = new HashSet<>();
        this.objectPropertyRange.add(range);

        return this;
    }

    @JsonProperty("PropertyDomain")
    public Set<ClassExpression> getPropertyDomain() {
        return propertyDomain;
    }

    public ObjectProperty setPropertyDomain(Set<ClassExpression> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }
    public ObjectProperty addPropertyDomain(ClassExpression domain) {
        if (this.propertyDomain == null)
            this.propertyDomain = new HashSet<>();
        this.propertyDomain.add(domain);

        return this;
    }



    @JsonProperty("SubPropertyChain")
    public Set<SubPropertyChain> getSubPropertyChain() {
        return subPropertyChain;
    }

    public ObjectProperty setSubPropertyChain(Set<SubPropertyChain> subPropertyChain) {
        this.subPropertyChain = subPropertyChain;
        return this;
    }

    public ObjectProperty addSubPropertyChain(SubPropertyChain subPropertyChain) {
        if (this.subPropertyChain == null)
            this.subPropertyChain = new HashSet<>();

        this.subPropertyChain.add(subPropertyChain);
        return this;
    }



}
