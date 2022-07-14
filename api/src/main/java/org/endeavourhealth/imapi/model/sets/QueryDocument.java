package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of query definitions for use in query and reporting or model transformation
 */
public class QueryDocument {
	private TTIriRef id;
	private List<QueryEntity> query;

	public List<QueryEntity> getQuery() {
		return query;
	}

	public QueryDocument setQuery(List<QueryEntity> query) {
		this.query = query;
		return this;
	}

	public QueryDocument addQuery(QueryEntity set){
		if (this.query ==null)
			this.query = new ArrayList<>();
		this.query.add(set);
		return this;
	}


	public TTIriRef getId() {
		return id;
	}

	public QueryDocument setId(TTIriRef id) {
		this.id = id;
		return this;
	}


}
