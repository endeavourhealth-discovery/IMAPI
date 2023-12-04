package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "Status"})
public class OrgRole {
    private String id;
    private String status;

    public String getId() {
        return id;
    }

    public OrgRole setId(String role) {
        this.id = role;
        return this;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    public OrgRole setStatus(String status) {
        this.status = status;
        return this;
    }
}
