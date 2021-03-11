package org.endeavourhealth.dataaccess.graph.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.vocabulary.IM;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.Test;
import org.locationtech.jts.util.Assert;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;


public class TTNodeTreeTest {

    @Test
    public void tripleTreeTest() throws JsonProcessingException {
        TTConcept adverseReaction = getTestConcept();
        checkConcept(adverseReaction);
    }

    @Test
    public void serializationTest() throws JsonProcessingException {
        TTConcept adverseReaction = getTestConcept();

        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        System.out.println(json);
    }

    @Test
    public void deserializationTest() throws JsonProcessingException {
        TTConcept adverseReaction = getTestConcept();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        // Deserialize
        adverseReaction = om.readValue(json, TTConcept.class);

        checkConcept(adverseReaction);
    }

    @Test
    public void flipFlopTest() throws JsonProcessingException {
        TTConcept adverseReaction = getTestConcept();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        // Deserialize
        adverseReaction = om.readValue(json, TTConcept.class);

        // Reserialize
        String out = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        System.out.println("=======================================");
        System.out.println(json);
        System.out.println("---------------------------------------");
        System.out.println(out);
        System.out.println("=======================================");

        // Compare
        JsonNode expected = om.readTree(json);
        JsonNode actual = om.readTree(out);

        Assert.isTrue(expected.equals(actual));
    }

    public TTConcept getTestConcept() {
        return new TTConcept("http://envhealth.info/im#25451000252115")
/*            .addPrefix("http://envhealth.info/im#", "")
            .addPrefix("http://snomed.info/sct#", "sn")
            .addPrefix("http://www.w3.org/2002/07/owl#", "owl")
            .addPrefix("http://www.w3.org/2000/01/rdf-schema#", "rdfs")
            .addPrefix("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf")*/

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
            );
    }

    private void checkConcept(TTConcept concept) {
/*        Assert.equals("Adverse reaction to Amlodipine Besilate", concept
            .getAsLiteral(RDFS.LABEL)
            .getValue()
            .stringValue()
        );
        Assert.equals(2, concept
            .getAsArray(OWL.EQUIVALENTCLASS)
            .get(0).asNode()
            .getAsArray(OWL.INTERSECTIONOF)
            .size());
        Assert.equals("http://snomed.info/sct#384976003", concept
            .getAsArray(OWL.EQUIVALENTCLASS).get(0)
            .asNode().getAsArray(OWL.INTERSECTIONOF).get(1)
            .asNode().getAsIriRef(OWL.SOMEVALUESFROM).getIri()
        );*/
    }
}
