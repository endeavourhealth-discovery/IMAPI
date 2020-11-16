package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ClassExpression implements IMEntity{
    private Integer dbid;
    private Boolean inferred;
    private ConceptReference clazz;
    private List<ClassExpression> intersection;
    private List<ClassExpression> union;
    private ClassExpression complementOf;
    private ObjectPropertyValue objectPropertyValue;
    private DataPropertyValue dataPropertyValue;

    private List<ConceptReference> objectOneOf;



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

    public ClassExpression setClazz(ConceptReference clazz) {
        this.clazz = clazz;
        return this;
    }

    @JsonIgnore
    public ClassExpression setClazz(String clazz) {
        this.clazz = new ConceptReference(clazz);
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



    @JsonProperty("ObjectPropertyValue")
    public ObjectPropertyValue getObjectPropertyValue() {
        return objectPropertyValue;
    }

    public ClassExpression setObjectPropertyValue(ObjectPropertyValue propertyValue) {
        this.objectPropertyValue = propertyValue;
        return this;
    }


    @JsonProperty("DataPropertyValue")
    public DataPropertyValue getDataPropertyValue() {
        return dataPropertyValue;
    }

    public ClassExpression setDataPropertyValue(DataPropertyValue propertyValue) {
        this.dataPropertyValue = propertyValue;
        return this;
    }






    @JsonProperty("ObjectOneOf")
    public List<ConceptReference> getObjectOneOf() {
        return objectOneOf;
    }

    public ClassExpression setObjectOneOf(List<ConceptReference> objectOneOf) {
        this.objectOneOf = objectOneOf;
        return this;
    }

    public ClassExpression addObjectOneOf(ConceptReference oneOf) {
        if (this.objectOneOf==null)
            this.objectOneOf = new ArrayList<>();
        objectOneOf.add(oneOf);
        return this;
    }

    public ClassExpression addObjectOneOf(String oneOf) {
        if (this.objectOneOf==null)
            this.objectOneOf = new ArrayList<>();
        objectOneOf.add(new ConceptReference(oneOf));
        return this;
    }

    public Integer getDbid() {
        return dbid;
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

    public ClassExpression setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }
}
