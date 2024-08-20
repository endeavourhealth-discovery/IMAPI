package org.endeavourhealth.imapi.filer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TTPrefixManager {
  private static final Logger LOG = LoggerFactory.getLogger(TTPrefixManager.class);
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
      LOG.error("invalid iri: {}", iri);
      return null;
    }
  }
}
