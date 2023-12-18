package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class OSQueryTest_IM {

    private OSQuery osq;

    @BeforeEach
    void setup() {
        osq = new OSQuery();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void imQuery_term() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1");

        List<SearchResultSummary> results = osq.openSearchQuery(req);
        assertEquals(2, results.size());
        List<String> iris = new ArrayList<>();
        results.forEach(e -> iris.add(e.getIri()));
        assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void imQuery_term_multiScheme() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setProperty(List.of(
                            new Property()
                                .setIri(IM.HAS_SCHEME.getIri())
                                .setIs(List.of(new Node().setIri(SNOMED.NAMESPACE.iri)))
                        ))
                ))
            );

        List<SearchResultSummary> results = osq.openSearchQuery(req);
        assertEquals(1, results.size());
        assertEquals("http://snomed.info/sct#702450004", results.get(0).getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void imQuery_term_isA() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setInstanceOf(new Node().setIri("http://snomed.info/sct#57148006")
                        .setDescendantsOrSelfOf(true)
                )))
            );

        List<SearchResultSummary> results = osq.openSearchQuery(req);
        assertEquals(1, results.size());
        assertEquals("http://snomed.info/sct#702450004", results.get(0).getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void imQuery_term_multiIsA() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .addArgument(new Argument().setParameter("isas").setValueIriList(
                List.of(TTIriRef.iri("http://snomed.info/sct#57148006", "http://snomed.info/sct#11164009"))
            ))
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setInstanceOf(new Node().setParameter("$isas")
                        .setDescendantsOrSelfOf(true)
                )))
            );

        List<SearchResultSummary> results = osq.openSearchQuery(req);
        assertEquals(1, results.size());
        assertEquals("http://snomed.info/sct#702450004", results.get(0).getIri());
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void imQuery_term_multiMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .addProperty(new Property()
                            .setIri(IM.HAS_MEMBER.getIri())
                            .setInverse(true)
                            .setIs(List.of(new Node().setIri("http://endhealth.info/im#VSET_ASD")))
                        )
                ))
            );

        List<SearchResultSummary> results = osq.openSearchQuery(req);
        assertEquals(1, results.size());
        assertEquals("http://snomed.info/sct#702450004", results.get(0).getIri());
    }
}
