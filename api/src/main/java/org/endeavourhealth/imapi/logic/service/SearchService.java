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
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.sets.DataSet;
import org.endeavourhealth.imapi.model.sets.Select;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.transforms.IMToSparql;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {

	private static final Logger LOG = LoggerFactory.getLogger(org.endeavourhealth.imapi.logic.service.SearchService.class);
	private final OpenSearchRepository repo = new OpenSearchRepository();



	/**
	 * Queries any IM entity using the query model
	 * @param query data model entity object to populate
	 * @return a generic JSONArray containing the results
	 * @throws DataFormatException if query format is invalid
	 */
	public JSONArray queryIM(DataSet query) throws DataFormatException {
		IMToSparql converter= new IMToSparql();
		String spq= converter.convert(query);
		JSONArray result= new JSONArray();
		Map<Value, JSONObject> entityMap= new HashMap<>();
		JSONObject root= new JSONObject();
		result.add(root);
		try (RepositoryConnection repo= ConnectionManager.getIMConnection()) {
			TupleQuery qry = repo.prepareTupleQuery(spq);
			try (TupleQueryResult rs = qry.evaluate()){
				while (rs.hasNext()){
					BindingSet bs= rs.next();
					bindResults(converter,"entity",query,bs,result,root,entityMap);

				}
			}
		}
		return result;
	}

	private void bindResults(IMToSparql converter, String subject, DataSet dataSet, BindingSet bs,
													 List<JSONObject> result, JSONObject root, Map<Value, JSONObject> entityMap) {

		Value entityIri= bs.getValue(subject);
		if (entityMap.get(entityIri)==null) {
			entityMap.put(entityIri, root);
			result.add(root);
		}
		root= entityMap.get(entityIri);
		if (dataSet.getSelect()!=null) {
			for (Select selection : dataSet.getSelect()) {
				String var = selection.getVar();
				String alias= selection.getAlias();
				if (alias!=null){
					var= alias;
				}
				if (converter.isId(var))
					root.put("@id",entityIri.stringValue());
				else {
					if (selection.getObject() != null) {
						root.putIfAbsent(var, new ArrayList<>());
						JSONObject subNode = new JSONObject();
						bindResults(converter, var, selection.getObject(), bs, result, subNode, entityMap);
						if (!subNode.isEmpty()) {
							((ArrayList) root.get(var)).add(subNode);
						}
					}
					else {
						Value bound = bs.getValue(var);
						if (bound!=null) {
							if (bound.isLiteral())
								root.put(var, bound.stringValue());
						}
					}
				}
			}
		}
	}

	/**
	 * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
	 * @param request holding the search term (multi or single word) + type/status/scheme filters
	 * @return A set of Summaries of entity documents from the store
	 *
	 */
	public List<SearchResultSummary> getEntitiesByTerm(SearchRequest request) throws URISyntaxException,
		IOException, InterruptedException, ExecutionException, OpenSearchException {
		if (request == null || request.getTermFilter() == null || request.getTermFilter().isEmpty())
			return Collections.emptyList();
		if (request.getIndex()==null)
			request.setIndex("concept");
		String term= request.getTermFilter();

		QueryBuilder qry;

		if (request.getTermFilter().length() < 3) {
			qry = buildCodeKeyQuery(request);
			return repo.getEntities(qry, request);
		} else if (!term.contains(" ")) {
			if (term.contains(":")){
				String namespace= EntityCache.getDefaultPrefixes().getNamespace(term.substring(0,term.indexOf(":")));
				if (namespace!=null)
					request.setTermFilter(namespace+ term.split(":")[1]);
			}
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
		return new FunctionScoreQueryBuilder(boolBuilder,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
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
		return new FunctionScoreQueryBuilder(outer,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));
	}

	private QueryBuilder buildSimpleTermMatch(SearchRequest request) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		MatchPhraseQueryBuilder mpq= new MatchPhraseQueryBuilder("termCode.term", request.getTermFilter()).boost(1.5F);
		MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", request.getTermFilter()).boost(0.5F);
		boolQuery.should(mpq);
		boolQuery.should(mfs);
		addFilters(boolQuery, request);
		return new FunctionScoreQueryBuilder(boolQuery,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));

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
		return new FunctionScoreQueryBuilder(qry,
			ScoreFunctionBuilders.fieldValueFactorFunction("weighting").factor(0.5F).missing(1F));

	}
}


