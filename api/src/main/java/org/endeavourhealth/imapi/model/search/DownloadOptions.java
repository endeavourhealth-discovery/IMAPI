package org.endeavourhealth.imapi.model.search;

import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;

@Getter
public class DownloadOptions {
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

    public DownloadOptions setQueryRequest(QueryRequest queryRequest) {
        this.queryRequest = queryRequest;
        return this;
    }

    public DownloadOptions setEclSearchRequest(EclSearchRequest eclSearchRequest) {
        this.eclSearchRequest = eclSearchRequest;
        return this;
    }

    public DownloadOptions setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public DownloadOptions setFormat(String format) {
        this.format = format;
        return this;
    }

    public DownloadOptions setIncludeDefinition(boolean includeDefinition) {
        this.includeDefinition = includeDefinition;
        return this;
    }

    public DownloadOptions setIncludeCore(boolean includeCore) {
        this.includeCore = includeCore;
        return this;
    }

    public DownloadOptions setIncludeLegacy(boolean includeLegacy) {
        this.includeLegacy = includeLegacy;
        return this;
    }

    public DownloadOptions setIncludeSubsets(boolean includeSubsets) {
        this.includeSubsets = includeSubsets;
        return this;
    }

    public DownloadOptions setSubsetsOnOwnRow(boolean subsetsOnOwnRow) {
        this.subsetsOnOwnRow = subsetsOnOwnRow;
        return this;
    }

    public DownloadOptions setIm1id(boolean im1id) {
        this.im1id = im1id;
        return this;
    }
}
