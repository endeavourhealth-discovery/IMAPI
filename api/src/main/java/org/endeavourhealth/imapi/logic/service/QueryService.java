package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class QueryService {
    private final QueryRepository queryRepository = new QueryRepository();
    private final EntityRepository entityRepository = new EntityRepository();

    public Query labelQuery(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }
    public Query describeQuery(Query query) throws JsonProcessingException {
        return new QueryDescriptor().describeQuery(query);
    }
    public Query describeQuery(String queryIri) throws JsonProcessingException {
        return new QueryDescriptor().describeQuery(queryIri);
    }


    public List<TTIriRef> getAllQueries() {
        return queryRepository.getAllQueries();
    }

    public List<TTIriRef> getAllByType(String typeIri) {
        return queryRepository.getAllByType(typeIri);
    }

    public SearchResponse convertQueryIMResultsToSearchResultSummary(JsonNode queryResults, JsonNode highestUsageResults) {
        SearchResponse searchResponse = new SearchResponse();

        if (queryResults.has("entities")) {
            JsonNode entities = queryResults.get("entities");
            if (entities.isArray()) {
                Set<String> iris = new HashSet<>();
                for (JsonNode entity : queryResults.get("entities")) {
                    iris.add(entity.get("@id").asText());
                }
                List<SearchResultSummary> summaries = entityRepository.getEntitySummariesByIris(iris);
                searchResponse.setEntities(summaries);
            }
        }
        if (queryResults.has("totalCount")) searchResponse.setTotalCount(queryResults.get("totalCount").asInt());
        if (queryResults.has("count")) searchResponse.setCount(queryResults.get("count").asInt());
        if (queryResults.has("page")) searchResponse.setPage(queryResults.get("page").asInt());
        if (queryResults.has("term")) searchResponse.setTerm(queryResults.get("term").asText());

        if (highestUsageResults.has("entities")) {
            JsonNode entities = queryResults.get("entities");
            if (entities.isArray() && !entities.isEmpty() && entities.get(0).has("usageTotal")) {
                searchResponse.setHighestUsage(Integer.parseInt(entities.get(0).get("usageTotal").asText()));
            } else searchResponse.setHighestUsage(0);
        }
        return searchResponse;
    }

    private List<TTIriRef> jsonNodeToTTIriRef(JsonNode entity, String iri) {
        List<TTIriRef> result = new ArrayList<>();

        if (entity.has(iri)) {
            for (JsonNode node : entity.get(iri)) {
                TTIriRef iriRef = new TTIriRef(node.get("@id").asText());
                if (node.has(RDFS.LABEL))
                    iriRef.setName(node.get(RDFS.LABEL).asText());

                result.add(iriRef);
            }
        }

        return result;
    }
}
