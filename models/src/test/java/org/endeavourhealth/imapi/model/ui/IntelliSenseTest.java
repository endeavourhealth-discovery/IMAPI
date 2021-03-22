package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class IntelliSenseTest {
    private TTNode restriction;
    private IntelliSenseRequest request;

    @Before
    public void setData() {
        restriction = new TTNode()
            .set(RDF.TYPE, OWL.RESTRICTION);

        TTConcept concept = new TTConcept("http://endhealth.info/im#25451000252115")
            .addPrefix("http://endhealth.info/im#", "im")
            .addPrefix("http://snomed.info/sct#", "sn")
            .addPrefix("http://www.w3.org/2002/07/owl#", "owl")
            .addPrefix("http://www.w3.org/2000/01/rdf-schema#", "rdfs")
            .addPrefix("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf")

            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription("Adverse reaction to Amlodipine Besilate or its derivatives")
            .setCode("25451000252115")
            .setScheme(iri("http://snomed.info/sct#891071000252105", "SNOMED"))
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
            .setConcept(concept);
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
        System.out.println("=============== SUGGEST 1 ===============");

        List<TTValue> result = intelliSense.evaluate(request);
        printResult(result);

        // Second test
        System.out.println("=============== SUGGEST 2 ===============");
        restriction.set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003", "Caus"));

        request.setIndex(OWL.EQUIVALENTCLASS,
            literal("0"),
            OWL.INTERSECTIONOF,
            literal("1"),
            OWL.ONPROPERTY
        );

        result = intelliSense.evaluate(request);
        printResult(result);
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
        System.out.println("=============== ADD 1 ===============");
        List<TTValue> result = intelliSense.evaluate(request);
        printResult(result);

        // Second test
        System.out.println("=============== ADD 2 ===============");
        restriction.set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003", "Causative agent"));

        result = intelliSense.evaluate(request);
        printResult(result);
    }

    private void printResult(List<TTValue> result) {
        for(TTValue v : result) {
            if (v.isIriRef()) {
                System.out.println("I: " + v.asIriRef().getIri() + "|" + v.asIriRef().getName());
            } else if (v.isLiteral()) {
                System.out.println("V: " + v.asLiteral().getValue());
            } else {
                System.err.println("Unhandled result value type!");
            }
        }
    }
}
