package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class EntityTctRepositoryImpl {
    public List<TTIriRef> findAncestorsByType(String childIri, String relationshipIri, List<String> candidateAncestorIris) {
        List<TTIriRef> result = new ArrayList<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?aname")
            .add("WHERE {")
            .add("  ?c ?r ?a .")
            .add("  ?a rdfs:label ?aname .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("c", iri(childIri));
            qry.setBinding("r", iri(relationshipIri));

            for(String candidate: candidateAncestorIris) {
                qry.setBinding("a", iri(candidate));
                try (TupleQueryResult rs = qry.evaluate()) {
                    if (rs.hasNext()) {
                        BindingSet bs = rs.next();
                        result.add(new TTIriRef()
                            .setIri(candidate)
                            .setName(bs.getValue("aname").stringValue())
                        );
                    }
                }
            }
        }
        return result;
    }
}
