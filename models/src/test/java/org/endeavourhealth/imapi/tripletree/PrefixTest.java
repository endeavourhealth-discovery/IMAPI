package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.Assert;
import org.junit.Test;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class PrefixTest {
    @Test
    public void prefixSerializationConceptTest() throws JsonProcessingException {
        TTConcept c = new TTConcept()
            .addPrefix(IM.NAMESPACE,"im")
            .addPrefix(SNOMED.NAMESPACE,"sn")
            .addPrefix(OWL.NAMESPACE,"owl")
            .addPrefix(RDF.NAMESPACE,"rdf")
            .addPrefix(RDFS.NAMESPACE,"rdfs")
            .addPrefix(XSD.NAMESPACE,"xsd")
            .addPrefix("http://endhealth.info/READ2#","r2")
            .setIri("emis:H33")
            .setName("Asthma")
            .setDescription("Asthma")
            .setCode("H33")
            .setScheme(IM.CODE_SCHEME_EMIS)
            .set(IM.SYNONYM, new TTArray().add(
                new TTNode()
                    .set(IM.CODE, literal("11"))
                    .set(RDFS.LABEL, literal("Bronchial asthma"))
            ))
            .set(IM.IS_CHILD_OF, iri("r2:H3..."))
            .set(IM.HAS_MAP, new TTArray().add(
               new TTNode().set(IM.MATCHED_TO, new TTArray().add(iri("sn:195967001")))
            ));

        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(c);
        System.out.println(json);

        System.out.println("===========================================================================================");
        json = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(c);
        System.out.println(json);
    }

    @Test
    public void prefixSerializationDocumentTest() throws JsonProcessingException {
        TTDocument document = new TTDocument();

        document.addPrefix(IM.NAMESPACE,"im");
        document.addPrefix(SNOMED.NAMESPACE,"sn");
        document.addPrefix(OWL.NAMESPACE,"owl");
        document.addPrefix(RDF.NAMESPACE,"rdf");
        document.addPrefix(RDFS.NAMESPACE,"rdfs");
        document.addPrefix(XSD.NAMESPACE,"xsd");
        document.addPrefix("http://endhealth.info/READ2#","r2");

        TTConcept c = new TTConcept()
            .setIri("emis:H33")
            .setName("Asthma")
            .setDescription("Asthma")
            .setCode("H33")
            .setScheme(IM.CODE_SCHEME_EMIS)
            .set(IM.SYNONYM, new TTArray().add(
                new TTNode()
                    .set(IM.CODE, literal("11"))
                    .set(RDFS.LABEL, literal("Bronchial asthma"))
            ))
            .set(IM.IS_CHILD_OF, iri("r2:H3..."))
           .set(IM.HAS_MAP, new TTArray().add(
              new TTNode().set(IM.MATCHED_TO, new TTArray().add(iri("sn:195967001")))
        ));

        document.addConcept(c);

        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        System.out.println(json);

        System.out.println("===========================================================================================");
        json = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
        System.out.println(json);
    }

    @Test
    public void prefixSerializationConceptInDocumentTest() throws JsonProcessingException {
        TTDocument document = new TTDocument();

        document.addPrefix(IM.NAMESPACE,"im");
        document.addPrefix(SNOMED.NAMESPACE,"sn");
        document.addPrefix(OWL.NAMESPACE,"owl");
        document.addPrefix(RDF.NAMESPACE,"rdf");
        document.addPrefix(RDFS.NAMESPACE,"rdfs");
        document.addPrefix(XSD.NAMESPACE,"xsd");
        document.addPrefix("http://endhealth.info/READ2#","r2");

        TTConcept c = new TTConcept()
            .setIri("emis:H33")
            .setName("Asthma")
            .setDescription("Asthma")
            .setCode("H33")
            .setScheme(IM.CODE_SCHEME_EMIS)
            .set(IM.SYNONYM, new TTArray().add(
                new TTNode()
                    .set(IM.CODE, literal("11"))
                    .set(RDFS.LABEL, literal("Bronchial asthma"))
            ))
            .set(IM.IS_CHILD_OF, iri("r2:H3..."))
            .set(IM.HAS_MAP, new TTArray().add(
                new TTNode().set(IM.MATCHED_TO, new TTArray().add(iri("sn:195967001")))
            ));

        document.addConcept(c);

        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(c);
        System.out.println(json);

        System.out.println("===========================================================================================");
        json = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(c);
        System.out.println(json);
    }
}
