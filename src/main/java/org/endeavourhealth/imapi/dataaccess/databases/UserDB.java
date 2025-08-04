package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.GRAPH;

public class UserDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("user");
  public static UserDB getConnection() {
    return new UserDB();
  }

  private UserDB() {
    super(GRAPH.USER);
    conn = repository.getConnection();
  }

  public Update prepareInsertSparql(String sparql) {
    return super.prepareInsertSparql(sparql, GRAPH.USER);
  }

}
