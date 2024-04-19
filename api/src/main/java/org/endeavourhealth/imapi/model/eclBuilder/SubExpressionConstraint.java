package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@Getter
public class SubExpressionConstraint {
    private ConceptReference concept;
    private String constraintOperator;

    public SubExpressionConstraint() {
    }

    public SubExpressionConstraint setConcept(ConceptReference concept) {
        this.concept = concept;
        return this;
    }

    public SubExpressionConstraint setConstraintOperator(String constraintOperator) {
        this.constraintOperator = constraintOperator;
        return this;
    }
}
