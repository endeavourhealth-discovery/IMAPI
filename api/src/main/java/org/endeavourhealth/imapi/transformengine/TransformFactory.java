package org.endeavourhealth.imapi.transformengine;

public class TransformFactory {
  private TransformFactory() {
    throw new IllegalStateException("Utility class");
  }


  public static SyntaxTranslator createConverter(String format) {
    if (format.equalsIgnoreCase("JSON"))
      return new JsonTranslator();
    else if (format.equalsIgnoreCase("JSON-LD"))
      return new TTTranslator();
    else
      throw new IllegalArgumentException("Source format : " + format + " syntax converter Not configured");
  }

}
