package org.endeavourhealth.dataaccess.graph.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.vocabulary.OWL;

import java.util.HashMap;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class TTNode extends TTValue {
    private HashMap<IRI, TTValue> predicateValues = new HashMap<>();

    public TTNode() {}
    public TTNode(String iri) {
        set(OWL.HASKEY, iri(iri));
    }

    public TTNode set(IRI predicate, Literal value) {
        return this.set(predicate, new TTLiteral().setValue(value));
    }
    public TTNode set(IRI predicate, IRI iri) {
        return this.set(predicate, new TTIriRef(iri));
    }
    public TTNode set(IRI predicate, IRI iri, String name) {
        return this.set(predicate, new TTIriRef(iri, name));
    }

    public TTNode set(IRI predicate, TTValue value) {
        predicateValues.put(predicate, value);
        return this;
    }

    public HashMap<IRI, TTValue> getPredicateMap() {
        return this.predicateValues;
    }

    @Override
    public TTNode asNode() {
        return this;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    TTLiteral getAsLiteral(IRI predicate) {
        return (TTLiteral) predicateValues.get(predicate);
    }

    TTIriRef getAsIriRef(IRI predicate) {
        return (TTIriRef) predicateValues.get(predicate);
    }

    public TTArray getAsArray(IRI predicate) {
        return (TTArray) predicateValues.get(predicate);
    }

}
