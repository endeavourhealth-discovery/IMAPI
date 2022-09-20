package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.PathQuery;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Wrapper class for a query containing the run time parameters for passing into the query
 */
public class QueryRequest{
	private String name;
	private Page page;

	private String textSearch;
	private Map<String,Object> argument;
	private Query query;
	private PathQuery pathQuery;
	private String referenceDate;

	public PathQuery getPathQuery() {
		return pathQuery;
	}

	public QueryRequest setPathQuery(PathQuery pathQuery) {
		this.pathQuery = pathQuery;
		return this;
	}

	public String getName() {
		return name;
	}

	public QueryRequest setName(String name) {
		this.name = name;
		return this;
	}

	public Map<String, Object> getArgument() {
		return argument;
	}

	@JsonSetter
	public QueryRequest setArgument(Map<String, Object> argument) {
		this.argument = argument;
		return this;
	}

	@JsonIgnore
	public QueryRequest putArgument(String variable, Object value){
		if (this.argument==null)
			this.argument= new HashMap<>();
		argument.put(variable,value);
		return this;
	}

	@JsonIgnore
	public QueryRequest addArgument(String variable,Object value){
		putArgument(variable,value);
		return this;
	}


	public String getReferenceDate() {
		return referenceDate;
	}

	public QueryRequest setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
		return this;
	}

	public Page getPage() {
		return page;
	}

	@JsonSetter
	public QueryRequest setPage(Page page) {
		this.page = page;
		return this;
	}

	public QueryRequest page(Consumer<Page> builder){
		Page page= new Page();
		this.page= page;
		builder.accept(page);
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
