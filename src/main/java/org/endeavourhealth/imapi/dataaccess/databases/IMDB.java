package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.Graph;


public class IMDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("im");

  private IMDB() {
    super(Graph.IM);
    conn = repository.getConnection();
  }

  public static IMDB getConnection() {
    return new IMDB();
  }
}
