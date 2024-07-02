package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    void multiPhaseQuery_term() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1");

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(2, results.getEntities().size());
        List<String> iris = results.getEntities().stream().map(SearchResultSummary::getIri).toList();
        assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_scheme() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .addMatch(new Match()
                    .addWhere(new Where().setIri(IM.HAS_SCHEME).addIs(new Node(SNOMED.NAMESPACE)))));

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(1, results.getEntities().size());
        SearchResultSummary result = results.getEntities().get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_IsA() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query().addMatch(new Match().addWhere(new Where().setIri(IM.IS_A).addIs("http://snomed.info/sct#57148006"))));

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(1, results.getEntities().size());
        SearchResultSummary result = results.getEntities().get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_Member() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query().addMatch(new Match().addWhere(new Where().setIri(IM.HAS_MEMBER).addIs("http://endhealth.info/im#VSET_ASD"))));

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(1, results.getEntities().size());
        SearchResultSummary result = results.getEntities().get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_IsA_Member() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query().addMatch(new Match()
                .addWhere(new Where().setIri(IM.IS_A).addIs("http://snomed.info/sct#57148006"))
                .addWhere(new Where().setIri(IM.HAS_MEMBER).addIs("http://endhealth.info/im#VSET_ASD"))));

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(1, results.getEntities().size());
        SearchResultSummary result = results.getEntities().get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_NotMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query().addMatch(new Match().addWhere(new Where().setIri(IM.HAS_MEMBER).addIs("http://endhealth.info/im#VSET_LongTermConditions"))));

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(0, results.getEntities().size());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_IsA_NotMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query().addMatch(new Match()
                .addWhere(new Where().setIri(IM.IS_A).addIs("http://snomed.info/sct#57148006"))
                .addWhere(new Where().setIri(IM.HAS_MEMBER).addIs("http://endhealth.info/im#VSET_LongTermConditions"))));

        SearchResponse results = osq.multiPhaseQuery(req);
        assertEquals(0, results.getEntities().size());
    }
}
