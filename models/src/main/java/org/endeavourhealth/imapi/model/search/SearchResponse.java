package org.endeavourhealth.imapi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    private Integer page;
    private Integer count;
    private List<ConceptSummary> concepts = new ArrayList<>();

    public Integer getPage() {
        return page;
    }

    public SearchResponse setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public SearchResponse setCount(Integer count) {
        this.count = count;
        return this;
    }

    public List<ConceptSummary> getConcepts() {
        return concepts;
    }

    public SearchResponse setConcepts(List<ConceptSummary> concepts) {
        this.concepts = concepts;
        return this;
    }
}
