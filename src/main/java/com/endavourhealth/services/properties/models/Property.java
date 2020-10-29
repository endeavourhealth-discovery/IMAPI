package com.endavourhealth.services.properties.models;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.services.concept.models.Concept;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Code
 */
@Validated

public class Property {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("minCardinality")
	private Integer minCardinality = null;

	@JsonProperty("maxCardinality")
	private Integer maxCardinality = null;

	@JsonProperty("value")
	private Concept value = null;

	@JsonIgnore
	private Concept owner = null;
	
//	public Property(ConceptPropertyObject conceptPropertyObject, Concept propertyConcept, Value value) {
//		this.setIri(propertyConcept.getIri());
//		this.setName(propertyConcept.getName());
//		this.setDescription(propertyConcept.getDescription());
//		this.setMinCardinality(conceptPropertyObject.getMinCardinality());
//		this.setMaxCardinality(conceptPropertyObject.getMaxCardinality());
//		this.setValue(value);
//	}

	public Property(String iri, Concept value, Concept owner) {
		this.iri = iri;
		this.value = value;
		this.owner = owner;
	}

	@NotNull

	public String getIri() {
		return iri;
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

	public Integer getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(Integer minCardinality) {
		this.minCardinality = minCardinality;
	}

	public Integer getMaxCardinality() {
		return minCardinality;
	}

	public void setMaxCardinality(Integer minCardinality) {
		this.minCardinality = minCardinality;
	}

	public Concept getValue() {
		return value;
	}
	
	public Concept getOwner() {
		return owner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iri == null) ? 0 : iri.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Property other = (Property) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Property [iri=" + iri + ", value=" + value + ", owner=" + owner + "]";
	}
}
