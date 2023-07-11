package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.net.URISyntaxException;
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
    void multiPhaseQuery_term() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1");

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(2, results.size());
        List<String> iris = results.stream().map(SearchResultSummary::getIri).toList();
        assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_scheme() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setSchemeFilter(List.of(SNOMED.NAMESPACE));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(1, results.size());
        SearchResultSummary result = results.get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_IsA() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setIsA(List.of("http://snomed.info/sct#57148006"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(1, results.size());
        SearchResultSummary result = results.get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_Member() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setMemberOf(List.of("http://endhealth.info/im#VSET_ASD"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(1, results.size());
        SearchResultSummary result = results.get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_IsA_Member() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setIsA(List.of("http://snomed.info/sct#57148006"))
            .setMemberOf(List.of("http://endhealth.info/im#VSET_ASD"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(1, results.size());
        SearchResultSummary result = results.get(0);
        assertEquals("http://snomed.info/sct#702450004", result.getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_NotMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setMemberOf(List.of("http://endhealth.info/im#VSET_LongTermConditions"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(0, results.size());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void multiPhaseQuery_term_IsA_NotMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setIsA(List.of("http://snomed.info/sct#57148006"))
            .setMemberOf(List.of("http://endhealth.info/im#VSET_LongTermConditions"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(0, results.size());
    }
}
