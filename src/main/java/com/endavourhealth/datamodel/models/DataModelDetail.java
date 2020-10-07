package com.endavourhealth.datamodel.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.endavourhealth.dataaccess.entity.Concept;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DataModel
 */
@Validated

public class DataModelDetail {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;
	
	@JsonProperty("properties")
	@Valid
	private List<Property> properties = new ArrayList<Property>();

	@JsonProperty("parents")
	@Valid
	private List<Ancestory> parents = new ArrayList<Ancestory>();

	@JsonProperty("children")
	@Valid
	private List<DataModelDetail> children = new ArrayList<DataModelDetail>();
	
	public DataModelDetail() {
		parents = new ArrayList<Ancestory>();
		children = new ArrayList<DataModelDetail>();
		properties = new ArrayList<Property>();
	}
	
	public DataModelDetail(Concept concept, List<Property> properties) {
		this.setName(concept.getName());
		this.setIri(concept.getIri());
		this.setDescription(concept.getDescription());
		this.properties = properties;
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

	public boolean addChild(DataModelDetail child) {
		return this.children.add(child);
	}

	@Valid
	public List<DataModelDetail> getChildren() {
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
		DataModelDetail other = (DataModelDetail) obj;
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
