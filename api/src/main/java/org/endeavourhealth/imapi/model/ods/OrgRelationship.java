package org.endeavourhealth.imapi.model.ods;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"Status", "Target", "id"})
public class OrgRelationship {
    public String id;
    public String status;
    public OrgRelTarget target;

    public String getId() {
        return id;
    }

    public OrgRelationship setId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    public OrgRelationship setStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("Target")
    public OrgRelTarget getTarget() {
        return target;
    }

    public OrgRelationship setTarget(OrgRelTarget target) {
        this.target = target;
        return this;
    }
}
