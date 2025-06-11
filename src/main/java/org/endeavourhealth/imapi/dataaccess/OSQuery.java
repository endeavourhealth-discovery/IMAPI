package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class OSQuery {
  private final IMQToOS converter = new IMQToOS();
  private boolean ignoreInvalid;
  private EntityService entityService = new EntityService();

  private static void processNodeResults(QueryRequest request, JsonNode root, CachedObjectMapper om, ArrayNode resultNodes) throws JsonProcessingException {
    for (JsonNode hit : root.get("hits").get("hits")) {
      ObjectNode resultNode = om.createObjectNode();
      resultNodes.add(resultNode);
      ObjectNode osResult = om.treeToValue(hit.get("_source"), ObjectNode.class);

      resultNode.set("iri", osResult.get("iri"));
      resultNode.set(RDFS.LABEL, osResult.get("name"));
      processBestMatch(om,hit, resultNode);
      processNodeResultReturn(request, osResult, resultNode);
    }
  }

  private static void processBestMatch(CachedObjectMapper om,JsonNode hit,ObjectNode resultNode){
    JsonNode innerHits= hit.get("inner_hits");
    if (innerHits != null) {
      if (innerHits.get("termCode") != null &&!innerHits.get("termCode").get("hits").get("hits").isEmpty()) {
        String name=resultNode.get(RDFS.LABEL).asText();
        JsonNode bestHit =innerHits.get("termCode").get("hits").get("hits").get(0).get("_source").get("term");
        String bestTerm=bestHit.asText();
        if (!bestTerm.endsWith(")")&& name.endsWith(")") && name.contains("(")){
          bestTerm=bestTerm+" "+name.substring(name.lastIndexOf("("));
        }
        resultNode.put(IM.BEST_MATCH, bestTerm);
      }
    }
  }

  private static void processNodeResultReturn(QueryRequest request, ObjectNode osResult, ObjectNode resultNode) {
    if (null == request.getQuery().getReturn()){
      processSourceReturns(osResult, resultNode);
      return;
    }
    Return select = request.getQuery().getReturn();
    processNodeResultReturnProperty(osResult, resultNode, select);
  }

  private static void processSourceReturns(ObjectNode osResult, ObjectNode resultNode) {
    Set<String> sources = new HashSet<>(List.of(RDFS.LABEL, "iri", IM.PREFERRED_NAME, IM.CODE, IM.USAGE_TOTAL, RDF.TYPE, IM.HAS_SCHEME, IM.HAS_STATUS));
    for (String field : sources) {
      String osField = field.substring(field.lastIndexOf("#") + 1);
      if (osResult.get(osField) != null) {
        resultNode.set(field, osResult.get(osField));
      }
    }
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

  private JsonNode getOSResults(QueryRequest request) throws OpenSearchException, QueryException {
    Query query= request.getQuery();
    if (request.getTextSearchStyle()==null) request.setTextSearchStyle(TextSearchStyle.autocomplete);
    SearchSourceBuilder builder;
    if (request.getTextSearchStyle()==TextSearchStyle.autocomplete){
      builder = converter.buildQuery(request, query,TextSearchStyle.autocomplete);
      if (builder == null)
        return null;
      request.addTiming("Entry point for initial search\"" + request.getTextSearch() + "\"");
      JsonNode results = runQuery(builder);
      if (!results.get("hits").get("hits").isEmpty()) {
        return results;
      }
    }
    builder = converter.buildQuery(request, query, TextSearchStyle.ngram, Fuzziness.ZERO);
    if (builder == null) return null;
    JsonNode results = runQuery(builder);
    if (!results.get("hits").get("hits").isEmpty()) {
      return results;
    } else {
      builder = converter.buildQuery(request, query, TextSearchStyle.multiword);
      results = runQuery(builder);
      if (!results.get("hits").get("hits").isEmpty()) {
        return results;
      } else {
        builder = converter.buildQuery(request, query, TextSearchStyle.ngram, Fuzziness.TWO);
        results = runQuery(builder);
        if (!results.get("hits").get("hits").isEmpty()) {
          return results;
        }
      }
    }

    String corrected = spellingCorrection(request);
    if (corrected != null) {
      request.setTextSearch(corrected);
      builder = new IMQToOS().buildQuery(request, query, TextSearchStyle.autocomplete, Fuzziness.ZERO);
      if (builder==null) return null;
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
      log.debug("Open search request failed with code: {}", response.statusCode());
      throw new OpenSearchException("Search request failed. Error =. " + response.body());
    }
    return response;


  }

  private SearchResponse getStandardResults(QueryRequest request) throws OpenSearchException, QueryException {
    try {
      JsonNode root = IMOSQuery(request);
      if (root == null)
        return null;
      if (root.has("entities")) {
        return processIMQueryResponse(root, request);
      }
      else return new SearchResponse();
    } catch (JsonProcessingException e) {
      throw new OpenSearchException("Could not parse OpenSearch response", e);
    }


  }

  private SearchResultSummary getSummary(TTEntity entity){
    SearchResultSummary summary= new SearchResultSummary();
    summary.setIri(entity.getIri());
    summary.setName(entity.getName());
    summary.setPreferredName(entity.getPreferredName());
    summary.setType(entity.getTypes());
    summary.setCode(entity.getCode());
    summary.setStatus(entity.getStatus());
    summary.setScheme(entity.getScheme());
    summary.setUsageTotal(entity.getUsageTotal());
    summary.setBestMatch(entity.getBestMatch());
    if (summary.getPreferredName() != null) {
      summary.setName(summary.getPreferredName());
    }
    return summary;
  }

  private SearchResponse processIMQueryResponse(JsonNode root, QueryRequest request) throws OpenSearchException, JsonProcessingException {
    try (CachedObjectMapper resultMapper = new CachedObjectMapper()) {
      SearchResponse searchResults = new SearchResponse();
      searchResults.setHighestUsage(0);
      searchResults.setCount(0);
      for (JsonNode hit : root.get("entities")) {
        TTEntity entity = resultMapper.treeToValue(hit, TTEntity.class);
        SearchResultSummary summary =getSummary(entity);
        searchResults.addEntity(summary);
        if (summary.getUsageTotal() != null && summary.getUsageTotal() > searchResults.getHighestUsage())
          searchResults.setHighestUsage(summary.getUsageTotal());
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
  public JsonNode IMOSQuery(QueryRequest request) throws OpenSearchException {
    try {
      JsonNode root = getOSResults(request);
      if (root == null)
        return null;
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        if (!root.get("hits").get("hits").isEmpty()) {
          ObjectNode searchResults = om.createObjectNode();
          searchResults.set("totalCount", root.get("hits").get("total").get("value"));
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
      throw new OpenSearchException(e.getMessage(), e);
    }
  }

}
