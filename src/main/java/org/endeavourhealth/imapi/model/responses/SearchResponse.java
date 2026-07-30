package org.endeavourhealth.imapi.model.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;

@Getter
public class SearchResponse {

  private Integer page;
  private Integer size;
  private Integer totalCount;
  private Integer highestUsage;
  private String term;
  private List<SearchResultSummary> entities = new ArrayList<>();
  private boolean isExactMatch;

  public boolean isExactMatch() {
    return isExactMatch;
  }

  public SearchResponse setExactMatch(boolean exactMatch) {
    isExactMatch = exactMatch;
    return this;
  }

  public SearchResponse setTerm(String term) {
    this.term = term;
    return this;
  }

  public SearchResponse setPage(Integer page) {
    this.page = page;
    return this;
  }

  public SearchResponse setSize(Integer size) {
    this.size = size;
    return this;
  }

  public SearchResponse setHighestUsage(Integer maxUsage) {
    this.highestUsage = maxUsage;
    return this;
  }

  public SearchResponse setEntities(List<SearchResultSummary> entities) {
    this.entities = entities;
    return this;
  }

  public SearchResponse addEntity(SearchResultSummary entity) {
    this.entities.add(entity);
    return this;
  }

  public SearchResponse addEntities(List<SearchResultSummary> entities) {
    this.entities.addAll(entities);
    return this;
  }

  public SearchResponse setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }
}
