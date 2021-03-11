package org.endeavourhealth.imapi.model.tripletree;

public class TTLiteral extends TTValue {
    public static TTLiteral literal(String value) {
        return new TTLiteral(value);
    }

    public static TTLiteral literal(String value, String type) {
        return new TTLiteral(value, type);
    }

    String value;
    String type;

    public TTLiteral() {}
    public TTLiteral(String value) {
        this.value = value;
    }
    public TTLiteral(String value, String type) {
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
