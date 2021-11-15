package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

import java.io.File;
import java.util.*;

public class EntityTripleRepositoryImpl implements EntityTripleRepository {
    private final Map<String, Integer> bnodes = new HashMap<>();
    private int row = 0;

    @Override
    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates, int limit) throws DALException {
        System.out.println("Connecting");
        Repository repo = new SailRepository(new NativeStore(new File("Z:\\rdf4j")));

        List<Tpl> triples = new ArrayList<>();
        repo.init();
        try (RepositoryConnection conn = repo.getConnection()) {
            System.out.println("Connected");

            IRI subject = Values.iri("http://endhealth.info/im#VSET_RecordType_FamilyHistory");

            addTriples(conn, triples, subject, null, predicates);

        } catch (RepositoryException e) {
            System.out.println("Failed");
        }
        repo.shutDown();

        return triples;
    }

    @Override
    public List<Tpl> getTriplesRecursiveByExclusions(String iri, Set<String> exclusionPredicates, int limit) throws DALException {
        return null;
    }

    @Override
    public Set<ValueSetMember> getObjectBySubjectAndPredicate(String subjectIri, String predicateIri) {
        return Collections.emptySet();
    }

    @Override
    public boolean hasChildren(String iri, boolean inactive) throws DALException {
        return false;
    }

    @Override
    public List<TTIriRef> findImmediateChildrenByIri(String iri, Integer rowNumber, Integer pageSize, boolean inactive) {
        return Collections.emptyList();
    }

    @Override
    public List<TTIriRef> findImmediateParentsByIri(String iri, Integer rowNumber, Integer pageSize, boolean inactive) {
        return Collections.emptyList();
    }

    @Override
    public List<TTIriRef> getActiveSubjectByObjectExcludeByPredicate(String objectIri, Integer rowNumber, Integer pageSize, String excludePredicateIri) {
        return Collections.emptyList();
    }

    @Override
    public Integer getCountOfActiveSubjectByObjectExcludeByPredicate(String objectIri, String excludePredicateIri) {
        return null;
    }

    @Override
    public Set<ValueSetMember> getSubjectByObjectAndPredicateAsValueSetMembers(String objectIri, String predicateIri) {
        return Collections.emptySet();
    }

    @Override
    public Set<TTIriRef> getObjectIriRefsBySubjectAndPredicate(String subjectIri, String predicateIri) {
        return Collections.emptySet();
    }

    @Override
    public List<Namespace> findNamespaces() {
        return Collections.emptyList();
    }

    @Override
    public Collection<SimpleMap> getSubjectFromObjectPredicate(String objectIri, TTIriRef predicate) {
        return null;
    }

    @Override
    public TTBundle getEntityPredicates(String entityIri, Set<String> predicates, int unlimited) {
        return null;
    }

    @Override
    public Set<EntitySummary> getLegacyConceptSummaries(Set<EntitySummary> result) {
        return Collections.emptySet();
    }

    @Override
    public Collection<EntitySummary> getSubjectAndDescendantSummariesByPredicateObjectRelType(String predicate, String object) {
        return null;
    }

    @Override
    public Set<EntitySummary> getSubclassesAndReplacements(String iri) {
        return Collections.emptySet();
    }

    private void addTriples(RepositoryConnection conn, List<Tpl> triples, Resource subject, Integer parent, Set<String> predicates) {
        List<Statement> statements = new ArrayList<>();

        if (predicates == null) {
            RepositoryResult<Statement> s = conn.getStatements(subject, null, null);
            statements.addAll(s.asList());
        } else {
            for (String predicate : predicates) {
                RepositoryResult<Statement> s = conn.getStatements(subject, Values.iri(predicate), null);
                statements.addAll(s.asList());
            }
        }

        for(Statement stmt: statements) {
            Tpl tpl = new Tpl()
                .setDbid(row++)
                .setParent(parent)
                .setPredicate(TTIriRef.iri(stmt.getPredicate().stringValue()));

            triples.add(tpl);

            Value object = stmt.getObject();
            if (object.isIRI())
                tpl.setObject(TTIriRef.iri(object.stringValue()));
            else if (object.isLiteral())
                tpl.setLiteral(object.stringValue());
            else if (object.isBNode()) {
                bnodes.put(object.stringValue(), row - 1);
                addTriples(conn, triples, (BNode) object, row - 1, null);
            }

            System.out.println(tpl.getDbid() + ": " + tpl.getParent() + " - " + tpl.getPredicate().getIri() + " => " + tpl.getLiteral() + "/" + tpl.getObject());
        }
    }
}
