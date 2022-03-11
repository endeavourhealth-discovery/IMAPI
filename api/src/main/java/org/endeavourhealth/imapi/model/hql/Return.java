package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class Return {
	TTIriRef id;
	String name;
	String description;
	private List<ReturnSet> returnSets;

	public TTIriRef getId() {
		return id;
	}

	public Return setId(TTIriRef id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Return setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Return setDescription(String description) {
		this.description = description;
		return this;
	}

	public List<ReturnSet> getReturnSets() {
		return returnSets;
	}

	public Return setReturnSets(List<ReturnSet> returnSets) {
		this.returnSets = returnSets;
		return this;
	}
}
