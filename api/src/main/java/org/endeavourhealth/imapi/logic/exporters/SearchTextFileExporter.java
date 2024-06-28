package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.DownloadOptions;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

public class SearchTextFileExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SearchTextFileExporter.class);
    private SearchService searchService = new SearchService();
    private EclService eclService = new EclService();

    public String getSearchFile(DownloadOptions downloadOptions) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException, DataFormatException, DownloadException {
        LOG.debug("Exporting search results to " + downloadOptions.getFormat());
        SearchResponse searchResponse = null;
        if (null != downloadOptions.getQueryRequest()) {
            QueryRequest queryRequest = downloadOptions.getQueryRequest();
            queryRequest.setPage(new Page().setPageNumber(1).setPageSize(downloadOptions.getTotalCount()));
            searchResponse = searchService.queryIMSearch(downloadOptions.getQueryRequest());
        } else if (null != downloadOptions.getEclSearchRequest()) {
            EclSearchRequest eclSearchRequest = downloadOptions.getEclSearchRequest();
            eclSearchRequest.setPage(1);
            eclSearchRequest.setSize(downloadOptions.getTotalCount());
            searchResponse = eclService.eclSearch(eclSearchRequest);
        }
        if (null == searchResponse) throw new QueryException("Download must have either queryRequest or eclSearchRequest");
        List<SearchResultSummary> entities = searchResponse.getEntities();
        if (null == entities || entities.isEmpty()) return "";
        return generateFile(entities, downloadOptions);
    }

    private String generateFile(List<SearchResultSummary> entities, DownloadOptions downloadOptions) throws DownloadException {
        LOG.trace("Generating output...");
        StringJoiner results = new StringJoiner(System.lineSeparator());
        addHeader(downloadOptions, results);
        for (SearchResultSummary entity : entities) {
            addRow(entity, downloadOptions, results);
        }
        return results.toString();
    }

    private void addHeader(DownloadOptions downloadOptions, StringJoiner results) {
        String deliminator = downloadOptions.getFormat().equals("csv") ? "," : "\t";
        results.add("Iri" + deliminator + "Name" + deliminator + "Code" + deliminator + "Description" + deliminator + "Status" + deliminator + "Scheme" + deliminator + "Type");
    }

    private void addRow(SearchResultSummary entity, DownloadOptions downloadOptions, StringJoiner results) throws DownloadException {
        StringJoiner line = new StringJoiner(downloadOptions.getFormat().equals("csv") ? "," : "\t");
        addItemToJoiner(line, entity.getIri(),false);
        addItemToJoiner(line ,entity.getName(),false);
        addItemToJoiner(line, entity.getCode(),false);
        addItemToJoiner(line, entity.getDescription(), false);
        addItemToJoiner(line, entity.getStatus(), false);
        addItemToJoiner(line, entity.getScheme(), false);
        addItemToJoiner(line, entity.getEntityType(), false);
        results.add(line.toString());
    }

    private String iriToString(TTIriRef iri, boolean inArray) {
        if (inArray) return "\"" + iri.getName() + "\"";
        else return iri.getName();
    }

    private String arrayToString(List<?> list) throws DownloadException {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Object item : list) {
            addItemToJoiner(stringJoiner, item, list.size() > 1);
        }
        if (list.size() > 1) return "[" + stringJoiner.toString() + "]";
        return stringJoiner.toString();
    }

    private String setToString(Set<?> set) throws DownloadException {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Object item : set) {
            addItemToJoiner(stringJoiner, item, set.size() > 1);
        }
        if (set.size() > 1) return "[" + stringJoiner.toString() + "]";
        return stringJoiner.toString();
    }

    private void addItemToJoiner(StringJoiner stringJoiner, Object item, boolean inArray) throws DownloadException {
        if (item instanceof String string) stringJoiner.add("\"" + string + "\"");
        else if (item instanceof Integer integer) stringJoiner.add(integer.toString());
        else if (item instanceof TTIriRef ttIriRef) stringJoiner.add(iriToString(ttIriRef, inArray  ));
        else if (item instanceof List<?> list) stringJoiner.add(arrayToString(list));
        else if (item instanceof Set<?> subSet) stringJoiner.add(setToString(subSet));
        else if (null==item) stringJoiner.add("");
        else throw new DownloadException("Unexpected type encountered while converting to csv" + item);
    }
}
