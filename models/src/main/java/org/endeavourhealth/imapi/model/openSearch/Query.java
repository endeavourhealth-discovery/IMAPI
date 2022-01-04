package org.endeavourhealth.imapi.model.openSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

//@JsonRootName(value = "bool")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "bool")
public class Query {
    private List<Prefix> musts;
    private List<Filter> filters;

    public Query(List<Prefix> must, List<Filter> filter) {
        this.musts = must;
        this.filters = filter;
    }

    public Query() {
        this.musts = new ArrayList<>();
        this.filters = new ArrayList<>();
    }

    @JsonProperty("must")
    public List<Prefix> getMusts() {
        return musts;
    }

    public void setMusts(List<Prefix> must) {
        this.musts = must;
    }

    public Query addMust(String must) {
        this.musts.add(new Prefix(must));
        return this;
    }

    @JsonProperty("filter")
    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filter) {
        this.filters = filter;
    }

    public Query addFilter(Filter filter) {
        this.filters.add(filter);
        return this;
    }
}
