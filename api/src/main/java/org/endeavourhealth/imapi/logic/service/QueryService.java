package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryService {
    private final QueryRepository queryRepository = new QueryRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    public Query labelQuery(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }

    public List<TTIriRef> getAllQueries() {
        return queryRepository.getAllQueries();
    }

    public List<TTIriRef> getAllByType(String typeIri) {
        return queryRepository.getAllByType(typeIri);
    }

    public JsonNode convertResultsToSimpleNames(JsonNode node) throws JsonProcessingException {
        if (node == null || !node.has("entities") || !node.get("entities").isArray())
            throw new InvalidFormatException("Invalid JSON structure.  Must contain entities[] at root", node, JsonNode.class);

        ObjectNode result = mapper.createObjectNode();
        ArrayNode entityNode = mapper.createArrayNode();
        result.set("entities",entityNode);
        for (JsonNode searchResult : node.get("entities")) {
            ObjectNode resultNode = mapper.createObjectNode();
            entityNode.add(resultNode);
            resultNode.set("iri", searchResult.get("@id"));
            resultNode.set("name", searchResult.get(RDFS.LABEL.getIri()));
            resultNode.set("comment", searchResult.get(RDFS.COMMENT.getIri()));
            resultNode.set("code", searchResult.get(IM.CODE.getIri()));
            resultNode.set("status", searchResult.get(IM.HAS_STATUS.getIri()));
            resultNode.set("scheme", searchResult.get(IM.HAS_SCHEME.getIri()));
            resultNode.set("entityType", searchResult.get(RDF.TYPE.getIri()));
            resultNode.set("weighting", searchResult.get(IM.WEIGHTING.getIri()));
        }
        return result;
    }
}
