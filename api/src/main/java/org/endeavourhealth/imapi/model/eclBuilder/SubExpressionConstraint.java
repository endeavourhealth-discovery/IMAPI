package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@Getter
public class SubExpressionConstraint {
    private ConceptReference concept;
    private String constraintOperator;
    private Boolean memberOf;

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

    public SubExpressionConstraint setMemberOf(Boolean memberOf) {
        this.memberOf = memberOf;
        return this;
    }
}
