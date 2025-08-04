package org.endeavourhealth.imapi.dataaccess.databases;

import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.endeavourhealth.imapi.vocabulary.GRAPH;

public class WorkflowDB extends BaseDB {
  private static final Repository repository = BaseDB.getRepository("workflow");

  public static WorkflowDB getConnection() {
    return new WorkflowDB();
  }

  private WorkflowDB() {
    super(GRAPH.WORKFLOW);
    conn = repository.getConnection();
  }

  public Update prepareInsertSparql(String sparql) {
    return prepareInsertSparql(sparql, GRAPH.WORKFLOW);
  }
}