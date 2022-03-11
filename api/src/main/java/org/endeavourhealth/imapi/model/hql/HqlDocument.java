package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a data manapulation process of some kind including query update delete etc
 * The definition of the process is held as a json literal value of the im:definition predicate
 */
public class HqlDocument {
	private TTIriRef id;
	private List<Profile> profile;

	public TTIriRef getId() {
		return id;
	}

	public HqlDocument setId(TTIriRef id) {
		this.id = id;
		return this;
	}

	public List<Profile> getProfile() {
		return profile;
	}

	public HqlDocument setProfile(List<Profile> query) {
		this.profile = query;
		return this;
	}

	public HqlDocument addProfile (Profile query){
		if (this.profile ==null)
			this.profile = new ArrayList<>();
		this.profile.add(query);
		return this;
	}
}
