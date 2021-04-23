package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTLiteral extends TTValue {
    // Static helpers
    public static TTLiteral literal(String value, TTIriRef type) {
        return new TTLiteral(value, type);
    }
    public static TTLiteral literal(String value, String type) {
        return new TTLiteral(value, type);
    }
    public static TTLiteral literal(String value) {
        return new TTLiteral(value);
    }
    public static TTLiteral literal(Boolean value) {
        return new TTLiteral(value);
    }
    public static TTLiteral literal(Integer value) {
        return new TTLiteral(value);
    }

    public static TTLiteral literal(Pattern value) {
        return new TTLiteral(value);
    }

    private String value;
    private TTIriRef type;

    // General constructors
    public TTLiteral() {}
    public TTLiteral(String value, TTIriRef type) {
        this.value = value;
        this.type = type;
    }
    public TTLiteral(String value, String type) {
        this.value = value;
        this.type = iri(type);
    }

    // Type specific constructors
    public TTLiteral(String value) {
        this.value = value;
        //overly typed? this.type = XSD.STRING;
    }
    public TTLiteral(Boolean value) {
        this.value = value.toString();
        this.type = XSD.BOOLEAN;
    }
    public TTLiteral(Integer value) {
        this.value = value.toString();
        this.type = XSD.INTEGER;
    }

    public TTLiteral(Pattern value) {
        this.value = value.toString();
        this.type = XSD.PATTERN;
    }

    public String getValue() {
        return value;
    }

    // Type specific getters
    public Boolean booleanValue() {
        return Boolean.parseBoolean(this.value);
    }

    public Integer intValue() {
        return Integer.parseInt(this.value);
    }

    public Long longValue() {
        return Long.parseLong(this.value);
    }

    public Pattern patternValue() {
        return Pattern.compile(this.value);
    }

    public TTLiteral setValue(String value) {
        this.value = value;
        return this;
    }

    public TTIriRef getType() {
        return type;
    }

    public TTLiteral setType(TTIriRef type) {
        this.type = type;
        return this;
    }

    @Override
    public TTLiteral asLiteral() {
        return this;
    }

    @Override
    @JsonIgnore
    public boolean isLiteral() {
        return true;
    }
}
