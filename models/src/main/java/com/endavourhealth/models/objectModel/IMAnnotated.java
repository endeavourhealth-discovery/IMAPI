package com.endavourhealth.models.objectModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","status","version","annotations"})
public interface IMAnnotated extends IMEntity{
    List<Annotation> getAnnotations();
    IMAnnotated setAnnotations(List<Annotation> annotationList);
    IMAnnotated addAnnotation(Annotation annotation);
}
