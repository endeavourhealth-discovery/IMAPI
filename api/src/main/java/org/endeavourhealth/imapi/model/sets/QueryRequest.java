package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.function.Consumer;

/**
 * Wrapper class for a query containing the run time parameters for passing into the query
 */
public class QueryRequest {
	private int page;
	private int pageSize;
	private String textSearch;
	private String focusVariable;
	private Query query;

	public int getPage() {
		return page;
	}

	public QueryRequest setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public QueryRequest setPageSize(int pageSize) {
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

	public String getFocusVariable() {
		return focusVariable;
	}

	public QueryRequest setFocusVariable(String focusVariable) {
		this.focusVariable = focusVariable;
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
	public QueryRequest Query(Consumer<Query> builder) {
		this.query= new Query();
		builder.accept(this.query);
		return this;
	}

	@JsonIgnore
	String getJson(Object object) throws JsonProcessingException {
		return Query.getJson(this);
	}
}
