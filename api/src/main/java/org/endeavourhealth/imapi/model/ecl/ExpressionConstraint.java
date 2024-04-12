package org.endeavourhealth.imapi.model.ecl;

import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.Bool;

import java.util.List;

@Getter
public class ExpressionConstraint implements EclValue {
    private final String type;
    private Bool conjunction;
    private List<EclValue> items;
    private Boolean attributeGroup;

    public ExpressionConstraint() {
        this.type = "ExpressionConstraint";
    }

    @Override
    public boolean isExpressionConstraint() {
        return true;
    }

    @Override
    public ExpressionConstraint asExpressionConstraint() {
        return this;
    }
}
