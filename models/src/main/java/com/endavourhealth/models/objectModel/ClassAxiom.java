package com.endavourhealth.models.objectModel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassAxiom extends ClassExpression implements IMAnnotated{
    private String id;
    private ConceptStatus status;
    private Integer version;
    private List<Annotation> annotationList;

    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public IMEntity setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public IMEntity setVersion(Integer version) {
        this.version= version;
        return this;
    }

    @Override
    public IMEntity setId(String id) {
        this.id= id;
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    @JsonProperty("annotations")
    public List<Annotation> getAnnotations() {
        return annotationList;
    }

    @Override
    public IMAnnotated setAnnotations(List<Annotation> annotationList) {
        this.annotationList= annotationList;
        return this;
    }

    @Override
    public IMAnnotated addAnnotation(Annotation annotation) {
        if (annotationList==null)
            annotationList= new ArrayList<>();
        annotationList.add(annotation);
        return this;
    }
}
