package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Wrapper class for a query containing the run time parameters for passing into the query
 */
public class QueryRequest {
	private Integer page;
	private Integer pageSize;
	private String textSearch;
	private Map<String,String> argument;
	private Query query;
	private TTIriRef queryIri;
	private String referenceDate;

	public Map<String, String> getArgument() {
		return argument;
	}

	@JsonSetter
	public QueryRequest setArgument(Map<String, String> argument) {
		this.argument = argument;
		return this;
	}

	@JsonIgnore
	public QueryRequest putArgument(String variable,String value){
		if (this.argument==null)
			this.argument= new HashMap<>();
		argument.put(variable,value);
		return this;
	}

	@JsonIgnore
	public QueryRequest addArgument(String variable,String value){
		putArgument(variable,value);
		return this;
	}

	public TTIriRef getQueryIri() {
		return queryIri;
	}

	public QueryRequest setQueryIri(TTIriRef queryIri) {
		this.queryIri = queryIri;
		return this;
	}

	public String getReferenceDate() {
		return referenceDate;
	}

	public QueryRequest setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
		return this;
	}

	public Integer getPage() {
		return page;
	}

	public QueryRequest setPage(Integer page) {
		this.page = page;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public QueryRequest setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public String getTextSearch() {
		return textSearch;
	}

	public QueryRequest setTextSearch(String textSearch) {
		this.textSearch = textSearch;
		return this;
	}



	public Query getQuery() {
		return query;
	}

	@JsonSetter
	public QueryRequest setQuery(Query query) {
		this.query = query;
		return this;
	}

	@JsonIgnore
	public QueryRequest query(Consumer<Query> builder) {
		this.query= new Query();
		builder.accept(this.query);
		return this;
	}

	@JsonIgnore
	public String getJson() throws JsonProcessingException {
		return Query.getJson(this);
	}
}
