package org.endeavourhealth.imapi.logic.service;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.endeavourhealth.imapi.dataaccess.OpenSearchRepository;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.query.Query;
import org.endeavourhealth.imapi.model.query.Selection;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.transforms.IMQToSparql;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */
public class SearchService {
	private static final Logger LOG = LoggerFactory.getLogger(org.endeavourhealth.imapi.logic.service.SearchService.class);
	private String searchTerm;
	private final OpenSearchRepository repo = new OpenSearchRepository();


	/**
	 * Queries any IM entity using the query model
	 * @param query Query object
	 * @return a generic JSONArray containing the results
	 * @throws DataFormatException
	 */
	public JSONArray queryIM(Query query) throws DataFormatException {
		IMQToSparql converter= new IMQToSparql();
		String spq= converter.convert(query);
		JSONArray result= new JSONArray();
		Map<Value,JSONObject> entityMap= new HashMap<>();
		try (RepositoryConnection repo= ConnectionManager.getIMConnection()) {
			TupleQuery qry = repo.prepareTupleQuery(spq);
			try (TupleQueryResult rs = qry.evaluate()){
				while (rs.hasNext()){
					BindingSet bs= rs.next();
					bindResults(converter,query.getSelect(),bs,result,entityMap);
				}
			}
		}
		return result;
	}

	private void bindResults(IMQToSparql converter,List<Selection> select, BindingSet bs,
													 JSONArray result,Map<Value,JSONObject> entityMap) {
		JSONObject root;
		Value entityIri= bs.getValue("entity");
		if (entityMap.get(entityIri)==null) {
			root = new JSONObject();
			entityMap.put(entityIri, root);
			result.add(root);
		}
		root= entityMap.get(entityIri);
		for (Selection selection:select){
			String property=selection.getProperty().getIri();
			String var= converter.getPropertyMap().get(property);
			if (IMQToSparql.isId(property))
				root.put("@id",entityIri.stringValue());
			else {
				if (selection.getAlias() != null)
					var = converter.getAliasMap().get(var);
				Value value = bs.getValue(var);
				if (value!=null) {
					if (value.isLiteral())
						root.put(property, value.stringValue());
				}
			}
			if (selection.getSelect()!=null){
				root.putIfAbsent(property, new JSONArray());
				JSONObject subNode= new JSONObject();
				for (Selection subSelect:selection.getSelect()) {
					bindSubResults(converter, subSelect,property + "/",subNode,entityMap,bs);
				}
				if (!subNode.isEmpty())
					((JSONArray)root.get(property)).add(subNode);
			}
		}
	}

	private void bindSubResults(IMQToSparql converter,Selection selection,String path,JSONObject node,
															Map<Value,JSONObject> entityMap,BindingSet bs){
		String property=selection.getProperty().getIri();
		String var= converter.getPropertyMap().get(path+property);
			if (selection.getAlias() != null)
				var = converter.getAliasMap().get(var);
			Value value = bs.getValue(var);
			if (value!=null) {
				if (value.isLiteral()) {
					node.put(property, value.stringValue());
				}
			}
			if (selection.getSelect()!=null){
				node.putIfAbsent(property, new JSONArray());
				JSONObject subNode= new JSONObject();
				for (Selection subSelect:selection.getSelect()) {
					bindSubResults(converter, subSelect,property + "/",subNode,entityMap,bs);
				}
				if (!subNode.isEmpty())
					((JSONArray)node.get(property)).add(subNode);
			}
	}


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

	private QueryBuilder buildSimpleTermCodeMatch(SearchRequest request){
		MatchPhraseQueryBuilder mpq= new MatchPhraseQueryBuilder("termCode.term", request.getTermFilter()).boost(1.5F);
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", request.getTermFilter()).boost(0.5F);
		BoolQueryBuilder outer = new BoolQueryBuilder();
		outer.should(mpq);
		outer.should(mfs);
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
		MatchPhraseQueryBuilder mpq= new MatchPhraseQueryBuilder("termCode.term", request.getTermFilter()).boost(1.5F);
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", request.getTermFilter()).boost(0.5F);
		boolQuery.should(mpq);
		boolQuery.should(mfs);
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


