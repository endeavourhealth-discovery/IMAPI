package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.Test;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;

public class PrefixTest {
    @Test
    public void prefixSerializationEntityTest() throws JsonProcessingException {
        TTEntity c = new TTEntity()
            .addPrefix(IM.NAMESPACE, "im")
            .addPrefix(SNOMED.NAMESPACE, "sn")
            .addPrefix(OWL.NAMESPACE, "owl")
            .addPrefix(RDF.NAMESPACE, "rdf")
            .addPrefix(RDFS.NAMESPACE, "rdfs")
            .addPrefix(XSD.NAMESPACE, "xsd")
            .addPrefix("http://endhealth.info/READ2#", "r2")
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
        String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(c);
        String expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@id\" : \"emis:H33\",")
            .add("  \"http://endhealth.info/im#code\" : \"H33\",")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Asthma\",")
            .add("  \"http://endhealth.info/im#scheme\" : {")
            .add("    \"@id\" : \"http://endhealth.info/im#EMISCodeScheme\"")
            .add("  },")
            .add("  \"http://endhealth.info/im#hasMap\" : [ {")
            .add("    \"http://endhealth.info/im#matchedTo\" : [ {")
            .add("      \"@id\" : \"http://snomed.info/sct#195967001\"")
            .add("    } ]")
            .add("  } ],")
            .add("  \"http://endhealth.info/im#synonym\" : [ {")
            .add("    \"http://endhealth.info/im#code\" : \"11\",")
            .add("    \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Bronchial asthma\"")
            .add("  } ],")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Asthma\",")
            .add("  \"http://endhealth.info/im#isChildOf\" : {")
            .add("    \"@id\" : \"http://endhealth.info/READ2#H3...\"")
            .add("  }")
            .add("}")
            .toString();

        assertEquals(expected, actual);

        actual = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(c);
        expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@context\" : {")
            .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
            .add("    \"im\" : \"http://endhealth.info/im#\",")
            .add("    \"owl\" : \"http://www.w3.org/2002/07/owl#\",")
            .add("    \"r2\" : \"http://endhealth.info/READ2#\",")
            .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
            .add("    \"sn\" : \"http://snomed.info/sct#\",")
            .add("    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",")
            .add("    \"entities\" : {")
            .add("      \"@id\" : \"http://envhealth.info/im#entities\",")
            .add("      \"@container\" : \"@set\"")
            .add("    },")
            .add("    \"individuals\" : {")
            .add("      \"@id\" : \"http://envhealth.info/im#individuals\",")
            .add("      \"@container\" : \"@set\"")
            .add("    }")
            .add("  },")
            .add("  \"@id\" : \"emis:H33\",")
            .add("  \"im:code\" : \"H33\",")
            .add("  \"rdfs:comment\" : \"Asthma\",")
            .add("  \"im:scheme\" : {")
            .add("    \"@id\" : \"im:EMISCodeScheme\"")
            .add("  },")
            .add("  \"im:hasMap\" : [ {")
            .add("    \"im:matchedTo\" : [ {")
            .add("      \"@id\" : \"sn:195967001\"")
            .add("    } ]")
            .add("  } ],")
            .add("  \"im:synonym\" : [ {")
            .add("    \"im:code\" : \"11\",")
            .add("    \"rdfs:label\" : \"Bronchial asthma\"")
            .add("  } ],")
            .add("  \"rdfs:label\" : \"Asthma\",")
            .add("  \"im:isChildOf\" : {")
            .add("    \"@id\" : \"r2:H3...\"")
            .add("  }")
            .add("}")
            .toString();
        assertEquals(expected, actual);
    }

    @Test
    public void prefixSerializationDocumentTest() throws JsonProcessingException {
        TTDocument document = new TTDocument();

        document.addPrefix(IM.NAMESPACE, "im");
        document.addPrefix(SNOMED.NAMESPACE, "sn");
        document.addPrefix(OWL.NAMESPACE, "owl");
        document.addPrefix(RDF.NAMESPACE, "rdf");
        document.addPrefix(RDFS.NAMESPACE, "rdfs");
        document.addPrefix(XSD.NAMESPACE, "xsd");
        document.addPrefix("http://endhealth.info/READ2#", "r2");

        TTEntity c = new TTEntity()
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

        document.addEntity(c);

        ObjectMapper om = new ObjectMapper();
        String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        String expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"entities\" : [ {")
            .add("    \"@id\" : \"emis:H33\",")
            .add("    \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Asthma\",")
            .add("    \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Asthma\",")
            .add("    \"http://endhealth.info/im#code\" : \"H33\",")
            .add("    \"http://endhealth.info/im#scheme\" : {")
            .add("      \"@id\" : \"http://endhealth.info/im#EMISCodeScheme\"")
            .add("    },")
            .add("    \"http://endhealth.info/im#hasMap\" : [ {")
            .add("      \"http://endhealth.info/im#matchedTo\" : [ {")
            .add("        \"@id\" : \"http://snomed.info/sct#195967001\"")
            .add("      } ]")
            .add("    } ],")
            .add("    \"http://endhealth.info/im#synonym\" : [ {")
            .add("      \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Bronchial asthma\",")
            .add("      \"http://endhealth.info/im#code\" : \"11\"")
            .add("    } ],")
            .add("    \"http://endhealth.info/im#isChildOf\" : {")
            .add("      \"@id\" : \"http://endhealth.info/READ2#H3...\"")
            .add("    }")
            .add("  } ]")
            .add("}")
            .toString();
        assertEquals(expected, actual);

        actual = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(document);
        expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@context\" : {")
            .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
            .add("    \"im\" : \"http://endhealth.info/im#\",")
            .add("    \"owl\" : \"http://www.w3.org/2002/07/owl#\",")
            .add("    \"r2\" : \"http://endhealth.info/READ2#\",")
            .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
            .add("    \"sn\" : \"http://snomed.info/sct#\",")
            .add("    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",")
            .add("    \"entities\" : {")
            .add("      \"@id\" : \"http://envhealth.info/im#entities\",")
            .add("      \"@container\" : \"@set\"")
            .add("    },")
            .add("    \"individuals\" : {")
            .add("      \"@id\" : \"http://envhealth.info/im#individuals\",")
            .add("      \"@container\" : \"@set\"")
            .add("    }")
            .add("  },")
            .add("  \"entities\" : [ {")
            .add("    \"@id\" : \"emis:H33\",")
            .add("    \"rdfs:label\" : \"Asthma\",")
            .add("    \"rdfs:comment\" : \"Asthma\",")
            .add("    \"im:code\" : \"H33\",")
            .add("    \"im:scheme\" : {")
            .add("      \"@id\" : \"im:EMISCodeScheme\"")
            .add("    },")
            .add("    \"im:hasMap\" : [ {")
            .add("      \"im:matchedTo\" : [ {")
            .add("        \"@id\" : \"sn:195967001\"")
            .add("      } ]")
            .add("    } ],")
            .add("    \"im:synonym\" : [ {")
            .add("      \"rdfs:label\" : \"Bronchial asthma\",")
            .add("      \"im:code\" : \"11\"")
            .add("    } ],")
            .add("    \"im:isChildOf\" : {")
            .add("      \"@id\" : \"r2:H3...\"")
            .add("    }")
            .add("  } ]")
            .add("}")
            .toString();

        assertEquals(expected, actual);
    }

    @Test
    public void prefixSerializationEntityInDocumentTest() throws JsonProcessingException {
        TTDocument document = new TTDocument();

        document.addPrefix(IM.NAMESPACE, "im");
        document.addPrefix(SNOMED.NAMESPACE, "sn");
        document.addPrefix(OWL.NAMESPACE, "owl");
        document.addPrefix(RDF.NAMESPACE, "rdf");
        document.addPrefix(RDFS.NAMESPACE, "rdfs");
        document.addPrefix(XSD.NAMESPACE, "xsd");
        document.addPrefix("http://endhealth.info/READ2#", "r2");

        TTEntity c = new TTEntity()
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

        document.addEntity(c);

        ObjectMapper om = new ObjectMapper();
        String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(c);
        String expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@id\" : \"emis:H33\",")
            .add("  \"http://endhealth.info/im#code\" : \"H33\",")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Asthma\",")
            .add("  \"http://endhealth.info/im#scheme\" : {")
            .add("    \"@id\" : \"http://endhealth.info/im#EMISCodeScheme\"")
            .add("  },")
            .add("  \"http://endhealth.info/im#hasMap\" : [ {")
            .add("    \"http://endhealth.info/im#matchedTo\" : [ {")
            .add("      \"@id\" : \"http://snomed.info/sct#195967001\"")
            .add("    } ]")
            .add("  } ],")
            .add("  \"http://endhealth.info/im#synonym\" : [ {")
            .add("    \"http://endhealth.info/im#code\" : \"11\",")
            .add("    \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Bronchial asthma\"")
            .add("  } ],")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Asthma\",")
            .add("  \"http://endhealth.info/im#isChildOf\" : {")
            .add("    \"@id\" : \"http://endhealth.info/READ2#H3...\"")
            .add("  }")
            .add("}")
            .toString();
        assertEquals(expected, actual);

        actual = om.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(c);
        expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@context\" : {")
            .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
            .add("    \"im\" : \"http://endhealth.info/im#\",")
            .add("    \"owl\" : \"http://www.w3.org/2002/07/owl#\",")
            .add("    \"r2\" : \"http://endhealth.info/READ2#\",")
            .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
            .add("    \"sn\" : \"http://snomed.info/sct#\",")
            .add("    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",")
            .add("    \"entities\" : {")
            .add("      \"@id\" : \"http://envhealth.info/im#entities\",")
            .add("      \"@container\" : \"@set\"")
            .add("    },")
            .add("    \"individuals\" : {")
            .add("      \"@id\" : \"http://envhealth.info/im#individuals\",")
            .add("      \"@container\" : \"@set\"")
            .add("    }")
            .add("  },")
            .add("  \"@id\" : \"emis:H33\",")
            .add("  \"im:code\" : \"H33\",")
            .add("  \"rdfs:comment\" : \"Asthma\",")
            .add("  \"im:scheme\" : {")
            .add("    \"@id\" : \"im:EMISCodeScheme\"")
            .add("  },")
            .add("  \"im:hasMap\" : [ {")
            .add("    \"im:matchedTo\" : [ {")
            .add("      \"@id\" : \"sn:195967001\"")
            .add("    } ]")
            .add("  } ],")
            .add("  \"im:synonym\" : [ {")
            .add("    \"im:code\" : \"11\",")
            .add("    \"rdfs:label\" : \"Bronchial asthma\"")
            .add("  } ],")
            .add("  \"rdfs:label\" : \"Asthma\",")
            .add("  \"im:isChildOf\" : {")
            .add("    \"@id\" : \"r2:H3...\"")
            .add("  }")
            .add("}")
            .toString();
        assertEquals(expected, actual);
    }
}
