package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTContext;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"@context","query"})
public class QueryDocument {
	private TTContext context;
	private List<Query> query;

	@JsonProperty("@context")
	public TTContext getContext() {
		return context;
	}

	public QueryDocument setContext(TTContext context) {
		this.context = context;
		return this;
	}

	public List<Query> getQuery() {
		return query;
	}

	public QueryDocument setQuery(List<Query> query) {
		this.query = query;
		return this;
	}

	public QueryDocument addQuery(Query query){
		if (this.query==null)
			this.query= new ArrayList<>();
		this.query.add(query);
		return this;
	}
}
