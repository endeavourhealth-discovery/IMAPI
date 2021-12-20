package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTArrayDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTArraySerializer;
import org.endeavourhealth.imapi.query.MatchClause;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
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
    public boolean isLiteral() {
        if (elements.stream().findFirst().isPresent())
            return elements.size() == 1 && elements.stream().findFirst().get().isLiteral();
        else return false;
    }
    public TTLiteral asLiteral() { return (TTLiteral) elements.stream().findFirst().orElse(null); }

    public boolean isIriRef() {
        if (elements.stream().findFirst().isPresent())
            return elements.size() == 1 && elements.stream().findFirst().get().isIriRef();
        else return false;
    }
    public TTIriRef asIriRef() { return (TTIriRef) elements.stream().findFirst().orElse(null); }

    public boolean isNode() {
        if (elements.stream().findFirst().isPresent())
            return elements.size() == 1 && elements.stream().findFirst().get().isNode();
        else return false;
    }
    public TTNode asNode() { return (TTNode) elements.stream().findFirst().orElse(null); }

    public TTValue asValue() { return elements.stream().findFirst().orElse(null); }

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

    public List<TTValue> getAsOrdered(){
        if (elements==null)
            return null;
        return getElements()
          .stream().sorted(Comparator.comparing(TTValue::getOrder))
          .collect(Collectors.toList());
    }


}
