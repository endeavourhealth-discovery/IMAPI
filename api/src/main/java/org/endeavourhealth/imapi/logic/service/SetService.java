package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.transforms.ECLToIML;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Component
public class SetService {
    private final SetRepository setRepository = new SetRepository();
    private final ECLToIML eclToIML = new ECLToIML();
    private final QueryRepository queryRepository = new QueryRepository();

    public Query getQueryFromECL(String ecl) throws DataFormatException {
        Query query = eclToIML.getQueryFromECL(ecl);
        queryRepository.labelQuery(query);
        return query;
    }

    public Set<Concept> evaluateECL(String ecl, boolean includeLegacy) throws DataFormatException, JsonProcessingException {
        Query definition = eclToIML.getQueryFromECL(ecl);
        return setRepository.getSetExpansion(definition, includeLegacy);
    }

    public SearchResponse eclSearch(boolean includeLegacy, Integer limit, String ecl) throws DataFormatException, JsonProcessingException {
        Set<Concept> evaluated = evaluateECL(ecl, includeLegacy);
        List<SearchResultSummary> evaluatedAsSummary = evaluated.stream().limit(limit != null ? limit : 1000).map(concept ->
                new SearchResultSummary().setIri(concept.getIri()).setName(concept.getName()).setCode(concept.getCode())).collect(Collectors.toList());
        SearchResponse result = new SearchResponse();
        result.setEntities(evaluatedAsSummary);
        result.setCount(evaluated.size());
        result.setPage(1);
        return result;
    }

    public Query setQueryLabels(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }

    public String getECLFromQuery(Query query) throws DataFormatException {
        return IMLToECL.getECLFromQuery(query, true);
    }

    public boolean validateECL(String ecl) {
        try {
            eclToIML.getQueryFromECL(ecl);
            return true;
        } catch (Exception _e) {
            return false;
        }
    }
}
