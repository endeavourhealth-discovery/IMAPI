package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataModelAttribute {
    private String iri;
    private List<String> range;
    private Integer min;
    private Integer max;
    private String description;
    private Integer headerOrder;
    private String displayHeader;
    private Integer displayOrder;

    public String getIri() {
        return iri;
    }

    public DataModelAttribute setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("Range")
    public List<String> getRange() {
        return range;
    }

    public DataModelAttribute setRange(List<String> range) {
        this.range = range;
        return this;
    }

    public DataModelAttribute addRange(String range) {
        if (this.range == null)
            this.range = new ArrayList<>();

        this.range.add(range);
        return this;
    }

    public Integer getMin() {
        return min;
    }

    public DataModelAttribute setMin(Integer min) {
        this.min = min;
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public DataModelAttribute setMax(Integer max) {
        this.max = max;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DataModelAttribute setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getHeaderOrder() {
        return headerOrder;
    }

    public DataModelAttribute setHeaderOrder(Integer headerOrder) {
        this.headerOrder = headerOrder;
        return this;
    }

    public String getDisplayHeader() {
        return displayHeader;
    }

    public DataModelAttribute setDisplayHeader(String displayHeader) {
        this.displayHeader = displayHeader;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public DataModelAttribute setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }
}
