package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.OSQuery;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OSQueryTest_IM {

  private OSQuery osq;

  @BeforeEach
  void setup() {
    osq = new OSQuery();
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void imQuery_term() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1");

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(2, results.getEntities().size());
    List<String> iris = new ArrayList<>();
    results.getEntities().forEach(e -> iris.add(e.getIri()));
    assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void imQuery_term_multiScheme() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .setQuery(new Query()
        .setWhere(
          new Where()
            .setIri(IM.HAS_SCHEME)
            .setIs(List.of(new Node().setIri(Namespace.SNOMED.toString())))
        )
      );

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    assertEquals("http://snomed.info/sct#702450004", results.getEntities().getFirst().getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void imQuery_term_isA() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .setQuery(new Query()
        .addInstanceOf(new Node().setIri("http://snomed.info/sct#57148006")
          .setDescendantsOrSelfOf(true)
        ));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    assertEquals("http://snomed.info/sct#702450004", results.getEntities().getFirst().getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void imQuery_term_multiIsA() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .addArgument(new Argument().setParameter("isas").setValueIriList(
        (Set.of(TTIriRef.iri("http://snomed.info/sct#57148006", "http://snomed.info/sct#11164009")))
      ))
      .setQuery(new Query()
        .addInstanceOf(new Node().setParameter("$isas")
          .setDescendantsOrSelfOf(true)
        ));

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    assertEquals("http://snomed.info/sct#702450004", results.getEntities().getFirst().getIri());
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
  void imQuery_term_multiMember() throws OpenSearchException {
    QueryRequest req = new QueryRequest()
      .setTextSearch("FOXG1")
      .setQuery(new Query()
        .setWhere(new Where()
          .setIri(IM.HAS_MEMBER)
          .setInverse(true)
          .setIs(List.of(new Node().setIri("http://endhealth.info/im#VSET_ASD")))
        )
      );

    SearchResponse results = osq.openSearchQuery(req);
    assertEquals(1, results.getEntities().size());
    assertEquals("http://snomed.info/sct#702450004", results.getEntities().getFirst().getIri());
  }
}
