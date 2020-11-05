package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConceptStatus {
    DRAFT((byte)0, "Draft"),
    ACTIVE((byte)1, "Active"),
    INACTIVE((byte)2, "Inactive");

    private byte _value;
    private String _name;

    private ConceptStatus(byte value, String name) {
        this._value = value;
        this._name = name;
    }

    public byte getValue() {
        return this._value;
    }

    @JsonValue
    public String getName() {
        return this._name;
    }

    public static ConceptStatus byValue(byte value) {
        for (ConceptStatus t: ConceptStatus.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static ConceptStatus byName(String name) {
        for (ConceptStatus t: ConceptStatus.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
