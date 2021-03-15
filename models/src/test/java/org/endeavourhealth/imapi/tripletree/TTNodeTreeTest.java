package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTNodeTreeTest {

    @Test
    public void testTTIriRefEquality() {
        // Same iri  - EQUAL
        TTIriRef test1 = iri("http://endhealth.info/im#11111");
        TTIriRef test2 = iri("http://endhealth.info/im#11111");
        Assert.assertEquals(test1, test2);
        Assert.assertEquals(test1.hashCode(), test2.hashCode());

        // Same iri, different name - EQUAL
        test1 = iri("http://endhealth.info/im#11111", "test1");
        test2 = iri("http://endhealth.info/im#11111", "test2");
        Assert.assertEquals(test1, test2);
        Assert.assertEquals(test1.hashCode(), test2.hashCode());

        // Different iri, same name - NOT EQUAL
        test1 = iri("http://endhealth.info/im#11111", "test1");
        test2 = iri("http://endhealth.info/im#22222", "test1");
        Assert.assertNotEquals(test1, test2);
        Assert.assertNotEquals(test1.hashCode(), test2.hashCode());

        // Different iri, different name - NOT EQUAL
        test1 = iri("http://endhealth.info/im#11111", "test1");
        test2 = iri("http://endhealth.info/im#22222", "test2");
        Assert.assertNotEquals(test1, test2);
        Assert.assertNotEquals(test1.hashCode(), test2.hashCode());
    }

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

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testVisitor() {
        TTConcept concept = getTestConcept();
        TTConceptVisitor visitor = new TTConceptVisitor();

        AtomicInteger i = new AtomicInteger();
        AtomicReference<String> indent = new AtomicReference<>("");

        visitor.LiteralVisitor = (predicate, literal) -> System.out.println("L " + indent + predicate.getIri() + " : " + literal.getValue());
        visitor.IriRefVisitor = (predicate, iriRef) -> System.out.println("I " + indent + predicate.getIri() + " : " + iriRef.getIri());
        visitor.NodeVisitor = (predicate, node) -> {
            System.out.println("N " + indent +(predicate == null ? "" : predicate.getIri()) + "{");
            i.getAndIncrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
        };
        visitor.NodeExitVisitor = (predicate, node) -> {
            i.getAndDecrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
            System.out.println("N " + indent +"}");
        };
        visitor.ListVisitor = (predicate, list) -> {
            System.out.println("A " + indent +"[");
            i.getAndIncrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
        };
        visitor.ListExitVisitor = (predicate, list) -> {
            i.getAndDecrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
            System.out.println("A " + indent +"]");
        };

        visitor.visit(concept);
    }

    public TTConcept getTestConcept() {
        return new TTConcept("http://endhealth.info/im#25451000252115")
/*            .addPrefix("http://endhealth.info/im#", ":")
            .addPrefix("http://snomed.info/sct#", "sn:")
            .addPrefix("http://www.w3.org/2002/07/owl#", "owl:")
            .addPrefix("http://www.w3.org/2000/01/rdf-schema#", "rdfs:")
            .addPrefix("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:")*/

            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription("Adverse reaction to Amlodipine Besilate or its derivatives")
            .setCode("25451000252115")
            .setScheme(iri("http://snomed.info/sct#891071000252105", "SNOMED"))
            .setType(OWL.CLASS)

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
        Assert.assertEquals("Adverse reaction to Amlodipine Besilate", concept
            .getAsLiteral(RDFS.LABEL)
            .getValue()
        );
        Assert.assertEquals("Adverse reaction to Amlodipine Besilate", concept.getName());
        Assert.assertEquals(2, concept
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0)
            .getAsArray(OWL.INTERSECTIONOF)
            .size());
        Assert.assertEquals("http://snomed.info/sct#384976003", concept
            .getAsArray(OWL.EQUIVALENTCLASS)
            .getAsNode(0).getAsArray(OWL.INTERSECTIONOF)
            .getAsNode(1).getAsIriRef(OWL.SOMEVALUESFROM).getIri()
        );
    }
}
