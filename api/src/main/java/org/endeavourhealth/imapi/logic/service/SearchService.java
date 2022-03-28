package org.endeavourhealth.imapi.logic.service;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.endeavourhealth.imapi.dataaccess.OpenSearchRepository;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * Methods for searching open search / elastic repositories
 */
public class SearchService {
	private static final Logger LOG = LoggerFactory.getLogger(org.endeavourhealth.imapi.logic.service.SearchService.class);
	private String searchTerm;
	private final OpenSearchRepository repo = new OpenSearchRepository();


	/**
	 * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
	 * @param request holding the search term (multi or single word) + type/status/scheme filters
	 * @return A set of Summaries of entity documents from the store
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws OpenSearchException
	 */
	public List<SearchResultSummary> getEntitiesByTerm(SearchRequest request) throws URISyntaxException, IOException, InterruptedException, ExecutionException, OpenSearchException {
		if (request == null || request.getTermFilter() == null || request.getTermFilter().isEmpty())
			return Collections.emptyList();
		if (request.getIndex()==null)
			request.setIndex("concept");
		searchTerm = request.getTermFilter();

		QueryBuilder qry;
		HttpResponse<String> response;

		if (request.getTermFilter().length() < 3) {
			qry = buildCodeKeyQuery(request);
			return repo.getEntities(qry, request);
		} else if (!request.getTermFilter().contains(" ")) {
			qry = buildSimpleTermCodeMatch(request);
			return repo.getEntities(qry, request);
		} else {
			qry = buildSimpleTermMatch(request);
			List<SearchResultSummary> results = repo.getEntities(qry, request);
			if (!results.isEmpty()) {
				return results;
			} else {
				qry = buildMultiWordMatch(request);
				return repo.getEntities(qry, request);
			}
		}
	}

	private QueryBuilder buildCodeKeyQuery(SearchRequest request) {
		BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
		TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
		tqb.boost(2F);
		boolBuilder.should(tqb);

		boolBuilder.should(tqb);
		tqb = new TermQueryBuilder("key", request.getTermFilter().toLowerCase());
		boolBuilder.should(tqb).minimumShouldMatch(1);
		addFilters(boolBuilder, request);
		FunctionScoreQueryBuilder funcScoreQuery = new FunctionScoreQueryBuilder(boolBuilder,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		return funcScoreQuery;
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
	}

	private QueryBuilder buildSimpleTermCodeMatch(SearchRequest request) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", request.getTermFilter());
		boolQuery.must(mfs);
		BoolQueryBuilder outer = new BoolQueryBuilder();
		outer.should(boolQuery);
		TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
		TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
		tqb.boost(2F);
		tqiri.boost(2F);
		outer.should(tqb).should(tqiri).minimumShouldMatch(1);
		addFilters(outer, request);
		FunctionScoreQueryBuilder funcScoreQuery = new FunctionScoreQueryBuilder(outer,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		return funcScoreQuery;
	}

	private QueryBuilder buildSimpleTermMatch(SearchRequest request) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", request.getTermFilter());
		boolQuery.must(mfs);
		addFilters(boolQuery, request);
		FunctionScoreQueryBuilder funcScoreQuery = new FunctionScoreQueryBuilder(boolQuery,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		return funcScoreQuery;

	}

	private QueryBuilder buildMultiWordMatch(SearchRequest request) {
		BoolQueryBuilder qry = new BoolQueryBuilder();
		int wordPos = 0;
		for (String term : request.getTermFilter().split(" ")) {
			wordPos++;
			MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", term);
			mfs.boost(wordPos == 1 ? 4 : 1);
			qry.must(mfs);
		}
		addFilters(qry, request);
		FunctionScoreQueryBuilder funcScoreQuery = new FunctionScoreQueryBuilder(qry,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
		return funcScoreQuery;

	}
}


