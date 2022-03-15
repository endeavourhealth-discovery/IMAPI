package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class HqlPackage {
	TTIriRef id;
	String name;
	String description;

	public TTIriRef getId() {
		return id;
	}

	public HqlPackage setId(TTIriRef id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public HqlPackage setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public HqlPackage setDescription(String description) {
		this.description = description;
		return this;
	}
}
