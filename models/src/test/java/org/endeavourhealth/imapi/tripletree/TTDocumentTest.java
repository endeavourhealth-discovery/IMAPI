package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;

public class TTDocumentTest {

   @Test
   public void serializeTest() throws JsonProcessingException {
       TTDocument document = getTestDocument();

       ObjectMapper om = new ObjectMapper();
       String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(document);
       String expected = new StringJoiner(System.lineSeparator())
           .add("{")
           .add("  \"@graph\" : {")
           .add("    \"@id\" : \"http://testgraph\"")
           .add("  },")
           .add("  \"entities\" : [ {")
           .add("    \"@id\" : \"http://endhealth.info/im#TestEntity\",")
           .add("    \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {")
           .add("      \"@id\" : \"http://endhealth.info/im#ValueSet\"")
           .add("    },")
           .add("    \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Adverse reaction to Amlodipine Besilate\",")
           .add("    \"http://endhealth.info/im#code\" : \"25451000252115\",")
           .add("    \"http://endhealth.info/im#scheme\" : {")
           .add("      \"@id\" : \"http://snomed.info/sct#891071000252105\"")
           .add("    },")
           .add("    \"http://www.w3.org/2002/07/owl#equivalentClass\" : [ {")
           .add("      \"http://www.w3.org/2002/07/owl#intersectionOf\" : [ {")
           .add("        \"@id\" : \"http://snomed.info/sct#62014003\"")
           .add("      }, {")
           .add("        \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {")
           .add("          \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"")
           .add("        },")
           .add("        \"http://www.w3.org/2002/07/owl#someValuesFrom\" : {")
           .add("          \"@id\" : \"http://snomed.info/sct#384976003\"")
           .add("        },")
           .add("        \"http://www.w3.org/2002/07/owl#onProperty\" : {")
           .add("          \"@id\" : \"http://snomed.info/sct#246075003\"")
           .add("        }")
           .add("      } ]")
           .add("    } ]")
           .add("  } ]")
           .add("}")
           .toString();

       assertEquals(expected, actual);
   }

   @Test
   public void deserializeTest() throws JsonProcessingException {
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


   public TTDocument getTestDocument() {
      TTDocument result= new TTDocument();
      result.setGraph(iri("http://testgraph"));
      result.addPrefix(new TTPrefix("http://endhealth.info/im#","im"));
      result.addEntity( new TTEntity("http://endhealth.info/im#TestEntity")
          .set(RDF.TYPE,TTIriRef.iri("im:ValueSet"))
          .set(RDFS.LABEL, literal("Adverse reaction to Amlodipine Besilate"))
          .set(IM.CODE,literal("25451000252115").setType(XSD.STRING))
          .set(IM.HAS_SCHEME, iri("http://snomed.info/sct#891071000252105"))
          .set(OWL.EQUIVALENTCLASS, new TTArray()
              .add(new TTNode()
                  .set(OWL.INTERSECTIONOF, new TTArray()
                      .add(iri("http://snomed.info/sct#62014003"))
                      .add(new TTNode()
                          .set(RDF.TYPE, OWL.RESTRICTION)
                          .set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003"))
                          .set(OWL.SOMEVALUESFROM, iri("http://snomed.info/sct#384976003"))
                      )
                  )
              )
          ));
      return result;
   }

}
