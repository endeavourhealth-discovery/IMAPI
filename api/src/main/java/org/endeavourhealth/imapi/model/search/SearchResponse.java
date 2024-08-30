package org.endeavourhealth.imapi.model.search;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class SearchResponse {
  private Integer page;
  private Integer count;
  private Integer totalCount;
  private Integer highestUsage;
  private String term;
  private List<SearchResultSummary> entities = new ArrayList<>();

  public SearchResponse setTerm(String term) {
    this.term = term;
    return this;
  }

  public SearchResponse setPage(Integer page) {
    this.page = page;
    return this;
  }

  public SearchResponse setCount(Integer count) {
    this.count = count;
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
