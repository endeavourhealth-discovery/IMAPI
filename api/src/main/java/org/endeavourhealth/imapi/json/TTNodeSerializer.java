package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTNodeSerializer {
  public static final String SIMPLE_PROPERTIES = "SIMPLE_PROPERTIES";
  private final TTContext contextMap;
  private boolean usePrefixes = false;
  private SerializerProvider prov;
  private Boolean simpleProperties;

  /**
   * @param contextMap the context object for the JSON-LD document
   */
  public TTNodeSerializer(TTContext contextMap) {
    this.contextMap = contextMap;
  }

  public TTNodeSerializer(TTContext contextMap, boolean usePrefixes) {
    this.contextMap = contextMap;
    this.usePrefixes = usePrefixes;
  }


  public void serializeNode(TTNode node, JsonGenerator gen, SerializerProvider prov) throws IOException {
    this.prov = prov;
    simpleProperties = (Boolean) prov.getAttribute(TTNodeSerializer.SIMPLE_PROPERTIES);
    simpleProperties = (simpleProperties != null && simpleProperties);
    if ((!(node instanceof TTEntity)) && node.getIri()!=null)
      gen.writeStringField("@id", prefix(node.getIri()));
    serializePredicates(node, gen, prov);
  }

  private void serializePredicates(TTNode node, JsonGenerator gen, SerializerProvider prov) throws IOException {
    List<TTIriRef> orderedPredicates = Stream.of(RDF.TYPE,RDFS.LABEL,RDFS.COMMENT,IM.HAS_STATUS).collect(Collectors.toList());
    if (node.get(RDF.TYPE) != null) {
      for (TTValue type : node.get(RDF.TYPE).getElements()) {
        List<TTIriRef> orderForType= EntityCache.getPredicateOrder(type.asIriRef().getIri());
        if (orderForType!=null)
          orderedPredicates= orderForType;
      }
    }
    serializeOrdered(node, orderedPredicates, gen);
  }


  private void serializeOrdered(TTNode node, List<TTIriRef> predicates, JsonGenerator gen) throws IOException {
    for (TTIriRef predicate:predicates){
      if (node.get(predicate)!=null) {
        serializeFieldValue(predicate.getIri(), node.get(predicate), gen);
      }
    }
    Map<TTIriRef, TTArray> nodePredicates = node.getPredicateMap();
    if (nodePredicates != null && !nodePredicates.isEmpty()) {
      for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
        if (!predicates.contains(entry.getKey()))
          serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
      }
    }
  }


    public void serializeFieldValue(String field, TTArray value, JsonGenerator gen) throws IOException {
        if(simpleProperties){
          if (field.contains("#"))
            field = field.substring(field.indexOf("#")+1);
        }
        if (value.isLiteral()) {
            gen.writeFieldName(prefix(field));
            serializeValue(value.asLiteral(), gen);
        } else {
            gen.writeArrayFieldStart(prefix(field));
            for (TTValue v : value.iterator()) {
                serializeValue(v, gen);
            }
            gen.writeEndArray();
        }
    }

   public void serializeFieldValue(String field, TTValue value, JsonGenerator gen) throws IOException {
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

   public void serializeValue(TTValue value, JsonGenerator gen) throws IOException {
      if (value.isIriRef()) {
         TTIriRef ref = value.asIriRef();
         gen.writeStartObject();
         gen.writeStringField("@id", prefix(ref.getIri()));
         if (ref.getName() != null && !ref.getName().isEmpty())
            gen.writeStringField("name", ref.getName());
         gen.writeEndObject();
      } else if (value.isLiteral()) {
         serializeLiteral(value.asLiteral(), gen);
      } else if (value.isNode()) {
         gen.writeStartObject();
         serializeNode((TTNode)value, gen,prov);
         gen.writeEndObject();
      } else {
        prov.defaultSerializeValue(value, gen);
      }
   }

   public void serializeLiteral(TTLiteral literal, JsonGenerator gen) throws IOException {
      if (literal.getType()!=null){
         if (XSD.STRING.equals(literal.getType()))
            gen.writeString(literal.getValue());
         else if (XSD.BOOLEAN.equals(literal.getType()))
            gen.writeBoolean(literal.booleanValue());
         else if (XSD.INTEGER.equals(literal.getType()))
            gen.writeNumber(literal.intValue());
         else if (XSD.LONG.equals(literal.getType()))
             gen.writeNumber(literal.longValue());
         else if (XSD.PATTERN.equals(literal.getType())) {
             gen.writeStartObject();
             gen.writeStringField("@value", literal.getValue());
             gen.writeStringField("@type",prefix(literal.getType().getIri()));
             gen.writeEndObject();
         } else
            throw new IOException("Unhandled literal type ["+literal.getType().getIri()+"]");

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
         gen.writeFieldName("@context");
         gen.writeStartObject();

         for(TTPrefix prefix : prefixes) {
            contextMap.add(prefix.getIri(), prefix.getPrefix());
            gen.writeStringField(prefix.getPrefix(),prefix.getIri());
         }
         gen.writeFieldName("entities");
         gen.writeStartObject();
         gen.writeStringField("@id","http://envhealth.info/im#entities");
         gen.writeStringField("@container","@set");
         gen.writeEndObject();

         gen.writeEndObject();
      }
   }


}
