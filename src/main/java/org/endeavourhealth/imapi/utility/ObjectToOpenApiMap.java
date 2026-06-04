package org.endeavourhealth.imapi.utility;

import java.util.Map;

public class ObjectToOpenApiMap {
  private ObjectToOpenApiMap() {
  }

  public static Map<String, Object> convert(Object obj) {
    if (!(obj instanceof Map<?, ?> rawMap)) {
      throw new IllegalArgumentException("Object needs to be a Map");
    }

    if (rawMap.keySet().stream().allMatch(String.class::isInstance)) {
      @SuppressWarnings("unchecked")
      Map<String, Object> result = (Map<String, Object>) rawMap;
      return result;
    }

    throw new IllegalArgumentException("Object is not a Map<String, Object> object");
  }
}
