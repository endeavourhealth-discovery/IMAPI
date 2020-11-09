package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashSet;
import java.util.Set;

public class ClassExpression implements IMEntity{
    private Boolean inferred;
    private ConceptReference clazz;
    private Set<ClassExpression> intersection;
    private Set<ClassExpression> union;
    private ClassExpression complementOf;
    private OPECardinalityRestriction propertyObject;
    private DPECardinalityRestriction propertyData;
    private Set<ConceptReference> objectOneOf;



    @JsonProperty("Inferred")
    public Boolean getInferred() {
        return inferred;
    }

    public ClassExpression setInferred(Boolean inferred) {
        this.inferred = inferred;
        return this;
    }

    @JsonProperty("Class")
    public ConceptReference getClazz() {
        return clazz;
    }

    @JsonSetter
    public ClassExpression setClazz(ConceptReference clazz) {
        this.clazz = clazz;
        return this;
    }

    public ClassExpression setClazz(String clazz) {
        this.clazz = new ConceptReference(clazz);
        return this;
    }

    @JsonProperty("Intersection")
    public Set<ClassExpression> getIntersection() {
        return intersection;
    }

    public ClassExpression setIntersection(Set<ClassExpression> intersection) {
        this.intersection = intersection;
        return this;
    }

    public ClassExpression addIntersection(ClassExpression intersection) {
        if (this.intersection == null)
            this.intersection = new HashSet<>();
        this.intersection.add(intersection);
        return this;
    }

    @JsonProperty("Union")
    public Set<ClassExpression> getUnion() {
        return union;
    }

    public ClassExpression setUnion(Set<ClassExpression> union) {
        this.union = union;
        return this;
    }

    public ClassExpression addUnion(ClassExpression union) {
        if (this.union == null)
            this.union= new HashSet<>();
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
    public Set<ConceptReference> getObjectOneOf() {
        return objectOneOf;
    }

    public ClassExpression setObjectOneOf(Set<ConceptReference> objectOneOf) {
        this.objectOneOf = objectOneOf;
        return this;
    }

    public ClassExpression addObjectOneOf(ConceptReference oneOf) {
        if (this.objectOneOf==null)
            this.objectOneOf = new HashSet<>();
        objectOneOf.add(oneOf);
        return this;
    }

    public ClassExpression addObjectOneOf(String oneOf) {
        if (this.objectOneOf==null)
            this.objectOneOf = new HashSet<>();
        objectOneOf.add(new ConceptReference(oneOf));
        return this;
    }

    @Override
    public ConceptStatus getStatus() {
        return null;
    }

    @Override
    public IMEntity setStatus(ConceptStatus status) {
        return null;
    }

    @Override
    public Integer getVersion() {
        return null;
    }

    @Override
    public IMEntity setVersion(Integer version) {
        return null;
    }
}
