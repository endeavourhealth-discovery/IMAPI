package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IMDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("im");
  public static IMDB getConnection(Graph main, Graph ...additional) {
    List<Graph> g = new ArrayList<>();
    g.add(main);
    g.addAll(Arrays.asList(additional));
    return new IMDB(g.toArray(new Graph[0]));
  }

  private IMDB(Graph ...graphs) {
    super(graphs);
    conn = repository.getConnection();
  }
}
