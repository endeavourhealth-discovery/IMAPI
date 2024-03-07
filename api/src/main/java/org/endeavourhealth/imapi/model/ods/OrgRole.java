package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "Status", "code", "displayName"})
public class OrgRole {
    private String id;
    private String status;
    private String code;
    private String displayName;

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

    public String getCode() {
        return code;
    }

    public OrgRole setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public OrgRole setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}
