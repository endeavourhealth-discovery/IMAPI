package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Map;

public class JsonLDMapper extends ObjectMapper {
  private Map<String, String> iriPrefixMap;
  private Map<String, String> prefixIriMap;
  private boolean pretty;

  public JsonLDMapper() {
    SimpleModule module = new SimpleModule();
    module.addSerializer(Object.class, new JsonLDSerializer(Object.class));
    registerModule(module);
  }

  public Map<String, String> getIriPrefixMap() {
    return iriPrefixMap;
  }

  public JsonLDMapper setIriPrefixMap(Map<String, String> iriPrefixMap) {
    this.iriPrefixMap = iriPrefixMap;
    return this;
  }


}
