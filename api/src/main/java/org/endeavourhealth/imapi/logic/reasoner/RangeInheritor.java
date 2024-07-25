package org.endeavourhealth.imapi.logic.reasoner;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;

public class RangeInheritor {

  /**
   * Updates ranges for properties based on their super properties ranges
   */
  public void inheritRanges(RepositoryConnection conn) {
    new EntityRepository2().inheritRanges(conn);
  }
}
