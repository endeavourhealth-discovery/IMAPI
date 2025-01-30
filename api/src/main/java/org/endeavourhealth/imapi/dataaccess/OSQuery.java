package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OSQuery {
  private static final Logger LOG = LoggerFactory.getLogger(OSQuery.class);
  private final IMQToOS converter = new IMQToOS();
  private boolean ignoreInvalid;
  private EntityService entityService = new EntityService();

  private static void processNodeResults(QueryRequest request, JsonNode root, CachedObjectMapper om, ArrayNode resultNodes) throws JsonProcessingException {
    for (JsonNode hit : root.get("hits").get("hits")) {
      ObjectNode resultNode = om.createObjectNode();
      resultNodes.add(resultNode);
      ObjectNode osResult = om.treeToValue(hit.get("_source"), ObjectNode.class);
      resultNode.set("@id", osResult.get("iri"));
      resultNode.set(RDFS.LABEL, osResult.get("name"));
      processNodeResultReturn(request, osResult, resultNode);
    }
  }

  private static void processNodeResultReturn(QueryRequest request, ObjectNode osResult, ObjectNode resultNode) {
    if (null == request.getQuery().getReturn())
      return;
    Return select = request.getQuery().getReturn();
    processNodeResultReturnProperty(osResult, resultNode, select);
  }

  private static void processNodeResultReturnProperty(ObjectNode osResult, ObjectNode resultNode, Return select) {
    if (select.getProperty() == null)
      return;

    for (ReturnProperty prop : select.getProperty()) {
      if (prop.getIri() != null) {
        String field = prop.getIri();
        String osField = field.substring(field.lastIndexOf("#") + 1);
        if (osResult.get(osField) != null) {
          resultNode.set(field, osResult.get(osField));
        }
      }
    }

  }

  public SearchResponse openSearchQuery(QueryRequest request) throws QueryException, OpenSearchException {
    return getStandardResults(request);
  }

  public JsonNode imQuery(QueryRequest request) {
    return getNodeResults(request);
  }

  public JsonNode imQuery(QueryRequest request, boolean ignoreInvalid) {
    this.ignoreInvalid = ignoreInvalid;
    return getNodeResults(request);
  }

  public JsonNode getIMOSResults(QueryRequest request) throws QueryException, OpenSearchException {
    Query query = request.getQuery();
    JsonNode results;
    if (query.isImQuery()) {
      Page page = request.getPage();
      request.setPage(null);
      results = new QueryRepository().queryIM(request, false);
      request.setPage(page);
    } else {
      results = getOsResults(request, query);
    }
    if (query.getQuery() != null) {
      for (Query childQuery : query.getQuery()) {
        childQuery.setParentResult(results);
        if (childQuery.isImQuery()) {
          results = new QueryRepository().queryIM(request, false);
        } else {
          results = getOsResults(request, childQuery);
        }
      }
      return results;
    } else
      return results;
  }

  private JsonNode getOsResults(QueryRequest request, Query query) throws QueryException, OpenSearchException {
    if (ignoreInvalid)
      converter.setIgnoreInvalid(true);
    SearchSourceBuilder builder = converter.buildQuery(request, query, IMQToOS.QUERY_TYPE.AUTOCOMPLETE);
    if (builder == null)
      return null;

    request.addTiming("Entry point for autocomplete\"" + request.getTextSearch() + "\"");
    JsonNode results = runQuery(builder);
    if (!results.get("hits").get("hits").isEmpty()) {
      return results;
    }
    if (request.getTextSearch().contains(" ")) {
      builder = converter.buildQuery(request, query, IMQToOS.QUERY_TYPE.NGRAM, Fuzziness.ZERO);
      results = runQuery(builder);
      if (!results.get("hits").get("hits").isEmpty()) {
        return results;
      } else {
        builder = converter.buildQuery(request, query, IMQToOS.QUERY_TYPE.MULTIWORD);
        results = runQuery(builder);
        if (!results.get("hits").get("hits").isEmpty()) {
          return results;
        } else {
          builder = converter.buildQuery(request, query, IMQToOS.QUERY_TYPE.NGRAM, Fuzziness.TWO);
          results = runQuery(builder);
          if (!results.get("hits").get("hits").isEmpty()) {
            return results;
          }
        }
      }
    }

    String corrected = spellingCorrection(request);
    if (corrected != null) {
      request.setTextSearch(corrected);
      builder = new IMQToOS().buildQuery(request, query, IMQToOS.QUERY_TYPE.AUTOCOMPLETE);
      return runQuery(builder);
    }
    return results;
  }

  private String spellingCorrection(QueryRequest request) throws OpenSearchException {
    String oldTerm = request.getTextSearch();
    String newTerm = null;
    String suggester = "{\n" +
      "  \"suggest\": {\n" +
      "    \"spell-check\": {\n" +
      "      \"text\": \"" + oldTerm + "\",\n" +
      "      \"term\": {\n" +
      "        \"field\": \"termCode.term\",\n" +
      "        \"sort\": \"score\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}";
    HttpResponse<String> response = getResponse(suggester);
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      JsonNode root = om.readTree(response.body());
      JsonNode suggestions = root.get("suggest").get("spell-check");
      String[] words = oldTerm.split(" ");
      newTerm = processSuggestions(suggestions, oldTerm, newTerm, words);
      if (newTerm == null) {
        newTerm = String.join(" ", words);
      }
      if (newTerm.equals(oldTerm))
        return null;
      else
        return newTerm;
    } catch (JsonProcessingException e) {
      throw new OpenSearchException("Could not parse OpenSearch response", e);
    }
  }

  private String processSuggestions(JsonNode suggestions, String oldTerm, String newTerm, String[] words) {
    for (JsonNode suggest : suggestions) {
      JsonNode options = suggest.get("options");

      if (options != null && !options.isEmpty()) {
        String original = suggest.get("text").asText();
        JsonNode swapNode = options.get(0).get("text");
        if (original.equals(oldTerm))
          newTerm = swapNode.asText();
        else {
          int wordPos = getWordPos(words, original);
          if (wordPos > -1) {
            words[wordPos] = swapNode.asText();
          }
        }
      }
    }
    return newTerm;
  }

  private int getWordPos(String[] words, String wordToFind) {
    for (int i = 0; i < words.length; i++) {
      if (words[i].equals(wordToFind))
        return i;
    }
    return -1;
  }

  private JsonNode runQuery(SearchSourceBuilder bld) throws OpenSearchException {
    String elastic = bld.toString();
    HttpResponse<String> response = getResponse(elastic);
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return om.readTree(response.body());
    } catch (JsonProcessingException e) {
      throw new OpenSearchException("Query execution failed", e);
    }
  }

  private HttpResponse<String> getResponse(String queryJson) throws OpenSearchException {

    String url = System.getenv("OPENSEARCH_URL");
    if (url == null)
      throw new OpenSearchException("Environmental variable OPENSEARCH_URL is not set");

    String index = System.getenv("OPENSEARCH_INDEX");
    if (System.getenv("OPENSEARCH_AUTH") == null)
      throw new OpenSearchException("Environmental variable OPENSEARCH_AUTH token is not set");

    HttpRequest httpRequest;
    try {
      httpRequest = HttpRequest.newBuilder()
        .uri(new URI(url + index + "/_search"))
        .header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(queryJson))
        .build();
    } catch (URISyntaxException e) {
      throw new OpenSearchException("Invalid OpenSearch URI", e);
    }

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    try {
      response = client
        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
        .thenApply(res -> res)
        .get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new OpenSearchException("OpenSearch call failed", e);
    } catch (Exception e) {
      throw new OpenSearchException("OpenSearch call failed", e);
    }

    if (299 < response.statusCode()) {
      LOG.debug("Open search request failed with code: {}", response.statusCode());
      throw new OpenSearchException("Search request failed. Error =. " + response.body());
    }
    return response;


  }

  private SearchResponse getStandardResults(QueryRequest request) throws OpenSearchException, QueryException {
    SearchResponse searchResults = new SearchResponse();

    try {
      JsonNode root = getIMOSResults(request);

      if (root == null)
        return null;
      if (root.has("entities")) {
        return processIMQueryResponse(root, request);
      }
      try (CachedObjectMapper resultMapper = new CachedObjectMapper()) {
        searchResults.setHighestUsage(0);
        searchResults.setCount(0);
        for (JsonNode hit : root.get("hits").get("hits")) {
          SearchResultSummary source = resultMapper.treeToValue(hit.get("_source"), SearchResultSummary.class);
          searchResults.addEntity(source);
          if (source.getUsageTotal() != null && source.getUsageTotal() > searchResults.getHighestUsage())
            searchResults.setHighestUsage(source.getUsageTotal());
          source.setMatch(source.getName());
          if (source.getPreferredName() != null) {
            source.setName(source.getPreferredName());
            source.setMatch(source.getName());
          }
          source.setTermCode(null);
        }
        Integer totalCount = resultMapper.treeToValue(root.get("hits").get("total").get("value"), Integer.class);
        if (null != totalCount) searchResults.setCount(totalCount);
        if (request.getPage() != null) {
          searchResults.setPage(request.getPage().getPageNumber());
        }
        request.addTiming("Results List built");
        searchResults.setTerm(request.getTextSearch());
        return searchResults;
      }
    } catch (JsonProcessingException e) {
      throw new OpenSearchException("Could not parse OpenSearch response", e);
    }
  }

  private SearchResponse processIMQueryResponse(JsonNode root, QueryRequest request) throws OpenSearchException, JsonProcessingException {
    try (CachedObjectMapper resultMapper = new CachedObjectMapper()) {
      SearchResponse searchResults = new SearchResponse();
      searchResults.setHighestUsage(0);
      searchResults.setCount(0);
      for (JsonNode hit : root.get("entities")) {
        TTEntity entity = resultMapper.treeToValue(hit, TTEntity.class);
        SearchResultSummary summary = entityService.getSummary(entity.getIri());
        searchResults.addEntity(summary);
        if (summary.getUsageTotal() != null && summary.getUsageTotal() > searchResults.getHighestUsage())
          searchResults.setHighestUsage(summary.getUsageTotal());
        summary.setMatch(summary.getName());
        if (summary.getPreferredName() != null) {
          summary.setName(summary.getPreferredName());
          summary.setMatch(summary.getName());
        }
        summary.setTermCode(null);
      }
      Integer totalCount = resultMapper.treeToValue(root.get("totalCount"), Integer.class);
      if (null != totalCount) searchResults.setCount(totalCount);
      if (request.getPage() != null) {
        searchResults.setPage(request.getPage().getPageNumber());
      }
      request.addTiming("Results List built");
      searchResults.setTerm(request.getTextSearch());
      return searchResults;
    }
  }

  private JsonNode getNodeResults(QueryRequest request) {
    try {
      JsonNode root = getIMOSResults(request);
      if (root == null)
        return new ObjectMapper().createObjectNode();
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        if (!root.get("hits").get("hits").isEmpty()) {
          ObjectNode searchResults = om.createObjectNode();
          ArrayNode resultNodes = om.createArrayNode();
          searchResults.set("entities", resultNodes);
          processNodeResults(request, root, om, resultNodes);
          request.addTiming("Results List built");
          return searchResults;
        }
        request.addTiming("no results returned");
      }
      return new ObjectMapper().createObjectNode();
    } catch (Exception e) {
      return new ObjectMapper().createObjectNode();
    }
  }
}
