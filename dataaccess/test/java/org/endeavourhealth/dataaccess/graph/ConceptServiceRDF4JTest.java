package org.endeavourhealth.dataaccess.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.IM;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.endeavourhealth.dataaccess.graph.tripletree.TTConcept;
import org.endeavourhealth.dataaccess.graph.tripletree.TTNodeTreeTest;
import org.junit.Test;


import static org.eclipse.rdf4j.model.util.Values.*;
import static org.junit.Assert.*;

public class ConceptServiceRDF4JTest {
    @Test
    public void saveConcept() throws JsonProcessingException {
        Repository db = new SailRepository(new MemoryStore());
        ConceptServiceRDF4J svc = new ConceptServiceRDF4J(db);

        TTNodeTreeTest test = new TTNodeTreeTest();
        TTConcept concept = test.getTestConcept();

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

        TTNodeTreeTest test = new TTNodeTreeTest();
        TTConcept expected = test.getTestConcept();

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

        IRI concept = iri("http://envhealth.info/im#25451000252115");

        m.add(concept, RDF.TYPE, OWL.CLASS);
        m.add(concept, RDFS.LABEL, literal("Adverse reaction to Amlodipine Besilate"));
        m.add(concept, IM.CODE, literal("25451000252115"));
        m.add(concept, IM.HAS_SCHEME, iri("http://snomed.info/sct#891071000252105"));

        BNode equivalent = bnode();
        m.add(concept, OWL.EQUIVALENTCLASS, equivalent);

        m.add(equivalent, OWL.INTERSECTIONOF, iri("http://snomed.info/sct#62014003"));

        BNode restriction = bnode();
        m.add(equivalent, OWL.INTERSECTIONOF, restriction);

        m.add(restriction, RDF.TYPE, OWL.RESTRICTION);
        m.add(restriction, OWL.ONPROPERTY, iri("http://snomed.info/sct#246075003"));
        m.add(restriction, OWL.SOMEVALUESFROM, iri("http://snomed.info/sct#384976003"));

        return m;
    }
}
