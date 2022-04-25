package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of dataSet definitions for use in query and reporting or model transformation
 */
public class SetDocument {
	private TTIriRef id;
	private List<DataSet> dataSet;
	private List<DataSet> profile;

	public List<DataSet> getSetModel() {
		return dataSet;
	}

	public SetDocument setSetModel(List<DataSet> dataSet) {
		this.dataSet = dataSet;
		return this;
	}

	public SetDocument addSetModel(DataSet set){
		if (this.dataSet ==null)
			this.dataSet = new ArrayList<>();
		this.dataSet.add(set);
		return this;
	}

	public List<DataSet> getProfile() {
		return profile;
	}

	public SetDocument setProfile(List<DataSet> profile) {
		this.profile = profile;
		return this;
	}

	public SetDocument addProfile(DataSet profile){
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
