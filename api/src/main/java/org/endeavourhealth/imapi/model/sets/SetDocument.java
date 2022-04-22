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

	public TTIriRef getId() {
		return id;
	}

	public SetDocument setId(TTIriRef id) {
		this.id = id;
		return this;
	}




	public List<SetModel> getSet() {
		return setModel;
	}

	public SetDocument setSet(List<SetModel> setModel) {
		this.setModel = setModel;
		return this;
	}

	public SetDocument addSet(SetModel setModel){
		if (this.setModel ==null)
			this.setModel = new ArrayList<>();
		this.setModel.add(setModel);
		return this;
	}
}
