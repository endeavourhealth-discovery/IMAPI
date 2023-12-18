package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

public class TTTuple {
    private TTIriRef predicate;
    private TTValue value;

    public TTTuple() {
    }

    public TTTuple(TTIriRef predicate, TTValue value) {
        this.predicate = predicate;
        this.value = value;
    }
    public TTTuple(Vocabulary predicate, TTValue value) {
        this.predicate = predicate.asTTIriRef();
        this.value = value;
    }

    public TTIriRef getPredicate() {
        return predicate;
    }

    @JsonSetter
    public TTTuple setPredicate(TTIriRef predicate) {
        this.predicate = predicate;
        return this;
    }
    public TTTuple setPredicate(Vocabulary predicate) {
        return setPredicate(predicate.asTTIriRef());
    }

    public TTValue getValue() {
        return value;
    }

    public TTTuple setValue(TTValue value) {
        this.value = value;
        return this;
    }
}
