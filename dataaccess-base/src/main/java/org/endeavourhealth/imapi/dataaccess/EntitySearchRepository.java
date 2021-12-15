package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;

import java.util.List;

public interface EntitySearchRepository {
    List<SearchResultSummary> advancedSearch(SearchRequest request);
}
