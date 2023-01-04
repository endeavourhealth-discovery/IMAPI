package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({"iri","label","type","comment","status","scheme","isContainedIn","subClassOf"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Entity {
	private String iri;
	private TTIriRef type;
	private String name;
	private String description;
	private Set<TTIriRef> isContainedIn;

	private TTIriRef status;
	private TTIriRef scheme;

	@JsonProperty("@id")
	public String getIri() {
		return iri;
	}

	public Entity setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public TTIriRef getType() {
		return type;
	}

	public Entity setType(TTIriRef type) {
		this.type = type;
		return this;
	}

	public String getName() {
		return name;
	}

	public Entity setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Entity setDescription(String description) {
		this.description = description;
		return this;
	}

	public Set<TTIriRef> getIsContainedIn() {
		return isContainedIn;
	}

	public Entity setIsContainedIn(Set<TTIriRef> isContainedIn) {
		this.isContainedIn = isContainedIn;
		return this;
	}
	public Entity addIsContainedIn(TTIriRef folder){
		if (this.isContainedIn ==null)
			this.isContainedIn = new HashSet<>();
		this.isContainedIn.add(folder);
		return this;
	}



	public TTIriRef getStatus() {
		return status;
	}

	public Entity setStatus(TTIriRef status) {
		this.status = status;
		return this;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public Entity setScheme(TTIriRef scheme) {
		this.scheme = scheme;
		return this;
	}
}
