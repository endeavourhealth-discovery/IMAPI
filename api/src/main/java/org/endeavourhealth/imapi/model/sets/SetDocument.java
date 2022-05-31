package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of query definitions for use in query and reporting or model transformation
 */
public class SetDocument {
	private TTIriRef id;
	private List<Query> query;
	private List<Query> profile;

	public List<Query> getDataSet() {
		return query;
	}

	public SetDocument setDataSet(List<Query> query) {
		this.query = query;
		return this;
	}

	public SetDocument addDataSet(Query set){
		if (this.query ==null)
			this.query = new ArrayList<>();
		this.query.add(set);
		return this;
	}

	public List<Query> getProfile() {
		return profile;
	}

	public SetDocument setProfile(List<Query> profile) {
		this.profile = profile;
		return this;
	}

	public SetDocument addProfile(Query profile){
		if (this.profile==null)
			this.profile= new ArrayList<>();
		this.profile.add(profile);
		return this;
	}

	public TTIriRef getId() {
		return id;
	}

	public SetDocument setId(TTIriRef id) {
		this.id = id;
		return this;
	}


}
