package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public JsonNode convertResultsToConceptSummary(JsonNode node, QueryRequest queryRequest) throws JsonProcessingException {
        EntityRepository entityRepository = new EntityRepository();
        OSQuery osQuery = new OSQuery();
        TTArray entities = mapper.treeToValue(node.get("entities"),TTArray.class);
        List<SearchResultSummary> searchResultSummaries = entities.stream().map(entity -> entityRepository.getEntitySummaryByIri(entity.asIriRef().getIri())).collect(Collectors.toList());
        ObjectNode result = mapper.createObjectNode();
        ArrayNode entityNode = mapper.createArrayNode();
        result.set("entities",entityNode);
        for (SearchResultSummary searchResult : searchResultSummaries) {
            ObjectNode resultNode = mapper.createObjectNode();
            entityNode.add(resultNode);
            resultNode.put("iri", searchResult.getIri());
            resultNode.put("name", searchResult.getName());
            resultNode.put("comment", searchResult.getDescription());
            resultNode.put("code", searchResult.getCode());
            resultNode.set("status", convertIri(searchResult.getStatus()));
            resultNode.set("scheme", convertIri(searchResult.getScheme()));
            resultNode.set("entityType", convertArray(searchResult.getEntityType()));
            resultNode.put("weighting", searchResult.getWeighting());
        }
        return result;
    }

    private ObjectNode convertIri(TTIriRef iri) {
        ObjectNode node = mapper.createObjectNode();
        node.put("@id",iri.getIri());
        if (null != iri.getName()) node.put("name",iri.getName());
        return node;
    }

    private ArrayNode convertArray(Set<TTIriRef> iris) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (TTIriRef iri : iris) {
            arrayNode.add(convertIri(iri));
        }
        return arrayNode;
    }
}
