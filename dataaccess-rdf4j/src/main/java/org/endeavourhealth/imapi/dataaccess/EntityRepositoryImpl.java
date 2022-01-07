package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class EntityRepositoryImpl implements EntityRepository {

    @Override
    public TTIriRef getEntityReferenceByIri(String iri) {
        TTIriRef result = new TTIriRef();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname")
            .add("WHERE {")
            .add("  ?s rdfs:label ?sname")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.setIri(iri).setName(bs.getValue("sname").stringValue());
                }
            }
        }
        return result;
    }

    private TTArray getTypesByIri(String iri) {
        TTArray result = new TTArray();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?o ?oname")
                .add("WHERE {")
                .add("?s rdf:type ?o .")
                .add("?o rdfs:label ?oname .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()));
                }
            }
        }
        return result;
    }

    @Override
    public SearchResultSummary getEntitySummaryByIri(String iri) {
        SearchResultSummary result = new SearchResultSummary();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname ?scode ?sstatus ?sstatusname ?g ?gname")
            .add("WHERE {")
            .add("  GRAPH ?g {")
            .add("    ?s rdfs:label ?sname .")
            .add("  }")
            .add("  OPTIONAL { ?s im:code ?scode . }")
            .add("  OPTIONAL { ?s im:status ?sstatus . ?sstatus rdfs:label ?sstatusname . }")
            .add("  OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    TTArray types = getTypesByIri(iri);
                    BindingSet bs = rs.next();
                    result
                        .setIri(iri)
                        .setName(bs.getValue("sname").stringValue())
                        .setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue())
                        .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
                        .setEntityType(types)
                        .setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue()));
                }
            }
        }
        return result;
    }
}
