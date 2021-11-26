package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IMQ;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * An optional graph and set of triples and filters
 */
@JsonPropertyOrder({"triples","filters","unions"})
public class WhereClause {
	private List<QueryTriple> triples = new ArrayList<>();
	private List<QueryFilter> filters;
	private List<WhereClause> unions;

	public List<QueryTriple> getTriples() {
		return triples;
	}

	public WhereClause setTriples(List<QueryTriple> paths) {
		this.triples = paths;
		return this;
	}


	public QueryTriple addTriple(boolean optional, String subject, String predicate, String object) throws DataFormatException {
		QueryTriple tpl = new QueryTriple()
			.setS(subject)
			.setP(predicate)
			.setO(object)
			.setOptional(optional);
		this.triples.add(tpl);
		return tpl;

	}

	/**
	 * Adds a filter clause to the query block
	 * @param filterType an IRI of filter functions or clarifications such as notexist
	 * @param expression the expression to evaluate, its content and layout being SPARQL expression
	 * @return QueryFilter
	 */
	public QueryFilter addFilter(FilterType filterType, String expression){
		if (this.filters==null)
			this.filters= new ArrayList<>();
		QueryFilter filter= new QueryFilter()
			.setType(filterType)
			.setExpression(expression);
		this.filters.add(filter);
		return filter;

	}

	public List<QueryFilter> getFilters() {
		return filters;
	}

	public WhereClause setFilters(List<QueryFilter> filters) {
		this.filters = filters;
		return this;
	}

	public List<WhereClause> getUnions() {
		return unions;
	}

	public WhereClause setUnions(List<WhereClause> unions) {
		this.unions = unions;
		return this;
	}
}
