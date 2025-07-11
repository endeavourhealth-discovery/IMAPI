package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.Graph;

public class ProvDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("im");
  public static ProvDB getConnection() {
    return new ProvDB();
  }

  private ProvDB() {
    super(Graph.PROV);
    conn = repository.getConnection();
  }
}
