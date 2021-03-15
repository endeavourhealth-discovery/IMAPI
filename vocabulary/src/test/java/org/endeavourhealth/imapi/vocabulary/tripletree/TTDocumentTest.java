package org.endeavourhealth.imapi.vocabulary.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTDocumentTest {

   @Test
   public void serializeTest() throws JsonProcessingException {
      TTDocument document= getTestDocument();

      ObjectMapper om = new ObjectMapper();
      String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(document);

      System.out.println(json);
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
      Assert.assertEquals(first.getGraph(),second.getGraph());
      Assert.assertEquals(first.getConcepts().get(0).getIri(),
          second.getConcepts().get(0).getIri());
   }




   public TTDocument getTestDocument() {
      TTDocument result= new TTDocument();
      result.setGraph("http://testgraph");
      result.addPrefix(new TTPrefix("http://endhealth.info/im#","im:"));
      result.addConcept( new TTConcept("http://endhealth.info/im#TestConcept")
          .set(IM.MODELTYPE,IM.VALUESET)
          .set(RDFS.LABEL, literal("Adverse reaction to Amlodipine Besilate"))
          .set(IM.CODE, literal("25451000252115"))
          .set(IM.HAS_SCHEME, iri("http://snomed.info/sct#891071000252105"))

          .set(RDF.TYPE, OWL.CLASS)
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
