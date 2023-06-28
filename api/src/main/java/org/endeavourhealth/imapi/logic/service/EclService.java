package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Component
public class EclService {
    private static final Logger LOG = LoggerFactory.getLogger((EclService.class));
    private final QueryRepository queryRepository = new QueryRepository();
    private final SetRepository setRepository = new SetRepository();

    public String getEcl(Query inferred) throws DataFormatException, JsonProcessingException {
        if (inferred == null) throw new DataFormatException("Missing data for ECL conversion");
        else return IMLToECL.getECLFromQuery(inferred,true);
    }

    public Set<Concept> evaluateECLQuery(EclSearchRequest request) throws DataFormatException, JsonProcessingException, QueryException {
        return setRepository.getSetExpansion(request.getEclQuery(), request.isIncludeLegacy(),request.getStatusFilter());
    }

    public SearchResponse eclSearch(EclSearchRequest request) throws DataFormatException, JsonProcessingException, QueryException {
        int limit = request.getLimit();
        Set<Concept> evaluated = evaluateECLQuery(request);
        List<SearchResultSummary> evaluatedAsSummary = evaluated
            .stream()
            .limit(limit != 0 ? limit : 1000)
            .map(concept ->
                new SearchResultSummary()
                    .setIri(concept.getIri())
                    .setName(concept.getName())
                    .setCode(concept.getCode())
                    .setScheme(concept.getScheme())
                    .setStatus(concept.getStatus())
                    .setEntityType(concept.getType())
            ).collect(Collectors.toList());
        SearchResponse result = new SearchResponse();
        result.setEntities(evaluatedAsSummary);
        result.setCount(evaluated.size());
        result.setPage(1);
        return result;
    }

    public String getECLFromQuery(Query query) throws DataFormatException {
        return IMLToECL.getECLFromQuery(query, true);
    }
}
