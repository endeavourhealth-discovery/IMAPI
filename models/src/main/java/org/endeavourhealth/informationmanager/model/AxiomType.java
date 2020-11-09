package org.endeavourhealth.informationmanager.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AxiomType {
    SUBCLASSOF((byte)0, ":subClassOf"),
    EQUIVALENTTO((byte)1, ":equivalentTo"),
    SUBOBJECTPROPERTY((byte)2, ":SubObjectPropertyOf"),
    SUBDATAPROPERTY((byte)3, ":SubDataPropertyOf"),
    OBJECTPROPERTYRANGE((byte)4, ":propertyRange"),
    PROPERTYDOMAIN((byte)5, ":propertyDomain"),
    SUBANNOTATIONPROPERTY((byte)6, ":"),
    DISJOINTWITH((byte)7, ":disjointWith"),
    ANNOTATION((byte)8, ":"),
    SUBPROPERTYCHAIN((byte)9, ":SubPropertyChain"),
    INVERSEPROPERTYOF((byte)10, ":InverseOf"),
    ISFUNCTIONAL((byte)11, ":isFunctional"),
    ISTRANSITIVE((byte)12, ":IsTransitive"),
    ISSYMMETRIC((byte)13, ":IsSymmetrical"),
    PROPERTYDATAVALUE((byte)14, ":"),
    DATAPROPERTYRANGE((byte)15,":dataPropertyRange"),
    ISREFLEXIVE((byte)16,":IsReflexive");

    private byte _value;
    private String _name;

    private AxiomType(byte value, String name) {
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

    public static AxiomType byValue(byte value) {
        for (AxiomType t: AxiomType.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static AxiomType byName(String name) {
        for (AxiomType t: AxiomType.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
