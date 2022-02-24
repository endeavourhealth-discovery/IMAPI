package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * Methods for searching open search / elastic repositories
 */
public class LuceneRepository {
	private static final Logger LOG = LoggerFactory.getLogger(LuceneRepository.class);


	/**
	 * Runs q lucene query on open search or other elastic compatible repository
	 * @param qry  - Elastice query builder
	 * @param request the request object as submitted via the API
	 * @return a list of search results
	 * @throws OpenSearchException
	 * @throws URISyntaxException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws JsonProcessingException
	 */
	public List<SearchResultSummary> getEntities(QueryBuilder qry, SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
		SearchSourceBuilder bld= new SearchSourceBuilder()
		//.fetchSource(fetch,null)
		.size(request.getSize())
		.query(qry);

		String query= bld.toString();

		String url= System.getenv("OPENSEARCH_URL");
		if (request.getIndex()!=null){
			if (url.contains("_search")){
				url=url.substring(0,url.substring(0,url.lastIndexOf("/")).lastIndexOf("/"));
				url=url+"/"+ request.getIndex();
			}
		}


		//String query= "{\"query\": "+ qry.toString()+"}";
		HttpRequest httpRequest = HttpRequest.newBuilder()
			.uri(new URI(url+"/_search"))
//				.timeout(Duration.of(10, ChronoUnit.SECONDS))
			.header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(query))
			.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client
			.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
			.thenApply(res -> res)
			.get();

		if (299 < response.statusCode()) {
			LOG.debug("Open search request failed with code: " + response.statusCode());
			throw new OpenSearchException("Search request failed. Error connecting to opensearch.");
		}

		ObjectMapper resultMapper = new ObjectMapper();
		JsonNode root = resultMapper.readTree(response.body());
		List<SearchResultSummary> searchResults = new ArrayList<>();
		for (JsonNode hit : root.get("hits").get("hits")) {
			SearchResultSummary source = resultMapper.treeToValue(hit.get("_source"), SearchResultSummary.class);
			searchResults.add(source);
		}
		//Finds a match for the first few
		for (int i=0; i<6; i++){
			if(!(i<searchResults.size()))
				break;
			SearchResultSummary res= searchResults.get(i);
			if (res.getCode()!=null)
				if (request.getTermFilter().equals(res.getCode()))
					break;
			fetchMatchTerm(searchResults.get(i),request.getTermFilter());
		}

		return searchResults;

	}


	private void fetchMatchTerm(SearchResultSummary searchResult, String searchTerm) {
		if (searchResult.getName()==null)
			return;
		if (searchResult.getName().toLowerCase().startsWith(searchTerm.toLowerCase()))
			return;
		if (searchResult.getCode()!=null)
			if (searchResult.getCode().equals(searchTerm))
				return;

		String[] searchWords=searchTerm.toLowerCase().split(" ");

		if (searchResult.getTermCode()!=null) {
			for (SearchTermCode tc : searchResult.getTermCode()) {
				TTIriRef status= tc.getStatus();
				if (status==null || (!status.equals(IM.INACTIVE))){
					boolean hit = true;
					if (tc.getTerm() != null) {
						String[] synonyms = tc.getTerm().toLowerCase(Locale.ROOT).split(" ");
						for (int i = 0; i < searchWords.length; i++) {
							if (i < synonyms.length) {
								if (!synonyms[i].startsWith(searchWords[i])) {
									hit = false;
									break;
								}
							}
							else
								hit=false;
						}
					}
					if (hit) {
						searchResult.setMatch(tc.getTerm());
						break;
					}
				}
			}
			//Set to null
			searchResult.setTermCode(null);
		}
	}


}
