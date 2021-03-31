package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {
    private String termFilter;
    private List<String> statusFilter = new ArrayList<>();
    private List<String> typeFilter = new ArrayList<>();
    private List<String> schemeFilter = new ArrayList<>();
    private List<String> descendentFilter = new ArrayList<>();
    private List<String> markIfDescendentOf = new ArrayList<>();
    private int page = 1;
    private int size = 20;

    public String getTermFilter() {
        return termFilter;
    }

    public SearchRequest setTermFilter(String termFilter) {
        this.termFilter = termFilter;
        return this;
    }

    public List<String> getStatusFilter() {
        return statusFilter;
    }

    public SearchRequest setStatusFilter(List<String> statusFilter) {
        this.statusFilter = statusFilter;
        return this;
    }

    public List<String> getTypeFilter() {
        return typeFilter;
    }

    public SearchRequest setTypeFilter(List<String> typeFilter) {
        this.typeFilter = typeFilter;
        return this;
    }

    public List<String> getSchemeFilter() {
        return schemeFilter;
    }

    public SearchRequest setSchemeFilter(List<String> schemeFilter) {
        this.schemeFilter = schemeFilter;
        return this;
    }

    public List<String> getDescendentFilter() {
        return descendentFilter;
    }

    public SearchRequest setDescendentFilter(List<String> descendentFilter) {
        this.descendentFilter = descendentFilter;
        return this;
    }

    public List<String> getMarkIfDescendentOf() {
        return markIfDescendentOf;
    }

    public SearchRequest setMarkIfDescendentOf(List<String> markIfDescendentOf) {
        this.markIfDescendentOf = markIfDescendentOf;
        return this;
    }

    public int getPage() {
        return page;
    }

    public SearchRequest setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public SearchRequest setSize(int size) {
        this.size = size;
        return this;
    }
}
