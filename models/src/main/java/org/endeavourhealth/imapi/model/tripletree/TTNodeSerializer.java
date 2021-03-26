package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonGenerator;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTConcept or TTDocument serializer
 */
public class TTNodeSerializer {
   private Map<String, String> contextMap = new HashMap<>();
   private List<TTIriRef> predicateTemplate;
   /**
    *
    * @param contextMap the context object for the JSON-LD document
    */
   public TTNodeSerializer(Map<String, String> contextMap){
      this.contextMap= contextMap;
   }
   public TTNodeSerializer(Map<String, String> contextMap,List<TTIriRef> predicateTemplate){
      this.contextMap= contextMap;
      this.predicateTemplate= predicateTemplate;
   }

   public void serializeNode(TTNode node, JsonGenerator gen) throws IOException {
      if (predicateTemplate!=null)
         serializeOrdered(node,gen);
      else {
         HashMap<TTIriRef, TTValue> predicates = node.getPredicateMap();
         if (predicates != null && !predicates.isEmpty()) {
            Set<Map.Entry<TTIriRef, TTValue>> entries = predicates.entrySet();
            for (Map.Entry<TTIriRef, TTValue> entry : entries) {
               serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
            }
         }
      }
   }

   private void serializeOrdered(TTNode node, JsonGenerator gen) throws IOException {
      for (TTIriRef predicate:predicateTemplate){
         if (node.get(predicate)!=null)
            serializeFieldValue(predicate.getIri(),node.get(predicate),gen);
      }
      HashMap<TTIriRef, TTValue> predicates = node.getPredicateMap();
      if (predicates != null && !predicates.isEmpty()) {
         Set<Map.Entry<TTIriRef, TTValue>> entries = predicates.entrySet();
         for (Map.Entry<TTIriRef, TTValue> entry : entries) {
            if (!predicateTemplate.contains(entry.getKey()))
               serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
         }
      }

   }

   public void serializeFieldValue(String field, TTValue value, JsonGenerator gen) throws IOException {
      if (value.isList()) {
         gen.writeArrayFieldStart(prefix(field));
         serializeValue(value, gen);
         gen.writeEndArray();
      } else {
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
         serializeNode((TTNode)value, gen);
         gen.writeEndObject();
      } else if (value.isList()) {
         for (TTValue v : ((TTArray)value).elements) {
            serializeValue(v, gen);
         }
      }
   }

   public void serializeLiteral(TTLiteral literal, JsonGenerator gen) throws IOException {
      if (literal.getType()!=null){
         gen.writeStartObject();
         if (XSD.STRING.equals(literal.getType()))
            gen.writeStringField("@value", literal.getValue());
         else if (XSD.BOOLEAN.equals(literal.getType()))
            gen.writeBooleanField("@value", literal.booleanValue());
         else if (XSD.INTEGER.equals(literal.getType()))
            gen.writeNumberField("@value", literal.intValue());
         else if (XSD.LONG.equals(literal.getType()))
            gen.writeNumberField("@value", literal.longValue());
         else if (XSD.PATTERN.equals(literal.getType()))
            gen.writeStringField("@value", literal.getValue());
         else
            throw new IOException("Unhandled literal type ["+literal.getType().getIri()+"]");

         gen.writeStringField("@type",prefix(literal.getType().getIri()));
         gen.writeEndObject();
      } else
         // No type, assume string
         gen.writeString(literal.getValue());
   }

   public String prefix(String iri) {
      int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
      String path = iri.substring(0, end + 1);
      String prefix = contextMap.get(path);
      if (prefix == null)
         return iri;
      else
         return prefix + ":" + iri.substring(end + 1);
   }

   public void serializeContexts(List<TTPrefix> prefixes, JsonGenerator gen) throws IOException {
      if (prefixes != null && !prefixes.isEmpty()) {
         gen.writeFieldName("@context");
         gen.writeStartObject();

         for(TTPrefix prefix : prefixes) {
            contextMap.put(prefix.getIri(), prefix.getPrefix());
            gen.writeStringField(prefix.getPrefix(),prefix.getIri());
         }
         gen.writeFieldName("concepts");
         gen.writeStartObject();
         gen.writeStringField("@id","http://envhealth.info/im#concepts");
         gen.writeStringField("@container","@set");
         gen.writeEndObject();
         gen.writeFieldName("individuals");
         gen.writeStartObject();
         gen.writeStringField("@id","http://envhealth.info/im#individuals");
         gen.writeStringField("@container","@set");
         gen.writeEndObject();

         gen.writeEndObject();
      }
   }


}
