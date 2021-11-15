package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class EntityTypeRepositoryImpl implements EntityTypeRepository {

    @Override
    public TTArray getEntityTypes(String iri) {
        TTArray result = new TTArray();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?o ?oname")
            .add("WHERE {")
            .add("    ?s rdf:type ?o .")
            .add("    ?o rdfs:label ?oname .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());

            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new TTIriRef()
                        .setIri(bs.getValue("o").stringValue())
                        .setName(bs.getValue("oname").stringValue())

                    );
                }
            }
        }

        return result;
    }
}
