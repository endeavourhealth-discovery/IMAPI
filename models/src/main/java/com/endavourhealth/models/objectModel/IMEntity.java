package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","status","version"})
public interface IMEntity{
    ConceptStatus getStatus();
    IMEntity setStatus(ConceptStatus status);
    Integer getVersion();
    IMEntity setVersion(Integer version);
    IMEntity setId(String id);
    String getId();

}

