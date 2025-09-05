package org.endeavourhealth.imapi.utility;

public class Pluraliser {

  public static String pluralise(String word) {
    if (word == null || word.isEmpty()) {
      return word;
    }

    String lowerWord = word.toLowerCase();

    switch (lowerWord) {
      case "child":
        return preserveCase(word, "children");
      case "person":
        return preserveCase(word, "people");
      case "man":
        return preserveCase(word, "men");
      case "woman":
        return preserveCase(word, "women");
      case "mouse":
        return preserveCase(word, "mice");
      case "goose":
        return preserveCase(word, "geese");
      case "tooth":
        return preserveCase(word, "teeth");
      case "foot":
        return preserveCase(word, "feet");
      case "ox":
        return preserveCase(word, "oxen");
    }
    if (word.endsWith("s")) {
      return word;
    }

    if (word.matches("(?i).+[^aeiou]y$")) {
      return word.substring(0, word.length() - 1) + "ies";
    }

    if (word.matches("(?i).*(s|x|z|ch|sh)$")) {
      return word + "es";
    }

    if (word.endsWith("fe")) {
      return word.substring(0, word.length() - 2) + "ves";
    } else if (word.endsWith("f")) {
      return word.substring(0, word.length() - 1) + "ves";
    }

    // Default: add "s"
    return word + "s";
  }

  // Helper to preserve original case
  private static String preserveCase(String original, String plural) {
    if (Character.isUpperCase(original.charAt(0))) {
      return plural.substring(0, 1).toUpperCase() + plural.substring(1);
    }
    return plural;
  }
}
