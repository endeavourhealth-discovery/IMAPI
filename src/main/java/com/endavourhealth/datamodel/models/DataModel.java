package com.endavourhealth.datamodel.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DataModel
 */
@Validated

public class DataModel {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("properties")
	@Valid
	private List<Property> properties = null;

	@JsonProperty("parents")
	@Valid
	private List<Ancestory> parents = null;

	@JsonProperty("children")
	@Valid
	private List<DataModel> children = null;
	
	public DataModel() {
		parents = new ArrayList<Ancestory>();
		children = new ArrayList<DataModel>();
		properties = new ArrayList<Property>();
	}

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

	public boolean addProperty(Property property) {
		return properties.add(property);
	}

	public List<Property> getProperties() {
		return Collections.unmodifiableList(properties);
	}

	public boolean addParent(Ancestory parent) {
		return this.parents.add(parent);
	}

	@Valid
	public List<Ancestory> getParents() {
		return Collections.unmodifiableList(parents);
	}

	public boolean addChild(DataModel child) {
		return this.children.add(child);
	}

	@Valid
	public List<DataModel> getChildren() {
		return Collections.unmodifiableList(children);
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
		DataModel other = (DataModel) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataModel [iri=" + iri + "]";
	}
}
