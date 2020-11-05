package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ClassExpression {
    private Boolean inferred;
    private String clazz;
    private List<ClassExpression> intersection;
    private List<ClassExpression> union;
    private ClassExpression complementOf;
    private OPECardinalityRestriction propertyObject;
    private DPECardinalityRestriction propertyData;
    private List<String> objectOneOf;



    @JsonProperty("Inferred")
    public Boolean getInferred() {
        return inferred;
    }

    public ClassExpression setInferred(Boolean inferred) {
        this.inferred = inferred;
        return this;
    }

    @JsonProperty("Class")
    public String getClazz() {
        return clazz;
    }

    public ClassExpression setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    @JsonProperty("Intersection")
    public List<ClassExpression> getIntersection() {
        return intersection;
    }

    public ClassExpression setIntersection(List<ClassExpression> intersection) {
        this.intersection = intersection;
        return this;
    }

    public ClassExpression addIntersection(ClassExpression intersection) {
        if (this.intersection == null)
            this.intersection = new ArrayList<>();
        this.intersection.add(intersection);
        return this;
    }

    @JsonProperty("Union")
    public List<ClassExpression> getUnion() {
        return union;
    }

    public ClassExpression setUnion(List<ClassExpression> union) {
        this.union = union;
        return this;
    }

    public ClassExpression addUnion(ClassExpression union) {
        if (this.union == null)
            this.union= new ArrayList<>();
        this.union.add(union);
        return this;
    }

    @JsonProperty("ComplementOf")
    public ClassExpression getComplementOf() {
        return complementOf;
    }

    public ClassExpression setComplementOf(ClassExpression complementOf) {
        this.complementOf = complementOf;
        return this;
    }




    @JsonProperty("PropertyObject")
    public OPECardinalityRestriction getPropertyObject() {
        return propertyObject;
    }

    public ClassExpression setPropertyObject(OPECardinalityRestriction propertyObject) {
        this.propertyObject = propertyObject;
        return this;
    }




    @JsonProperty("PropertyData")
    public DPECardinalityRestriction getPropertyData() {
        return propertyData;
    }

    public ClassExpression setPropertyData(DPECardinalityRestriction propertyData) {
        this.propertyData = propertyData;
        return this;
    }

    @JsonProperty("ObjectOneOf")
    public List<String> getObjectOneOf() {
        return objectOneOf;
    }

    public ClassExpression setObjectOneOf(List<String> objectOneOf) {
        this.objectOneOf = objectOneOf;
        return this;
    }

    public ClassExpression addObjectOneOf(String oneOf) {
        if (this.objectOneOf==null)
            this.objectOneOf = new ArrayList<>();
        objectOneOf.add(oneOf);
        return this;
    }
}
