package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class Axiom implements IMAnnotated{
    private Integer dbid;
    private ConceptStatus status;
    private Integer version;
    private Set<Annotation> annotationList;

    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public Axiom setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Axiom setVersion(Integer version) {
        this.version= version;
        return this;
    }

    @Override
    public Axiom setDbid(Integer dbid) {
        this.dbid= dbid;
        return this;
    }

    @Override
    public Integer getDbid() {
        return dbid;
    }

    @Override
    @JsonProperty("annotations")
    public Set<Annotation> getAnnotations() {
        return annotationList;
    }

    @Override
    public IMAnnotated setAnnotations(Set<Annotation> annotationList) {
        this.annotationList= annotationList;
        return this;
    }

    @Override
    public IMAnnotated addAnnotation(Annotation annotation) {
        if (annotationList==null)
            annotationList= new HashSet<>();
        annotationList.add(annotation);
        return this;
    }
}
