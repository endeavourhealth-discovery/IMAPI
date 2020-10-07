package com.endavourhealth.valueset.models;

import java.util.List;

public class ValueSet {
	private String iri;
	private String name;
	private String description;
	private List<Code> members = null;

	public ValueSet() {
		super();
	}

	public ValueSet(String iri, String name, String description, List<Code> members) {
		super();
		this.iri = iri;
		this.name = name;
		this.description = description;
		this.members = members;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Code> getMembers() {
		return members;
	}

	public void setMembers(List<Code> members) {
		this.members = members;
	}

}
