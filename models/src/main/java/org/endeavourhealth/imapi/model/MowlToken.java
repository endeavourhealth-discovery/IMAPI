package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MowlToken {


   CLASS((byte)0, "Class:"),
   OBJECT_PROPERTY((byte)1,"ObjectProperty:"),
   NAME((byte)2,"Name:"),
   DESCRIPTION((byte)3,"Description:"),
   CODE((byte)4,"Code:"),
   SCHEME((byte)5,"Scheme:"),
   SUBCLASS_OF((byte)6,"SubClassOf:"),
   EQUIVALENT_TO((byte)7,"EquivalentTo:"),
   CLASS_IRI((byte)8,"SimpleClass:"),
   ROLE((byte)9,"Role:"),
   ROLE_GROUP((byte)10,"RoleGroup:"),
   DATATYPE((byte)11,"Datatype:"),
   DATA_PROPERTY((byte)12,"DataProperty:"),
   AND((byte)13,"and"),
   OR((byte)15,"or"),
   SOME((byte)16,"some"),
   MAX((byte)17,"max"),
   MIN((byte)18,"min"),
   DATATYPE_RESTRICTION((byte)19,"DataTypeRestriction:"),
   FACET((byte)20,"Facet");

   private byte _value;
   private String _name;

   private MowlToken(byte value, String name) {
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

   public static MowlToken byValue(byte value) {
      for (MowlToken t: MowlToken.values()) {
         if (t._value == value)
            return t;
      }

      return null;
   }

   public static MowlToken byName(String name) {
      for (MowlToken t: MowlToken.values()) {
         if (t._name.equals(name))
            return t;
      }

      return null;
   }


   }

