package org.endeavourhealth.imapi.model;

import org.junit.Assert;
import org.junit.Test;

public class ObjectModelVisitorTest {
    boolean visited = false;

    @Test
    public void testVisitor() {
        Concept concept = new Concept();
        concept.addSubClassOf(new ClassExpression());
        ObjectModelVisitor omv = new ObjectModelVisitor();
        omv.SubClassVisitor = (sub) -> visited = true;
        omv.visit(concept);

        Assert.assertTrue(visited);
    }
}
