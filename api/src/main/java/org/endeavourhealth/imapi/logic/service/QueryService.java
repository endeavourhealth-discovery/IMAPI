package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.imq.Query;
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
                    SearchResultSummary summary = new SearchResultSummary();
                    summary.setIri(entity.get("@id").asText());
                    summary.setName(entity.get(RDFS.LABEL).asText());
                    if (entity.has(RDFS.COMMENT)) summary.setDescription(entity.get(RDFS.COMMENT).asText());
                    if (entity.has(IM.CODE)) summary.setCode(entity.get(IM.CODE).asText());
                    summary.setStatus(jsonNodeToTTIriRef(entity, IM.HAS_STATUS).get(0));
                    summary.setScheme(jsonNodeToTTIriRef(entity, IM.HAS_SCHEME).get(0));
                    summary.getEntityType().addAll(jsonNodeToTTIriRef(entity, RDF.TYPE));
                    if (entity.has(IM.WEIGHTING)) summary.setWeighting(entity.get(IM.WEIGHTING).asInt());

                    result.add(summary);
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
