package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sets.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {

	private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);
	private final TTIriRef PROPNAME= TTIriRef.iri(IM.NAMESPACE+"propName");
	private final TTIriRef OBNAME= TTIriRef.iri(IM.NAMESPACE+"obName");

	/**
	 * Queries any IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @return a generic JSONDocument containing the results in a format defined by the selecr staement and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public ObjectNode queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
		return new QueryRepository().queryIM(queryRequest);
	}


	/**
	 * Queries for a standard entity summary any IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @return a SearchResultSummary array containing the results in a format defined by the selecr staement and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public List<SearchResultSummary> summmaryEntityQuery(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
		return new QueryRepository().entitySummaryQuery(queryRequest);
	}


	/**
	 * Queries for a standard entity summary any IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @return a SearchResultSummary array containing the results in a format defined by the selecr staement and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public List<TTEntity> entityQuery(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
		return new QueryRepository().entityQuery(queryRequest);
	}


	/**
	 * Validation true or false query of the IM
	 * @param iri iri of the query
	 * @param variables a map of parameter value pairs  '$this' for the value being validated
	 * @return a boolean true or false
	 * @throws DataFormatException
	 * @throws JsonProcessingException
	 */
	public boolean booleanQueryIM(String iri,Map<String,String> variables) throws DataFormatException, JsonProcessingException {
		return new QueryRepository().booleanQueryIM(iri,variables);
	}



	/**
	 * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
	 * @param request holding the search term (multi or single word) + type/status/scheme filters
	 * @return A set of Summaries of entity documents from the store
	 *
	 */
	public List<SearchResultSummary> getEntitiesByTerm(SearchRequest request) throws DataFormatException {
		return new OSQuery().multiPhaseQuery(request);
	}
}


