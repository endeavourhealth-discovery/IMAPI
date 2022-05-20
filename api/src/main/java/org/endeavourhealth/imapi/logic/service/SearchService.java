package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sets.DataSet;
import org.endeavourhealth.imapi.model.sets.ResultNode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {

	private static final Logger LOG = LoggerFactory.getLogger(org.endeavourhealth.imapi.logic.service.SearchService.class);
	private final TTIriRef PROPNAME= TTIriRef.iri(IM.NAMESPACE+"propName");
	private final TTIriRef OBNAME= TTIriRef.iri(IM.NAMESPACE+"obName");






	/**
	 * Queries any IM entity using the query model
	 * @param query data model entity object to populate
	 * @return a generic JSONDocument containing the results and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public ResultNode queryIM(DataSet query) throws DataFormatException{
		return new IMQuery().queryIM(query);

	}




	/**
	 * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
	 * @param request holding the search term (multi or single word) + type/status/scheme filters
	 * @return A set of Summaries of entity documents from the store
	 *
	 */
	public List<SearchResultSummary> getEntitiesByTerm(SearchRequest request) throws URISyntaxException,
		IOException, InterruptedException, ExecutionException, OpenSearchException, DataFormatException {
		List<SearchResultSummary> result= new OSQuery().multiPhaseQuery(request);
		return result;


	}


}


