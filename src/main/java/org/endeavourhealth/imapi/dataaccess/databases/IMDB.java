package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.interfacemanager.model.GRAPH;


public class IMDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("im");

  private IMDB() {
    super(GRAPH.IM);
    conn = repository.getConnection();
  }

  public static IMDB getConnection() {
    return new IMDB();
  }
}
