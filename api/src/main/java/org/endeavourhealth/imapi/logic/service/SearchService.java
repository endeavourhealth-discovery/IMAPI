package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.iml.Select;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;

import java.util.List;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {


	/**
	 * Queries any IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @return a generic JSONDocument containing the results in a format defined by the selecr staement and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public TTDocument queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
		validateQueryRequest(queryRequest);
		return new QueryRepository().queryIM(queryRequest);
	}
	public void validateQueryRequest(QueryRequest queryRequest) throws DataFormatException {
			if (queryRequest.getQuery()==null)
				throw new DataFormatException("Query request must have a queryIri or Query");
			if (queryRequest.getQuery().getIri()==null)
				validateQuery(queryRequest.getQuery());
			else if (queryRequest.getQuery().getSelect()!=null)
				validateQuery(queryRequest.getQuery());
	}


	private void validateQuery(Query query) throws DataFormatException {
		if (query.getWith()==null)
			if (query.getWhere() == null)
					if (query.getSelect()==null)
					  throw new DataFormatException("Query must have a with from , select or  where clause");

		if (query.getWhere()!=null)
			validateWhere(query.getWhere());
		else 	if (query.getSelect() == null) {
			throw new DataFormatException("Query must have a where or a select");
		}
		if (query.getSelect()!=null){
			for (Select select:query.getSelect()){
				validateSelect(select);
			}

		}
	}
	private void validateWhere(Where match) throws DataFormatException {
		if (match.getOr()!=null){
			for (Where or:match.getOr())
				validateWhere(or);
		}
		else if (match.getAnd()!=null){
			for (Where and:match.getAnd())
				validateWhere(and);
		}
		if (match.getProperty()==null)
			if (match.getWhere()==null)
				throw new DataFormatException("Match clause must have a path or where");
	}

	private void validateSelect(Select select) throws DataFormatException {
		if (select.getProperty() == null)
			throw new DataFormatException("Select without property or alias");
		if (select.getSelect()!=null)
				for (Select subSelect:select.getSelect())
				  validateSelect(subSelect);
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


