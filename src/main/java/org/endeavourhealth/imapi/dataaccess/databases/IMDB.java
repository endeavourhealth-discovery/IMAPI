package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class IMDB extends BaseDB {
  private static Repository repository = null;

  public static RepositoryConnection getConnection() {
    return getRepo().getConnection();
  }

  public static TupleQuery prepareTupleSparql(RepositoryConnection connection, String sparql, String graph) {
    return BaseDB.prepareTupleSparql(connection, sparql, graph);
  }

  public static GraphQuery prepareGraphSparql(RepositoryConnection conn, String sparql, String graph) {
    return BaseDB.prepareGraphSparql(conn, sparql, graph);
  }

  public static BooleanQuery prepareBooleanSparql(RepositoryConnection conn, String sparql, String graph) {
    return BaseDB.prepareBooleanSparql(conn, sparql, graph);
  }

  public static Update prepareUpdateSparql(RepositoryConnection conn, String sparql, String graph) {
    return BaseDB.prepareUpdateSparql(conn, sparql, graph);
  }

  private static Repository getRepo() {
    if (repository == null) {
      repository = getRepository("im");
    }

    return repository;
  }
}
