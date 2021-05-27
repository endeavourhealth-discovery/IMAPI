package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;

public class TTArrayJsonTest {
    @Test
    public void serializationTest() throws JsonProcessingException {
        TTArray node = getTestArray();

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
        TTArray array = om.readValue(getJson(), TTArray.class);

        checkArray(array);
    }

    public TTArray getTestArray() {
        return new TTArray()
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
            );
    }

    public String getJson() {
        return "[ {\r\n" +
            "  \"http://www.w3.org/2002/07/owl#intersectionOf\" : [ {\r\n" +
            "    \"@id\" : \"http://snomed.info/sct#62014003\"\r\n" +
            "  }, {\r\n" +
            "    \"http://www.w3.org/2002/07/owl#someValuesFrom\" : {\r\n" +
            "      \"@id\" : \"http://snomed.info/sct#384976003\"\r\n" +
            "    },\r\n" +
            "    \"http://www.w3.org/2002/07/owl#onProperty\" : {\r\n" +
            "      \"name\" : \"\\\"quoted with \\\\'s\\\"\",\r\n" +
            "      \"@id\" : \"http://snomed.info/sct#246075003\"\r\n" +
            "    },\r\n" +
            "    \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {\r\n" +
            "      \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"\r\n" +
            "    }\r\n" +
            "  }, {\r\n" +
            "    \"http://www.w3.org/2002/07/owl#someValuesFrom\" : {\r\n" +
            "      \"@value\" : 12345,\r\n" +
            "      \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\"\r\n" +
            "    },\r\n" +
            "    \"http://www.w3.org/2002/07/owl#onProperty\" : \"\\\"quoted with \\\\'s\\\"\",\r\n" +
            "    \"http://www.w3.org/2002/07/owl#Class\" : {\r\n" +
            "      \"@value\" : true,\r\n" +
            "      \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\"\r\n" +
            "    },\r\n" +
            "    \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {\r\n" +
            "      \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"\r\n" +
            "    },\r\n" +
            "    \"http://www.w3.org/2002/07/owl#oneOf\" : {\r\n" +
            "      \"@value\" : \"test.*\",\r\n" +
            "      \"@type\" : \"http://www.w3.org/2001/XMLSchema#pattern\"\r\n" +
            "    }\r\n" +
            "  } ]\r\n" +
            "} ]";
    }

    private void checkArray(TTArray array) {
        Assert.assertEquals(3, array
            .getAsNode(0)
            .getAsArray(OWL.INTERSECTIONOF)
            .size());
        Assert.assertEquals("http://snomed.info/sct#384976003", array
            .getAsNode(0).getAsArray(OWL.INTERSECTIONOF)
            .getAsNode(1).getAsIriRef(OWL.SOMEVALUESFROM).getIri()
        );
    }
}
