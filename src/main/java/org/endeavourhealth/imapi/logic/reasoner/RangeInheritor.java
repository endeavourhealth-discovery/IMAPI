package org.endeavourhealth.imapi.logic.reasoner;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.vocabulary.Graph;

public class RangeInheritor {

  /**
   * Updates ranges for properties based on their super properties ranges
   */
  public void inheritRanges(RepositoryConnection conn, Graph graph) {
    new EntityRepository().inheritRanges(conn, graph);
  }
}
