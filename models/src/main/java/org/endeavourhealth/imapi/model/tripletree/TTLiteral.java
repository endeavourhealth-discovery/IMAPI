package org.endeavourhealth.imapi.model.tripletree;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTLiteral extends TTValue {
    public static TTLiteral literal(String value) {
        return new TTLiteral(value);
    }

    public static TTLiteral literal(String value, TTIriRef type) {
        return new TTLiteral(value, type);
    }

    public static TTLiteral literal(String value, String type) {
        return new TTLiteral(value, type);
    }

    public static TTLiteral literal(String value, String type, String typeName) {
        return new TTLiteral(value, type, typeName);
    }

    String value;
    TTIriRef type;

    public TTLiteral() {}
    public TTLiteral(String value) {
        this.value = value;
    }
    public TTLiteral(String value, String type) {
        this(value, iri(type));
    }
    public TTLiteral(String value, String type, String typeName) {
        this(value, iri(type, typeName));
    }
    public TTLiteral(String value, TTIriRef type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public TTLiteral setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public TTLiteral asLiteral() {
        return this;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }
}
