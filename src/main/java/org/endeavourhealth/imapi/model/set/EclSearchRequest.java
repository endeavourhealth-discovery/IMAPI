package org.endeavourhealth.imapi.model.set;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class EclSearchRequest {
  private Query eclQuery;
  private boolean includeLegacy;
  private int limit = 1000;
  private Set<TTIriRef> statusFilter = new HashSet<>();
  private int page = 1;
  private int size = 20;
  private Set<String> select = new HashSet<>();

  public Query getEclQuery() {
    return eclQuery;
  }

  public EclSearchRequest setEclQuery(Query eclQuery) {
    this.eclQuery = eclQuery;
    return this;
  }

  public boolean isIncludeLegacy() {
    return includeLegacy;
  }

  public EclSearchRequest setIncludeLegacy(boolean includeLegacy) {
    this.includeLegacy = includeLegacy;
    return this;
  }

  public int getLimit() {
    return limit;
  }

  public EclSearchRequest setLimit(int limit) {
    this.limit = limit;
    return this;
  }

  public Set<TTIriRef> getStatusFilter() {
    return statusFilter;
  }

  public EclSearchRequest setStatusFilter(Set<TTIriRef> statusFilter) {
    this.statusFilter = statusFilter;
    return this;
  }

  public int getPage() {
    return page;
  }

  public EclSearchRequest setPage(int page) {
    this.page = page;
    return this;
  }

  public int getSize() {
    return size;
  }

  public EclSearchRequest setSize(int size) {
    this.size = size;
    return this;
  }

  public Set<String> getSelect() {
    return select;
  }

  public EclSearchRequest setSelect(Set<String> select) {
    this.select = select;
    return this;
  }
}
