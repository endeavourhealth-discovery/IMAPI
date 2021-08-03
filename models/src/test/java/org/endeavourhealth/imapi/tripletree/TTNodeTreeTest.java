package org.endeavourhealth.imapi.tripletree;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;

public class TTNodeTreeTest {

    @Test
    public void testTTIriRefEquality() {
        // Same iri  - EQUAL
        TTIriRef test1 = iri("http://endhealth.info/im#11111");
        TTIriRef test2 = iri("http://endhealth.info/im#11111");
        assertEquals(test1, test2);
        assertEquals(test1.hashCode(), test2.hashCode());

        // Same iri, different name - EQUAL
        test1 = iri("http://endhealth.info/im#11111", "test1");
        test2 = iri("http://endhealth.info/im#11111", "test2");
        assertEquals(test1, test2);
        assertEquals(test1.hashCode(), test2.hashCode());

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
    public void tripleTreeTest() {
        TTEntity adverseReaction = getTestEntity();
        checkEntity(adverseReaction);
    }

    @Test
    public void testVisitor() {
        TTEntity entity = getTestEntity();
        TTVisitor visitor = new TTVisitor();

        AtomicInteger i = new AtomicInteger();
        AtomicReference<String> indent = new AtomicReference<>("");

        visitor.onLiteral((predicate, literal) -> System.out.println("L " + indent + predicate.getIri() + " : " + literal.getValue()));
        visitor.onIriRef((predicate, iriRef) -> System.out.println("I " + indent + predicate.getIri() + " : " + iriRef.getIri()));
        visitor.onNode((predicate, node) -> {
            System.out.println("N " + indent +(predicate == null ? "" : predicate.getIri()) + "{");
            i.getAndIncrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
        });
        visitor.onNodeExit((predicate, node) -> {
            i.getAndDecrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
            System.out.println("N " + indent +"}");
        });
        visitor.onList((predicate, list) -> {
            System.out.println("A " + indent +"[");
            i.getAndIncrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
        });
        visitor.onListExit((predicate, list) -> {
            i.getAndDecrement();
            indent.set(String.join("", Collections.nCopies(i.get(), "\t")));
            System.out.println("A " + indent +"]");
        });

        visitor.visit(entity);

        assertEquals(0, i.get());
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
