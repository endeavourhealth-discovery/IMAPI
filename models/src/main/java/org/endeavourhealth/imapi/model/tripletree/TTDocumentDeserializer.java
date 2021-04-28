package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTDocumentDeserializer extends StdDeserializer<TTDocument> {
   private TTContext context = new TTContext();
   private TTNodeDeserializer helper;

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
      helper= new TTNodeDeserializer(context);

      List<TTPrefix> prefixes = new ArrayList<>();

      helper.populatePrefixesFromJson(node,prefixes);
      if (!prefixes.isEmpty())
         result.setContext(context);
      if (node.get("@graph")!=null)
         result.setGraph(iri(helper.expand(node.get("@graph").get("@id").asText())));
      if (node.get("concepts")!=null) {
         result.setConcepts(getConcepts(node.withArray("concepts")));
      }
      if (node.get("individuals")!=null){
         result.setIndividuals(getInstances(node.withArray("individuals")));
      }

      return result;
   }



   private List<TTConcept> getConcepts(ArrayNode arrayNode) throws IOException {
      List result = new ArrayList();
      Iterator<JsonNode> iterator = arrayNode.elements();
      while(iterator.hasNext()) {
         JsonNode conceptNode = iterator.next();
         TTConcept concept= new TTConcept();
         result.add(concept);
         Iterator<Map.Entry<String, JsonNode>> fields = conceptNode.fields();
         while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (field.getKey().equals("@id")) {
               concept.setIri(helper.expand(field.getValue().textValue()));
              // System.out.println(concept.getIri());
            }
            else
               concept.set(iri(helper.expand(field.getKey())),helper.getJsonNodeAsValue(field.getValue()));
         }
      }

      return result;
   }


   private List<TTInstance> getInstances(ArrayNode arrayNode) throws IOException {
      List result = new ArrayList();
      Iterator<JsonNode> iterator = arrayNode.elements();
      while(iterator.hasNext()) {
         JsonNode instanceNode = iterator.next();
         TTInstance instance= new TTInstance();
         result.add(instance);
         Iterator<Map.Entry<String, JsonNode>> fields = instanceNode.fields();
         while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (field.getKey().equals("@id")) {
               instance.setIri(helper.expand(field.getValue().textValue()));
            }
            else
               instance.set(iri(helper.expand(field.getKey())),helper.getJsonNodeAsValue(field.getValue()));
         }
      }

      return result;
   }

}
