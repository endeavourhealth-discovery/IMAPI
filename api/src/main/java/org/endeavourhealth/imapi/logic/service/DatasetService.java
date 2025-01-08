package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DatasetRepository;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatasetService {
  private DatasetRepository datasetRepository = new DatasetRepository();
  private EntityService entityService = new EntityService();

  public SearchResponse searchAllowableDatamodelProperties(String datasetIri, String searchTerm, int page, int size) throws Exception {
    List<TTIriRef> results = datasetRepository.searchAllowableDatamodelProperties(datasetIri, searchTerm, page, size);
    List<SearchResultSummary> resultsAsSummaries = new ArrayList<>();
    for (TTIriRef result : results) {
      resultsAsSummaries.add(entityService.getSummary(result.getIri()));
    }
    SearchResponse searchResponse = new SearchResponse();
    searchResponse.addEntities(resultsAsSummaries);
    return searchResponse;
  }
}
