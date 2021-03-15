package org.endeavourhealth.imapi.model.tripletree;

import java.util.ArrayList;
import java.util.List;

public class TTArray extends TTValue {
    List<TTValue> elements = new ArrayList<>();

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

    public TTNode getAsNode(int index) {
        return (TTNode)elements.get(index);
    }
    public TTLiteral getAsLiteral(int index) {
        return (TTLiteral)elements.get(index);
    }
    public TTIriRef getAsIriRef(int index) {
        return (TTIriRef)elements.get(index);
    }
    public TTArray getAsArray(int index) {
        return (TTArray)elements.get(index);
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
