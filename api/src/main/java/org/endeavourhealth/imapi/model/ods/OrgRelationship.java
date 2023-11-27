package org.endeavourhealth.imapi.model.ods;

public class OrgRelationship {
    public String rel;
    public String status;
    public String target;

    public String getRel() {
        return rel;
    }

    public OrgRelationship setRel(String rel) {
        this.rel = rel;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrgRelationship setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public OrgRelationship setTarget(String target) {
        this.target = target;
        return this;
    }
}
