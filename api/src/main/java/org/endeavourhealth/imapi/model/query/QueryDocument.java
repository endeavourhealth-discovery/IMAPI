package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a data manapulation process of some kind including query update delete etc
 * The definition of the process is held as a json literal value of the im:definition predicate
 */
public class QueryDocument {
	private TTIriRef id;
	private List<Profile> query;

	public TTIriRef getId() {
		return id;
	}

	public QueryDocument setId(TTIriRef id) {
		this.id = id;
		return this;
	}

	public List<Profile> getProfile() {
		return query;
	}

	public QueryDocument setProfile(List<Profile> query) {
		this.query = query;
		return this;
	}

	public QueryDocument addProfile (Profile query){
		if (this.query ==null)
			this.query = new ArrayList<>();
		this.query.add(query);
		return this;
	}
}
