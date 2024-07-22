package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.transforms.IMQToOS;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

public class OSQuery {
	private static final Logger LOG = LoggerFactory.getLogger(OSQuery.class);
	private final List<Map<Long,String>>timings = new ArrayList<>();
	private IMQToOS converter= new IMQToOS();


	public SearchResponse openSearchQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException, JsonProcessingException, DataFormatException {
			SearchResponse results = getStandardResults(request);
			return results;
		}


	public JsonNode imQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException, JsonProcessingException, DataFormatException {
		JsonNode results=getNodeResults(request);
		return results;
	}


	public JsonNode getIMOSResults(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException, JsonProcessingException, DataFormatException {
		Query query = request.getQuery();
		JsonNode results;
		if (query.isImQuery()) {
			results = new QueryRepository().queryIM(request, false);
		}
		else {
			results = getOsResults(request, query);
		}
		if (query.getQuery() != null){
			for (Query childQuery:query.getQuery()){
				childQuery.setParentResult(results);
				if (childQuery.isImQuery()){
					results= new QueryRepository().queryIM(request,false);
				}
				else {
					results = getOsResults(request, childQuery);
				}
			}
			return results;
		}
		else
			return results;
	}

	private JsonNode getOsResults(QueryRequest request, Query query) throws QueryException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
	SearchSourceBuilder builder= converter.buildQuery(request,query, IMQToOS.QUERY_TYPE.AUTOCOMPLETE);
		if (builder==null)
			return null;

		request.addTiming("Entry point for autocomplete\"" + request.getTextSearch() + "\"");
		JsonNode results = runQuery(builder,request);
		if (results.get("hits").get("hits").size() > 0){
			return results;
		}
		if (request.getTextSearch().contains(" ")) {
			builder = converter.buildQuery(request,query, IMQToOS.QUERY_TYPE.NGRAM,Fuzziness.ZERO);
			results = runQuery(builder, request);
			if (results.get("hits").get("hits").size() > 0) {
				return results;
			}
			else {
				builder = converter.buildQuery(request,query, IMQToOS.QUERY_TYPE.MULTIWORD);
				results = runQuery(builder, request);
				if (results.get("hits").get("hits").size() > 0) {
					return results;
				}
				else {
					builder = converter.buildQuery(request,query, IMQToOS.QUERY_TYPE.NGRAM, Fuzziness.TWO);
					results = runQuery(builder, request);
					if (results.get("hits").get("hits").size() > 0) {
						return results;
					}
				}
			}
		}

		String corrected = spellingCorrection(request);
		if (corrected != null) {
			request.setTextSearch(corrected);
			builder= new IMQToOS().buildQuery(request,query, IMQToOS.QUERY_TYPE.AUTOCOMPLETE);
			return runQuery(builder,request);
		}
		return results;
	}

	private String spellingCorrection(QueryRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
		String oldTerm = request.getTextSearch();
		String newTerm = null;
		String suggestor = "{\n" +
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
		HttpResponse<String> response = getResponse(false,suggestor);
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			JsonNode root = om.readTree(response.body());
			JsonNode suggestions = root.get("suggest").get("spell-check");
			String[] words = oldTerm.split(" ");
			for (JsonNode suggest : suggestions) {
				if (suggest.has("options")) {
					JsonNode options = suggest.get("options");
					if (options.size() > 0) {
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
			}
			if (newTerm == null) {
				newTerm = String.join(" ", words);
			}
			if (newTerm.equals(oldTerm))
				return null;
			else
				return newTerm;
		}
	}

	private int getWordPos(String[] words, String wordToFind) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].equals(wordToFind))
				return i;
		}
		return -1;
	}




	private JsonNode runQuery(SearchSourceBuilder bld, QueryRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
		String elastic= bld.toString();
		HttpResponse<String> response = getResponse(false,elastic );
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			JsonNode root = om.readTree(response.body());
			return root;
			/*
			SearchResponse searchResults= new SearchResponse();
			standardResults(request,root,om,searchResults);
			addTiming("Query run and response received. Ready to produce search results");
			return searchResults;

			 */
		}
	}

	private HttpResponse<String> getResponse(boolean template, String queryJson ) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {

		String url = System.getenv("OPENSEARCH_URL");
		if (url == null)
			throw new OpenSearchException("Environmental variable OPENSEARCH_URL is not set");

		String index= System.getenv("OPENSEARCH_INDEX");
		if (System.getenv("OPENSEARCH_AUTH") == null)
			throw new OpenSearchException("Environmental variable OPENSEARCH_AUTH token is not set");
		HttpRequest httpRequest = HttpRequest.newBuilder()
			.uri(new URI(url + index + "/_search" + (template ? "/template" : "")))
			.header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(queryJson))
			.build();
		addTiming("Query sent...");

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client
			.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
			.thenApply(res -> res)
			.get();

		if (299 < response.statusCode()) {
			LOG.debug("Open search request failed with code: {}", response.statusCode());
			throw new OpenSearchException("Search request failed. Error =. " + response.body());
		}
		return response;


	}

	private SearchResponse getStandardResults(QueryRequest request) throws JsonProcessingException, QueryException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, DataFormatException {
		try {
			JsonNode root = getIMOSResults(request);
			if (root == null)
				return null;
			try (CachedObjectMapper resultMapper = new CachedObjectMapper()) {
				SearchResponse searchResults = new SearchResponse();
				int resultNumber = 0;
				searchResults.setHighestUsage(0);
				searchResults.setCount(0);
				for (JsonNode hit : root.get("hits").get("hits")) {
					resultNumber++;
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
		}catch (Exception e) {
			return new SearchResponse();
		}
	}

	private JsonNode getNodeResults(QueryRequest request) throws JsonProcessingException, QueryException, DataFormatException, OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
		try {
			JsonNode root = getIMOSResults(request);
			if (root == null)
				return new ObjectMapper().createObjectNode();
			try (CachedObjectMapper om = new CachedObjectMapper()) {
				if (root.get("hits").get("hits").size() > 0) {
					ObjectNode searchResults = om.createObjectNode();
					ArrayNode resultNodes = om.createArrayNode();
					searchResults.set("entities", resultNodes);
					for (JsonNode hit : root.get("hits").get("hits")) {
						ObjectNode resultNode = om.createObjectNode();
						resultNodes.add(resultNode);
						ObjectNode osResult = om.treeToValue(hit.get("_source"), ObjectNode.class);
						resultNode.set("@id", osResult.get("iri"));
						resultNode.set(RDFS.LABEL, osResult.get("name"));
						if (request.getQuery().getReturn() != null) {
							for (Return select : request.getQuery().getReturn()) {
								if (select.getProperty() != null) {
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
							}
						}
					}
					request.addTiming("Results List built");
					return searchResults;
				}
				request.addTiming("no results returned");
			}
			return null;
		}
		catch (Exception e) {
			return new ObjectMapper().createObjectNode();
		}
	}






	public void addTiming(String position){
		long now = new Date().getTime();
		Map<Long,String> timingMap= new HashMap<>();
		timingMap.put(now,position);
		timings.add(timingMap);
	}





}
