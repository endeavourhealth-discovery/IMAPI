package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgRelTarget {
    private OrgId orgId;

    @JsonProperty("OrgId")
    public OrgId getOrgId() {
        return orgId;
    }

    public OrgRelTarget setOrgId(OrgId orgId) {
        this.orgId = orgId;
        return this;
    }
}
