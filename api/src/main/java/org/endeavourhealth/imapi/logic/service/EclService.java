package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.eclBuilder.BoolGroup;
import org.endeavourhealth.imapi.model.eclBuilder.EclBuilderException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.transforms.ECLBuilderToIMQ;
import org.endeavourhealth.imapi.transforms.ECLToIMQ;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.transforms.IMQToECLBuilder;
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

    public String getEcl(Query inferred) throws QueryException {
        if (inferred == null) throw new QueryException("Missing data for ECL conversion");
        else return new IMQToECL().getECLFromQuery(inferred,true);
    }

    public int getEclSearchTotalCount(EclSearchRequest request) throws QueryException {
        return setRepository.getSetExpansionTotalCount(request.getEclQuery(), request.isIncludeLegacy(),request.getStatusFilter(), List.of());
    }

    public Set<Concept> evaluateECLQuery(EclSearchRequest request) throws JsonProcessingException, QueryException {
        return setRepository.getSetExpansion(request.getEclQuery(), request.isIncludeLegacy(),request.getStatusFilter(), List.of(),new Page().setPageNumber(request.getPage()).setPageSize(request.getSize()));
    }

    public SearchResponse eclSearch(EclSearchRequest request) throws JsonProcessingException, QueryException {
        int totalCount = getEclSearchTotalCount(request);
        Set<Concept> evaluated = evaluateECLQuery(request);
        List<SearchResultSummary> evaluatedAsSummary = evaluated
            .stream()
            .map(concept ->
                new SearchResultSummary()
                    .setIri(concept.getIri())
                    .setName(concept.getName())
                    .setCode(concept.getCode())
                    .setScheme(concept.getScheme())
                    .setStatus(concept.getStatus())
                    .setEntityType(concept.getEntityType())
            ).collect(Collectors.toList());
        SearchResponse result = new SearchResponse();
        result.setEntities(evaluatedAsSummary);
        result.setCount(totalCount);
        result.setPage(request.getPage());
        return result;
    }

    public String getECLFromQuery(Query query,Boolean includeNames) throws QueryException {
        return new IMQToECL().getECLFromQuery(query, includeNames);
    }

    public Query getQueryFromEcl(String ecl) throws DataFormatException, EclFormatException {
        return new ECLToIMQ().getQueryFromECL(ecl);
    }

    public BoolGroup getEclBuilderFromQuery(Query query) throws QueryException, EclBuilderException {
        return new IMQToECLBuilder().getEclBuilderFromQuery(query);
    }

    public Query getQueryFromEclBuilder(BoolGroup boolGroup) throws EclBuilderException {
        Match match = new ECLBuilderToIMQ().getIMQFromEclBuilder(boolGroup);
        return new Query().addMatch(match);
    }

    public Boolean validateEcl(String ecl) {
        return new ECLToIMQ().validateEcl(ecl);
    }
}
