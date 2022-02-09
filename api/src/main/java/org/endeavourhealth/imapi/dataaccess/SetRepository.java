package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.IMv2v1Map;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;

import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class SetRepository {
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    public Set<TTEntity> getAllConceptSets(TTIriRef type) {
        Set<TTEntity> result = new HashSet<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?s")
            .add("WHERE {")
            .add("  ?s rdf:type ?o")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", Values.iri(type.getIri()));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String iri = bs.getValue("s").stringValue();
                    TTEntity set = getSetDefinition(iri);
                    if (set.get(IM.DEFINITION) != null)
                        result.add(set);
                }
            }
        }
        return result;
    }

    public TTEntity getSetDefinition(String setIri) {
        Set<String> predicates= new HashSet<>();
        predicates.add(IM.DEFINITION.getIri());
        predicates.add(IM.IS_CONTAINED_IN.getIri());
        predicates.add(RDFS.LABEL.getIri());

        TTBundle entityPredicates = entityTripleRepository.getEntityPredicates(setIri, predicates, 0);
        return entityPredicates.getEntity();
    }

    public TTEntity getExpansion(TTEntity conceptSet) {
        return null;
    }

    public TTEntity getLegacyExpansion(TTEntity conceptSet) {
        return null;
    }

    public TTEntity getIM1Expansion(TTEntity conceptSet) {
        return null;
    }

    public Set<IMv2v1Map> getIMv2v1Maps(Set<EntitySummary> members) {
        Set<IMv2v1Map> result = new HashSet<>();

        // TODO: How to implement in Graph?

        return result;
    }

    public List<ValueSetMember> expandMember(String iri) {
        return expandMember(iri, null);
    }

    public List<ValueSetMember> expandMember(String iri, Integer limit) throws DALException {
        return Collections.emptyList();
    }
}
