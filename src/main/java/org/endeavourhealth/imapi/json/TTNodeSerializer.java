package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTNodeSerializer {
  public static final String SIMPLE_PROPERTIES = "SIMPLE_PROPERTIES";
  private final TTContextJava contextMap;
  private boolean usePrefixes = false;
  private SerializerProvider prov;
  private Boolean simpleProperties;

  /**
   * @param contextMap the context object for the JSON-LD document
   */
  public TTNodeSerializer(TTContextJava contextMap) {
    this.contextMap = contextMap;
  }

  public TTNodeSerializer(TTContextJava contextMap, boolean usePrefixes) {
    this.contextMap = contextMap;
    this.usePrefixes = usePrefixes;
  }


  public void serializeNode(TTNodeJava node, JsonGenerator gen, SerializerProvider prov) throws IOException {
    this.prov = prov;
    simpleProperties = (Boolean) prov.getAttribute(TTNodeSerializer.SIMPLE_PROPERTIES);
    simpleProperties = (simpleProperties != null && simpleProperties);
    if ((!(node instanceof TTEntityJava)) && node.getIri() != null)
      gen.writeStringField("iri", prefix(node.getIri()));
    serializePredicates(node, gen);
  }

  private void serializePredicates(TTNodeJava node, JsonGenerator gen) throws IOException {
    List<TTIriRef> orderedPredicates = Stream.of(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.
      HAS_STATUS)).toList();
    if (node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE)) != null) {
      for (TTValueJava type : node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE)).getElements()) {
        List<TTIriRef> orderForType = EntityCache.getPredicateOrder(type.asIriRef().getIri());
        if (orderForType != null)
          orderedPredicates = orderForType;
      }
    }
    serializeOrdered(node, orderedPredicates, gen);
  }


  private void serializeOrdered(TTNodeJava node, List<TTIriRef> predicates, JsonGenerator gen) throws IOException {
    for (TTIriRef predicate : predicates) {
      if (node.get(predicate) != null) {
        serializeFieldValue(predicate.getIri(), node.get(predicate), gen);
      }
    }
    Map<TTIriRef, TTArrayJava> nodePredicates = node.getPredicateMap();
    if (nodePredicates != null && !nodePredicates.isEmpty()) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : node.getPredicateMap().entrySet()) {
        if (!predicates.contains(entry.getKey()))
          serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
      }
    }
  }


  public void serializeFieldValue(String field, TTArrayJava value, JsonGenerator gen) throws IOException {
    if (simpleProperties && field.contains("#")) {
      field = field.substring(field.indexOf("#") + 1);
    }
    if (value.isLiteral()) {
      gen.writeFieldName(prefix(field));
      serializeValue(value.asLiteral(), gen);
    } else {
      gen.writeArrayFieldStart(prefix(field));
      for (TTValueJava v : value.iterator()) {
        serializeValue(v, gen);
      }
      gen.writeEndArray();
    }
  }

  public void serializeFieldValue(String field, TTValueJava value, JsonGenerator gen) throws IOException {
    if (value.isLiteral()) {
      if (value.asLiteral().getValue() != null) {
        gen.writeFieldName(prefix(field));
        serializeValue(value, gen);
      }
    } else {
      gen.writeFieldName(prefix(field));
      serializeValue(value, gen);
    }
  }

  public void serializeValue(TTValueJava value, JsonGenerator gen) throws IOException {
    if (value.isIriRef()) {
      TTIriRef ref = value.asIriRef();
      gen.writeStartObject();
      gen.writeStringField("iri", prefix(ref.getIri()));
      if (ref.getName() != null && !ref.getName().isEmpty())
        gen.writeStringField("name", ref.getName());
      gen.writeEndObject();
    } else if (value.isLiteral()) {
      serializeLiteral(value.asLiteral(), gen);
    } else if (value.isNode()) {
      gen.writeStartObject();
      serializeNode((TTNodeJava) value, gen, prov);
      gen.writeEndObject();
    } else {
      prov.defaultSerializeValue(value, gen);
    }
  }

  public void serializeLiteral(TTLiteralJava literal, JsonGenerator gen) throws IOException {
    if (literal.getType() != null) {
      switch (XsdVocab.fromValue(literal.getType().getIri())) {
        case XsdVocab.STRING -> gen.writeString(literal.getValue());
        case XsdVocab.BOOLEAN -> gen.writeBoolean(literal.booleanValue());
        case XsdVocab.INTEGER -> gen.writeNumber(literal.intValue());
        case XsdVocab.LONG -> gen.writeNumber(literal.longValue());
        case XsdVocab.PATTERN -> {
          gen.writeStartObject();
          gen.writeStringField("value", literal.getValue());
          gen.writeStringField("type", prefix(literal.getType().getIri()));
          gen.writeEndObject();
        }
        case null, default -> throw new IOException("Unhandled literal type [" + literal.getType().getIri() + "]");
      }

    } else
      // No type, assume string
      gen.writeString(literal.getValue());
  }

  public String prefix(String iri) {
    if (usePrefixes)
      return contextMap.prefix(iri);
    else
      return contextMap.expand(iri);
  }

  public void serializeContexts(List<TTPrefix> prefixes, JsonGenerator gen) throws IOException {
    if (usePrefixes && prefixes != null && !prefixes.isEmpty()) {
      gen.writeFieldName("context");
      gen.writeStartObject();

      for (TTPrefix prefix : prefixes) {
        contextMap.add(prefix.getIri(), prefix.getPrefix());
        gen.writeStringField(prefix.getPrefix(), prefix.getIri());
      }
      gen.writeFieldName("entities");
      gen.writeStartObject();
      gen.writeStringField("iri", "http://envhealth.info/im#entities");
      gen.writeStringField("container", "set");
      gen.writeEndObject();

      gen.writeEndObject();
    }
  }


}
