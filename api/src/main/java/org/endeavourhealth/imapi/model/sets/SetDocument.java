package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of setModel definitions for use in query and reporting or model transformation
 */
public class SetDocument {
	private TTIriRef id;
	private List<SetModel> setModel;
	private List<SetModel> profile;

	public List<SetModel> getSetModel() {
		return setModel;
	}

	public SetDocument setSetModel(List<SetModel> setModel) {
		this.setModel = setModel;
		return this;
	}

	public SetDocument addSetModel(SetModel set){
		if (this.setModel==null)
			this.setModel= new ArrayList<>();
		this.setModel.add(set);
		return this;
	}

	public List<SetModel> getProfile() {
		return profile;
	}

	public SetDocument setProfile(List<SetModel> profile) {
		this.profile = profile;
		return this;
	}

	public SetDocument addProfile(SetModel profile){
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
