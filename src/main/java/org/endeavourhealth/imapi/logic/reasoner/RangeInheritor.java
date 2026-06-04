package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.interfacemanager.model.GraphVocab;

public class RangeInheritor {

  /**
   * Updates ranges for properties based on their super properties ranges
   */
  public void inheritRanges(IMDB conn, GraphVocab graph) {
    new EntityRepository().inheritRanges(conn, graph);
  }
}
