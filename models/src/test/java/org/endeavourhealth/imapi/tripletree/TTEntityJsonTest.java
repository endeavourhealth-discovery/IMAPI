package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TTEntityJsonTest {
    @Test
    public void serializationTest() throws JsonProcessingException {
        TTEntity adverseReaction = getTestEntity();

        ObjectMapper om = new ObjectMapper();
        String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);
        String expected = new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@id\" : \"http://endhealth.info/im#25451000252115\",")
            .add("  \"http://endhealth.info/im#code\" : \"25451000252115\",")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Adverse reaction to Amlodipine Besilate or its derivatives\",")
            .add("  \"http://endhealth.info/im#scheme\" : {")
            .add("    \"@id\" : \"http://snomed.info/sct#\"")
            .add("  },")
            .add("  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {")
            .add("    \"@id\" : \"http://www.w3.org/2002/07/owl#Class\"")
            .add("  } ],")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Adverse reaction to Amlodipine Besilate\",")
            .add("  \"http://www.w3.org/2002/07/owl#equivalentClass\" : [ {")
            .add("    \"http://www.w3.org/2002/07/owl#intersectionOf\" : [ {")
            .add("      \"@id\" : \"http://snomed.info/sct#62014003\"")
            .add("    }, {")
            .add("      \"http://www.w3.org/2002/07/owl#someValuesFrom\" : {")
            .add("        \"@id\" : \"http://snomed.info/sct#384976003\"")
            .add("      },")
            .add("      \"http://www.w3.org/2002/07/owl#onProperty\" : {")
            .add("        \"@id\" : \"http://snomed.info/sct#246075003\",")
            .add("        \"name\" : \"\\\"quoted with \\\\'s\\\"\"")
            .add("      },")
            .add("      \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {")
            .add("        \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"")
            .add("      }")
            .add("    }, {")
            .add("      \"http://www.w3.org/2002/07/owl#someValuesFrom\" : 12345,")
            .add("      \"http://www.w3.org/2002/07/owl#onProperty\" : \"\\\"quoted with \\\\'s\\\"\",")
            .add("      \"http://www.w3.org/2002/07/owl#Class\" : true,")
            .add("      \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : {")
            .add("        \"@id\" : \"http://www.w3.org/2002/07/owl#Restriction\"")
            .add("      },")
            .add("      \"http://www.w3.org/2002/07/owl#oneOf\" : {")
            .add("        \"@value\" : \"test.*\",")
            .add("        \"@type\" : \"http://www.w3.org/2001/XMLSchema#pattern\"")
            .add("      }")
            .add("    } ]")
            .add("  } ]")
            .add("}")
            .toString();

        assertEquals(expected, actual);
    }

    @Test
    public void deserializationTest() throws JsonProcessingException {
        TTEntity adverseReaction = getTestEntity();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        // Deserialize
        adverseReaction = om.readValue(json, TTEntity.class);

        checkEntity(adverseReaction);
    }

    @Test
    public void flipFlopTest() throws JsonProcessingException {
        TTEntity adverseReaction = getTestEntity();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        // Deserialize
        adverseReaction = om.readValue(json, TTEntity.class);

        // Reserialize
        String out = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        System.out.println("================= IN ==================");
        System.out.println(json);
        System.out.println("----------------- OUT -----------------");
        System.out.println(out);
        System.out.println("=======================================");

        // Compare
        String expected = Arrays.stream(json.split("\n")).sorted().collect(Collectors.joining("\n"));
        String actual = Arrays.stream(out.split("\n")).sorted().collect(Collectors.joining("\n"));

        assertEquals(expected, actual);
    }

    public TTEntity getTestEntity() {
        return new TTEntity("http://endhealth.info/im#25451000252115")
            .addPrefix("http://endhealth.info/im#", "im")
            .addPrefix("http://snomed.info/sct#", "sn")
            .addPrefix("http://www.w3.org/2002/07/owl#", "owl")
            .addPrefix("http://www.w3.org/2000/01/rdf-schema#", "rdfs")
            .addPrefix("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription("Adverse reaction to Amlodipine Besilate or its derivatives")
            .setCode("25451000252115")
          .setScheme(iri("http://snomed.info/sct#"))
            .setType(new TTArray().add(OWL.CLASS))
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

    private void checkEntity(TTEntity entity) {
        assertEquals("Adverse reaction to Amlodipine Besilate", entity
            .getAsLiteral(RDFS.LABEL)
            .getValue()
        );
        assertEquals("Adverse reaction to Amlodipine Besilate", entity.getName());
        assertEquals(3, entity
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0)
            .getAsArray(OWL.INTERSECTIONOF)
            .size());
        assertEquals("http://snomed.info/sct#384976003", entity
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0).getAsArray(OWL.INTERSECTIONOF)
            .getAsNode(1).getAsIriRef(OWL.SOMEVALUESFROM).getIri()
        );
    }
}
