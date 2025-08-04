package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.vocabulary.GRAPH;

public class ConfigDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("config");
  public static ConfigDB getConnection() {
    try {
      return new ConfigDB();
    } catch (Exception e) {
      throw new DALException(e.getMessage(), e);
    }
  }

  private ConfigDB() {
    super(GRAPH.CONFIG);
    conn = repository.getConnection();
  }

  public Update prepareInsertSparql(String sparql) {
    return super.prepareInsertSparql(sparql, GRAPH.CONFIG);
  }
}
