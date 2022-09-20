package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder ({"inverse","iri","name","alias","path"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TTAlias extends TTIriRef {
	private String alias;
	private boolean inverse;
	private String variable;
	private boolean includeSupertypes;
	private boolean includeSubtypes;
	private boolean includeMembers;


	public boolean isIncludeSupertypes() {
		return includeSupertypes;
	}

	public TTAlias setIncludeSupertypes(boolean includeSupertypes) {
		this.includeSupertypes = includeSupertypes;
		return this;
	}

	public boolean isIncludeSubtypes() {
		return includeSubtypes;
	}

	public TTAlias setIncludeSubtypes(boolean includeSubtypes) {
		this.includeSubtypes = includeSubtypes;
		return this;
	}

	public boolean isIncludeMembers() {
		return includeMembers;
	}

	public TTAlias setIncludeMembers(boolean includeMembers) {
		this.includeMembers = includeMembers;
		return this;
	}

	public static TTAlias iri(String iri) {
		return new TTAlias().setIri(iri);
	}

	public TTAlias(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public String getVariable() {
		return variable;
	}

	public TTAlias setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public boolean isInverse() {
		return inverse;
	}

	public TTAlias setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public TTAlias(){}








	public String getAlias() {
		return alias;
	}

	public TTAlias setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	@JsonProperty("@id")
	public TTAlias setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public TTAlias setName(String name){
		super.setName(name);
		return this;
	}
}
