package com.endavourhealth.services.concept.models;

import java.util.Set;

import com.endavourhealth.services.properties.models.Property;
import com.endavourhealth.services.properties.models.PropertyNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Concept {

	@JsonIgnore
	int dbId;
	
	String name;
	String iri;
	String description;
	Set<PropertyNode> parents;
	Set<Property> children;
	Set<Property> properties; // inherited, core, extended?
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
	
	public Set<PropertyNode> getParents() {
		return parents;
	}

	public void setParents(Set<PropertyNode> parents) {
		this.parents = parents;
	}

	public Set<Property> getChildren() {
		return children;
	}

	public void setChildren(Set<Property> children) {
		this.children = children;
		setChildCount((long) children.size());
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
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
