package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.List;

public class IMDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("im");

  private IMDB(Graph... graphs) {
    super(graphs);
    conn = repository.getConnection();
  }

  public static IMDB getConnection(List<Graph> graphs) {
    return new IMDB(graphs.toArray(new Graph[0]));
  }
}
