package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

/**
 * A class that represents a data manapulation process of some kind including query update delete etc
 * The definition of the process is held as a json literal value of the im:definition predicate
 */
public class Process{
	private TTIriRef id;
	private Profile profile;

	public TTIriRef getId() {
		return id;
	}

	public Process setId(TTIriRef id) {
		this.id = id;
		return this;
	}

	public Profile getProfile() {
		return profile;
	}

	public Profile createProfile() {
		this.profile= new Profile();
		return this.profile;
	}

	public Process setProfile(Profile profile) {
		this.profile = profile;
		return this;
	}
}
