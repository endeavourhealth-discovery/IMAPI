package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.model.tripletree.json.TTLiteralDeserializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTLiteralSerializer;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize(using = TTLiteralSerializer.class)
@JsonDeserialize(using = TTLiteralDeserializer.class)
public class TTLiteral implements TTValue {
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
    public static TTLiteral literal(Long value) {
        return new TTLiteral(value);
    }
    public static TTLiteral literal(Pattern value) {
        return new TTLiteral(value);
    }

    public static TTLiteral literal(JsonNode node) {
        if (!node.isValueNode())
            throw new IllegalStateException("Only value Json nodes currently handled");

        if (node.isBoolean())
            return literal(node.booleanValue());
        else if (node.isLong())
            return literal(node.longValue());
        else if (node.isInt())
            return literal(node.intValue());
        else
            return literal(node.textValue());
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
        this.type = null;
    }
    public TTLiteral(Boolean value) {
        this.value = value.toString();
        this.type = XSD.BOOLEAN;
    }
    public TTLiteral(Integer value) {
        this.value = value.toString();
        this.type = XSD.INTEGER;
    }
    public TTLiteral(Long value) {
        this.value = value.toString();
        this.type = XSD.LONG;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TTLiteral v = (TTLiteral) o;

        if (value == null && v.value != null) return false;
        if (value != null && !value.equals(v.value)) return false;

        if (type == null && v.type != null) return false;

        return type == null || type.equals(v.type);
    }

    @Override
    public int hashCode() {
        String toHash = "";
        if (value != null)
            toHash += value;
        if (type != null)
            toHash += type.getIri();
        return toHash.hashCode();
    }
}
