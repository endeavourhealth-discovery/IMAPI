package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.vocabulary.GRAPH;

public class ConfigDB extends BaseDB {
  private static Repository repository = null;

  public static RepositoryConnection getConnection() {
    return getRepo().getConnection();
  }

  public static TupleQuery prepareTupleSparql(RepositoryConnection connection, String sparql) {
    return prepareTupleSparql(connection, sparql, GRAPH.CONFIG);
  }

  public static Update prepareUpdateSparql(RepositoryConnection conn, String sparql) {
    return prepareUpdateSparql(conn, sparql, GRAPH.CONFIG);
  }

  private static Repository getRepo() {
    if (repository == null) {
      repository = getRepository("config");
    }

    return repository;
  }
}
