package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.interfacemanager.model.GRAPH;

public class ProvDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("im");

  private ProvDB() {
    super(GRAPH.PROV);
    conn = repository.getConnection();
  }

  public static ProvDB getConnection() {
    return new ProvDB();
  }
}
