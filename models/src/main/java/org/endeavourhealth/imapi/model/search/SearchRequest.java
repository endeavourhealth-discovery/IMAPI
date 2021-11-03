package org.endeavourhealth.imapi.model.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
    value="Search request",
    description = "Structure containing search request parameters and filters"
)
public class SearchRequest {
    private String termFilter;
    private List<String> statusFilter = new ArrayList<>();
    private List<String> typeFilter = new ArrayList<>();
    private List<String> schemeFilter = new ArrayList<>();
    private List<String> descendentFilter = new ArrayList<>();
    private List<String> markIfDescendentOf = new ArrayList<>();
    private int page = 1;
    private int size = 20;

    @ApiModelProperty(value = "Term filter",
        notes = "Plain text, space separated list of terms",
        example = "Encounter record")
    public String getTermFilter() {
        return termFilter;
    }

    public SearchRequest setTermFilter(String termFilter) {
        this.termFilter = termFilter;
        return this;
    }

    @ApiModelProperty(value = "Status filter",
        notes = "List of entity status IRI's",
        allowableValues = "http://endhealth.info/im#Draft, http://endhealth.info/im#Active, http://endhealth.info/im#Inactive",
        example = "['http://endhealth.info/im#Draft', 'http://endhealth.info/im#Active']"
    )
    public List<String> getStatusFilter() {
        return statusFilter;
    }

    public SearchRequest setStatusFilter(List<String> statusFilter) {
        this.statusFilter = statusFilter;
        return this;
    }

    @ApiModelProperty(value = "Type filter",
        notes = "List of entity type IRI's",
        example = "['http://www.w3.org/2002/07/owl#Class', 'http://endhealth.info/im#RecordType']")
    public List<String> getTypeFilter() {
        return typeFilter;
    }

    public SearchRequest setTypeFilter(List<String> typeFilter) {
        this.typeFilter = typeFilter;
        return this;
    }

    @ApiModelProperty(value = "Code scheme filter",
        notes = "List of code scheme IRI's",
        example = "['http://endhealth.info/im#SnomedCodeScheme', 'http://endhealth.info/im#DiscoveryCodeScheme']")
    public List<String> getSchemeFilter() {
        return schemeFilter;
    }

    public SearchRequest setSchemeFilter(List<String> schemeFilter) {
        this.schemeFilter = schemeFilter;
        return this;
    }

    @ApiModelProperty(value = "Entity subtype filter",
        notes = "List of IRI's of which the entity must be a descendant",
        example = "['http://endhealth.info/im#DiscoveryOntology']")
    public List<String> getDescendentFilter() {
        return descendentFilter;
    }

    public SearchRequest setDescendentFilter(List<String> descendentFilter) {
        this.descendentFilter = descendentFilter;
        return this;
    }

    @ApiModelProperty(value = "Entity inheritance filter",
        notes = "Marks the results if they are descendants of any of these entities, but does not filter by them",
        example = "['http://endhealth.info/im#Encounter']")
    public List<String> getMarkIfDescendentOf() {
        return markIfDescendentOf;
    }

    public SearchRequest setMarkIfDescendentOf(List<String> markIfDescendentOf) {
        this.markIfDescendentOf = markIfDescendentOf;
        return this;
    }

    @ApiModelProperty(value = "Search result page number",
        notes = "The search result page number to retrieve",
        example = "1")
    public int getPage() {
        return page;
    }

    public SearchRequest setPage(int page) {
        this.page = page;
        return this;
    }

    @ApiModelProperty(value = "Search result page size",
        notes = "The number of results to retrieve per page",
        example = "15")
    public int getSize() {
        return size;
    }

    public SearchRequest setSize(int size) {
        this.size = size;
        return this;
    }
}
