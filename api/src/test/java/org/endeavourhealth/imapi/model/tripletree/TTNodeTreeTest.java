package org.endeavourhealth.imapi.model.tripletree;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TTNodeTreeTest {

    @Test
    void testTTIriRefEquality() {
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
        assertNotEquals(test1, test2);
        assertNotEquals(test1.hashCode(), test2.hashCode());

        // Different iri, different name - NOT EQUAL
        test1 = iri("http://endhealth.info/im#11111", "test1");
        test2 = iri("http://endhealth.info/im#22222", "test2");
        assertNotEquals(test1, test2);
        assertNotEquals(test1.hashCode(), test2.hashCode());
    }

    @Test
    void tripleTreeTest() {
        TTEntity adverseReaction = TestHelper.getTestEntity();
        TestHelper.checkEntity(adverseReaction);
    }

    @Test
    void testVisitor() {
        TTEntity entity = TestHelper.getTestEntity();
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
}
