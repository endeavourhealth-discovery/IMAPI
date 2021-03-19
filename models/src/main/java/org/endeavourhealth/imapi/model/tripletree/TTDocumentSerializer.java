package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * JSON LD- Serializer for TTDocument (triple tree node with collection of concepts)
 * <p>Uses @context for prefixes and common annotation elements</p>
 */
public class TTDocumentSerializer extends StdSerializer<TTDocument> {
   private Map<String, String> contextMap = new HashMap<>();
   private Map<Class,List<String>> predicateTemplate;

   public TTDocumentSerializer() {
      this(null);
   }

   public TTDocumentSerializer(Class<TTDocument> t) {
      super(t);
   }


   @Override
   public void serialize(TTDocument document, JsonGenerator gen, SerializerProvider prov) throws IOException {

      if (document.getPredicateTemplate()!=null)
         this.predicateTemplate= document.getPredicateTemplate();
      gen.writeStartObject();
      serializeContexts(document.getPrefixes(), gen);
      if (document.getGraph()!=null) {
         gen.writeFieldName("@graph");
         gen.writeStartObject();
         gen.writeStringField("@id", prefix(document.getGraph()));
         gen.writeEndObject();
      }
      if (document.getConcepts()!=null) {
         gen.writeArrayFieldStart("concepts");
         for (TTConcept concept: document.getConcepts()){
            gen.writeStartObject();
            gen.writeStringField("@id",prefix(concept.getIri()));
            serializeNode(concept, gen);
            gen.writeEndObject();
         }
         gen.writeEndArray();
      }
      if (document.getIndividuals()!=null) {
         gen.writeArrayFieldStart("individuals");
         for (TTConcept concept: document.getIndividuals()){
            gen.writeStartObject();
            gen.writeStringField("@id",prefix(concept.getIri()));
            serializeNode(concept, gen);
            gen.writeEndObject();
         }
         gen.writeEndArray();
      }

      gen.writeEndObject();
   }

   private void serializeContexts(List<TTPrefix> prefixes, JsonGenerator gen) throws IOException {
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

   private void serializeNode(TTNode node, JsonGenerator gen) throws IOException {
      HashMap<TTIriRef, TTValue> predicates = node.getPredicateMap();
      if (predicates != null && !predicates.isEmpty()) {
         Set<Map.Entry<TTIriRef, TTValue>> entries = predicates.entrySet();
         if (predicateTemplate!=null &&(predicateTemplate.get(node.getClass())!=null)){
            List<String> order= predicateTemplate.get(node.getClass());
            for (String predicate:order){
               TTValue value= node.get(iri(predicate));
               if (value!=null)
                  serializeFieldValue(predicate,value,gen );
            }
            for(Map.Entry<TTIriRef, TTValue> entry : entries) {
               if (!order.contains(entry.getKey().getIri()))
                  serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
            }

         } else {
            for (Map.Entry<TTIriRef, TTValue> entry : entries) {
               serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
            }
         }
      }
   }

   private void serializeFieldValue(String field, TTValue value, JsonGenerator gen) throws IOException {
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

   private void serializeValue(TTValue value, JsonGenerator gen) throws IOException {
      if (value.isIriRef()) {
         TTIriRef ref = value.asIriRef();
         gen.writeStartObject();
         gen.writeStringField("@id", prefix(ref.getIri()));
         if (ref.getName() != null && !ref.getName().isEmpty())
            gen.writeStringField("name", ref.getName());
         gen.writeEndObject();
      } else if (value.isLiteral()) {
         if (value.asLiteral().getType()!=null){
            gen.writeStartObject();
            gen.writeStringField("@value",value.asLiteral().getValue());
            gen.writeStringField("@type",prefix(value.asLiteral().getType().getIri()));
            gen.writeEndObject();
         } else
            gen.writeString(value.asLiteral().getValue());

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


   private String prefix(String iri) {
      int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
      String path = iri.substring(0, end + 1);
      String prefix = contextMap.get(path);
      if (prefix == null)
         return iri;
      else
         return prefix + ":" + iri.substring(end + 1);
   }
}

