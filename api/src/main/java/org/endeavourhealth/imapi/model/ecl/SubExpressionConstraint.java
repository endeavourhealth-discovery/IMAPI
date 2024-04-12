package org.endeavourhealth.imapi.model.ecl;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SubExpressionConstraint {
    private TTIriRef focusConcept;
    private ExpressionConstraint expressionConstraint;
    private String constraintOperator;
    private Boolean memberOf;
//    private List<MemberFilter> memberFilters;
//    private DescriptionFilterConstraint descriptionFilterConstraint;
//    private ConceptFilterConstraint conceptFilterConstraint;
//    private HistorySupplement historySupplement;

    public SubExpressionConstraint setFocusConcept(TTIriRef focusConcept) {
        this.focusConcept = focusConcept;
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

    public SubExpressionConstraint subExpressionConstraint(ExpressionConstraint expressionConstraint) {
        this.expressionConstraint = expressionConstraint;
        return this;
    }
}
