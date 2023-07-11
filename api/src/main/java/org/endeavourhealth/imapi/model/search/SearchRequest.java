package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.ArrayList;
import java.util.List;

@Schema(
    name="Search request",
    description = "Structure containing search request parameters and filters"
)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SearchRequest {
    private String termFilter;
    private String index = System.getenv("OPENSEARCH_INDEX") == null ? "concept" : System.getenv("OPENSEARCH_INDEX");
    private List<String> statusFilter = new ArrayList<>();
    private List<String> typeFilter = new ArrayList<>();
    private List<String> schemeFilter = new ArrayList<>();
    private List<String> markIfDescendentOf = new ArrayList<>();
    private List<String> isA = new ArrayList<>();
    private List<String> memberOf = new ArrayList<>();
    private int page = 1;
    private int size = 20;
    private int from;
    private List<String> select = new ArrayList<>();
    private String sortField;
    private String sortDirection;

    public int getFrom() {
        return from;
    }

    public SearchRequest setFrom(int from) {
        this.from = from;
        return this;
    }

    @Schema(name = "field selections",
    description = "list of fields or property paths from search result summary to return ",
    example = "name, iri, entityType.@id")
    public List<String> getSelect() {
        return select;
    }

    public SearchRequest setSelect(List<String> select) {
        this.select = select;
        return this;
    }

    @JsonIgnore
    public SearchRequest addSelect(String select){
        this.select.add(select);
        return this;
    }

    @Schema(name = "is a  filter",
      description = "List of IRIs that must be supertypes of the matches",
      example = "Encounter record")
    public List<String> getIsA() {
        return isA;
    }

    public SearchRequest setIsA(List<String> isA) {
        this.isA = isA;
        return this;
    }

    @Schema(name = "member of filter",
        description = "List of set IRIs that the match must be a member of",
        example = "Encounter record")
    public List<String> getMemberOf() {
        return memberOf;
    }

    public SearchRequest setMemberOf(List<String> memberOf) {
        this.memberOf = memberOf;
        return this;
    }

    @Schema(name = "Term filter",
        description = "Plain text, space separated list of terms",
        example = "Encounter record")
    public String getTermFilter() {
        return termFilter;
    }

    public SearchRequest setTermFilter(String termFilter) {
        this.termFilter = termFilter;
        return this;
    }

    @Schema(name = "Status filter",
        description = "List of entity status IRI's",
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

    @Schema(name = "Type filter",
        description = "List of entity type IRI's",
        example = "['http://www.w3.org/2002/07/owl#Class', 'http://endhealth.info/im#RecordType']")
    public List<String> getTypeFilter() {
        return typeFilter;
    }

    public SearchRequest setTypeFilter(List<String> typeFilter) {
        this.typeFilter = typeFilter;
        return this;
    }
    public SearchRequest addType(String type){
        if (this.getTypeFilter()==null)
            this.setTypeFilter(new ArrayList<>());
        this.getTypeFilter().add(type);
        return this;
    }

    @Schema(name = "Code scheme filter",
        description = "List of code scheme IRI's",
        example = "['http://endhealth.info/im#SnomedCodeScheme', 'http://endhealth.info/im#DiscoveryCodeScheme']")
    public List<String> getSchemeFilter() {
        return schemeFilter;
    }

    public SearchRequest setSchemeFilter(List<String> schemeFilter) {
        this.schemeFilter = schemeFilter;
        return this;
    }



    @Schema(name = "SetModel inheritance filter",
        description = "Marks the results if they are descendants of any of these entities, but does not filter by them",
        example = "['http://endhealth.info/im#Encounter']")
    public List<String> getMarkIfDescendentOf() {
        return markIfDescendentOf;
    }

    public SearchRequest setMarkIfDescendentOf(List<String> markIfDescendentOf) {
        this.markIfDescendentOf = markIfDescendentOf;
        return this;
    }

    @Schema(name = "Search result page number",
        description = "The search result page number to retrieve",
        example = "1")
    public int getPage() {
        return page;
    }

    public SearchRequest setPage(int page) {
        this.page = page;
        return this;
    }

    @Schema(name = "Search result page size",
        description = "The number of results to retrieve per page",
        example = "15")
    public int getSize() {
        return size;
    }

    public SearchRequest setSize(int size) {
        this.size = size;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public SearchRequest setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getSortField() {
        return sortField;
    }

    public SearchRequest setSortField(String sortField) {
        this.sortField = sortField;
        return this;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public SearchRequest setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }


}
