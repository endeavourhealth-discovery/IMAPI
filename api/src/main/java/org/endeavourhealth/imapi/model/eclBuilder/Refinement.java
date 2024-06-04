package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;

@Getter
public class Refinement extends BuilderComponent{
    private String operator;
    private SubExpressionConstraint property;
    private SubExpressionConstraint value;
    public Refinement() {
        super("Refinement");
    }

    @Override
    public boolean isRefinement() {
        return true;
    }

    @Override
    public Refinement asRefinement() {
        return this;
    }

    public Refinement setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Refinement setProperty(SubExpressionConstraint property) {
        this.property = property;
        return this;
    }

    public Refinement setValue(SubExpressionConstraint value) {
        this.value = value;
        return this;
    }
}
