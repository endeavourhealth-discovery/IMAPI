package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTTuple;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class IntelliSense {
    public List<TTValue> evaluate(IntelliSenseRequest request) {
        TTValue v = request.getEntity();
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

        return action(request.getAction(), stack);
    }

    private List<TTValue> action(String action, Stack<TTTuple> stack) {
        List<TTValue> result = new ArrayList<>();

        TTTuple t = stack.pop();
        TTIriRef p = t.getPredicate();
        TTValue v = t.getValue();

        if ("SUGGEST".equals(action)) {
            result.add(literal("SQL SELECT WHERE LIKE '" + v.asIriRef().getName() + "%';"));
            if (stack.peek().getValue().isNode())
                result.add(literal("AND SUBTYPE OF RANGE OF " + p.asIriRef().getIri()));
        } else if ("ADD".equals(action)) {
            if (v.isNode() && v.asNode().has(RDF.TYPE)) {
                if (!v.asNode().has(OWL.ONPROPERTY)) result.add(OWL.ONPROPERTY);
                if (!v.asNode().has(OWL.SOMEVALUESFROM)) result.add(OWL.SOMEVALUESFROM);
            }
        } else {
            System.err.println("Unhandled action!");
        }

        return result;
    }

    private void debugStack(Stack<TTTuple> stack) {
        System.out.println("========== STACK ==========");
        while (!stack.isEmpty()) {
            TTTuple t = stack.pop();
            TTIriRef p = t.getPredicate();
            TTValue v = t.getValue();
            if (p == null)
                System.out.print("P: (root)\t");
            else
                System.out.print("P: [" + p.getIri() + "]\t");

            if (v.isLiteral())
                System.out.println("V: [" + v.asLiteral().getValue() + "]");
            else if (v.isIriRef())
                System.out.println("V: [" + v.asIriRef().getIri() + " | " + v.asIriRef().getName() + "]");
            else if (v.isNode())
                System.out.println("V: Node");
            else if (v.isList())
                System.out.println("V: List[]");
            else
                System.out.println("V: ????");
        }
    }
}
