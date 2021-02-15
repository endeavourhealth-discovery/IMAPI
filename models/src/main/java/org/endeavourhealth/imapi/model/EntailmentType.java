package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EntailmentType {
   SAME_OR_DESCENDENT((byte)0, "<<"),
   DESCENDANT((byte)1, "<"),
   SAME_AS((byte)2, "");

   private byte _value;
   private String _name;


   EntailmentType (byte value, String name) {
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

   public static EntailmentType byValue(byte value) {
      for (EntailmentType t: EntailmentType.values()) {
         if (t._value == value)
            return t;
      }

      return null;
   }

   public static EntailmentType byName(String name) {
      for (EntailmentType t: EntailmentType.values()) {
         if (t._name.equals(name))
            return t;
      }

      return null;
   }
}
