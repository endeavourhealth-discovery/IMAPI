package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TTContextHelper {
  private TTContext context;
  private boolean usePrefixes = false;

  public TTContextHelper() {
  }

  public TTContextHelper(TTContext context, boolean usePrefixes) {
    this.context = context;
    this.usePrefixes = usePrefixes;
  }

  public boolean isUsePrefixes() {
    return usePrefixes;
  }

  public TTContextHelper setUsePrefixes(boolean usePrefixes) {
    this.usePrefixes = usePrefixes;
    return this;
  }

  public String prefix(String iri) {
    if (usePrefixes)
      return context.prefix(iri);
    else
      return context.expand(iri);
  }

  public String expand(String iri) {
    return context.expand(iri);
  }

  public void serializeContexts(List<TTPrefix> prefixes, JsonGenerator gen) throws IOException {
    if (usePrefixes && prefixes != null && !prefixes.isEmpty()) {
      gen.writeFieldName("context");
      gen.writeStartObject();

      for (TTPrefix prefix : prefixes) {
        context.add(prefix.getIri(), prefix.getPrefix());
        gen.writeStringField(prefix.getPrefix(), prefix.getIri());
      }
      gen.writeFieldName("entities");
      gen.writeStartObject();
      gen.writeStringField("id", "http://envhealth.info/im#entities");
      gen.writeStringField("container", "set");
      gen.writeEndObject();
      gen.writeEndObject();
    }
  }

  public void deserializeContexts(JsonNode document, List<TTPrefix> prefixes) {
    JsonNode contextNode = document.get("context");
    if (contextNode != null) {
      Iterator<Map.Entry<String, JsonNode>> fields = contextNode.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        String key = field.getKey();
        JsonNode value = field.getValue();
        if (value.isTextual() && value.textValue().startsWith("http")) {
          prefixes.add(new TTPrefix(value.textValue(), key));
          context.add(value.asText(), key);
        }
      }
    }
  }
}
