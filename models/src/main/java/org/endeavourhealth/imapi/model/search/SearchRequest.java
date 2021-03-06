package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.ConceptReference;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {
    private String terms;
    private List<String> types = new ArrayList<>();
    private List<ConceptReference> codeSchemes = new ArrayList<>();
    private int page = 1;
    private int size = 20;
    private List<Byte> statuses = new ArrayList<>();

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

    public List<ConceptReference> getCodeSchemes() {
        return codeSchemes;
    }

    public SearchRequest setCodeSchemes(List<ConceptReference> codeSchemes) {
        this.codeSchemes = codeSchemes;
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

    public List<Byte> getStatuses() {
        return statuses;
    }

    public SearchRequest setStatuses(List<Byte> statuses) {
        this.statuses = statuses;
        return this;
    }
}
