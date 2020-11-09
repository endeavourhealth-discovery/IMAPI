package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(value={ "id" })   // TODO: Remove!!!
@JsonPropertyOrder({"status","version"})
public interface IMEntity{
    ConceptStatus getStatus();
    IMEntity setStatus(ConceptStatus status);
    Integer getVersion();
    IMEntity setVersion(Integer version);
}

