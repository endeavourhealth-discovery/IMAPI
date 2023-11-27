package org.endeavourhealth.imapi.model.ods;

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

    public String getStatus() {
        return status;
    }

    public OrgRole setStatus(String status) {
        this.status = status;
        return this;
    }
}
