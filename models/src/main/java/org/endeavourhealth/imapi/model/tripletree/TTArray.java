package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTArrayDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTArraySerializer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@JsonSerialize(using = TTArraySerializer.class)
@JsonDeserialize(using = TTArrayDeserializer.class)
public class TTArray implements Serializable {
    private LinkedHashSet<TTValue> elements = new LinkedHashSet<>();

    public TTArray add(TTValue value) {
        if (elements != null && elements.contains(value))
            return this;

        if (elements == null)
            elements = new LinkedHashSet<>();

        elements.add(value);

        return this;
    }

    public int size() {
        return elements.size();
    }
    public boolean contains(TTValue value) {
        return elements.contains(value);
    }

    // Single element helpers
    public boolean isLiteral() { return elements.size() == 1 && elements.stream().findFirst().get().isLiteral(); }
    public TTLiteral asLiteral() { return elements.stream().findFirst().get().asLiteral(); }

    public boolean isIriRef() { return elements.size() == 1 && elements.stream().findFirst().get().isIriRef(); }
    public TTIriRef asIriRef() { return elements.stream().findFirst().get().asIriRef(); }

    public boolean isNode() { return elements.size() == 1 && elements.stream().findFirst().get().isNode(); }
    public TTNode asNode() { return elements.stream().findFirst().get().asNode(); }

    public TTValue asValue() { return elements.stream().findFirst().get(); }

    public Iterable<TTValue> iterator() {
        return elements;
    }

    public TTValue get(int index) {
        return getElements().get(index);
    }

    public List<TTValue> getElements() {
        return new ArrayList<>(elements);
    }

    public void remove(TTValue remove) {
        elements.remove(remove);
    }

    public Stream<TTValue> stream() {
        return elements.stream();
    }
}
