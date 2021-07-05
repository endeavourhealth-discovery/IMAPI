package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTTuple;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class IntelliSense {
    private static final Logger LOG = LoggerFactory.getLogger(IntelliSense.class);

    public List<TTValue> evaluate(IntelliSenseRequest request) {
        TTValue v = request.getEntity();
        TTIriRef p = null;
        Deque<TTTuple> stack = new ArrayDeque<>();
        stack.push(new TTTuple(null, v));

        for (TTValue i : request.getIndex()) {
            if (v.isLiteral() || v.isIriRef())
                throw new IllegalStateException("Something went wrong!");
            else {
                if (v.isNode() && i.isIriRef()) {
                    p = i.asIriRef();
                    v = v.asNode().get(i.asIriRef());
                    stack.push(new TTTuple(p, v));
                } else if (v.isList() && i.isLiteral()) {
                    v = v.asArray().get(Integer.parseInt(i.asLiteral().getValue()));
                    stack.push(new TTTuple(p, v));
                } else {
                    throw new IllegalStateException("Something went wrong!");
                }
            }
        }

        return action(request.getAction(), stack);
    }

    private List<TTValue> action(String action, Deque<TTTuple> stack) {
        List<TTValue> result = new ArrayList<>();

        TTTuple t = stack.pop();
        TTIriRef p = t.getPredicate();
        TTValue v = t.getValue();

        if ("SUGGEST".equals(action)) {
            result.add(literal("SQL SELECT WHERE LIKE '" + v.asIriRef().getName() + "%';"));
            if (stack.peek() != null && stack.peek().getValue().isNode())
                result.add(literal("AND SUBTYPE OF RANGE OF " + p.asIriRef().getIri()));
        } else if ("ADD".equals(action)) {
            if (v.isNode() && v.asNode().has(RDF.TYPE)) {
                if (!v.asNode().has(OWL.ONPROPERTY)) result.add(OWL.ONPROPERTY);
                if (!v.asNode().has(OWL.SOMEVALUESFROM)) result.add(OWL.SOMEVALUESFROM);
            }
        } else {
            LOG.error("Unhandled action!");
        }

        return result;
    }
}
