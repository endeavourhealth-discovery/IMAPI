package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.ConceptReference;

public class ValueSetMembership {
    private ConceptReference includedBy;
    private ConceptReference excludedBy;

    public ConceptReference getIncludedBy() {
        return includedBy;
    }

    public ValueSetMembership setIncludedBy(ConceptReference includedBy) {
        this.includedBy = includedBy;
        return this;
    }

    public ConceptReference getExcludedBy() {
        return excludedBy;
    }

    public ValueSetMembership setExcludedBy(ConceptReference excludedBy) {
        this.excludedBy = excludedBy;
        return this;
    }
}
