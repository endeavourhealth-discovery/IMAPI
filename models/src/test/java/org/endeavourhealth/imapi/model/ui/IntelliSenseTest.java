package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class IntelliSenseTest {
    private TTNode restriction;
    private IntelliSenseRequest request;

    @Before
    public void setData() {
        restriction = new TTNode()
            .set(RDF.TYPE, OWL.RESTRICTION);

        TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
            .addPrefix("http://endhealth.info/im#", "im")
            .addPrefix("http://snomed.info/sct#", "sn")
            .addPrefix("http://www.w3.org/2002/07/owl#", "owl")
            .addPrefix("http://www.w3.org/2000/01/rdf-schema#", "rdfs")
            .addPrefix("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf")

            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription("Adverse reaction to Amlodipine Besilate or its derivatives")
            .setCode("25451000252115")
            .addType(OWL.CLASS)

            .set(OWL.EQUIVALENTCLASS, new TTArray()
                .add(new TTNode()
                    .set(OWL.INTERSECTIONOF, new TTArray()
                        .add(iri("http://snomed.info/sct#62014003", "Drug"))
                        .add(restriction)
                    )
                )
            );

        request = new IntelliSenseRequest()
            .setEntity(entity);
    }

    @Test
    public void testIntelliSense_SUGGEST() {
        IntelliSense intelliSense = new IntelliSense();

        request.setAction("SUGGEST")
            .setIndex(OWL.EQUIVALENTCLASS,
                literal("0"),
                OWL.INTERSECTIONOF,
                literal("0")
            );

        // First test
        List<TTValue> actual = intelliSense.evaluate(request);
        assertEquals(1, actual.size());
        assertEquals("SQL SELECT WHERE LIKE 'Drug%';", actual.get(0).asLiteral().getValue());

        // Second test
        restriction.set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003", "Caus"));

        request.setIndex(OWL.EQUIVALENTCLASS,
            literal("0"),
            OWL.INTERSECTIONOF,
            literal("1"),
            OWL.ONPROPERTY
        );

        actual = intelliSense.evaluate(request);
        assertEquals(2, actual.size());
        assertEquals("SQL SELECT WHERE LIKE 'Caus%';", actual.get(0).asLiteral().getValue());
        assertEquals("AND SUBTYPE OF RANGE OF http://www.w3.org/2002/07/owl#onProperty", actual.get(1).asLiteral().getValue());
    }

    @Test
    public void testIntelliSense_ADD() {
        IntelliSense intelliSense = new IntelliSense();

        request
            .setAction("ADD")
            .setIndex(OWL.EQUIVALENTCLASS,
                literal("0"),
                OWL.INTERSECTIONOF,
                literal("1"));

        // First test
        List<TTValue> actual = intelliSense.evaluate(request);
        assertEquals(2, actual.size());
        assertEquals("http://www.w3.org/2002/07/owl#onProperty", actual.get(0).asIriRef().getIri());
        assertEquals("http://www.w3.org/2002/07/owl#someValuesFrom", actual.get(1).asIriRef().getIri());

        // Second test
        restriction.set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003", "Causative agent"));

        actual = intelliSense.evaluate(request);
        assertEquals("http://www.w3.org/2002/07/owl#someValuesFrom", actual.get(0).asIriRef().getIri());
    }
}
