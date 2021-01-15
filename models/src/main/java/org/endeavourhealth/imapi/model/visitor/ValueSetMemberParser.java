package org.endeavourhealth.imapi.model.visitor;

import org.endeavourhealth.imapi.model.ClassExpression;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ObjectPropertyValue;

import java.util.*;

public class ValueSetMemberParser  {
    public final List<ConceptReference> included = new ArrayList<>();
    public final List<ConceptReference> excluded = new ArrayList<>();

    private ObjectPropertyValue hasMembersProperty;
    private Deque<Boolean> complementOfHistory = new ArrayDeque<>();
    private boolean complementOf = false;

    public ValueSetMemberParser(ObjectModelVisitor visitor) {
        visitor.ExpressionVisitor = (this::onEnterExpression);
        visitor.ExpressionExitVisitor = (this::onExitExpression);
        visitor.ObjectPropertyValueVisitor = (this::onEnterObjectPropertyValue);
        visitor.ObjectPropertyValueExitVisitor = (this::onExitObjectPropertyValue);
        visitor.ClassVisitor = (this::onEnterClass);
    }

    public void onEnterExpression(ClassExpression expression) {
        if (this.hasMembersProperty != null  && expression.getComplementOf() != null) {
            this.onEnterComplementOf(expression.getComplementOf());
        }
    }

    public void onExitExpression(ClassExpression expression) {
        if (this.hasMembersProperty != null && expression.getComplementOf() != null) {
            this.onExitComplementOf(expression.getComplementOf());
        }
    }

    public void onEnterObjectPropertyValue(ObjectPropertyValue objectPropertyValue) {
        if(":hasMembers".equals(objectPropertyValue.getProperty().getIri())) {
            if(this.hasMembersProperty == null) {
                this.hasMembersProperty = objectPropertyValue;
            }
            else {
                // TODO error time - nested hasMember within outer hasMember - illegal syntax
            }
        }
    }

    public void onExitObjectPropertyValue(ObjectPropertyValue objectPropertyValue) {
        if((this.hasMembersProperty != null) && (this.hasMembersProperty == objectPropertyValue)) {
            this.hasMembersProperty = null;
        }
    }

    public void onEnterClass(ConceptReference clazz) {
        if(this.hasMembersProperty != null) {
            if (this.complementOf)
                this.excluded.add(clazz);
            else
                this.included.add(clazz);
        }
    }

    private void onEnterComplementOf(ClassExpression expression) {
        // add the current complementOf state to the history incase
        // we need to process further Class definitions at that level
        this.complementOfHistory.push(this.complementOf);

        // we have now entered a new level with it's own complementOf
        // which is set to the opposite of the previous level
        this.complementOf = !this.complementOf;

    }

    private void onExitComplementOf(ClassExpression expression) {
        // we've now moved back up a level in the hasMembers defintion
        // therefore the previous complementOf setting applies
        this.complementOf = this.complementOfHistory.pop();
    }
}
