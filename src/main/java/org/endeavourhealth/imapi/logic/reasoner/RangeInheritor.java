package org.endeavourhealth.imapi.logic.reasoner;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;

public class RangeInheritor {

  /**
   * Updates ranges for properties based on their super properties ranges
   */
  public void inheritRanges(RepositoryConnection conn, String graph) {
    new EntityRepository().inheritRanges(conn, graph);
  }
}
