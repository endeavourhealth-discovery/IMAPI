package org.endeavourhealth.imapi.model.tripletree;

import java.util.Map;

public class TTVisitor {
    public interface ITTLiteralVisitor { void visit(TTIriRef predicate, TTLiteral literal); }
    public interface ITTIriRefVisitor { void visit(TTIriRef predicate, TTIriRef iriRef); }
    public interface ITTNodeVisitor { void visit(TTIriRef predicate, TTNode node); }
    public interface ITTListVisitor { void visit(TTIriRef predicate, TTArray node); }
    public interface ITTPredicateVisitor { void visit(TTIriRef predicate); }

    private ITTLiteralVisitor literalVisitor = (predicate, literal) -> {};
    private ITTIriRefVisitor iriRefVisitor = (predicate, iriRef) -> {};
    private ITTNodeVisitor nodeVisitor = (predicate, node) -> {};
    private ITTNodeVisitor nodeExitVisitor = (predicate, node) -> {};
    private ITTListVisitor listVisitor = (predicate, list) -> {};
    private ITTListVisitor listExitVisitor = (predicate, list) -> {};
    private ITTPredicateVisitor predicateVisitor = predicate -> {};

    public TTVisitor onLiteral(ITTLiteralVisitor literalVisitor) {
        this.literalVisitor = literalVisitor;
        return this;
    }

    public TTVisitor onIriRef(ITTIriRefVisitor iriRefVisitor) {
        this.iriRefVisitor = iriRefVisitor;
        return this;
    }

    public TTVisitor onNode(ITTNodeVisitor nodeVisitor) {
        this.nodeVisitor = nodeVisitor;
        return this;
    }

    public TTVisitor onNodeExit(ITTNodeVisitor nodeExitVisitor) {
        this.nodeExitVisitor = nodeExitVisitor;
        return this;
    }

    public TTVisitor onList(ITTListVisitor listVisitor) {
        this.listVisitor = listVisitor;
        return this;
    }

    public TTVisitor onListExit(ITTListVisitor listExitVisitor) {
        this.listExitVisitor = listExitVisitor;
        return this;
    }

    public TTVisitor onPredicate(ITTPredicateVisitor predicateVisitor) {
        this.predicateVisitor = predicateVisitor;
        return this;
    }

    public void visit(TTNode node) {
        visit(null, node);
    }
    public void visit(TTArray array) { visit(null, array); }

    public void visit(TTIriRef predicate, TTNode node) {
        if (predicate != null)
            predicateVisitor.visit(predicate);

        nodeVisitor.visit(predicate, node);
        Map<TTIriRef, TTValue> predicateMap = node.getPredicateMap();
        for (Map.Entry<TTIriRef, TTValue> entry : predicateMap.entrySet()) {
            TTIriRef p = entry.getKey();
            TTValue v = entry.getValue();

            visit(p, v);
        }
        nodeExitVisitor.visit(predicate, node);
    }


    public void visit(TTIriRef predicate, TTValue value) {
        if (value.isLiteral()) {
            if (predicate != null)
                predicateVisitor.visit(predicate);
            literalVisitor.visit(predicate, value.asLiteral());
        } else if (value.isIriRef()) {
            if (predicate != null)
                predicateVisitor.visit(predicate);
            iriRefVisitor.visit(predicate, value.asIriRef());
        }
        else if (value.isNode()) {
            visit(predicate, value.asNode());
        } else if (value.isList()) {
            visit(predicate, value.asArray());
        }
    }

    public void visit(TTIriRef predicate, TTArray array) {
        if (predicate != null)
            predicateVisitor.visit(predicate);
        listVisitor.visit(predicate, array);
        for (TTValue value : array.getElements()) {
            visit(predicate, value);
        }
        listExitVisitor.visit(predicate, array);
    }


}
