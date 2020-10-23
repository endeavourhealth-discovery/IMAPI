package com.endavourhealth.services.concept.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Concept {

	@JsonIgnore
	int dbId;
	
	String name;
	String iri;
	String description;
	Set<Parent> parents;
	Set<Relationship> children;
	Long childCount;
	
	public Concept(String iri) {
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

	public String getIri() {
		return iri;
	}
	
	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}
	
	public Set<Parent> getParents() {
		return parents;
	}

	public void setParents(Set<Parent> parents) {
		this.parents = parents;
	}

	public Set<Relationship> getChildren() {
		return children;
	}

	public void setChildren(Set<Relationship> children) {
		this.children = children;
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
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
		Concept other = (Concept) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Concept [iri=" + iri + "]";
	}	
}
