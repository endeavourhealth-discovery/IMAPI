package org.endeavourhealth.imapi.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QueryCounterTest {

  @AfterEach
  void reset() {
    QueryCounter.configure(0);
  }

  @Test
  void disabledByDefaultAndRecordsNothing() {
    QueryCounter.configure(0);
    QueryCounter.start();
    QueryCounter.connectionOpened();
    QueryCounter.queryPrepared("SELECT 1");

    assertThat(QueryCounter.isEnabled()).isFalse();
    assertThat(QueryCounter.finish()).isNull();
  }

  @Test
  void countsQueriesAndConnectionsPerThread() throws Exception {
    QueryCounter.configure(5);
    QueryCounter.start();
    QueryCounter.connectionOpened();
    QueryCounter.queryPrepared("SELECT ?o ?oname\n  WHERE { ?s rdf:type ?o }");
    QueryCounter.queryPrepared("SELECT ?o ?oname WHERE { ?s rdf:type ?o }");
    QueryCounter.queryPrepared("ASK { ?s ?p ?o }");

    // another thread's work is not counted against this one
    Thread other = new Thread(() -> QueryCounter.queryPrepared("SELECT other"));
    other.start();
    other.join();

    QueryCounter.Stats stats = QueryCounter.finish();
    assertThat(stats).isNotNull();
    assertThat(stats.getQueries()).isEqualTo(3);
    assertThat(stats.getConnections()).isEqualTo(1);
    assertThat(stats.topQueries(1)).startsWith("2x [SELECT ?o ?oname WHERE { ?s rdf:type ?o }]");
  }

  @Test
  void finishClearsTheCounter() {
    QueryCounter.configure(1);
    QueryCounter.start();
    QueryCounter.queryPrepared("SELECT 1");
    QueryCounter.finish();

    assertThat(QueryCounter.finish()).isNull();
  }

  @Test
  void longQueriesAreTruncatedInTheSummary() {
    QueryCounter.configure(1);
    QueryCounter.start();
    QueryCounter.queryPrepared("SELECT " + "x".repeat(500));

    String top = QueryCounter.finish().topQueries(1);
    assertThat(top.length()).isLessThan(150);
  }
}
