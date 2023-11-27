package org.endeavourhealth.imapi.model.ods;

public class OdsResponse {
    private Organisation organisation;

    public Organisation getOrganisation() {
        return organisation;
    }

    public OdsResponse setOrganisation(Organisation organisation) {
        this.organisation = organisation;
        return this;
    }
}
