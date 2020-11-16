package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(value={ "id" })   // TODO: Remove!!!
@JsonPropertyOrder({"dbid","status","version"})
public interface IMEntity{
    ConceptStatus getStatus();
    IMEntity setStatus(ConceptStatus status);
    Integer getVersion();
    IMEntity setVersion(Integer version);
    IMEntity setDbid(Integer dbid);
    Integer getDbid();
}

