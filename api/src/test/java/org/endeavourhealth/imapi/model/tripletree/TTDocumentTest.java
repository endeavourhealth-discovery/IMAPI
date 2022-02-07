package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TTDocumentTest {

   @Test
   void serializeTest() throws JsonProcessingException {
       TTDocument document = getTestDocument();

       ObjectMapper om = new ObjectMapper();
       String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(document);
       String expected = new StringJoiner(System.lineSeparator())
           .add("{")
           .add("  \"@graph\" : {")
           .add("    \"@id\" : \"http://testgraph\"")
           .add("  },")
           .add("  \"entities\" : [ {")
           .add("    \"@id\" : \"http://endhealth.info/im#25451000252115\",")
           .add("    \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {")
           .add("      \"@id\" : \"http://endhealth.info/im#Concept\"")
           .add("    } ],")
           .add("    \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Partial amputation of toe of left foot\",")
           .add("    \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Partial amputation of toe of left foot (procedure)\",")
           .add("    \"http://endhealth.info/im#code\" : \"787213005\",")
           .add("    \"http://endhealth.info/im#scheme\" : [ {")
           .add("      \"@id\" : \"http://snomed.info/sct#\"")
           .add("    } ],")
           .add("    \"http://endhealth.info/im#isA\" : [ {")
           .add("      \"@id\" : \"http://snomed.info/sct#371186005\",")
           .add("      \"name\" : \"Amputation of toe (procedure)\"")
           .add("    }, {")
           .add("      \"@id\" : \"http://snomed.info/sct#732214009\",")
           .add("      \"name\" : \"Amputation of left lower limb\"")
           .add("    } ],")
           .add("    \"http://endhealth.info/im#roleGroup\" : [ {")
           .add("      \"http://snomed.info/sct#260686004\" : [ {")
           .add("        \"@id\" : \"http://snomed.info/sct#129309007\",")
           .add("        \"name\" : \"Amputation - action\"")
           .add("      } ],")
           .add("      \"http://snomed.info/sct#405813007\" : [ {")
           .add("        \"@id\" : \"http://snomed.info/sct#732939008\",")
           .add("        \"name\" : \"Part of toe of left foot\"")
           .add("      } ]")
           .add("    } ]")
           .add("  } ]")
           .add("}")
           .toString();

       assertEquals(expected, actual);
   }

   @Test
   void deserializeTest() throws JsonProcessingException {
      TTDocument first = getTestDocument();

      // Serialize
      ObjectMapper om = new ObjectMapper();
      String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(first);

      // Deserialize
      TTDocument second = om.readValue(json, TTDocument.class);

      checkDocument(first,second);
   }

   private void checkDocument(TTDocument first,TTDocument second) {
      assertEquals(first.getGraph(),second.getGraph());
      assertEquals(first.getEntities().get(0).getIri(),
          second.getEntities().get(0).getIri());
   }


   private TTDocument getTestDocument() {
       TTDocument result = new TTDocument();
       result.setGraph(iri("http://testgraph"));
       result.addPrefix(new TTPrefix("http://endhealth.info/im#", "im"));
       result.addEntity(TestHelper.getTestEntity());
       return result;
   }

}
