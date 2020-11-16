package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"conceptType","id","status","version","iri","name","description",
    "code","scheme","annotations","subClassOf",",equivalentTo","DisjointWith",
    "subAnnotationPropertyOf","propertyRange"})
public class AnnotationProperty extends Concept {
    private Set<PropertyAxiom> subAnnotationPropertyOf;
    private Set<AnnotationPropertyRangeAxiom> propertyRange;

    public AnnotationProperty(){
        this.setConceptType(ConceptType.ANNOTATION);
    }

    @JsonProperty("SubAnnotationPropertyOf")
    public Set<PropertyAxiom> getSubAnnotationPropertyOf() {
        return subAnnotationPropertyOf;
    }

    public AnnotationProperty setSubAnnotationPropertyOf(Set<PropertyAxiom> subAnnotationPropertyOf) {
        this.subAnnotationPropertyOf = subAnnotationPropertyOf;
        return this;
    }

    public AnnotationProperty addSubAnnotationPropertyOf(PropertyAxiom sub) {
        if (this.subAnnotationPropertyOf == null)
            this.subAnnotationPropertyOf = new HashSet<>();

        this.subAnnotationPropertyOf.add(sub);
        return this;
    }

    @JsonProperty("PropertyRange")
    public Set<AnnotationPropertyRangeAxiom> getPropertyRange() {
        return propertyRange;
    }

    public AnnotationProperty setPropertyRange(Set<AnnotationPropertyRangeAxiom> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
    public AnnotationProperty addPropertyRange(AnnotationPropertyRangeAxiom range) {
        if (this.propertyRange == null)
            this.propertyRange = new HashSet<>();
        this.propertyRange.add(range);

        return this;
    }
}
