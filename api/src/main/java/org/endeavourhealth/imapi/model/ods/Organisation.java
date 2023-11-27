package org.endeavourhealth.imapi.model.ods;

public class Organisation {
    private String code;
    private String name;
    private String status;
    private String orgRecordClass;
    private OrgGeoLocation geoLoc;
    private OrgRelationships rels;
    private OrgRoles roles;

    public String getCode() {
        return code;
    }

    public Organisation setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Organisation setName(String name) {
        this.name = name;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Organisation setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getOrgRecordClass() {
        return orgRecordClass;
    }

    public Organisation setOrgRecordClass(String orgRecordClass) {
        this.orgRecordClass = orgRecordClass;
        return this;
    }

    public OrgGeoLocation getGeoLoc() {
        return geoLoc;
    }

    public Organisation setGeoLoc(OrgGeoLocation geoLoc) {
        this.geoLoc = geoLoc;
        return this;
    }

    public OrgRelationships getRels() {
        return rels;
    }

    public Organisation setRels(OrgRelationships rels) {
        this.rels = rels;
        return this;
    }

    public OrgRoles getRoles() {
        return roles;
    }

    public Organisation setRoles(OrgRoles roles) {
        this.roles = roles;
        return this;
    }
}
