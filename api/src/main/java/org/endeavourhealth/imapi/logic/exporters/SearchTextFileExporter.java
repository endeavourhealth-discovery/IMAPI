package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.DownloadByQueryOptions;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Slf4j
public class SearchTextFileExporter {
  private SearchService searchService = new SearchService();
  private EclService eclService = new EclService();

  public String getSearchFile(DownloadByQueryOptions downloadByQueryOptions) throws OpenSearchException, JsonProcessingException, QueryException, DownloadException {
    log.debug("Exporting search results to {}", downloadByQueryOptions.getFormat());
    SearchResponse searchResponse = null;
    if (null != downloadByQueryOptions.getQueryRequest()) {
      QueryRequest queryRequest = downloadByQueryOptions.getQueryRequest();
      queryRequest.setPage(new Page().setPageNumber(1).setPageSize(downloadByQueryOptions.getTotalCount()));
      searchResponse = searchService.queryIMSearch(downloadByQueryOptions.getQueryRequest());
    } else if (null != downloadByQueryOptions.getEclSearchRequest()) {
      EclSearchRequest eclSearchRequest = downloadByQueryOptions.getEclSearchRequest();
      eclSearchRequest.setPage(1);
      eclSearchRequest.setSize(downloadByQueryOptions.getTotalCount());
      searchResponse = eclService.eclSearch(eclSearchRequest);
    }
    if (null == searchResponse) throw new QueryException("Download must have either queryRequest or eclSearchRequest");
    List<SearchResultSummary> entities = searchResponse.getEntities();
    if (null == entities || entities.isEmpty()) return "";
    return generateFile(entities, downloadByQueryOptions);
  }

  private String generateFile(List<SearchResultSummary> entities, DownloadByQueryOptions downloadByQueryOptions) throws DownloadException {
    log.trace("Generating output...");
    StringJoiner results = new StringJoiner(System.lineSeparator());
    addHeader(downloadByQueryOptions, results);
    for (SearchResultSummary entity : entities) {
      addRow(entity, downloadByQueryOptions, results);
    }
    return results.toString();
  }

  private void addHeader(DownloadByQueryOptions downloadByQueryOptions, StringJoiner results) {
    String deliminator = downloadByQueryOptions.getFormat().equals("csv") ? "," : "\t";
    results.add("Iri" + deliminator + "Name" + deliminator + "Code" + deliminator + "Description" + deliminator + "Status" + deliminator + "Scheme" + deliminator + "Type");
  }

  private void addRow(SearchResultSummary entity, DownloadByQueryOptions downloadByQueryOptions, StringJoiner results) throws DownloadException {
    StringJoiner line = new StringJoiner(downloadByQueryOptions.getFormat().equals("csv") ? "," : "\t");
    addItemToJoiner(line, entity.getIri(), false);
    addItemToJoiner(line, entity.getName(), false);
    addItemToJoiner(line, entity.getCode(), false);
    addItemToJoiner(line, entity.getDescription(), false);
    addItemToJoiner(line, entity.getStatus(), false);
    addItemToJoiner(line, entity.getScheme(), false);
    addItemToJoiner(line, entity.getType(), false);
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
    switch (item) {
      case String string -> stringJoiner.add("\"" + string + "\"");
      case Integer integer -> stringJoiner.add(integer.toString());
      case TTIriRef ttIriRef -> stringJoiner.add(iriToString(ttIriRef, inArray));
      case List<?> list -> stringJoiner.add(arrayToString(list));
      case Set<?> subSet -> stringJoiner.add(setToString(subSet));
      case null -> stringJoiner.add("");
      default -> throw new DownloadException("Unexpected type encountered while converting to csv" + item);
    }
  }
}
