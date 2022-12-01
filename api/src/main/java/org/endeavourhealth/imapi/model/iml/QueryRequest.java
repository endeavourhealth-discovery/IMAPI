package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.PathQuery;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Wrapper class for a query containing the run time parameters for passing into the query
 */
public class QueryRequest{
	private String name;
	private Page page;

	private String textSearch;
	private List<Argument> argument;
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

	public List<Argument> getArgument() {
		return argument;
	}

	@JsonSetter
	public QueryRequest setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}



	public QueryRequest addArgument(Argument argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}
	public QueryRequest argument(Consumer<Argument> builder){
		Argument argument= new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}

	public QueryRequest addArgument(String parameter, Object value){
		Argument argument= new Argument();
		argument.setParameter(parameter);
		if (value instanceof String)
			argument.setValueData((String) value);
		else if (value instanceof TTIriRef)
			argument.setValueIri((TTIriRef) value);
		else
			throw new IllegalArgumentException("Using add argument this way must include a string value or TTIref value");
		addArgument(argument);
		return this;
	}

	public Object getArgumentDataValue(String parameter){
		if (this.argument==null)
			return null;
		else {
			for (Argument arg : this.argument) {
				if (arg.getParameter().equals(parameter))
					return arg.getValueData();
			}
		}
		return null;

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
