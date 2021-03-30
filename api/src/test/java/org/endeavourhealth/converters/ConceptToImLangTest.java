package org.endeavourhealth.converters;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.util.Assert;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class ConceptToImLangTest {
    @Test
    void translateConceptToImLang() {
        TTConcept concept = getTestConcept();
        String actual = new ConceptToImLang().translateConceptToImLang(concept);
        Assert.equals(getTestString(), actual);
    }

    public TTConcept getTestConcept() {
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
            .setType(new TTArray().add(OWL.CLASS))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"))

            .set(OWL.EQUIVALENTCLASS, new TTArray()
                .add(new TTNode()
                    .set(OWL.INTERSECTIONOF, new TTArray()
                        .add(iri("http://snomed.info/sct#62014003", "Drug reaction"))
                        .add(new TTNode()
                            .set(RDF.TYPE, OWL.RESTRICTION)
                            .set(OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003", "Causative agent"))
                            .set(OWL.SOMEVALUESFROM, iri("http://snomed.info/sct#384976003", "Amlodipine besilate"))
                        )
                    )
                )
            );
    }

    public String getTestString() {
        return "http://endhealth.info/im#25451000252115\n" +
            "type \"http://www.w3.org/2002/07/owl#Class\";\n" +
            "Name \"Adverse reaction to Amlodipine Besilate\";\n" +
            "description \"Adverse reaction to Amlodipine Besilate or its derivatives\";\n" +
            "code \"25451000252115\";\n" +
            "scheme \"http://snomed.info/sct#891071000252105\";\n" +
            "status \"http://endhealth.info/im#Active | Active\";\n" +
            "equivalentTo \"http://snomed.info/sct#62014003 | Drug reaction\",\n" +
            "[\"http://snomed.info/sct#246075003 | Causative agent\" \"http://snomed.info/sct#384976003 | Amlodipine besilate\"];\n" +
            ".\n";
    }
}
