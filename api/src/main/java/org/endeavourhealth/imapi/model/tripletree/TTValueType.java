package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TTValueType {
   IRIREF((byte)0, "IRIRef"),
   NODE((byte)1, "Node"),
   LIST((byte)2, "List"),
   LITERAL((byte)3, "Literal");


   private final byte value;
   private final String name;

   TTValueType(byte value, String name) {
      this.value = value;
      this.name = name;
   }

   public byte getValue() {
      return this.value;
   }

   @JsonValue
   public String getName() {
      return this.name;
   }

   public static TTValueType byValue(byte value) {
      for (TTValueType t: TTValueType.values()) {
         if (t.value == value)
            return t;
      }

      return null;
   }

   public static TTValueType byName(String name) {
      for (TTValueType t: TTValueType.values()) {
         if (t.name.equals(name))
            return t;
      }

      return null;
   }
}
