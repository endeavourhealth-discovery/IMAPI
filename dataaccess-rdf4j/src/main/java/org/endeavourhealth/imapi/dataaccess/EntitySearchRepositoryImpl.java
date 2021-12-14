package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;

import java.util.Collections;
import java.util.List;

public class EntitySearchRepositoryImpl implements EntitySearchRepository {
    @Override
    public List<SearchResultSummary> advancedSearch(SearchRequest request) {
        return Collections.emptyList();
    }

}
