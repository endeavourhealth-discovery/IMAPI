package org.endeavourhealth.imapi.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ConceptContextMap {
    String id;
    String node;
    String value;
    String regex;
    String property;
    List<Context> context;

    public ConceptContextMap setId(String id) {
        this.id = id;
        return this;
    }

    public ConceptContextMap setNode(String node) {
        this.node = node;
        return this;
    }

    public ConceptContextMap setValue(String value) {
        this.value = value;
        return this;
    }

    public ConceptContextMap setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public ConceptContextMap setProperty(String property) {
        this.property = property;
        return this;
    }

    public ConceptContextMap setContext(List<Context> context) {
        this.context = context;
        return this;
    }
}
