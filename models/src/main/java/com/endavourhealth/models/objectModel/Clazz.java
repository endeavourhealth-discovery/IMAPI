package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Clazz extends Concept {
    private List<ClassAxiom> subClassOf;
    private List<ClassAxiom> equivalentTo;
    private ClassExpression expression;
    private List<String> disjointWithClass;

    @JsonProperty("DisjointWithClass")
    public List<String> getDisjointWithClass() {
        return disjointWithClass;
    }

    public Clazz setDisjointWithClass(List<String> disjointWithClass) {
        this.disjointWithClass = disjointWithClass;
        return this;
    }
    public Clazz addDisjointWithClass(String iri) {
        if (this.disjointWithClass == null)
            this.disjointWithClass = new ArrayList<>();
        this.disjointWithClass.add(iri);
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
    public List<ClassAxiom> getSubClassOf() {
        return subClassOf;
    }

    public Clazz setSubClassOf(List<ClassAxiom> subClassOf) {
        this.subClassOf = subClassOf;
        return this;
    }

    public Clazz addSubClassOf(ClassAxiom subClassOf) {
        if (this.subClassOf == null)
            this.subClassOf = new ArrayList<>();

        this.subClassOf.add(subClassOf);
        return this;
    }

    @JsonProperty("EquivalentTo")
    public List<ClassAxiom> getEquivalentTo() {
        return equivalentTo;
    }

    public Clazz setEquivalentTo(List<ClassAxiom> equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public Clazz addEquivalentTo(ClassAxiom equivalentTo) {
        if (this.equivalentTo == null)
            this.equivalentTo = new ArrayList<>();
        this.equivalentTo.add(equivalentTo);
        return this;
    }




}
