package org.endeavourhealth.dataaccess.graph.tripletree;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;

import java.util.ArrayList;
import java.util.List;

public class TTArray extends TTValue {
    List<TTValue> elements = new ArrayList<>();

    public TTArray add(Literal value) {
        return add(new TTLiteral(value));
    }

    public TTArray add(IRI iri) {
        return add(new TTIriRef(iri));
    }

    public TTArray add(IRI iri, String name) {
        return add(new TTIriRef(iri, name));
    }


    public TTArray add(TTValue value) {
        if (elements == null)
            elements = new ArrayList<>();

        elements.add(value);

        return this;
    }

    public List<TTValue> getElements() {
        return elements;
    }

    public TTValue get(int index) {
        return elements.get(index);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public TTArray asArray() {
        return this;
    }

    @Override
    public boolean isList() {
        return true;
    }
}
