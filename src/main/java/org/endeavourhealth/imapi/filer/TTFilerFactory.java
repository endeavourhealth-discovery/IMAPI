package org.endeavourhealth.imapi.filer;


import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.filer.rdf4j.ClosureGeneratorBulk;
import org.endeavourhealth.imapi.filer.rdf4j.TTBulkFiler;
import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.List;

public class TTFilerFactory {
  private static boolean bulk = false;
  private static int privacyLevel = 0;
  private static boolean transactional = false;

  private TTFilerFactory() {
  }

  public static boolean isTransactional() {
    return transactional;
  }

  public static void setTransactional(boolean transactional) {
    TTFilerFactory.transactional = transactional;
  }

  public static TTDocumentFiler getDocumentFiler(Graph graph) {
    if (!bulk)
      return new TTTransactionFiler(graph);
    else
      return new TTBulkFiler(graph);
  }

  public static TTEntityFiler getEntityFiler(List<Graph> userGraphs, Graph insertGraph) {
    return new TTEntityFilerRdf4j(IMDB.getConnection(userGraphs), insertGraph);
  }

  public static TCGenerator getClosureGenerator() {
    return new ClosureGeneratorBulk();
  }

  public static boolean isBulk() {
    return bulk;
  }

  public static void setBulk(boolean bulk) {
    TTFilerFactory.bulk = bulk;
  }

  public static int getPrivacyLevel() {
    return privacyLevel;
  }

  public static void setPrivacyLevel(int privacyLevel) {
    TTFilerFactory.privacyLevel = privacyLevel;
  }
}
