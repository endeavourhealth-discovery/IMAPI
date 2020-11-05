package com.endavourhealth.models.objectModel;

import java.util.List;

public class Restriction {
    private List<String> orderBy;
    private String restrictedBy;
    private Integer limit;

    public List<String> getOrderBy() {
        return orderBy;
    }

    public Restriction setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getRestrictedBy() {
        return restrictedBy;
    }

    public Restriction setRestrictedBy(String restrictedBy) {
        this.restrictedBy = restrictedBy;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public Restriction setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
