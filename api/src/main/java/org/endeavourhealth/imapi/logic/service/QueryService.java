package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryService {
    private final QueryRepository queryRepository = new QueryRepository();
    private final EntityRepository entityRepository = new EntityRepository();

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

    public SearchResponse convertQueryIMResultsToSearchResultSummary(JsonNode queryResults) {
        SearchResponse result = new SearchResponse();

        if (queryResults.has("entities")) {
            JsonNode entities = queryResults.get("entities");
            if (entities.isArray()) {
                for (JsonNode entity : queryResults.get("entities")) {
                    SearchResultSummary summary = entityRepository.getEntitySummaryByIri(entity.get("@id").asText());
                    result.addEntity(summary);
                }
            }
        }

        return result;
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
