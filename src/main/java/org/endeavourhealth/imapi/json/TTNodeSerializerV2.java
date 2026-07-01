package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTContextJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTNodeSerializerV2 extends StdSerializer<TTNodeJava> {
  private TTContextJava contextMap;
  private List<TTIriRef> predicateTemplate;
  private Boolean simpleProperties;

  public TTNodeSerializerV2() {
    this(null);
  }

  public TTNodeSerializerV2(Class<TTNodeJava> t) {
    super(t);
  }

  /**
   * @param contextMap the context object for the JSON-LD document
   */
  public TTNodeSerializerV2(Class<TTNodeJava> t, TTContextJava contextMap) {
    super(t);
    this.contextMap = contextMap;
  }

  public TTNodeSerializerV2(Class<TTNodeJava> t, TTContextJava contextMap, List<TTIriRef> predicateTemplate) {
    super(t);
    this.contextMap = contextMap;
    this.predicateTemplate = predicateTemplate;
  }

  public void serialize(TTNodeJava node, JsonGenerator gen, SerializerProvider prov) throws IOException {
    simpleProperties = (Boolean) prov.getAttribute(TTNodeSerializer.SIMPLE_PROPERTIES);
    simpleProperties = (simpleProperties != null && simpleProperties);
    if (predicateTemplate == null)
      predicateTemplate = new ArrayList<>();
    gen.writeStartObject();
    serializeTemplatedPredicates(node, gen, prov);
    serializeRemainingPredicates(node, gen, prov);
    gen.writeEndObject();
  }

  private void serializeTemplatedPredicates(TTNodeJava node, JsonGenerator gen, SerializerProvider prov) throws IOException {
    for (TTIriRef predicate : predicateTemplate) {
      if (node.get(predicate) != null)
        prov.defaultSerializeField(prefix(simpleProperties ? predicate.getIri().substring(predicate.getIri().indexOf("#"))
          : predicate.getIri()), node.get(predicate), gen);
    }
  }

  private void serializeRemainingPredicates(TTNodeJava node, JsonGenerator gen, SerializerProvider prov) throws IOException {

    Map<TTIriRef, TTArrayJava> predicates = node.getPredicateMap();
    if (predicates != null && !predicates.isEmpty()) {
      Set<Map.Entry<TTIriRef, TTArrayJava>> entries = predicates.entrySet();
      for (Map.Entry<TTIriRef, TTArrayJava> entry : entries) {
        if (!predicateTemplate.contains(entry.getKey())) {

          prov.defaultSerializeField(prefix(simpleProperties ? entry.getKey().getIri().substring(entry.getKey().getIri().indexOf("#"))
            : entry.getKey().getIri()), entry.getValue(), gen);
        }
      }
    }
  }

  public String prefix(String iri) {
    if (contextMap == null)
      return iri;
    else
      return contextMap.prefix(iri);
  }
}
