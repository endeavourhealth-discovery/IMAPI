package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryService {
    private final QueryRepository queryRepository = new QueryRepository();

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

    public List<SearchResultSummary> convertQueryIMResultsToSearchResultSummary(JsonNode queryResults) {
        List<SearchResultSummary> result = new ArrayList<>();

        if (queryResults.has("entities")) {
            JsonNode entities = queryResults.get("entities");
            if (entities.isArray()) {
                for (JsonNode entity : queryResults.get("entities")) {
                    SearchResultSummary summary = new SearchResultSummary()
                        .setIri(entity.get("@id").asText())
                        .setName(entity.get(RDFS.LABEL.getIri()).asText())
                        .setDescription(entity.get(RDFS.COMMENT.getIri()).asText())
                        .setCode(entity.get(IM.CODE.getIri()).asText());

                    result.add(summary);
                }
            }
        }

        return result;
    }
}
