package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AxiomType {

    SUBCLASSOF((byte)0, "SubClassOf"),
    EQUIVALENTTO((byte)1, "EquivalentTo"),
    SUBOBJECTPROPERTY((byte)2, "SubObjectPropertyOf"),
    SUBDATAPROPERTY((byte)3, "SubDataPropertyOf"),
    SUBANNOTATIONPROPERTY((byte)4, "SubAnnotationPropertyOf"),
    OBJECTPROPERTYRANGE((byte)5, "ObjectPropertyRange"),
    DATAPROPERTYRANGE((byte)6,"DataPropertyRange"),
    PROPERTYDOMAIN((byte)7, "PropertyDomain"),
    DISJOINTWITH((byte)8, "DisjointWith"),
    SUBPROPERTYCHAIN((byte)9, "SubPropertyChain"),
    INVERSEPROPERTYOF((byte)10, "InverseOf"),
    ISFUNCTIONAL((byte)11, "IsFunctional"),
    ISTRANSITIVE((byte)12, "IsTransitive"),
    ISSYMMETRIC((byte)13, "IsSymmetrical"),
    ISREFLEXIVE((byte)14,"IsReflexive"),
    OBJECTPROPERTYASSERTION((byte)15,"ObjectPropertyAssertion"),
    DATAPROPERTYASSERTION((byte)16,"DataPropertyAssertion"),
    ISTYPE((byte)17,"IsType"),
    ANNOTATIONASSERTION((byte)18,"AnnotationAssertion"),
    MEMBER((byte)19,"Member"),
    //MEMBER_EXPANSION((byte)20,"MemberExpansion"),
    PROPERTY((byte)21,"Property"),
    MAPPED_FROM((byte)22,"MappedFrom"),
    ROLE((byte)23,"Role"),
    MEMBER_EXC((byte)24,"MemberExclusion"),
    MEMBER_INSTANCE((byte)25,"MemberInstance"),
    MEMBER_EXC_INSTANCE((byte)26,"MemberInstanceExclusion");

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
