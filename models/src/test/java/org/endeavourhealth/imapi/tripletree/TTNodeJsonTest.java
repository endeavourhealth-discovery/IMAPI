package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TTNodeJsonTest {
    @Test
    public void serializationTest() throws JsonProcessingException {
        TTNode node = getTestNode();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        System.out.println(json);

        assertEquals(getJson(), json);
    }

    @Test
    public void deserializationTest() throws JsonProcessingException {
        // Deserialize
        ObjectMapper om = new ObjectMapper();
        TTNode adverseReaction = om.readValue(getJson(), TTNode.class);

        checkNode(adverseReaction);
    }

    public TTNode getTestNode() {
        return new TTNode()
            .set(RDFS.LABEL, literal("Adverse reaction to Amlodipine Besilate"))
            .set(RDFS.COMMENT, literal("Adverse reaction to Amlodipine Besilate or its derivatives"))
            .set(IM.CODE, literal("25451000252115"))
            .set(IM.HAS_SCHEME, iri("http://snomed.info/sct#891071000252105", "SNOMED"))
            .set(RDF.TYPE, new TTArray().add(OWL.CLASS))

            .set(OWL.EQUIVALENTCLASS, new TTArray()
                .add(new TTNode()
                    .set(OWL.INTERSECTIONOF, new TTArray()
                        .add(iri("http://snomed.info/sct#62014003"))
                        .add(new TTNode()
                            .set(RDF.TYPE, OWL.RESTRICTION)
                            .set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003", "\"quoted with \\'s\""))
                            .set(OWL.SOMEVALUESFROM, iri("http://snomed.info/sct#384976003"))
                        )
                        .add(new TTNode()
                            .set(RDF.TYPE, OWL.RESTRICTION)
                            .set(OWL.ONPROPERTY, literal("\"quoted with \\'s\""))
                            .set(OWL.SOMEVALUESFROM, literal(12345))
                            .set(OWL.CLASS, literal(true))
                            .set(OWL.ONEOF, literal(Pattern.compile("test.*")))
                        )
                    )
                )
            );
    }

    public String getJson() {
        return "{\r\n" +
            "  \"http://endhealth.info/im#code\" : \"25451000252115\",\r\n" +
            "  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Adverse reaction to Amlodipine Besilate or its derivatives\",\r\n" +
            "  \"http://endhealth.info/im#scheme\" : {\r\n" +
            "    \"name\" : \"SNOMED\",\r\n" +
            "    \"@id\" : \"http://snomed.info/sct#891071000252105\"\r\n" +
            "  },\r\n" +
            "  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\r\n" +
            "    \"@id\" : \"http://www.w3.org/2002/07/owl#Class\"\r\n" +
            "  } ],\r\n" +
            "  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Adverse reaction to Amlodipine Besilate\",\r\n" +
            "  \"http://www.w3.org/2002/07/owl#equivalentClass\" : [ {\r\n" +
            "    \"http://www.w3.org/2002/07/owl#intersectionOf\" : [ {\r\n" +
            "      \"@id\" : \"http://snomed.info/sct#62014003\"\r\n" +
            "    }, {\r\n" +
            "      \"http://www.w3.org/2002/07/owl#someValuesFrom\" : {\r\n" +
            "        \"@id\" : \"http://snomed.info/sct#384976003\"\r\n" +
            "      },\r\n" +
            "      \"http://www.w3.org/2002/07/owl#onProperty\" : {\r\n" +
            "        \"name\" : \"\\\"quoted with \\\\'s\\\"\",\r\n" +
            "        \"@id\" : \"http://snomed.info/sct#246075003\"\r\n" +
            "      },\r\n" +
            "      \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {\r\n" +
            "        \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"\r\n" +
            "      }\r\n" +
            "    }, {\r\n" +
            "      \"http://www.w3.org/2002/07/owl#someValuesFrom\" : {\r\n" +
            "        \"@value\" : 12345,\r\n" +
            "        \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\"\r\n" +
            "      },\r\n" +
            "      \"http://www.w3.org/2002/07/owl#onProperty\" : \"\\\"quoted with \\\\'s\\\"\",\r\n" +
            "      \"http://www.w3.org/2002/07/owl#Class\" : {\r\n" +
            "        \"@value\" : true,\r\n" +
            "        \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\"\r\n" +
            "      },\r\n" +
            "      \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {\r\n" +
            "        \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"\r\n" +
            "      },\r\n" +
            "      \"http://www.w3.org/2002/07/owl#oneOf\" : {\r\n" +
            "        \"@value\" : \"test.*\",\r\n" +
            "        \"@type\" : \"http://www.w3.org/2001/XMLSchema#pattern\"\r\n" +
            "      }\r\n" +
            "    } ]\r\n" +
            "  } ]\r\n" +
            "}";
    }

    private void checkNode(TTNode node) {
        Assert.assertEquals("Adverse reaction to Amlodipine Besilate", node
            .getAsLiteral(RDFS.LABEL)
            .getValue()
        );
        Assert.assertEquals(3, node
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0)
            .getAsArray(OWL.INTERSECTIONOF)
            .size());
        Assert.assertEquals("http://snomed.info/sct#384976003", node
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0).getAsArray(OWL.INTERSECTIONOF)
            .getAsNode(1).getAsIriRef(OWL.SOMEVALUESFROM).getIri()
        );
    }
}
