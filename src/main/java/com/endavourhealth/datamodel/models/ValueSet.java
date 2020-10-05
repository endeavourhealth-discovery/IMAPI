package com.endavourhealth.datamodel.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ValueSet
 */
@Validated

public class ValueSet {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("members")
	@Valid
	private List<Code> members = null;

	@NotNull

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	@NotNull

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

	public boolean addMember(Code member) {
		if (this.members == null) {
			this.members = new ArrayList<Code>();
		}
		return this.members.add(member);

	}

	@Valid
	public List<Code> getMembers() {
		return Collections.unmodifiableList(members);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iri == null) ? 0 : iri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueSet other = (ValueSet) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValueSet [iri=" + iri + "]";
	}
}
