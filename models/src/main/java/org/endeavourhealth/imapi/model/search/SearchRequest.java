package org.endeavourhealth.imapi.model.search;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {
    private String terms;
    private List<String> types = new ArrayList<>();
    private boolean includeLegacy = false;
    private int page = 1;
    private int size = 20;

    public String getTerms() {
        return terms;
    }

    public SearchRequest setTerms(String terms) {
        this.terms = terms;
        return this;
    }

    public List<String> getTypes() {
        return types;
    }

    public SearchRequest setTypes(List<String> types) {
        this.types = types;
        return this;
    }

    public boolean isIncludeLegacy() {
        return includeLegacy;
    }

    public SearchRequest setIncludeLegacy(boolean includeLegacy) {
        this.includeLegacy = includeLegacy;
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
