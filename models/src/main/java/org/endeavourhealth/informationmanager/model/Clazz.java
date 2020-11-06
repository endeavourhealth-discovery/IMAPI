package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class Clazz extends Concept {
    private Set<ClassAxiom> subClassOf;
    private Set<ClassAxiom> equivalentTo;
    private ClassExpression expression;
    private Set<ConceptReference> disjointWithClass;

    @JsonProperty("DisjointWithClass")
    public Set<ConceptReference> getDisjointWithClass() {
        return disjointWithClass;
    }

    public Clazz setDisjointWithClass(Set<ConceptReference> disjointWithClass) {
        this.disjointWithClass = disjointWithClass;
        return this;
    }
    public Clazz addDisjointWithClass(ConceptReference iri) {
        if (this.disjointWithClass == null)
            this.disjointWithClass = new HashSet<>();
        this.disjointWithClass.add(iri);
        return this;
    }

    public Clazz addDisjointWithClass(String iri) {
        if (this.disjointWithClass == null)
            this.disjointWithClass = new HashSet<>();
        this.disjointWithClass.add(new ConceptReference(iri));
        return this;
    }

    @JsonProperty("Expression")
    public ClassExpression getExpression(){
        return expression;
    }
    public Clazz setExpression(ClassExpression cex){
        this.expression = cex;
        return this;
    }

    @JsonProperty("SubClassOf")
    public Set<ClassAxiom> getSubClassOf() {
        return subClassOf;
    }

    public Clazz setSubClassOf(Set<ClassAxiom> subClassOf) {
        this.subClassOf = subClassOf;
        return this;
    }

    public Clazz addSubClassOf(ClassAxiom subClassOf) {
        if (this.subClassOf == null)
            this.subClassOf = new HashSet<>();

        this.subClassOf.add(subClassOf);
        return this;
    }

    @JsonProperty("EquivalentTo")
    public Set<ClassAxiom> getEquivalentTo() {
        return equivalentTo;
    }

    public Clazz setEquivalentTo(Set<ClassAxiom> equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public Clazz addEquivalentTo(ClassAxiom equivalentTo) {
        if (this.equivalentTo == null)
            this.equivalentTo = new HashSet<>();
        this.equivalentTo.add(equivalentTo);
        return this;
    }
}
