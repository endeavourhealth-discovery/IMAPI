package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.vocabulary.Graph;

public class UserDB extends BaseDB {
  private static Repository repository = null;

  public static RepositoryConnection getConnection() {
    return getRepo().getConnection();
  }

  public static TupleQuery prepareTupleSparql(RepositoryConnection connection, String sparql) {
    return prepareTupleSparql(connection, sparql, Graph.USER);
  }

  public static Update prepareUpdateSparql(RepositoryConnection conn, String sparql) {
    return prepareUpdateSparql(conn, sparql, Graph.USER);
  }

  public static BooleanQuery prepareBooleanSparql(RepositoryConnection conn, String sparql) {
    return BaseDB.prepareBooleanSparql(conn, sparql, Graph.USER);
  }

  private static Repository getRepo() {
    if (repository == null) {
      repository = getRepository("user");
    }

    return repository;
  }
}
