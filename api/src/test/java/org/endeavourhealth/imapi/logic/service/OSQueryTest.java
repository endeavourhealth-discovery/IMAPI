package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class OSQueryTest {

    private OSQuery osq;

    @BeforeEach
    void setup() {
        osq = new OSQuery();
    }

    @Test
    void multiPhaseQuery_term() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1");

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(2, results.size());
        List<String> iris = results.stream().map(SearchResultSummary::getIri).toList();
        assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
    }

    @Test
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
    void multiPhaseQuery_term_NotMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setMemberOf(List.of("http://endhealth.info/im#VSET_LongTermConditions"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(0, results.size());
    }

    @Test
    void multiPhaseQuery_term_IsA_NotMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchRequest req = new SearchRequest()
            .setTermFilter("FOXG1")
            .setIsA(List.of("http://snomed.info/sct#57148006"))
            .setMemberOf(List.of("http://endhealth.info/im#VSET_LongTermConditions"));

        List<SearchResultSummary> results = osq.multiPhaseQuery(req);
        assertEquals(0, results.size());
    }

    @Test
    void imQuery_term() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1");

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(2, entities.size());
        List<String> iris = new ArrayList<>();
        entities.forEach(e -> iris.add(e.get("@id").textValue()));
        assertTrue(List.of("http://snomed.info/sct#702450004", "http://endhealth.info/emis#7561151000006117").containsAll(iris));
    }

    @Test
    void imQuery_term_scheme() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setProperty(List.of(
                            new Property()
                                .setIri(IM.HAS_SCHEME.getIri())
                                .setValue(SNOMED.NAMESPACE)
                        ))
                ))
            );

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(1, entities.size());
        assertEquals("http://snomed.info/sct#702450004", entities.get(0).get("@id").textValue());
    }

    @Test
    void imQuery_term_multiScheme() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setProperty(List.of(
                            new Property()
                                .setIri(IM.HAS_SCHEME.getIri())
                                .setIn(List.of(new Node().setIri(SNOMED.NAMESPACE)))
                        ))
                ))
            );

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(1, entities.size());
        assertEquals("http://snomed.info/sct#702450004", entities.get(0).get("@id").textValue());
    }

    @Test
    void imQuery_term_isA() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setIri("http://snomed.info/sct#57148006")
                        .setDescendantsOrSelfOf(true)
                ))
            );

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(1, entities.size());
        assertEquals("http://snomed.info/sct#702450004", entities.get(0).get("@id").textValue());
    }

    @Test
    void imQuery_term_multiIsA() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .addArgument(new Argument().setParameter("isas").setValueIriList(
                List.of(TTIriRef.iri("http://snomed.info/sct#57148006", "http://snomed.info/sct#11164009"))
            ))
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .setParameter("$isas")
                        .setDescendantsOrSelfOf(true)
                ))
            );

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(1, entities.size());
        assertEquals("http://snomed.info/sct#702450004", entities.get(0).get("@id").textValue());
    }

    @Test
    void imQuery_term_member() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .addProperty(new Property()
                            .setIri(IM.IS_MEMBER_OF.getIri())
                            .setValue("http://endhealth.info/im#VSET_ASD")
                        )
                ))
            );

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(1, entities.size());
        assertEquals("http://snomed.info/sct#702450004", entities.get(0).get("@id").textValue());
    }

    @Test
    void imQuery_term_multiMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .addProperty(new Property()
                            .setIri(IM.IS_MEMBER_OF.getIri())
                            .setIn(List.of(new Node().setIri("http://endhealth.info/im#VSET_ASD")))
                        )
                ))
            );

        ObjectNode results = osq.openSearchQuery(req);
        assertTrue(results.has("entities"));
        assertTrue(results.get("entities").isArray());
        ArrayNode entities = (ArrayNode) results.get("entities");
        assertEquals(1, entities.size());
        assertEquals("http://snomed.info/sct#702450004", entities.get(0).get("@id").textValue());
    }
}
