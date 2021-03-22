package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.junit.Test;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class IntelliSenseTest {

    @Test
    public void testIntelliSense() {
        IntelliSenseRequest request = getTestRequest();

        IntelliSense intelliSense = new IntelliSense();

        intelliSense.evaluate(request, this::printResult);
    }

    private void printResult(String action, Stack<TTTuple> stack) {
        System.out.println("Success!");

        System.out.println("Action: [" + action + "]");

        System.out.println("========== STACK ==========");
        while (!stack.isEmpty()) {
            TTTuple t = stack.pop();
            TTIriRef p = t.getPredicate();
            TTValue v = t.getValue();
            if (p == null)
                System.out.print("P: (root)\t");
            else
                System.out.print("P: [" + p.getIri() + "]\t");

            if (v.isLiteral())
                System.out.println("V: [" + v.asLiteral().getValue() + "]");
            else if (v.isIriRef())
                System.out.println("V: [" + v.asIriRef().getIri() + " | " + v.asIriRef().getName() + "]");
            else if (v.isNode())
                System.out.println("V: Node");
            else if (v.isList())
                System.out.println("V: List[]");
            else
                System.out.println("V: ????");
        }
    }

    public IntelliSenseRequest getTestRequest() {
        List<TTValue> index = new ArrayList<>();
        index.add(OWL.EQUIVALENTCLASS);
        index.add(literal("0"));
        index.add(OWL.INTERSECTIONOF);
        index.add(literal("1"));
        index.add(OWL.ONPROPERTY);

        return new IntelliSenseRequest()
            .setIndex(index)
            .setAction("SUGGEST")
            .setConcept(
                getTestConcept()
            );
    }

    private TTConcept getTestConcept() {
        return new TTConcept("http://endhealth.info/im#25451000252115")
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
                        .add(iri("http://snomed.info/sct#62014003"))
                        .add(new TTNode()
                            .set(RDF.TYPE, OWL.RESTRICTION)
                            .set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003"))           // <-- "Position"
                            .set(OWL.SOMEVALUESFROM, iri("http://snomed.info/sct#384976003"))
                        )
                    )
                )
            );
    }
}
