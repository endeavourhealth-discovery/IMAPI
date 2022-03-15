package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ValueSetMembership {
    private TTIriRef includedBy;
    private TTIriRef excludedBy;

    public TTIriRef getIncludedBy() {
        return includedBy;
    }

    public ValueSetMembership setIncludedBy(TTIriRef includedBy) {
        this.includedBy = includedBy;
        return this;
    }

    public TTIriRef getExcludedBy() {
        return excludedBy;
    }

    public ValueSetMembership setExcludedBy(TTIriRef excludedBy) {
        this.excludedBy = excludedBy;
        return this;
    }
}
