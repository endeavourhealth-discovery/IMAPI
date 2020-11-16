package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QuantificationType {
   SOME((byte)0, "some"),
   ONLY((byte)1, "only"),
   EXACT((byte)2,"exact"),
   RANGE((byte)3,"range"),
   MIN((byte)4,"minimum"),
   MAX((byte)5,"maximum");

   private byte _value;
   private String _name;

   private QuantificationType(byte value, String name) {
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

   public static QuantificationType byValue(byte value) {
      for (QuantificationType t: QuantificationType.values()) {
         if (t._value == value)
            return t;
      }

      return null;
   }

   public static QuantificationType byName(String name) {
      for (QuantificationType t: QuantificationType.values()) {
         if (t._name.equals(name))
            return t;
      }

      return null;
   }
}
