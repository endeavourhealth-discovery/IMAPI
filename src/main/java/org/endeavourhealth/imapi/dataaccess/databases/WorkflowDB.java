package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.vocabulary.GRAPH;

public class WorkflowDB extends BaseDB {
  private static Repository repository = null;

  public static RepositoryConnection getConnection() {
    return getRepo().getConnection();
  }

  public static TupleQuery prepareTupleSparql(RepositoryConnection connection, String sparql) {
    return BaseDB.prepareTupleSparql(connection, sparql, GRAPH.WORKFLOW);
  }

  public static Update prepareUpdateSparql(RepositoryConnection conn, String sparql) {
    return BaseDB.prepareUpdateSparql(conn, sparql, GRAPH.WORKFLOW);
  }

  private static Repository getRepo() {
    if (repository == null) {
      repository = getRepository("workflow");
    }

    return repository;
  }
}
