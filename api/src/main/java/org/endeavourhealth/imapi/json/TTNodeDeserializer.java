package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.XSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * DeSerializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument Deserializer
 */
public class TTNodeDeserializer {
    private static final Logger LOG = LoggerFactory.getLogger(TTNodeDeserializer.class);

   private final TTContext context;

   /**
    *
    * @param context the map of prefixes to namespaces in string form.
    */
   public TTNodeDeserializer(TTContext context){
      this.context = context;
   }


   public void populatePrefixesFromJson(JsonNode document,List<TTPrefix> prefixes) {
      JsonNode contextNode = document.get("@context");
      if (contextNode !=null) {
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

   public void populateTTNodeFromJson(TTNode result, JsonNode node) throws IOException {
      Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
      while (iterator.hasNext()) {
         Map.Entry<String, JsonNode> field = iterator.next();
         String key = field.getKey();
         if (!"@context".equals(key)) {
            JsonNode value = field.getValue();
            if ("@id".equals(key))
               result.setIri(expand(value.textValue()));
            else if (value.isArray()) {
               result.set(iri(expand(key)), getArrayNodeAsTripleTreeArray((ArrayNode) value));
            } else {
               result.set(iri(expand(key)), getJsonNodeAsValue(value));
            }
         }
      }
   }

   public TTArray getArrayNodeAsTripleTreeArray(ArrayNode arrayNode) throws IOException {
      TTArray result = new TTArray();

      Iterator<JsonNode> iterator = arrayNode.elements();
      while(iterator.hasNext()) {
         JsonNode value = iterator.next();
         result.add(getJsonNodeAsValue(value));
      }

      return result;
   }

   public TTArray getJsonNodeArrayAsValue(JsonNode node) throws IOException {
       return  getArrayNodeAsTripleTreeArray((ArrayNode) node);
   }

   public TTValue getJsonNodeAsValue(JsonNode node) throws IOException {
       if (node.isValueNode())
           return TTLiteral.literal(node);
       else if (node.isObject()) {
           if (node.has(IM.IRI.iri)) {
               if (node.has("name"))
                   return iri(expand(node.get(IM.IRI.iri).asText()), node.get("name").asText());
               else
                   return iri(expand(node.get(IM.IRI.iri).asText()));
           } else {
               if (node.has(IM.VALUE.iri)) {
                   return getJsonNodeAsLiteral(node);
               } else {
                   TTNode result = new TTNode();
                   populateTTNodeFromJson(result, node);
                   return result;
               }
           }
       } else if (node.isArray()) {
           throw new IOException("Failed to deserialize node array");
       } else {
           LOG.warn("TTNode deserializer - Unhandled node type, reverting to String");
           return TTLiteral.literal(node.asText());
       }
   }

   public TTLiteral getJsonNodeAsLiteral(JsonNode node) throws IOException {
      if (!node.has(IM.TYPE.iri))
         return TTLiteral.literal(node.get(IM.VALUE.iri).textValue());

      TTIriRef type = iri(expand(node.get(IM.TYPE.iri).asText()));
      if (XSD.STRING.iri.equals(type.getIri()))
         return TTLiteral.literal(node.get(IM.VALUE.iri).textValue());
      else if (XSD.BOOLEAN.iri.equals(type.getIri())) {
         return TTLiteral.literal(Boolean.valueOf(node.get(IM.VALUE.iri).asText()));
      }
      else if (XSD.INTEGER.iri.equals(type.getIri())) {
         return TTLiteral.literal(Integer.valueOf(node.get(IM.VALUE.iri).asText()));
      }
      else if (XSD.PATTERN.iri.equals(type.getIri()))
         return TTLiteral.literal(Pattern.compile(node.get(IM.VALUE.iri).textValue()));
      else
         throw new IOException("Unhandled literal type ["+type.getIri()+"]");
   }

   public String expand(String iri) {
        return context.expand(iri);
   }

}
