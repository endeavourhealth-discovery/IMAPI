package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A constraint on the entailment rule on a concept. By default OWL states that a class entails itself and all of its equivalent
 * classes and subclasses. Likewise Discovery applies this by defaule
 * When searching records where the instance obkects use a class identifier then there is sometimes a need to restrict
 * subclasses<p>
 *    For example, the Snomed ECL descendant of  means the subclasses and equivalent classes NOT including 1234.
 *    1234 means instances that are precisely identified by 1234
 *    The default behaviour is represented by the Snomed  sameorDescendant of 1234.
 * </p>
 */
public enum EntailmentConstraint {
   SAME_OR_DESCENDENT((byte)0, "<<"),
   DESCENDANT((byte)1, "<"),
   SAME_AS((byte)2, "");

   private byte _value;
   private String _name;


   EntailmentConstraint(byte value, String name) {
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

   public static EntailmentConstraint byValue(byte value) {
      for (EntailmentConstraint t: EntailmentConstraint.values()) {
         if (t._value == value)
            return t;
      }

      return null;
   }

   public static EntailmentConstraint byName(String name) {
      for (EntailmentConstraint t: EntailmentConstraint.values()) {
         if (t._name.equals(name))
            return t;
      }

      return null;
   }
}
