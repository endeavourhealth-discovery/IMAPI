package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

/**
 * Class to create and runan open search text query
 */
public class OSQuery {
	private static final Logger LOG = LoggerFactory.getLogger(OSQuery.class);
	private final Set<String> resultCache= new HashSet<>();



	public Set<String> getResultCache() {
		return resultCache;
	}



	public OSQuery addResult(String item){
		resultCache.add(item);
		return this;
	}



	public List<SearchResultSummary> codeIriQuery(SearchRequest request) throws DataFormatException {
		QueryBuilder qry= buildCodeIriQuery(request);
		return wrapandRun(qry,request);

	}

	public List<SearchResultSummary> termQuery(SearchRequest request) throws DataFormatException {
		QueryBuilder qry= buildTermQuery(request);

		return wrapandRun(qry,request);
	}

	public List<SearchResultSummary> iriTermQuery(SearchRequest request) throws DataFormatException {
		QueryBuilder qry= buildIriTermQuery(request);

		return wrapandRun(qry,request);
	}

	public List<SearchResultSummary> multiWordQuery(SearchRequest request) throws DataFormatException {
		QueryBuilder qry= buildMultiWordQuery(request);
		return wrapandRun(qry,request);

	}

	/**
	 * A rapid response page oriented query that bundles together code/iri key prefix term and the multi-word sequentially
	 * <p> Each time a result is found, if more than one page it may be returned leaving the calling method to determine whether to add more results.
	 * <p>Paging is supported</p>
	 * subsequent calls.
	 * @param request Search request object
	 * @return search request object
	 * @throws DataFormatException if problem with data format of query
	 */
	public List<SearchResultSummary> multiPhaseQuery(SearchRequest request) throws DataFormatException {

		String term = request.getTermFilter();
		int page = request.getPage();
		int size = request.getSize();

		request.setFrom(size * (page - 1));

		List<SearchResultSummary> results ;

		request.setPage(1);
	  if (request.getTermFilter().length() < 3)
			return codeIriQuery(request);

		if (page == 1 &&(!term.contains(" "))) {
			if (term.contains(":")) {
				String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
				if (namespace != null)
					request.setTermFilter(namespace + term.split(":")[1]);
			}
			results= iriTermQuery(request);
			if (!results.isEmpty())
				return results;
		}

		results= termQuery(request);
		if (!results.isEmpty()) {
				return results;
			}
		return multiWordQuery(request);

	}



	private QueryBuilder buildCodeIriQuery(SearchRequest request) {
		BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
		String term= request.getTermFilter();
		if (term.contains(":")){
			String namespace= EntityCache.getDefaultPrefixes().getNamespace(term.substring(0,term.indexOf(":")));
			if (namespace!=null)
				request.setTermFilter(namespace+ term.split(":")[1]);
		}

		TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
		tqb.boost(2F);
		boolBuilder.should(tqb);
		TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
		tqiri.boost(2F);
		boolBuilder.should(tqiri);
		tqb = new TermQueryBuilder("key", request.getTermFilter().toLowerCase());
		boolBuilder.should(tqb).minimumShouldMatch(1);
		addFilters(boolBuilder, request);
		return boolBuilder;
	}




	private void addFilters(BoolQueryBuilder qry, SearchRequest request) {
		if (!request.getSchemeFilter().isEmpty()) {
			List<String> schemes = new ArrayList<>(request.getSchemeFilter());
			TermsQueryBuilder tqr = new TermsQueryBuilder("scheme.@id", schemes);
			qry.filter(tqr);
		}
		if (!request.getStatusFilter().isEmpty()) {
			List<String> statuses = new ArrayList<>(request.getStatusFilter());
			TermsQueryBuilder tqr = new TermsQueryBuilder("status.@id", statuses);
			qry.filter(tqr);
		}
		if (!request.getTypeFilter().isEmpty()) {
			List<String> types = new ArrayList<>(request.getTypeFilter());
			TermsQueryBuilder tqr = new TermsQueryBuilder("entityType.@id", types);
			qry.filter(tqr);
		}
		if (!request.getIsA().isEmpty()) {
			List<String> isas = new ArrayList<>(request.getIsA());
			TermsQueryBuilder tqr = new TermsQueryBuilder("isA.@id", isas);
			qry.filter(tqr);
		}
	}



	private QueryBuilder buildTermQuery(SearchRequest request) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		MatchPhraseQueryBuilder mpq= new MatchPhraseQueryBuilder("termCode.term", request.getTermFilter()).boost(1.5F);
		boolQuery.should(mpq);
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term",
			request.getTermFilter()).boost(0.5F);
		boolQuery.should(mfs).minimumShouldMatch(1);
		addFilters(boolQuery, request);
		return boolQuery;
		/*
			return new FunctionScoreQueryBuilder(boolQuery,
				ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		else
			return boolQuery;

		 */
	}

	private QueryBuilder buildIriTermQuery(SearchRequest request) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
		tqb.boost(4F);
		boolQuery.should(tqb);
		TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
		tqiri.boost(4F);
		boolQuery.should(tqiri);
		PrefixQueryBuilder pqb = new PrefixQueryBuilder("key", request.getTermFilter());
		pqb.boost(2F);
		boolQuery.should(pqb);
		MatchPhraseQueryBuilder mpq= new MatchPhraseQueryBuilder("termCode.term", request.getTermFilter()).boost(1.5F);
		boolQuery.should(mpq);
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term",
			request.getTermFilter()).boost(0.5F);
		boolQuery.should(mfs).minimumShouldMatch(1);
		addFilters(boolQuery, request);
		return boolQuery;
		/*
		if (request.getSortBy()!= SortBy.length) {
			return new FunctionScoreQueryBuilder(boolQuery,
				ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		}
		else
			return boolQuery;

		 */

	}

	private QueryBuilder buildMultiWordQuery(SearchRequest request) {
		BoolQueryBuilder qry = new BoolQueryBuilder();
		MatchPhrasePrefixQueryBuilder pqry = new MatchPhrasePrefixQueryBuilder("termCode.term", request.getTermFilter());
		qry.should(pqry);
		BoolQueryBuilder wqry= new BoolQueryBuilder();
		qry.should(wqry);
		int wordPos = 0;
		for (String term : request.getTermFilter().split(" ")) {
			wordPos++;
			MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", term);
			mfs.boost(wordPos == 1 ? 4 : 1);
			wqry.must(mfs);
		}
		qry.minimumShouldMatch(1);
		addFilters(qry, request);
		return qry;
		/*
			return new FunctionScoreQueryBuilder(qry,
				ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		*/

	}

	private List<SearchResultSummary> wrapandRun(QueryBuilder qry, SearchRequest request) throws DataFormatException {
	if (request.getIndex()==null)
			request.setIndex("concept");

		int size = request.getSize();
		int from = request.getFrom();

		SearchSourceBuilder bld = new SearchSourceBuilder()
			.size(size)
			.from(from)
			.query(qry);


		if (!request.getSelect().isEmpty()) {
			int extraFields=0;
			if (!request.getSelect().contains("termCode"))
				extraFields++;
			if (!request.getSelect().contains("name"))
				extraFields++;
			String[] fields= new String[request.getSelect().size()+extraFields];
			request.getSelect().toArray(fields);
			if (extraFields>0)
				fields[fields.length-extraFields]="name";
			if (extraFields==2)
				fields[fields.length-1]= "termCode";
			bld.fetchSource(fields,null);

		}
		else {
			String[] fields= {"iri","name","code","termCode","entityType","status","scheme"};
			bld.fetchSource(fields, null);
		}




		try {
			return runQuery(request,bld);
		} catch (Exception e)
		{
			throw new DataFormatException("failure to run open search query due to :"+ e.getMessage());
		}
	}
	public List<SearchResultSummary> runQuery(SearchRequest request,SearchSourceBuilder bld) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
		String queryJson= bld.toString();

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
			.POST(HttpRequest.BodyPublishers.ofString(queryJson))
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
		return standardResponse(request,root, resultMapper,searchResults);

	}

	private List<SearchResultSummary> standardResponse(SearchRequest request,JsonNode root, ObjectMapper resultMapper,List<SearchResultSummary> searchResults) throws JsonProcessingException {
		int resultNumber=0;
		for (JsonNode hit : root.get("hits").get("hits")) {
			resultNumber++;
			SearchResultSummary source = resultMapper.treeToValue(hit.get("_source"), SearchResultSummary.class);
				searchResults.add(source);
				source.setMatch(source.getName());
				if (resultNumber < 6) {
					fetchMatchTerm(source, request.getTermFilter());
				}
			source.setTermCode(null);
		}
		if (!searchResults.isEmpty())
		  sort(searchResults,request.getTermFilter());
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
			searchTerm= searchTerm.toLowerCase();
		if (searchResult.getTermCode()!=null) {
			for (SearchTermCode tc : searchResult.getTermCode()) {
				TTIriRef status = tc.getStatus();
				if ((status!=null) &&(!(status.equals(IM.INACTIVE)))) {
					if (tc.getTerm() != null) {
						if (tc.getTerm().toLowerCase().startsWith(searchTerm)) {
							searchResult.setMatch(tc.getTerm());
							break;
						}
					}
				}
			}
		}
	}

	private void sort(List<SearchResultSummary> initialList,String term){
		term=term.toLowerCase(Locale.ROOT);
		String finalTerm = term;
		initialList.sort(Comparator.comparing((SearchResultSummary sr) -> !sr.getMatch().toLowerCase(Locale.ROOT).startsWith(finalTerm))
			.thenComparing(sr -> sr.getMatch().length()));

	}



}
