package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;
import java.util.Map;

public class TTConceptVisitor {
    public interface ITTLiteralVisitor { void visit(TTIriRef predicate, TTLiteral literal); }
    public interface ITTIriRefVisitor { void visit(TTIriRef predicate, TTIriRef iriRef); }
    public interface ITTNodeVisitor { void visit(TTIriRef predicate, TTNode node); }
    public interface ITTListVisitor { void visit(TTIriRef predicate, TTArray node); }

    public ITTLiteralVisitor LiteralVisitor = (predicate, literal) -> {};
    public ITTIriRefVisitor IriRefVisitor = (predicate, iriRef) -> {};
    public ITTNodeVisitor NodeVisitor = (predicate, node) -> {};
    public ITTNodeVisitor NodeExitVisitor = (predicate, node) -> {};
    public ITTListVisitor ListVisitor = (predicate, list) -> {};
    public ITTListVisitor ListExitVisitor = (predicate, list) -> {};

    public void visit(TTConcept concept) {
        visit(null, concept);
    }

    public void visit(TTIriRef predicate, TTNode node) {
        NodeVisitor.visit(predicate, node);
        HashMap<TTIriRef, TTValue> predicateMap = node.getPredicateMap();
        for (Map.Entry<TTIriRef, TTValue> entry : predicateMap.entrySet()) {
            TTIriRef p = entry.getKey();
            TTValue v = entry.getValue();

            visit(p, v);
        }
        NodeExitVisitor.visit(predicate, node);
    }


    public void visit(TTIriRef predicate, TTValue value) {
        if (value.isLiteral())
            LiteralVisitor.visit(predicate, value.asLiteral());
        else if (value.isIriRef())
            IriRefVisitor.visit(predicate, value.asIriRef());
        else if (value.isNode()) {
            visit(predicate, value.asNode());
        } else if (value.isList()) {
            visit(predicate, value.asArray());
        }
    }

    public void visit(TTIriRef predicate, TTArray array) {
        ListVisitor.visit(predicate, array);
        for (TTValue value : array.elements) {
            visit(predicate, value);
        }
        ListExitVisitor.visit(predicate, array);
    }
}
