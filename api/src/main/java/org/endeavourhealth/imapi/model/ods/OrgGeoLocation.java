package org.endeavourhealth.imapi.model.ods;

public class OrgGeoLocation {
    private OrgLocation location;

    public OrgLocation getLocation() {
        return location;
    }

    public OrgGeoLocation setLocation(OrgLocation location) {
        this.location = location;
        return this;
    }
}
