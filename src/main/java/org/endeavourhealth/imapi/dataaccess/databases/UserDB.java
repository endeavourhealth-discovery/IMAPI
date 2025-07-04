package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.Graph;

public class UserDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("user");
  public static UserDB getConnection() {
    return new UserDB();
  }

  private UserDB() {
    super(Graph.USER);
    conn = repository.getConnection();
  }

  public Update prepareInsertSparql(String sparql) {
    return super.prepareInsertSparql(sparql, Graph.USER);
  }

}
