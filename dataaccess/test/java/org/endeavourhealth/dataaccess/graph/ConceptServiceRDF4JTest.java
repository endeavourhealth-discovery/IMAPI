package org.endeavourhealth.dataaccess.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.Test;


import static org.eclipse.rdf4j.model.util.Values.*;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.*;

public class ConceptServiceRDF4JTest {
    @Test
    public void saveConcept() throws JsonProcessingException {
        Repository db = new SailRepository(new MemoryStore());
        ConceptServiceRDF4J svc = new ConceptServiceRDF4J(db);

        TTConcept concept = getTestConcept();

        svc.saveTTConcept(concept);

        try (RepositoryConnection conn = db.getConnection()) {
            Model m = new TreeModel();
            for (Statement statement : conn.getStatements(null, null, null)) {
                m.add(statement);
            }
        }
    }

    @Test
    public void getConcept() throws JsonProcessingException {
        Repository db = new SailRepository(new MemoryStore());

        try (RepositoryConnection conn = db.getConnection()) {
            Model m = createBaseModel();
            conn.add(m);
        }

        ConceptServiceRDF4J svc = new ConceptServiceRDF4J(db);

        TTConcept concept = svc.getTTConcept("http://envhealth.info/im#25451000252115");

    }

    @Test
    public void flipFlop() throws JsonProcessingException {
        Repository db = new SailRepository(new MemoryStore());
        ConceptServiceRDF4J svc = new ConceptServiceRDF4J(db);

        TTConcept expected = getTestConcept();

        svc.saveTTConcept(expected);

        TTConcept actual = svc.getTTConcept("http://envhealth.info/im#25451000252115");

        ObjectMapper om = new ObjectMapper();
        String expectedJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        String actualJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(actual);

        System.out.println("=======================================");
        System.out.println(expectedJson);
        System.out.println("---------------------------------------");
        System.out.println(actualJson);
        System.out.println("=======================================");

        JsonNode expectedNode = om.readTree(expectedJson);
        JsonNode actualNode = om.readTree(actualJson);

        assertTrue(expectedNode.equals(actualNode));
    }


    private Model createBaseModel() {
        Model m = new TreeModel();

        IRI concept = Values.iri("http://envhealth.info/im#25451000252115");

        m.add(concept, org.eclipse.rdf4j.model.vocabulary.RDF.TYPE, org.eclipse.rdf4j.model.vocabulary.OWL.CLASS);
        m.add(concept, org.eclipse.rdf4j.model.vocabulary.RDFS.LABEL, Values.literal("Adverse reaction to Amlodipine Besilate"));
        m.add(concept, Values.iri(IM.CODE.getIri()), Values.literal("25451000252115"));
        m.add(concept, Values.iri(IM.HAS_SCHEME.getIri()), Values.iri("http://snomed.info/sct#891071000252105"));

        BNode equivalent = bnode();
        m.add(concept, org.eclipse.rdf4j.model.vocabulary.OWL.EQUIVALENTCLASS, equivalent);

        m.add(equivalent, org.eclipse.rdf4j.model.vocabulary.OWL.INTERSECTIONOF, Values.iri("http://snomed.info/sct#62014003"));

        BNode restriction = bnode();
        m.add(equivalent, org.eclipse.rdf4j.model.vocabulary.OWL.INTERSECTIONOF, restriction);

        m.add(restriction, org.eclipse.rdf4j.model.vocabulary.RDF.TYPE, org.eclipse.rdf4j.model.vocabulary.OWL.RESTRICTION);
        m.add(restriction, org.eclipse.rdf4j.model.vocabulary.OWL.ONPROPERTY, Values.iri("http://snomed.info/sct#246075003"));
        m.add(restriction, org.eclipse.rdf4j.model.vocabulary.OWL.SOMEVALUESFROM, Values.iri("http://snomed.info/sct#384976003"));

        return m;
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
}
