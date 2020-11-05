package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AnnotationProperty extends Concept {
    private List<PropertyAxiom> subAnnotationPropertyOf;
    private List<AnnotationPropertyRangeAxiom> propertyRange;

    @JsonProperty("SubAnnotationPropertyOf")
    public List<PropertyAxiom> getSubAnnotationPropertyOf() {
        return subAnnotationPropertyOf;
    }

    public AnnotationProperty setSubAnnotationPropertyOf(List<PropertyAxiom> subAnnotationPropertyOf) {
        this.subAnnotationPropertyOf = subAnnotationPropertyOf;
        return this;
    }

    public AnnotationProperty addSubAnnotationPropertyOf(PropertyAxiom sub) {
        if (this.subAnnotationPropertyOf == null)
            this.subAnnotationPropertyOf = new ArrayList<>();

        this.subAnnotationPropertyOf.add(sub);
        return this;
    }

    @JsonProperty("PropertyRange")
    public List<AnnotationPropertyRangeAxiom> getPropertyRange() {
        return propertyRange;
    }

    public AnnotationProperty setPropertyRange(List<AnnotationPropertyRangeAxiom> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
    public AnnotationProperty addPropertyRange(AnnotationPropertyRangeAxiom range) {
        if (this.propertyRange == null)
            this.propertyRange = new ArrayList<>();
        this.propertyRange.add(range);

        return this;
    }
}
