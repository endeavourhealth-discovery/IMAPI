package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTNodeSerializer {
   private final TTContext contextMap;
   private List<TTIriRef> predicateTemplate;
   private boolean usePrefixes = false;
   /**
    *
    * @param contextMap the context object for the JSON-LD document
    */
   public TTNodeSerializer(TTContext contextMap){
      this.contextMap = contextMap;
   }
   public TTNodeSerializer(TTContext contextMap,List<TTIriRef> predicateTemplate){
      this.contextMap = contextMap;
      this.predicateTemplate= predicateTemplate;
   }
    public TTNodeSerializer(TTContext contextMap, boolean usePrefixes){
        this.contextMap = contextMap;
        this.usePrefixes = usePrefixes;
    }
    public TTNodeSerializer(TTContext contextMap,List<TTIriRef> predicateTemplate, boolean usePrefixes){
        this.contextMap= contextMap;
        this.predicateTemplate= predicateTemplate;
        this.usePrefixes = usePrefixes;
    }

    public void serializeNode(TTNode node, JsonGenerator gen) throws IOException {
       if (node.getPredicateTemplate()!=null)
          serializeOrdered(node, Arrays.asList(node.getPredicateTemplate()),gen);
      else if (predicateTemplate!=null)
         serializeOrdered(node,predicateTemplate,gen);
      else {
         Map<TTIriRef, TTValue> predicates = node.getPredicateMap();
         if (predicates != null && !predicates.isEmpty()) {
            Set<Map.Entry<TTIriRef, TTValue>> entries = predicates.entrySet();
            for (Map.Entry<TTIriRef, TTValue> entry : entries) {
               serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
            }
         }
      }
   }

   private void serializeOrdered(TTNode node, List<TTIriRef> predicateTemplate,JsonGenerator gen) throws IOException {
      for (TTIriRef predicate:predicateTemplate){
         if (node.get(predicate)!=null)
            serializeFieldValue(predicate.getIri(),node.get(predicate),gen);
      }
      Map<TTIriRef, TTValue> predicates = node.getPredicateMap();
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
         for (TTValue v : value.getElements()) {
            serializeValue(v, gen);
         }
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
         gen.writeFieldName("individuals");
         gen.writeStartObject();
         gen.writeStringField("@id","http://envhealth.info/im#individuals");
         gen.writeStringField("@container","@set");
         gen.writeEndObject();

         gen.writeEndObject();
      }
   }


}
