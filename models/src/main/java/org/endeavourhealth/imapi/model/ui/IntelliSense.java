package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTTuple;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.util.Stack;

public class IntelliSense {
    public interface IntelliSenseAction { void execute(String action, Stack<TTTuple> stack); }

    public void evaluate(IntelliSenseRequest request, IntelliSenseAction action) {
        TTValue v = request.getConcept();
        TTIriRef p = null;
        Stack<TTTuple> stack = new Stack<>();
        stack.push(new TTTuple(null, v));

        for (TTValue i : request.getIndex()) {
            if (v.isLiteral() || v.isIriRef())
                throw new IllegalStateException("Something went wrong!");
            else {
                if (v.isNode() && i.isIriRef()) {
                    p = i.asIriRef();
                    v = v.asNode().get(i.asIriRef());
                    stack.add(new TTTuple(p, v));
                } else if (v.isList() && i.isLiteral()) {
                    v = v.asArray().get(Integer.parseInt(i.asLiteral().getValue()));
                    stack.add(new TTTuple(p, v));
                } else {
                    throw new IllegalStateException("Something went wrong!");
                }
            }
        }
        action.execute(request.getAction(), stack);
    }
}
