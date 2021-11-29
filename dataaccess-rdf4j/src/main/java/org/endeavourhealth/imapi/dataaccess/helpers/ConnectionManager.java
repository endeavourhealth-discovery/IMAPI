package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import java.io.File;
import java.util.StringJoiner;

public class ConnectionManager {
    private static Repository repo;

    public static RepositoryConnection getConnection() {
        return getRepository().getConnection();
    }

    public static TupleQuery prepareSparql(RepositoryConnection conn, String sparql) {
        StringJoiner sj = new StringJoiner(System.lineSeparator());

        try {
            RepositoryResult<Namespace> namespaces = conn.getNamespaces();
            while (namespaces.hasNext()) {
                Namespace ns = namespaces.next();
                sj.add("PREFIX " + ns.getPrefix() + ": <" + ns.getName() + ">");
            }
            sj.add(sparql);

            return conn.prepareTupleQuery(sj.toString());
        } catch (Exception e) {
            throw new DALException("Failed to prepare SPARQL query", e);
        }
    }

    private static synchronized Repository getRepository() {
        if (repo == null)
           // repo = new SailRepository(new NativeStore(new File("Z:\\rdf4j\\im"), "spoc,posc,opsc"));
            repo = new HTTPRepository("http://localhost:7200/", "im");


        return repo;
    }
}
