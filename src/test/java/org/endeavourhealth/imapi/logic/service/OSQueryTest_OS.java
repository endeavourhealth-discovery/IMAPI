package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.OSQuery;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OSQueryTest_OS {

  private OSQuery osq;

  @BeforeEach
  void setup() {
    osq = new OSQuery();
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1");

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(2, results.getEntities().size());
    List<String> iris = results.getEntities().stream().map(SearchResultSummary::getIri).toList();
    assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term_scheme() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .where(w -> w
          .setIri(IM.HAS_SCHEME)
          .is(is -> is.setIri(Namespace.SNOMED.toString()))));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    SearchResultSummary result = results.getEntities().get(0);
    assertEquals("http://snomed.info/sct#702450004", result.getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term_IsA() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .instanceOf(n -> n.setIri("http://snomed.info/sct#57148006")
          .setDescendantsOrSelfOf(true)));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    SearchResultSummary result = results.getEntities().get(0);
    assertEquals("http://snomed.info/sct#702450004", result.getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term_Member() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .instanceOf(n -> n.setIri("http://endhealth.info/im#VSET_ASD")
          .setMemberOf(true)));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    SearchResultSummary result = results.getEntities().get(0);
    assertEquals("http://snomed.info/sct#702450004", result.getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term_IsA_Member() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .instanceOf(n -> n.setIri("http://snomed.info/sct#57148006")
          .setDescendantsOrSelfOf(true))
        .instanceOf(n -> n.setIri("http://endhealth.info/im#VSET_ASD")
          .setMemberOf(true)));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    SearchResultSummary result = results.getEntities().get(0);
    assertEquals("http://snomed.info/sct#702450004", result.getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term_NotMember() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .setQuery(new Query()
        .instanceOf(n -> n.setIri("http://endhealth.info/im#VSET_ASD")
          .setMemberOf(true)));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(0, results.getEntities().size());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void openSearchQuery_term_IsA_NotMember() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .query(q -> q
        .instanceOf(n -> n.setIri("http://snomed.info/sct#57148006")
          .setDescendantsOrSelfOf(true))
        .instanceOf(n -> n.setIri("http://endhealth.info/im#VSET_LongTermConditions")
          .setMemberOf(true)));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(0, results.getEntities().size());
  }
}
