package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpressionType {

    CLASS((byte)0, "Class"),
    PROPERTY((byte)1, "Property"),
    INTERSECTION((byte)2, "Intersection"),
    UNION((byte)3, "Union"),
    OBJECTONEOF((byte)4, "ObjectOneOf"),
    DATAONEOF((byte)5, "DataOneOf"),
    OBJECTPROPERTYVALUE((byte)6,"ObjectExactCardinality"),
    DATAPROPERTYVALUE((byte)7,"DataExactCardinality"),
    COMPLEMENTOF((byte)8, "ComplementOf"),
    DATATYPE((byte)9,"DataType"),
    OBJECTVALUE((byte)10,"ObjectValue"),
    //PROPERTY_CONSTRAINT((byte)11,"PropertyConstraint"),
    PROPERTY_VALUE((byte)12,"PropertyValue"),
    ROLE((byte)13,"Role"),
    INSTANCE((byte)14,"Instance");

    private byte _value;
    private String _name;


    ExpressionType (byte value, String name) {
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

    public static ExpressionType byValue(byte value) {
        for (ExpressionType t: ExpressionType.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static ExpressionType byName(String name) {
        for (ExpressionType t: ExpressionType.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}


