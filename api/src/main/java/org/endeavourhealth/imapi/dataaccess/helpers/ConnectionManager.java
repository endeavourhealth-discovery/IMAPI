package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.StringJoiner;

public class ConnectionManager {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
    private static Repository repo;
    private static String prefixes;

    private ConnectionManager() {
        throw new IllegalStateException("Utility class");
    }

    public static RepositoryConnection getConnection() {
        return getRepository().getConnection();
    }

    public static TupleQuery prepareSparql(RepositoryConnection conn, String sparql) {
        try {
            if (prefixes == null)
                prefixes = new ConfigManager().findByName("defaultPrefixes").getData();

            StringJoiner sj = new StringJoiner(System.lineSeparator());
            sj.add(prefixes);
            sj.add(sparql);

            return conn.prepareTupleQuery(sj.toString());
        } catch (Exception e) {
            throw new DALException("Failed to prepare SPARQL query", e);
        }
    }

    private static synchronized Repository getRepository() {
        if (repo == null) {
            String type = System.getenv("GRAPH_TYPE");
            if (type == null || type.isEmpty())
                type = "http";

            switch (type.toLowerCase()) {
                case "http":
                    repo = getHttpRepo();
                    break;
                case "file":
                    repo = getFileRepo();
                    break;
                case "sparql":
                    repo = getSparqlRepo();
                    break;
                default:
                    throw new DALException("Invalid graph connection parameters");
            }
        }

        return repo;
    }

    private static HTTPRepository getHttpRepo() {
        String server = System.getenv("GRAPH_SERVER");
        if (server == null || server.isEmpty()) {
            server = "http://localhost:7200/";
            LOG.warn("GRAPH_SERVER not set, defaulting to '{}'", server);
        }

        String repo = System.getenv("GRAPH_REPO");
        if (repo == null || repo.isEmpty()) {
            repo = "im";
            LOG.warn("GRAPH_REPO not set, defaulting to '{}'", repo);
        }

        return new HTTPRepository(server, repo);
    }

    private static SailRepository getFileRepo() {
        String filename = System.getenv("GRAPH_FILENAME");
        if (filename == null || filename.isEmpty()) {
            filename = "C:\\rdf4j\\im";
            LOG.warn("GRAPH_FILENAME not set, defaulting to '{}'", filename);
        }

        String indexes = System.getenv("GRAPH_INDEXES");
        if (indexes == null || indexes.isEmpty()) {
            indexes = "spoc,posc,opsc";
            LOG.warn("GRAPH_INDEXES not set, defaulting to '{}'", indexes);
        }

        return new SailRepository(new NativeStore(new File(filename), indexes));
    }

    private static SPARQLRepository getSparqlRepo() {
        String query = System.getenv("GRAPH_QUERY");
        if (query == null || query.isEmpty()) {
            query = "dev-im-test1.cluster-cwwwbkhdusnw.eu-west-2.neptune.amazonaws.com";
            LOG.warn("GRAPH_QUERY not set, defaulting to '{}'", query);
        }

        String update = System.getenv("GRAPH_UPDATE");
        if (update == null || update.isEmpty()) {
            update = "dev-im-test1.cluster-ro-cwwwbkhdusnw.eu-west-2.neptune.amazonaws.com";
            LOG.warn("GRAPH_UPDATE not set, defaulting to '{}'", update);
        }

        return new SPARQLRepository(query, update);
    }
}
