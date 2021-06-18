package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTEntityJsonTest {
    @Test
    public void serializationTest() throws JsonProcessingException {
        TTEntity adverseReaction = getTestEntity();

        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        System.out.println(json);
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

        Assert.assertEquals(expected, actual);
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
            .setScheme(iri("http://snomed.info/sct#891071000252105", "SNOMED"))
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
        Assert.assertEquals("Adverse reaction to Amlodipine Besilate", entity
            .getAsLiteral(RDFS.LABEL)
            .getValue()
        );
        Assert.assertEquals("Adverse reaction to Amlodipine Besilate", entity.getName());
        Assert.assertEquals(3, entity
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0)
            .getAsArray(OWL.INTERSECTIONOF)
            .size());
        Assert.assertEquals("http://snomed.info/sct#384976003", entity
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0).getAsArray(OWL.INTERSECTIONOF)
            .getAsNode(1).getAsIriRef(OWL.SOMEVALUESFROM).getIri()
        );
    }
}
