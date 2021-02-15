package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptType;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {
    private String termFilter;
    private List<Byte> statusFilter = new ArrayList<>();
    private List<ConceptReference> schemeFilter = new ArrayList<>();
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

    public List<Byte> getStatusFilter() {
        return statusFilter;
    }

    public SearchRequest setStatusFilter(List<Byte> statusFilter) {
        this.statusFilter = statusFilter;
        return this;
    }

    public List<ConceptReference> getSchemeFilter() {
        return schemeFilter;
    }

    public SearchRequest setSchemeFilter(List<ConceptReference> schemeFilter) {
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
