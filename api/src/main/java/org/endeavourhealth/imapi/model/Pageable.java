package org.endeavourhealth.imapi.model;

import java.util.List;

public class Pageable<T>{

    private Integer totalCount;
    private Integer currentPage;
    private Integer pageSize;
    private List<T> result;

    public Integer getTotalCount() {
        return totalCount;
    }

    public Pageable<T> setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Pageable<T> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Pageable<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<T> getResult() {
        return result;
    }

    public Pageable<T> setResult(List<T> result) {
        this.result = result;
        return this;
    }
}
