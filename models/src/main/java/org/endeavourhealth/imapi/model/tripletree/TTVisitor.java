package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;
import java.util.Map;

public class TTVisitor {
    public interface ITTLiteralVisitor { void visit(TTIriRef predicate, TTLiteral literal); }
    public interface ITTIriRefVisitor { void visit(TTIriRef predicate, TTIriRef iriRef); }
    public interface ITTNodeVisitor { void visit(TTIriRef predicate, TTNode node); }
    public interface ITTListVisitor { void visit(TTIriRef predicate, TTArray node); }
    public interface ITTPredicateVisitor { void visit(TTIriRef predicate); }

    public ITTLiteralVisitor LiteralVisitor = (predicate, literal) -> {};
    public ITTIriRefVisitor IriRefVisitor = (predicate, iriRef) -> {};
    public ITTNodeVisitor NodeVisitor = (predicate, node) -> {};
    public ITTNodeVisitor NodeExitVisitor = (predicate, node) -> {};
    public ITTListVisitor ListVisitor = (predicate, list) -> {};
    public ITTListVisitor ListExitVisitor = (predicate, list) -> {};
    public ITTPredicateVisitor PredicateVisitor = (predicate) -> {};

    public void visit(TTNode node) {
        visit(null, node);
    }
    public void visit(TTArray array) { visit(null, array); }

    public void visit(TTIriRef predicate, TTNode node) {
        if (predicate != null)
            PredicateVisitor.visit(predicate);

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
        if (value.isLiteral()) {
            if (predicate != null)
                PredicateVisitor.visit(predicate);
            LiteralVisitor.visit(predicate, value.asLiteral());
        } else if (value.isIriRef()) {
            if (predicate != null)
                PredicateVisitor.visit(predicate);
            IriRefVisitor.visit(predicate, value.asIriRef());
        }
        else if (value.isNode()) {
            visit(predicate, value.asNode());
        } else if (value.isList()) {
            visit(predicate, value.asArray());
        }
    }

    public void visit(TTIriRef predicate, TTArray array) {
        if (predicate != null)
            PredicateVisitor.visit(predicate);
        ListVisitor.visit(predicate, array);
        for (TTValue value : array.elements) {
            visit(predicate, value);
        }
        ListExitVisitor.visit(predicate, array);
    }
}
