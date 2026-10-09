package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

import java.util.Map;

/**
 * An inline script (painless, the default language) with optional parameters.
 */
public class Script {
  private final String source;
  private final Map<String, Object> params;

  public Script(String source) {
    this(source, Map.of());
  }

  public Script(String source, Map<String, Object> params) {
    this.source = source;
    this.params = params == null ? Map.of() : params;
  }

  ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    node.put("source", source);
    if (!params.isEmpty()) node.set("params", SharedObjectMapper.INSTANCE.valueToTree(params));
    return node;
  }
}
