package org.endeavourhealth.imapi.model.ecl;

import lombok.Getter;

@Getter
public class RefinedExpressionConstraint implements EclValue {
    private final String type;
    private SubExpressionConstraint subExpressionConstraint;
    private Refinement refinement;

    public RefinedExpressionConstraint() {
        this.type = "RefinedExpressionConstraint";
    }

    public RefinedExpressionConstraint setSubExpressionConstraint(SubExpressionConstraint subExpressionConstraint) {
        this.subExpressionConstraint = subExpressionConstraint;
        return this;
    }

    public RefinedExpressionConstraint setRefinement(Refinement refinement) {
        this.refinement = refinement;
        return this;
    }

    @Override
    public boolean isRefinedExpressionConstraint() {
        return true;
    }

    @Override
    public RefinedExpressionConstraint asRefinedExpressionConstraint() {
        return this;
    }
}
