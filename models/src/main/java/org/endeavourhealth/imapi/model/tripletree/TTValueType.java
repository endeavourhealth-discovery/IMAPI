package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.ConceptType;

public enum TTValueType {
   IRIREF((byte)0, "IRIRef"),
   NODE((byte)1, "Node"),
   LIST((byte)2, "List"),
   LITERAL((byte)3, "Literal");


   private byte _value;
   private String _name;

   private TTValueType(byte value, String name) {
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

   public static TTValueType byValue(byte value) {
      for (TTValueType t: TTValueType.values()) {
         if (t._value == value)
            return t;
      }

      return null;
   }

   public static TTValueType byName(String name) {
      for (TTValueType t: TTValueType.values()) {
         if (t._name.equals(name))
            return t;
      }

      return null;
   }
}
