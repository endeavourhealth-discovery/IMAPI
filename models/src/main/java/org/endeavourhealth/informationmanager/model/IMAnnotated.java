package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Set;

@JsonPropertyOrder({"id","status","version","annotations"})
public interface IMAnnotated extends IMEntity{
    Set<Annotation> getAnnotations();
    IMAnnotated setAnnotations(Set<Annotation> annotationList);
    IMAnnotated addAnnotation(Annotation annotation);
}
