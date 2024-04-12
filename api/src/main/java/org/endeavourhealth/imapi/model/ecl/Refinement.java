package org.endeavourhealth.imapi.model.ecl;

import lombok.Getter;

@Getter
public class Refinement implements EclValue {
    private final String type;
    public Refinement() {
        this.type = "Refinement";
    }

    @Override
    public boolean isRefinement() {
        return true;
    }

    @Override
    public Refinement asRefinement() {
        return this;
    }
}
