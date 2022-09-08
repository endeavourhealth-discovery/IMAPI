package org.endeavourhealth.imapi.model.iml;

import java.util.ArrayList;
import java.util.List;

public class QueryDocument {
	private List<Query> query;

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
