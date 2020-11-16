package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConceptType {
    CLASSONLY((byte)0, "Class"),
    OBJECTPROPERTY((byte)1, "ObjectProperty"),
    DATAPROPERTY((byte)2, "DataProperty"),
    DATATYPE((byte)3, "DataType"),
    ANNOTATION((byte)4, "Annotation"),
    INDIVIDUAL((byte)5,"Individual");


    private byte _value;
    private String _name;

    private ConceptType(byte value, String name) {
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

    public static ConceptType byValue(byte value) {
        for (ConceptType t: ConceptType.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static ConceptType byName(String name) {
        for (ConceptType t: ConceptType.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
