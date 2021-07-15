package org.endeavourhealth.imapi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    private Integer page;
    private Integer count;
    private List<EntitySummary> entities = new ArrayList<>();

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

    public List<EntitySummary> getEntities() {
        return entities;
    }

    public SearchResponse setEntities(List<EntitySummary> entities) {
        this.entities = entities;
        return this;
    }
}
