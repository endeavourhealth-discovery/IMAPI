package org.endeavourhealth.dataaccess.graph;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleIRI;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.IM;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.junit.Before;
import org.junit.Test;


import static org.eclipse.rdf4j.model.util.Values.*;
import static org.junit.Assert.*;

public class ConceptServiceRDF4JTest {
    RepositoryConnection conn;
    ConceptServiceRDF4J svc;

    @Before
    public void setup() {
        Repository db = new SailRepository(new MemoryStore());
        svc = new ConceptServiceRDF4J(db);

        conn = db.getConnection();

        Model m = createBaseModel();

        // Add extra triple for lesser comparison
        m.add(iri("http://localhost#Encounter"), RDFS.COMMENT, literal("An encounter"));

        conn.add(m);
    }

    @Test
    public void getConcept() {
        Model m2 = svc.getDefinition(conn, "http://localhost#Encounter");

        // Compare DB retrieval with lesser model
        Model m = createBaseModel();
        assertNotEquals(m, m2);

        // Add comment to match db
        m.add(iri("http://localhost#Encounter"), RDFS.COMMENT, literal("An encounter"));
        assertEquals(m, m2);

        // Add code for greater mismatch
        m.add(iri("http://localhost#Encounter"), IM.CODE, literal("1234"));
        assertNotEquals(m, m2);
    }

    private Model createBaseModel() {
        Model m = new TreeModel();
        m.add(iri("http://localhost#Encounter"), RDFS.LABEL, literal("Encounter"));

        BNode bNode = bnode();
        m.add(bNode, OWL.INTERSECTIONOF, literal("Member1"));
        m.add(bNode, OWL.INTERSECTIONOF, literal("Member2"));
        m.add(bNode, OWL.INTERSECTIONOF, literal("Member3"));

        m.add(iri("http://localhost#Encounter"), IM.HAS_MEMBERS, bNode);

        return m;
    }
}
