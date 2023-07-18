package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
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
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class OSQueryTest_IM {

    private OSQuery osq;

    @BeforeEach
    void setup() {
        osq = new OSQuery();
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
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
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
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
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
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
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
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
    @EnabledIfEnvironmentVariable(named = "OPENSEARCH_URL", matches = "http.*")
    void imQuery_term_multiMember() throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, DataFormatException {
        QueryRequest req = new QueryRequest()
            .setTextSearch("FOXG1")
            .setQuery(new Query()
                .setMatch(List.of(
                    new Match()
                        .addProperty(new Property()
                            .setIri(IM.HAS_MEMBER.getIri())
                            .setInverse(true)
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