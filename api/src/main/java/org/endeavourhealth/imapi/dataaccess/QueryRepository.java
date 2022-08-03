package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class QueryRepository {
    private static final Logger LOG = LoggerFactory.getLogger(QueryRepository.class);

    public String getConceptDbidByIri(String iri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname WHERE {")
            .add("  ?s ?p ?im1id")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            qry.setBinding("p", iri(IM.IM1ID.getIri()));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return bs.getValue("im1id").stringValue();
                } else {
                    return iri;
                }
            }
        }
    }
}
