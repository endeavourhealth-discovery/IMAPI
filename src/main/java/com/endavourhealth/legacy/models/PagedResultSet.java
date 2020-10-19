package com.endavourhealth.legacy.models;

import java.util.ArrayList;
import java.util.List;

public class PagedResultSet<T> {
    private int pageSize;
    private int page;
    private int totalRecords;
    private List<T> result = new ArrayList<>();

    public int getPageSize() {
        return pageSize;
    }

    public PagedResultSet<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PagedResultSet<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public PagedResultSet<T> setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
        return this;
    }

    public List<T> getResult() {
        return result;
    }

    public PagedResultSet<T> setResult(List<T> result) {
        this.result = result;
        return this;
    }
}
