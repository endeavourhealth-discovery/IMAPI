package org.endeavourhealth.imapi.filer;


import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TTPrefixManager {
  Map<String, String> prefixMap = new HashMap<>();

  public String expand(String iri) {
    if (prefixMap == null)
      return iri;
    try {
      int colonPos = iri.indexOf(":");
      String prefix = iri.substring(0, colonPos);
      String path = prefixMap.get(prefix);
      if (path == null)
        return iri;
      else
        return path + iri.substring(colonPos + 1);
    } catch (StringIndexOutOfBoundsException e) {
      log.error("invalid iri: {}", iri);
      return null;
    }
  }
}
