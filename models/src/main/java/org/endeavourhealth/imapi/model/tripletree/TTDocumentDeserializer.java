package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTDocumentDeserializer extends StdDeserializer<TTDocument> {
   private Map<String, String> prefixMap = new HashMap<>();

   public TTDocumentDeserializer() {
      this(null);
   }

   public TTDocumentDeserializer(Class<TTDocument> vc) {
      super(vc);
   }

   @Override
   public TTDocument deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      JsonNode node = jsonParser.getCodec().readTree(jsonParser);

      TTDocument result = new TTDocument();

      populatePrefixesFromJson(result, node);
      if (node.get("@graph")!=null)
         result.setGraph(node.get("@graph").get("@id").asText());
      if (node.get("concepts")!=null) {
         result.setConcepts(getConcepts(node.withArray("concepts")));
      }
      if (node.get("individuals")!=null){
         result.setIndividuals(getConcepts(node.withArray("individuals")));
      }

      return result;
   }

   private void populatePrefixesFromJson(TTDocument result, JsonNode document) {
      JsonNode context= document.get("@context");
      if (context!=null){
         Iterator<Map.Entry<String, JsonNode>> fields = context.fields();
         while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key= field.getKey();
            JsonNode value= field.getValue();
            if (value.isTextual())
               if (value.textValue().startsWith("http:")) {
                  result.addPrefix(new TTPrefix(value.textValue(), key));
                  prefixMap.put(key,value.asText());
               }
         }
      }

   }

   private void populateTTNodeFromJson(TTNode result, JsonNode node) {
      Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
      while (iterator.hasNext()) {
         Map.Entry<String, JsonNode> field = iterator.next();
         String key = field.getKey();
         JsonNode value = field.getValue();
         if (value.isArray()) {
               result.set(iri(expand(field.getKey())), getArrayNodeAsTripleTreeArray((ArrayNode) value));
         } else {
               result.set(iri(expand(field.getKey())), getJsonNodeAsValue(value));
            }
         }
   }

   private TTArray getArrayNodeAsTripleTreeArray(ArrayNode arrayNode) {
      TTArray result = new TTArray();

      Iterator<JsonNode> iterator = arrayNode.elements();
      while(iterator.hasNext()) {
         JsonNode value = iterator.next();
         result.add(getJsonNodeAsValue(value));
      }

      return result;
   }
   private List<TTConcept> getConcepts(ArrayNode arrayNode) {
      List result = new ArrayList();
      Iterator<JsonNode> iterator = arrayNode.elements();
      while(iterator.hasNext()) {
         JsonNode conceptNode = iterator.next();
         TTConcept concept= new TTConcept();
         result.add(concept);
         Iterator<Map.Entry<String, JsonNode>> fields = conceptNode.fields();
         while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (field.getKey().equals("@id"))
               concept.setIri(expand(field.getValue().textValue()));
            else
               concept.set(iri(expand(field.getKey())),getJsonNodeAsValue(field.getValue()));
         }
      }

      return result;
   }

   private TTValue getJsonNodeAsValue(JsonNode node) {
      if (node.isTextual())
         return literal(node.asText());
      else if (node.isObject()) {
         if (node.has("@id")) {
               if (node.has("name"))
                  return iri(expand(node.get("@id").asText()), node.get("name").asText());
               else
                  return iri(expand(node.get("@id").asText()));
            } else {
            TTNode result = new TTNode();
            populateTTNodeFromJson(result, node);
            return result;
         }
      } else if (node.isArray()) {
         return  getArrayNodeAsTripleTreeArray((ArrayNode) node);
      } else {
         System.err.println("Unhandled node type!");
         return null;
      }
   }

   private String expand(String iri) {
      String prefix = iri.substring(0, iri.indexOf(":") + 1);
      String path = prefixMap.get(prefix);
      if (path == null)
         return iri;
      else
         return iri.replace(prefix, path);
   }
}
