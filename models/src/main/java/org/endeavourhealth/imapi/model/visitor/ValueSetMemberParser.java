package org.endeavourhealth.imapi.model.visitor;

import org.endeavourhealth.imapi.model.ClassExpression;
import org.endeavourhealth.imapi.model.ConceptReference;

import java.util.*;

public class ValueSetMemberParser  {
    public final List<ConceptReference> included = new ArrayList<>();
    public final List<ConceptReference> excluded = new ArrayList<>();

    private final Deque<Boolean> complementOfHistory = new ArrayDeque<>();
    private boolean visitingMembers = false;
    private boolean complementOf = false;

    public ValueSetMemberParser(ObjectModelVisitor visitor) {
        visitor.MemberListVisitor = (this::onEnterMemberList);
        visitor.MemberListExitVisitor = (this::onExitMemberList);

        visitor.ComplementOfVisitor = (this::onEnterComplementOf);
        visitor.ComplementOfExitVisitor = (this::onExitComplementOf);

        visitor.ClassVisitor = (this::onEnterClass);
    }

    public void onEnterMemberList(List<ClassExpression> members) {
        this.visitingMembers = true;
    }

    public void onExitMemberList(List<ClassExpression> members) {
        this.visitingMembers = false;
    }

    public void onEnterClass(ConceptReference clazz) {
        if(this.visitingMembers) {
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
