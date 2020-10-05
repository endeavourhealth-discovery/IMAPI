package com.endavourhealth.datamodel.models;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Primitive
 */
@Validated

public class Primitive {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

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
		Primitive other = (Primitive) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Primitive [iri=" + iri + "]";
	}
}
