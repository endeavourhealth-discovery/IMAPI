package org.endeavourhealth.imapi.model.search;

import lombok.Getter;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;

@Getter
public class DownloadByQueryOptions {
  private QueryRequest queryRequest;
  private EclSearchRequest eclSearchRequest;
  private int totalCount;
  private String format;
  private boolean includeDefinition;
  private boolean includeCore;
  private boolean includeLegacy;
  private boolean includeSubsets;
  private boolean subsetsOnOwnRow;
  private boolean im1id;

  public DownloadByQueryOptions setQueryRequest(QueryRequest queryRequest) {
    this.queryRequest = queryRequest;
    return this;
  }

  public DownloadByQueryOptions setEclSearchRequest(EclSearchRequest eclSearchRequest) {
    this.eclSearchRequest = eclSearchRequest;
    return this;
  }

  public DownloadByQueryOptions setTotalCount(int totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  public DownloadByQueryOptions setFormat(String format) {
    this.format = format;
    return this;
  }

  public DownloadByQueryOptions setIncludeDefinition(boolean includeDefinition) {
    this.includeDefinition = includeDefinition;
    return this;
  }

  public DownloadByQueryOptions setIncludeCore(boolean includeCore) {
    this.includeCore = includeCore;
    return this;
  }

  public DownloadByQueryOptions setIncludeLegacy(boolean includeLegacy) {
    this.includeLegacy = includeLegacy;
    return this;
  }

  public DownloadByQueryOptions setIncludeSubsets(boolean includeSubsets) {
    this.includeSubsets = includeSubsets;
    return this;
  }

  public DownloadByQueryOptions setSubsetsOnOwnRow(boolean subsetsOnOwnRow) {
    this.subsetsOnOwnRow = subsetsOnOwnRow;
    return this;
  }

  public DownloadByQueryOptions setIm1id(boolean im1id) {
    this.im1id = im1id;
    return this;
  }
}
