package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

/**
 * DeSerializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument Deserializer
 */
public class TTNodeDeserializer {
   private TTContext context;

   /**
    *
    * @param context the map of prefixes to namespaces in string form.
    */
   public TTNodeDeserializer(TTContext context){
      this.context = context;
   }


   public void populatePrefixesFromJson(JsonNode document,List<TTPrefix> prefixes) {
      JsonNode contextNode = document.get("@context");
      if (contextNode !=null){
         Iterator<Map.Entry<String, JsonNode>> fields = contextNode.fields();
         while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key= field.getKey();
            JsonNode value= field.getValue();
            if (value.isTextual())
               if (value.textValue().startsWith("http")) {
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
            if (value.isArray()) {
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

   public TTValue getJsonNodeAsValue(JsonNode node) throws IOException {
      if (node.isValueNode())
         return literal(node);
      else if (node.isObject()) {
         if (node.has("@id")) {
            if (node.has("name"))
               return iri(expand(node.get("@id").asText()), node.get("name").asText());
            else
               return iri(expand(node.get("@id").asText()));
         } else {
            if (node.has("@value")){
               return getJsonNodeAsLiteral(node);
            } else {
               TTNode result = new TTNode();
               populateTTNodeFromJson(result, node);
               return result;
            }
         }
      } else if (node.isArray()) {
         return  getArrayNodeAsTripleTreeArray((ArrayNode) node);
      } else {
          System.err.println("TTNode deserializer - Unhandled node type, reverting to String");
         return literal(node.asText());
      }
   }

   public TTLiteral getJsonNodeAsLiteral(JsonNode node) throws IOException {
      if (!node.has("@type"))
         return literal(node.get("@value").textValue());

      TTIriRef type = iri(expand(node.get("@type").asText()));
      if (XSD.STRING.equals(type))
         return literal(node.get("@value").textValue());
      else if (XSD.BOOLEAN.equals(type)) {
         return literal(Boolean.valueOf(node.get("@value").asText()));
      }
      else if (XSD.INTEGER.equals(type)) {
         return literal(Integer.valueOf(node.get("@value").asText()));
      }
      else if (XSD.PATTERN.equals(type))
         return literal(Pattern.compile(node.get("@value").textValue()));
      else
         throw new IOException("Unhandled literal type ["+type.getIri()+"]");
   }

   public String expand(String iri) {
        return context.expand(iri);
   }

}
