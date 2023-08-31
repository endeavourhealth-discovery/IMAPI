package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.PathRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.PathDocument;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {


	/**
	 * Queries any IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @return a generic JSONDocument containing the results in a format defined by the select statement and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public JsonNode queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
        ObjectNode result = new ObjectMapper().createObjectNode();
        QueryRepository repo = new QueryRepository();
        repo.unpackQueryRequest(queryRequest, result);
        if (null != queryRequest.getTextSearch()) {
            OSQuery osq = new OSQuery();
            List<SearchResultSummary> osResult = osq.openSearchQuery(queryRequest);
            if (osResult != null)
                return osq.convertOSResult(osResult, queryRequest.getQuery());
        }

		return repo.queryIM(queryRequest);
	}

    /**
     * Queries any IM entity using the query model
     * @param queryRequest Query inside a request with parameters
     * @return a list of SearchResultSummary
     * @throws DataFormatException if query format is invalid
     */
    public List<SearchResultSummary> queryIMSearch(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
        ObjectNode result = new ObjectMapper().createObjectNode();
        QueryRepository repo = new QueryRepository();
        repo.unpackQueryRequest(queryRequest, result);

        // Set correct return properties for SearchResultSummary structure
        queryRequest.getQuery().return_(s -> s.property(p -> p.setIri(RDFS.LABEL.getIri()))
            .property(p -> p.setIri(RDFS.COMMENT.getIri()))
            .property(p -> p.setIri(IM.CODE.getIri()))
            .property(p -> p.setIri(IM.HAS_STATUS.getIri())
                .return_(r -> r.property(rp -> rp.setIri(RDFS.LABEL.getIri()))))
            .property(p -> p.setIri(IM.HAS_SCHEME.getIri())
                .return_(r -> r.property(rp -> rp.setIri(RDFS.LABEL.getIri()))))
            .property(p -> p.setIri(RDF.TYPE.getIri())
                .return_(r -> r.property(rp -> rp.setIri(RDFS.LABEL.getIri()))))
            .property(p -> p.setIri(IM.WEIGHTING.getIri()))
        );

        if (null != queryRequest.getTextSearch()) {
            List<SearchResultSummary> osResult = new OSQuery().openSearchQuery(queryRequest);
            if (osResult != null)
                return osResult;
        }

        JsonNode queryResults = repo.queryIM(queryRequest);
        return new QueryService().convertQueryIMResultsToSearchResultSummary(queryResults);
    }

	/**
	 * Queries and updates IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @throws DataFormatException if query format is invalid
	 */
	public void updateIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, QueryException {
		new QueryRepository().updateIM(queryRequest);
	}

	/**
	 * Queries any IM entity using the query model
	 * @param queryRequest Query inside a request with parameters
	 * @return a generic JSONDocument containing the results in a format defined by the selecr staement and including predicate map
	 * @throws DataFormatException if query format is invalid
	 */
	public PathDocument pathQuery(QueryRequest queryRequest) throws DataFormatException {
		validateQueryRequest(queryRequest);
		return new PathRepository().pathQuery(queryRequest);
	}

	public void validateQueryRequest(QueryRequest queryRequest) throws DataFormatException {
			if (queryRequest.getQuery()==null&& queryRequest.getPathQuery()==null)
				throw new DataFormatException("Query request must have a Query or an Query object with an iri or a pathQuery");
	}

    /**
     * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
     * @param request holding the search term (multi or single word) + type/status/scheme filters
     * @return A set of Summaries of entity documents from the store
     *
     */
	public List<SearchResultSummary> getEntitiesByTerm(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
		return new OSQuery().multiPhaseQuery(request);
	}


}


